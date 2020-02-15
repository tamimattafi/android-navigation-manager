package com.tamimattafi.navigationmanager.navigation

import android.content.Intent
import com.tamimattafi.navigationmanager.navigation.animation.AnimationSet
import com.tamimattafi.navigationmanager.navigation.fragments.DaggerNavigationFragment
import com.tamimattafi.navigationmanager.navigation.values.Deprecation.NAVIGATE_TO_REPLACEMENT


interface DaggerNavigationContract {


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




        /* THESE METHODS WILL BE DELETED IN FUTURE RELEASES */


        @Deprecated(NAVIGATE_TO_REPLACEMENT, level = DeprecationLevel.WARNING)
        fun requestAttachBaseScreen(fragment: DaggerNavigationFragment)


        @Deprecated(NAVIGATE_TO_REPLACEMENT, level = DeprecationLevel.WARNING)
        fun requestSlideLeftScreen(fragment: DaggerNavigationFragment, addToBackStack: Boolean = true, replace: Boolean = true)


        @Deprecated(NAVIGATE_TO_REPLACEMENT, level = DeprecationLevel.WARNING)
        fun requestSlideRightScreen(fragment: DaggerNavigationFragment, addToBackStack: Boolean = true, replace: Boolean = true)


        @Deprecated(NAVIGATE_TO_REPLACEMENT, level = DeprecationLevel.WARNING)
        fun requestFadeInScreen(fragment: DaggerNavigationFragment, addToBackStack: Boolean = true, replace: Boolean = true)


        @Deprecated(NAVIGATE_TO_REPLACEMENT, level = DeprecationLevel.WARNING)
        fun requestAttachScreen(fragment: DaggerNavigationFragment, addToBackStack: Boolean = true, replace: Boolean = true)


    }



    interface BackPressController {
        fun onBackPressed(): Boolean
    }

    interface SelectionListener {
        fun onSelected()
    }

    interface ActivityResultReceiver {
        fun onReceiveActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    }
}