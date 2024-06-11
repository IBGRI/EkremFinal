import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("com.squareup.sqldelight")
}

val coroutineVersion = "1.5.0-native-mt"
val sqldelightVersion = "1.5.0"
val turbineVersion = "0.5.1"

group = "com.example"
version = "1.0-SNAPSHOT"


android {
    configurations {
        create("androidTestApi")
        create("androidTestDebugApi")
        create("androidTestReleaseApi")
        create("testApi")
        create("testDebugApi")
        create("testReleaseApi")
    }
}

kotlin {
    android()
    ios {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }
    sourceSets {
        // experimental opt-ins only in tests for Turbine usage
        matching {
            it.name.endsWith("Test")
        }.configureEach {
            languageSettings.useExperimentalAnnotation("kotlin.time.ExperimentalTime")
            languageSettings.useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
        }

        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")

                implementation("com.squareup.sqldelight:coroutines-extensions:$sqldelightVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))

                implementation("app.cash.turbine:turbine:$turbineVersion")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:android-driver:$sqldelightVersion")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")

                implementation("com.squareup.sqldelight:sqlite-driver:$sqldelightVersion")
            }
        }
    }
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
    }
}

sqldelight {
    database("ToDoDatabase") {
        packageName = "com.ekrembgr.todo.shared.db"
    }
}


