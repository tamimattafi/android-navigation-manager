plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdkVersion(AndroidConfiguration.COMPILE_SDK_VERSION)

    defaultConfig {
        applicationId = "com.tamimattafi.navigation.sample"
        versionCode = AndroidConfiguration.VERSION_CODE
        versionName = AndroidConfiguration.VERSION_NAME
        minSdkVersion(AndroidConfiguration.MIN_SDK_VERSION)
        targetSdkVersion(AndroidConfiguration.TARGET_SDK_VERSION)
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
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
    implementation(project(Modules.CORE))
    implementation(project(Modules.BASIC))
}