import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    id("app.cash.sqldelight") version "2.3.2"

    kotlin("plugin.serialization") version "2.3.0"
}

repositories {
    google()
    mavenCentral()
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("feri.starter")
            srcDirs.setFrom("src/jvmMain/sqldelight")
        }
    }
}


kotlin {
    jvm()
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0")
            implementation("io.github.serpro69:kotlin-faker:1.6.0")
            implementation("io.github.pdvrieze.xmlutil:core:1.0.0-rc2")
            implementation("io.github.pdvrieze.xmlutil:serialization:1.0.0-rc2")

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation("app.cash.sqldelight:sqlite-driver:2.3.2")
            implementation("app.cash.sqldelight:primitive-adapters:2.3.2")
        }
    }
}


compose.desktop {
    application {
        mainClass = "feri.starter.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "DevTimer"
            packageVersion = "1.0.0"
            description = "Timer for developers"
            vendor = "Maercel"

            modules("java.sql")

            windows {
                iconFile.set(project.file("devtimer.ico"))
                menu = true
                shortcut = true
                // so user doesn't have to manually remove the previous version, and it just upgrades.
                // in powershell: [guid]::NewGuid()
                upgradeUuid = "f431edd2-feba-4959-a658-fc22ef4de4aa"
            }
        }
    }
}
