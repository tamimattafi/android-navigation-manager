# NavigationManager for Android
A Helper library for easy navigation between fragments.

# Installation:

1. Add this to your Project level build.gradle file:

```gradle
allprojects {
    repositories {
	maven { url 'https://jitpack.io' }
    }
}
```
2. Add this to your dependencies in your App level build.gradle file:


```gradle
dependencies {

    //Basic implementation
    implementation "com.github.tamimattafi.android-navigation-manager:basic:$latest_version"
    

    //OPTIONAL IMPLEMENTATIONS

    /*
    * # CORE
    * This is the core module of this library
    * This should only be included if you want to extend the core functionality in a different direction
    * This must be included if you want to override transaction animations
    */
    implementation "com.github.tamimattafi.android-navigation-manager:core:$latest_version"
   
   
    /*
    * # DAGGER SUPPORT
    * This module has support for dagger support injectors
    * It has the same functionality as the basic module so choosing one of them depends on your project requirements
    * Why not both? It is possible to use both modules alongside each other!
    */
    implementation "com.github.tamimattafi.android-navigation-manager:dagger:$latest_version"
   

   
}
```
# Docs
The code is well documented. A quick reading [here](https://github.com/tamimattafi/android-navigation-manager/blob/master/core/src/main/java/com/tamimattafi/navigation/core/NavigationContract.kt) would make everything clear to you!

# Happy coding!
