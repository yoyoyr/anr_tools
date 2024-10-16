package com.asm.plugin.transform

import com.didiglobal.booster.transform.Transformer
import com.didiglobal.booster.transform.asm.ClassTransformer
import com.google.auto.service.AutoService
import com.asm.plugin.asmtransformer.BaseAsmTransformer
import com.asm.plugin.classtransformer.MethodHookTransform
import org.gradle.api.Project


@AutoService(ClassTransformer::class)
class MethodHookTransform(androidProject: Project) : BaseTransform(androidProject) {

    override val transformers = listOf<Transformer>(
        BaseAsmTransformer(
            listOf(
                MethodHookTransform()
            )
        )
    )

    override fun getName(): String {
        return "MethodHookTransform"
    }

}
