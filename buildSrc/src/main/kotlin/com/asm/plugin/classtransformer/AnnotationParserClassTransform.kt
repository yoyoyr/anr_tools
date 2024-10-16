package com.asm.plugin.classtransformer

import com.asm.plugin.extension.AsmType
import com.didiglobal.booster.transform.TransformContext
import com.asm.plugin.privacymethod.AsmItem
import org.objectweb.asm.tree.ClassNode


class AnnotationParserClassTransform : AbsClassTransformer() {

    companion object {
        const val AsmFieldDesc = "Lcom/asm/asm_annotation/AsmMethodReplace;"
        var asmConfigs = mutableListOf<AsmItem>()
    }

    override fun transform(context: TransformContext, klass: ClassNode) = klass.also {
        if (onCommInterceptor(context, klass)) {
            return klass
        }

        klass.methods.forEach { method ->
            method.invisibleAnnotations?.forEach { node ->
                if (node.desc == AsmFieldDesc) {
                    val asmItem = AsmItem(klass.name, method, node)
                    if (!asmConfigs.contains(asmItem)) {
                        asmConfigs.add(asmItem)
                    }
                }
            }
        }
    }
}