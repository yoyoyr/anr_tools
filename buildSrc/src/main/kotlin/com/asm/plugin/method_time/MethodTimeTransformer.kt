package com.asm.plugin.method_time

import com.asm.plugin.InterceptClassNode
import com.asm.plugin.classtransformer.AbsClassTransformer
import com.asm.plugin.extension.AsmType
import com.asm.plugin.extension.ownerClassName
import com.asm.plugin.io_method.CHECK_THREAD_PACKAGE
import com.asm.plugin.io_method.IoMethods
import com.asm.plugin.privacymethod.toMethodNode
import com.didiglobal.booster.kotlinx.file
import com.didiglobal.booster.kotlinx.touch
import com.didiglobal.booster.transform.TransformContext
import com.didiglobal.booster.transform.Transformer
import com.didiglobal.booster.transform.asm.simpleName
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.function.Consumer


class MethodTimeTransformer : Transformer {
    override fun transform(context: TransformContext, bytecode: ByteArray): ByteArray {
        val cWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
        val classNode = InterceptClassNode(cWriter)
        ClassReader(bytecode).accept(classNode, ClassReader.EXPAND_FRAMES)
        return cWriter.toByteArray()

//        return ClassWriter(ClassWriter.COMPUTE_MAXS).also { writer ->
//            this.transformers.fold(ClassNode().also { klass ->
//                ClassReader(bytecode).accept(klass, 0)
//            }) { klass, transformer ->
//                this.threadMxBean.sumCpuTime(transformer) {
//                    transformer.transform(context, klass)
//                }
//            }.accept(writer)
//        }.toByteArray()
    }
}