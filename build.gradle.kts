// Top-level build file where you can add configuration options common to all sub-projects/modules.
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.room) apply false
    id("org.jetbrains.kotlinx.kover") version "0.8.3" apply true
}

subprojects {
    apply(plugin = "org.jetbrains.kotlinx.kover")

    tasks.withType<Test>().configureEach {
        doFirst {
            if (!temporaryDir.exists()) {
                temporaryDir.mkdirs()
            }
        }
    }

    extensions.configure<KoverProjectExtension> {
        reports {
            filters {
                excludes {
                    classes("*.di.*")
                    classes("*.BuildConfig")
                    classes("*_Impl")
                    classes("*.framework.theme.*")
                    classes("*.navigation.*")
                    classes("*.domain.model.helpers.*")
                    classes("**/*_Impl")

                }
            }
        }
    }

    pluginManager.withPlugin("com.android.base") {
    }
}
extensions.configure<KoverProjectExtension> {
    reports {
        filters {
            excludes {
                classes("*.di.*")
                classes("*.BuildConfig")
                classes("*_Impl")
                classes("*.navigation.*")
                classes("*.framework.theme.*")
                classes("*.domain.model.helpers.*")
                classes("**/*_Impl")
            }
        }
    }

    merge {
        subprojects()
    }
}