package com.asm.plugin

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter
import org.objectweb.asm.tree.MethodNode


//int api, MethodVisitor methodVisitor, int access, String name, String descriptor
open class InterceptMethodVisitor(
    val className: String,
    methodVisitor: MethodVisitor,
    access: Int,
    name: String?,
    descriptor: String?,
) : AdviceAdapter(Opcodes.ASM9, methodVisitor, access, name, descriptor) {

    private var timeLocalIndex = 0

    override fun onMethodEnter() {

        mv.visitLdcInsn("$className.$name")
        mv.visitMethodInsn(
            INVOKESTATIC,
            "com/asm/privacymethodhooker/test/TextUtil",
            "methodIn",
            "(Ljava/lang/String;)V",
            false
        )

        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
        timeLocalIndex = newLocal(Type.LONG_TYPE);
        mv.visitVarInsn(LSTORE, timeLocalIndex);
        super.onMethodEnter()
    }

    override fun onMethodExit(opcode: Int) {
        mv.visitVarInsn(LLOAD, timeLocalIndex)
        mv.visitMethodInsn(
            INVOKESTATIC,
            "com/asm/privacymethodhooker/test/TextUtil",
            "methodOut",
            "(J)V",
            false
        )
        super.onMethodExit(opcode)
    }


}