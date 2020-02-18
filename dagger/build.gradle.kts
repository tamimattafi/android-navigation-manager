plugins {
    id(Plugins.BASE_GRADLE_CONFIG)
    id(Plugins.KOTLIN_KAPT)
}

dependencies {
    implementation(project(Modules.CORE))
    implementation(Dependencies.DAGGER)
    implementation(Dependencies.DAGGER_ANDROID)
    implementation(Dependencies.DAGGER_ANDROID_SUPPORT)
    kapt(Dependencies.DAGGER_COMPILLER)
    kapt(Dependencies.DAGGER_ANDROID_PROCESSOR)

}