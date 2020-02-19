package customplugins

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(AndroidConfiguration.COMPILE_SDK_VERSION)

    defaultConfig {
        minSdkVersion(AndroidConfiguration.MIN_SDK_VERSION)
        targetSdkVersion(AndroidConfiguration.TARGET_SDK_VERSION)
    }

    sourceSets {
        getByName("main") {
            java.srcDir("src/main/kotlin")
        }
    }

    configurations {
        all {
            listOf(
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/license.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/notice.txt",
                "META-INF/ASL2.0",
                "META-INF/app_release.kotlin_module"
            ).forEach { exclude(it) }
        }
    }
}

dependencies {
    implementation(Dependencies.KOTLIN)
    implementation(Dependencies.KTX)
    implementation(Dependencies.APPCOMPAT)
}