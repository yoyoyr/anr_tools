package com.asm.plugin

import com.android.build.api.dsl.BuildType
import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.api.ApplicationVariant
import com.android.builder.compiling.BuildConfigType
import com.asm.plugin.method_time.MethodTimeTransform
import com.didiglobal.booster.gradle.getAndroid
import com.asm.plugin.transform.AnnotationTransform
import com.asm.plugin.transform.MethodHookTransform
import com.didiglobal.booster.transform.Transformer
import org.gradle.api.Plugin
import org.gradle.api.Project
import javax.xml.crypto.dsig.Transform


class Plugin : Plugin<Project> {
    override fun apply(project: Project) {

        when {
            project.plugins.hasPlugin("com.android.application") ||
                    project.plugins.hasPlugin("com.android.dynamic-feature") -> {
                project.getAndroid<AppExtension>().let { androidExt ->

                    //获取项目中的  需要替换的注解集合
                    androidExt.registerTransform(
                        AnnotationTransform(project)
                    )

                    //替换方法
                    androidExt.registerTransform(
                        MethodHookTransform(project)
                    )


                    //方法耗时埋点
                    androidExt.registerTransform(
                        MethodTimeTransform(project)
                    )


                }

            }
        }

    }
}