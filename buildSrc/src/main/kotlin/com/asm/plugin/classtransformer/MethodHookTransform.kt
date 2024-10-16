package com.asm.plugin.classtransformer

import com.asm.plugin.extension.AsmType
import com.asm.plugin.extension.ownerClassName
import com.asm.plugin.io_method.CHECK_THREAD_PACKAGE
import com.asm.plugin.io_method.IoMethods
import com.asm.plugin.privacymethod.toMethodNode
import com.didiglobal.booster.kotlinx.file
import com.didiglobal.booster.kotlinx.touch
import com.didiglobal.booster.transform.TransformContext
import com.didiglobal.booster.transform.asm.simpleName
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.function.Consumer


class MethodHookTransform : AbsClassTransformer() {

    private val check = false
    private lateinit var logger: PrintWriter
    private val asmItems = AnnotationParserClassTransform.asmConfigs
    private val WHITE_PACKAGE = arrayOf(
//        "java",
//        "android",
//        "androidx",
//        "dalvik",
//        "com.android",
        "com/asm/privacymethodhooker/utils",
    )

    val whiteListStr = mutableListOf<String>()
    val replaceStr = mutableListOf<String>()
    val insertBeforeStr = mutableListOf<String>()
    val insertAfterStr = mutableListOf<String>()
    val checkThreadStr = mutableListOf<String>()

    override fun onPreTransform(context: TransformContext) {
        super.onPreTransform(context)
        println("MethodReplaceRepo reportsDir "+context.reportsDir)
        println("MethodReplaceRepo name "+context.name)
        this.logger = context.reportsDir.file("MethodReplaceRepo").file(context.name)
            .file("report.txt").touch().printWriter()

        logger.println("--start-- ${System.currentTimeMillis().toTime()}")

        asmItems.run {
            logger.println("字节码插桩替换方法：")
            forEach(Consumer {
                logger.println(it)
            })
        }
        logger.println("")

    }

    override fun onPostTransform(context: TransformContext) {

        logger.println("\n白名单 ：")
        whiteListStr.forEach {
            logger.println(it)
        }
        logger.println("\n替换方法 ：")
        replaceStr.forEach {
            logger.println(it)
        }
        logger.println("\n方法前插入 ：")
        insertBeforeStr.forEach {
            logger.println(it)
        }
        logger.println("\n方法后插入 ：")
        insertAfterStr.forEach {
            logger.println(it)
        }
        logger.println("\n运行线程检测 ：")
        checkThreadStr.forEach {
            logger.println(it)
        }
        logger.println("\n --end-- ${System.currentTimeMillis().toTime()}")
        this.logger.close()
    }

    override fun transform(context: TransformContext, klass: ClassNode) = klass.also {

        if (onCommInterceptor(context, klass)) {
            return klass
        }

//        if (AnnotationParserClassTransform.asmConfigsMap.contains(klass.name)) {
//            logger.print("\nPrivacyMethodReplaceAsmHelper modifyClass ignore,classNode.name=${klass.name}\n")
//            return@also
//        }
        WHITE_PACKAGE.forEach {
            if (klass.name.startsWith(it)) {
                whiteListStr.add("${klass.name}")
                return@also
            }
        }

        //反编译路径 ： /Users/yr/jadx/build/jadx/bin/jadx-gui
        klass.methods.forEach { method ->
            method.instructions?.let { insnNodeList ->
                insnNodeList.forEach { insnNode ->
                    if (insnNode is MethodInsnNode) {
                        run breakFor@{
                            asmItems.forEach { asmItem ->
                                if (asmItem.oriDesc == insnNode.desc && asmItem.oriMethod == insnNode.name
                                    && asmItem.oriAccess == insnNode.opcode &&
                                    (asmItem.oriClass == insnNode.owner || "java/lang/Object" == asmItem.oriClass)
                                ) {
                                    if (asmItem.amsType == AsmType.REPLACE) {
//                                        replaceStr.add(
//                                            "${klass.name}#${insnNode.name}-${insnNode.desc}${insnNode.opcode}\n" +
//                                                    "->${asmItem.targetClass}#${asmItem.targetMethod}-${asmItem.targetDesc}${asmItem.oriAccess}\n"
//                                        )
                                        replaceStr.add(
                                            "${klass.simpleName}.${insnNode.name}->${asmItem.targetClass}.${asmItem.targetMethod}\n"
                                        )
                                        insnNode.opcode = asmItem.targetAccess
                                        insnNode.desc = asmItem.targetDesc
                                        insnNode.owner = asmItem.targetClass
                                        insnNode.name = asmItem.targetMethod
                                    } else if (asmItem.amsType == AsmType.INSERT_BEFORE) {
                                        val methodNode = asmItem.toMethodNode()
//                                        insertBeforeStr.add(
//                                            "${klass.name}#${insnNode.name}-${insnNode.desc}${insnNode.opcode}\n" +
//                                                    "->${methodNode.owner}#${methodNode.name}-${methodNode.desc}${methodNode.opcode}\n"
//                                        )
                                        insertBeforeStr.add(
                                            "${klass.simpleName}.${insnNode.name}->${methodNode.ownerClassName}.${methodNode.name}\n"
                                        )
                                        insnNodeList.insertBefore(insnNode, methodNode)
                                    } else if (asmItem.amsType == AsmType.INSERT_AFTER) {
                                        val methodNode = asmItem.toMethodNode()
//                                        insertAfterStr.add(
//                                            "${klass.name}#${insnNode.name}-${insnNode.desc}${insnNode.opcode}\n" +
//                                                    "->${methodNode.owner}#${methodNode.name}-${methodNode.desc}${methodNode.opcode}\n"
//                                        )
                                        insertAfterStr.add(
                                            "${klass.simpleName}.${insnNode.name}->${methodNode.ownerClassName}.${methodNode.name}\n"
                                        )
                                        insnNodeList.insert(insnNode, methodNode)
                                    }

                                    return@breakFor
                                }

                                //主线程调用检测

                            }
                        }
//                        加入线程检测
                        if(check){
                            run breakFor@{
                                IoMethods.forEach {
                                    if (
                                        insnNode.owner == it.klass && insnNode.name == it.methodName && insnNode.desc == it.desc) {
                                        insnNodeList.insertBefore(
                                            insnNode, MethodInsnNode(
                                                Opcodes.INVOKESTATIC,
                                                "com/asm/privacymethodhooker/test/TextUtil",
                                                "checkThread",
                                                "()V",
                                                false
                                            )
                                        )

                                        checkThreadStr.add("${klass.simpleName}.${method.name}\n")
                                        return@breakFor
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }


    }

    fun Long.toTime() = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this)

}