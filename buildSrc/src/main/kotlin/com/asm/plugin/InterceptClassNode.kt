package com.asm.plugin

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class InterceptClassNode(cWriter: ClassWriter) : ClassVisitor(Opcodes.ASM9, cWriter) {

    private var className: String? = null

    private val ignoreClass = mutableListOf<String>().apply{
        add("com/asm/privacymethodhooker/test/TextUtil") //打印日志
        add("com/asm/privacymethodhooker/LogProtos") //日志记录磁盘
        add("com/asm/privacymethodhooker/App")
    }

    private val shouldInterceptPKG = mutableListOf<String>().apply {
        add("com/asm/privacymethodhooker")
    }


    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        className = name
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitMethod(
        access: Int,
        methodName: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor? {
        return if (cv != null) {

            var shouldIntercept = false

            run ForBreak@{
                shouldInterceptPKG.forEach {
                   if( className?.startsWith(it) == true){
                       shouldIntercept = true
                       return@ForBreak
                   }
                }
            }

            if(shouldIntercept){
                run ForBreak@{
                    ignoreClass.forEach {
                        if( className?.startsWith(it) == true){
                            shouldIntercept = false
                            return@ForBreak
                        }
                    }
                }
            }


            if (shouldIntercept) {

                println("统计耗时方法： ${className}.$methodName")
                val index = className!!.lastIndexOf("/") + 1

                InterceptMethodVisitor(
                    className!!.substring(index, className!!.length),
                    cv.visitMethod(access, methodName, descriptor, signature, exceptions),
                    access,
                    methodName,
                    descriptor,
                )
            } else {
                cv.visitMethod(access, methodName, descriptor, signature, exceptions)
            }
        } else null
    }
}