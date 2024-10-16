package com.asm.plugin.method_time

import org.gradle.api.Project
import com.asm.plugin.InterceptClassNode
import com.asm.plugin.asmtransformer.BaseAsmTransformer
import com.asm.plugin.classtransformer.MethodHookTransform
import com.asm.plugin.transform.BaseTransform
import com.didiglobal.booster.transform.TransformContext
import com.didiglobal.booster.transform.Transformer
import com.didiglobal.booster.transform.asm.ClassTransformer
import com.google.auto.service.AutoService
import groovyjarjarasm.asm.ClassReader
import groovyjarjarasm.asm.ClassVisitor
import groovyjarjarasm.asm.ClassWriter
import groovyjarjarasm.asm.Opcodes

@AutoService(ClassTransformer::class)
class MethodTimeTransform(androidProject: Project) : BaseTransform(androidProject) {

    override val transformers = listOf<Transformer>(
        MethodTimeTransformer()
    )

    override fun getName(): String {
        return "MethodTimeTransform"
    }

}
