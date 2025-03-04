package com.asm.plugin

import com.android.build.api.transform.*
import com.android.build.api.transform.Status.*
import com.didiglobal.booster.gradle.*
import com.didiglobal.booster.kotlinx.NCPU
import com.didiglobal.booster.transform.AbstractKlassPool
import com.didiglobal.booster.transform.ArtifactManager
import com.didiglobal.booster.transform.TransformContext
import com.didiglobal.booster.transform.artifacts
import com.didiglobal.booster.transform.util.transform
import com.asm.plugin.extension.doTransform
import com.asm.plugin.extension.println
import com.asm.plugin.transform.BaseTransform
import java.io.File
import java.net.URI
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Represents a delegate of TransformInvocation
 *
 * @author johnsonlee
 */
internal class DelegateTransformInvocation(
    private val delegate: TransformInvocation,
    internal val transform: BaseTransform
) : TransformInvocation by delegate, TransformContext, ArtifactManager {

    val executor = Executors.newFixedThreadPool(NCPU)

    private val project = transform.project

    private val outputs = CopyOnWriteArrayList<File>()

    override val name: String = delegate.context.variantName

    override val projectDir: File = project.projectDir

    override val buildDir: File = project.buildDir

    override val temporaryDir: File = delegate.context.temporaryDir

    override val reportsDir: File = File(buildDir, "reports").also { it.mkdirs() }

    override val bootClasspath = delegate.bootClasspath

    override val compileClasspath = delegate.compileClasspath

    override val runtimeClasspath = delegate.runtimeClasspath

    override val artifacts = this

    override val dependencies: Collection<String> by lazy {
        ResolvedArtifactResults(variant).map {
            it.id.displayName
        }
    }

    override val klassPool: AbstractKlassPool =
        object : AbstractKlassPool(compileClasspath, transform.bootKlassPool) {}

    override val applicationId = delegate.applicationId

    override val originalApplicationId = delegate.originalApplicationId

    override val isDebuggable = variant.buildType.isDebuggable

    override val isDataBindingEnabled = delegate.isDataBindingEnabled

    override fun hasProperty(name: String) = project.hasProperty(name)

    override fun <T> getProperty(name: String, default: T): T = project.getProperty(name, default)

    override fun get(type: String) = variant.artifacts.get(type)

    internal fun doFullTransform() = doTransform(true)

    internal fun doIncrementalTransform() = doTransform(false)

    private fun onPreTransform() {
        transform.transformers.forEach {
            it.onPreTransform(this)
        }
    }

    private fun onPostTransform() {
        transform.transformers.forEach {
            it.onPostTransform(this)
        }
    }

    private fun doTransform(full:Boolean) {
        this.outputs.clear()
        this.onPreTransform()

        try {
            if (full){
                transformFully()
            } else {
                transformIncrementally()
            }
        } finally {
            executor.shutdown()
            executor.awaitTermination(1, TimeUnit.HOURS)
        }

        this.onPostTransform()

        if (transform.verifyEnabled) {
            this.doVerify()
        }
    }

    private fun transformFully() = this.inputs.map {
        it.jarInputs + it.directoryInputs
    }.flatten().map { input ->
        executor.submit {
            val format = if (input is DirectoryInput) Format.DIRECTORY else Format.JAR
            outputProvider?.let { provider ->
                project.logger.info("Transforming ${input.file}")
                input.transform(
                    provider.getContentLocation(
                        input.name,
                        input.contentTypes,
                        input.scopes,
                        format
                    )
                )
            }
        }.get()
    }

    private fun transformIncrementally() = this.inputs.map { input ->
        input.jarInputs.filter { it.status != NOTCHANGED }.map { jarInput ->
            executor.submit {
                doIncrementalTransform(jarInput)
            }.get()
        } + input.directoryInputs.filter { it.changedFiles.isNotEmpty() }.map { dirInput ->
            val base = dirInput.file.toURI()
            executor.submit {
                doIncrementalTransform(dirInput, base)
            }.get()
        }
    }.flatten()

    @Suppress("NON_EXHAUSTIVE_WHEN")
    private fun doIncrementalTransform(jarInput: JarInput) {
        when (jarInput.status) {
            REMOVED -> jarInput.file.delete()
            CHANGED, ADDED -> {
                project.logger.info("Transforming ${jarInput.file}")
                outputProvider?.let { provider ->
                    jarInput.transform(
                        provider.getContentLocation(
                            jarInput.name,
                            jarInput.contentTypes,
                            jarInput.scopes,
                            Format.JAR
                        )
                    )
                }
            }
            else -> {
                project.logger.info("${jarInput.file} not changed")
            }
        }
    }

    @Suppress("NON_EXHAUSTIVE_WHEN")
    private fun doIncrementalTransform(dirInput: DirectoryInput, base: URI) {
        dirInput.changedFiles.forEach { (file, status) ->
            when (status) {
                REMOVED -> {
                    project.logger.info("Deleting $file")
                    outputProvider?.let { provider ->
                        provider.getContentLocation(
                            dirInput.name,
                            dirInput.contentTypes,
                            dirInput.scopes,
                            Format.DIRECTORY
                        ).parentFile.listFiles()?.asSequence()
                            ?.filter { it.isDirectory }
                            ?.map { File(it, dirInput.file.toURI().relativize(file.toURI()).path) }
                            ?.filter { it.exists() }
                            ?.forEach { it.delete() }
                    }
                    file.delete()
                }
                ADDED, CHANGED -> {
                    project.logger.info("Transforming $file")
                    outputProvider?.let { provider ->
                        val root = provider.getContentLocation(
                            dirInput.name,
                            dirInput.contentTypes,
                            dirInput.scopes,
                            Format.DIRECTORY
                        )
                        val output = File(root, base.relativize(file.toURI()).path)
                        outputs += output
                        file.transform(output) { bytecode ->
                            bytecode.transform()
                        }
                    }
                }
                else -> {
                    project.logger.info("$file not changed")
                }
            }
        }
    }

    private fun doVerify() {
        // TODO unnecessary since Android 5.0
//        outputs.sortedBy(File::nameWithoutExtension).forEach { output ->
//            val out = temporaryDir.file(output.name)
//            val rc = out.dex(
//                output,
//                variant.extension.defaultConfig.targetSdkVersion?.apiLevel
//                    ?: DexFormat.API_NO_EXTENDED_OPCODES
//            )
//            println("${if (rc != 0) red("✗") else green("✓")} $output")
//            out.deleteRecursively()
//        }
    }

    private fun QualifiedContent.transform(output: File) {
        outputs += output
        try {
            this.file.doTransform(output) { bytecode ->
                bytecode.transform()
            }
        } catch (e: Exception) {
            "e===>${e.message}".println()
            e.printStackTrace()
        }

    }

    private fun ByteArray.transform(): ByteArray {
        return transform.transformers.fold(this) { bytes, transformer ->
            transformer.transform(this@DelegateTransformInvocation, bytes)
        }
    }
}