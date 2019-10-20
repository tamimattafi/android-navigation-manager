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
    implementation 'com.github.tamimattafi:NavigationManager:1.0.1'
}
```

**2- Implementation**:

1- Make your host activity extend **NavigationActivity**: 

```kotlin
class AppActivity : NavigationActivity() {

	@Inject
	lateinit var launcher: Launcher

	override val layoutId: Int = R.layout.activity_main
	override var rootId: Int = R.id.root

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		requestAttachBaseScreen(launcher.getLaunchFragment())
	}

}
```
**- layoutId**: activity's layout.

**- rootId**: the view that should be replaced on interaction such as FrameLayout.

**- onActivityCreated(savedInstanceState: Bundle?)** is called after **setContentView(layoutId)**


2- Make your fragments extend **NavigationContract.NavigationFragment()**:



```kotlin
class AddRoutineFragment : NavigationContract.NavigationFragment(), View {

	@Inject
	lateinit var presenter: Presenter

	override var fragmentName: String = "add-routine-fragment"
	override val layoutId: Int = R.layout.fragment_add_routine
        
        ...
}
```    
    
**- fragmentName**: fragment backstack's name.

**- layoutId**: fragment's layout.

3- Provide NavigationManager, Activity and Context to your fragments using Dagger2:

```kotlin
@Module
abstract class ActivityModule {

	@ContributesAndroidInjector(modules = [MainFragments::class])
	abstract fun mainActivity(): AppActivity

	@Binds
	abstract fun bindNavigationManager(mainActivity: AppActivity): NavigationContract.NavigationManager

	@Binds
	abstract fun bindActivity(mainActivity: AppActivity): Activity

	@Binds
	abstract fun bindContext(mainActivity: AppActivity): Context

	...

}
```
     
**You can now start navigating!**

- **Available NavigationManager methods:**:

```kotlin
interface NavigationManager {
        
	//Clears previous backstack and attachs a new fragment as base fragment
	fun requestAttachBaseScreen(fragment: NavigationFragment)

	//Slides a fragment from the left added to the backstack on the base fragment
	fun requestSlideLeftScreen(fragment: NavigationFragment)

	//Slides a fragment from the right added to the backstack over the base fragment
	fun requestSlideRightScreen(fragment: NavigationFragment)

	//Fades a fragment added to the backstack over the base fragment
	fun requestFadeInScreen(fragment: NavigationFragment)

	//Attaches a fragment without animation added to the backstack over the base fragment
	fun requestAttachScreen(fragment: NavigationFragment)
	
	//Restarts the current screen (new instance)
	fun restartCurrentScreen()

	//Requests the activity to invoke back button
	fun requestBackPress()

	//Starts an activity for result handled by the resultReceiver
	fun requestActivityForResult(
		resultReceiver: ActivityResultReceiver,
		intent: Intent,
		requestCode: Int
		)

	//Requests the activity to restart
	fun requestRestart()

}
```
          
- **Available NavigationFragment behaviours**:

```kotlin

interface NavigationContract {

	//When implemented by the current visible fragment, any back press will call its onBackPressed instead of activity's one.
	//Returning true will trigger activity's on backpress, returning false will not.
	interface BackPressController {
		fun onBackPressed(): Boolean
	}

	//When implemented by the current visible fragment, it will be notified onAttach.
	//becareful from using any view components because they might be still null when this method is called
	interface SelectionListener {
		fun onSelected()
	}

	//Makes the fragment listen to ActivityResults
	interface ActivityResultReceiver {
		fun onReceiveActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
	}

}
	  
	  
```
# How to navigate?

  - If you followed all steps correctly, you will have an instance of NavigationManager as navigationManager in every attached fragment.
  - Calling navigationManager.yourMethod(YourFragment()) from a fragment or somewhere else will attach a new fragment to your screen.         

# Happy coding!
