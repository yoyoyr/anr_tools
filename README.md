# PrivacyMethodHooker
关联文章：


probuf环境搭建：
    安装protobuf : brew install protobuf
    gradle引入：
    'com.google.protobuf:protobuf-gradle-plugin:0.8.15'
    apply plugin: 'com.google.protobuf'
    protobuf {
        protoc {
            // You still need protoc like in the non-Android case
            artifact = "com.google.protobuf:protoc:3.7.0:${protoc_platform}"
        }
        plugins {
            javalite {
                // The codegen for lite comes as a separate artifact
                artifact = "com.google.protobuf:protoc-gen-javalite:3.0.0:${protoc_platform}"
            }
        }
        generateProtoTasks {
            all().each { task ->
                task.builtins {
                    // In most cases you don't need the full Java output
                    // if you use the lite output.
                    remove java
                }
                task.plugins {
                    javalite {}
                }
            }
        }
    }
