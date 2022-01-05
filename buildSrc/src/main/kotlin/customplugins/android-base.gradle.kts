package customplugins

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("com.github.dcendents.android-maven")
}

group = "com.github.tamimattafi"


android {
    compileSdkVersion(AndroidConfiguration.COMPILE_SDK_VERSION)

    defaultConfig {
        versionCode = AndroidConfiguration.VERSION_CODE
        versionName = AndroidConfiguration.VERSION_NAME
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(Dependencies.KOTLIN)
    implementation(Dependencies.KTX)
    implementation(Dependencies.APPCOMPAT)
    implementation(Dependencies.FRAGMENT_KTX)
}