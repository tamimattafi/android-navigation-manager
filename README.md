# NavigationManager for Android
A Dagger2 Based library for easy navigation between fragments.

# Usage:

**1 - Installation**:

1- Add this to your Project level build.gradle file:

```gradle
allprojects {
    repositories {
	maven { url 'https://jitpack.io' }
    }
}
```
  2- Add this to your dependencies in your App level build.gradle file:

```gradle
dependencies {
   implementation "com.github.tamimattafi:android-dagger-navigationmanager:&lastest_version"
}
```

**2- Implementation**:

1- Make your host activity extend **NavigationActivity**: 

```kotlin
class AppActivity : DaggerNavigationActivity() {

        
        ...
	
        //This is the layout id of your activity that will be used in setContentView(layoutId)
	override val layoutId: Int = R.layout.activity_main
	
	//This is the view id of your layout that will be used to host fragments
	override var rootId: Int = R.id.root

        //This will be called after setContentView(layoutId)
	override fun onActivityCreated(savedInstanceState: Bundle?) {
		//You can attach your base fragment here using: restartNavigationFrom(YourFragment())
	}
	
	...

}
```

2- Make your fragments extend **DaggerNavigationFragment()**:



```kotlin
class AddRoutineFragment : DaggerNavigationFragment(), View {

	...

        //This is fragment name that will be used in back-stack
	override var fragmentName: String = "add-routine-fragment"
	
	//This is fragment layout id that will be used in onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
	override val layoutId: Int = R.layout.fragment_add_routine
        
        ...
}
```    

3- Provide NavigationManager, Activity and Context to your fragments using Dagger2:

```kotlin
@Module
abstract class ActivityModule {

	...

        //Bind your host activity instance to the Navigator interface in order to inject it in every child fragment
	@Binds
	abstract fun bindNavigator(navigationActivity: NavigationActivity): NavigationContract.Navigator

	...

}
```
     
**You can now start navigating!**

- **Available NavigationManager methods:**:

```kotlin
interface NavigationContract {
        
	 /*
    * #DESCRIPTION
    *
    * This is the API exposed to child fragments by the host activity
    * Every fragment will have an instance of this API
    *
    * */
    interface Navigator {

        /*
        * #DESCRIPTION
        *
        * This variable returns the current visible child fragment
        * If the current visible fragment is a base fragment, a null will be returned instead
        *
        * */
        val currentFragment: DaggerNavigationFragment?



        /*
        * #DESCRIPTION
        *
        * This variable returns the current base fragment
        * If no base fragment is attached, a null will be returned instead
        *
        * */
        var baseFragment: DaggerNavigationFragment?


        /*
        * #DESCRIPTION
        *
        * This method will attach a new fragment on top of the current fragment without replacing it,
        * Which means that the bottom fragment will still be living and it's data won't be cleared from memory.
        * This method is used for light-weight fragments such as menus or dialogs.
        * Fragments that store a quite large amount of data should navigate using switchTo in order to let the
        * os clear the unused data and load it again when needed.
        *
        *
        * #PARAMETERS
        *
        * - fragment (required): the desired fragment to be navigated to.
        * - addToBackStack (default set to true): if set to true, the current fragment before the attached fragment will be stored in the back stack
        *   and loaded again on back press, if set to false, the current fragment will be finished.
        *
        * */
        fun navigateTo(
            fragment: DaggerNavigationFragment,
            addCurrentToBackStack: Boolean = true
        )

        /*
        * #DESCRIPTION
        *
        * This method will attach a new fragment replacing the current one,
        * Which means that the bottom fragment will be destroyed and it's data will be cleared from memory.
        * This method is used for heavy fragments such as those that use Lists and ImageViews.
        * The os will save instance and will load it again when the user navigates back to this fragment.
        *
        *
        * #PARAMETERS
        *
        * - fragment (required): the desired fragment to be switched to (required)
        * - addToBackStack (default set to true): if set to true, the current fragment before the attached
        *   fragment will be stored in the back-stack and loaded again on back press, if set to false, the current fragment will be finished.
        *
        * */
        fun switchTo(
            fragment: DaggerNavigationFragment,
            addCurrentToBackStack: Boolean = true
        )


        /*
        * #DESCRIPTION
        *
        * This method will replace base fragment and popup the back-stack
        * Which means, all previously attached fragments will be destroyed and won't be reached on back-press.
        * This method is used for large navigation steps such as going from login screen to the main screen.
        *
        *
        * #PARAMETERS
        *
        * - fragment (required): the desired fragment to be attached as base fragment (required).
        *
        * */
        fun restartNavigationFrom(
            fragment: DaggerNavigationFragment
        )


        /*
        * #DESCRIPTION
        *
        * This method will restart the current child fragment, if no child fragment is attached, it will restart
        * the base fragment
        * This method is used when you want a fresh instance of the current fragment with fresh dependencies
        *
        * */
        fun restartCurrentFragment()


        /*
        * #DESCRIPTION
        *
        * This method will trigger activity onBackPress method
        * It is used for navigation buttons such as cancel, back, previous etc
        *
        * */
        fun performBackPress()


        /*
        * #DESCRIPTION
        *
        * This method will change the current activity result receive to this instance
        * When an activity result is returned, onReceiveActivityResult(requestCode, resultCode, data) in ActivityResultReceiver will be triggered
        *
        * #PARAMETERS
        * resultReceiver (required): the desired activity result listener
        *
        * */
        fun setActivityReceiver(resultReceiver: ActivityResultReceiver)

        /*
        * #DESCRIPTION
        *
        * This method will start an activity for a result
        * It is used for picking files, or recording audio etc
        *
        * #PARAMETERS
        *
        * intent (required): The intent that will start the activity
        * requestCode (required): The code that will be sent with the request and returned later to ActivityResultReceiver for checking
        *
        * */
        fun requestActivityForResult(intent: Intent, requestCode: Int)


        /*
        * #DESCRIPTION
        *
        * This method will finish and restart the navigation host activity
        * It's used to apply new settings such as themes, localization language etc
        *
        * */
        fun restartActivity()

    }

}
```
          
- **Available NavigationFragment behaviours**:

```kotlin

interface NavigationContract {

	/*
	 * #DESCRIPTION
	 * 
	 * When implemented by the current visible fragment, any back press will call its onBackPressed instead of activity's one.
	 * Returning true will trigger activity's on backpress, returning false will not.
	 *
	 * */
	interface BackPressController {
		fun onBackPressed(): Boolean
	}

	/* 
	 * #DESCRIPTION
	 *
	 * When implemented by the current visible fragment, it will be notified onAttach.
	 * becareful from using any view components because they might be still null when this method is called
	 *
	 * */
	interface SelectionListener {
		fun onSelected()
	}

	/*
	 * #DESCRIPTION
	 *
	 * Makes the fragment listen to ActivityResults
	 */
	interface ActivityResultReceiver {
		fun onReceiveActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
	}

}
	  
	  
```
# How to navigate?

  - If you followed all steps correctly, you will have an instance of Navigator as navigator in every attached fragment.
  - Calling navigator.navigateTo(YourFragment()) from a fragment or somewhere else will attach a new fragment to your screen.         

# Happy coding!
