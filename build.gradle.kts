import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("app.cash.sqldelight") version "2.0.0"
}

group = "TymurDesc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

sqldelight {
    databases {
        create("EclipseDb") {
            packageName.set("example")
        }
    }
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation("com.darkrockstudios:mpfilepicker:3.1.0")
    implementation("app.cash.sqldelight:sqlite-driver:2.0.0")
}

compose.desktop {
    application {
        mainClass = "gui.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Eclipse"
            packageVersion = "1.4.0"
            description = "TPV(Point of Sale) for Eclipse Bar"
            vendor = "Timasostima&RedondoDEV"
            includeAllModules = true
            windows {
                iconFile.set(project.file("src/main/resources/Logo.ico"))
                includeAllModules = true
            }
        }
    }
}
