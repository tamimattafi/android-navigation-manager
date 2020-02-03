package com.tamimattafi.navigationmanager.navigation

import android.content.Intent
import com.tamimattafi.navigationmanager.navigation.fragments.DaggerNavigationFragment


interface DaggerNavigationContract {

    interface NavigationManager {
        fun requestAttachBaseScreen(fragment: DaggerNavigationFragment)
        fun requestSlideLeftScreen(fragment: DaggerNavigationFragment, addToBackStack: Boolean = true)
        fun requestSlideRightScreen(fragment: DaggerNavigationFragment, addToBackStack: Boolean = true)
        fun requestFadeInScreen(fragment: DaggerNavigationFragment, addToBackStack: Boolean = true)
        fun requestAttachScreen(fragment: DaggerNavigationFragment, addToBackStack: Boolean = true)
        fun restartCurrentScreen()
        fun removeScreen(fragment: DaggerNavigationFragment)
        fun requestBackPress()
        fun setActivityReceiver(resultReceiver: ActivityResultReceiver)
        fun requestActivityForResult(intent: Intent, requestCode: Int)
        fun requestRestart()
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