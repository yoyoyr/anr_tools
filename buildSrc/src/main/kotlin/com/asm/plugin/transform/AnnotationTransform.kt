package com.asm.plugin.transform

import com.didiglobal.booster.transform.Transformer
import com.asm.plugin.classtransformer.AnnotationParserClassTransform
import com.asm.plugin.asmtransformer.BaseAsmTransformer
import org.gradle.api.Project


class AnnotationTransform(androidProject: Project) : BaseTransform(androidProject) {

    override val transformers = listOf<Transformer>(
        BaseAsmTransformer(
            listOf(
                AnnotationParserClassTransform()
            )
        )
    )

    override fun getName(): String {
        return "CommonTransform"
    }

}
