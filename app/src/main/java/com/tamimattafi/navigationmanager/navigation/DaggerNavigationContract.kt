package com.tamimattafi.navigationmanager.navigation

import android.content.Intent
import com.tamimattafi.navigationmanager.navigation.fragments.DaggerNavigationFragment


interface DaggerNavigationContract {

    interface NavigationManager {
        fun requestAttachBaseScreen(fragment: DaggerNavigationFragment)
        fun requestSlideLeftScreen(fragment: DaggerNavigationFragment)
        fun requestSlideRightScreen(fragment: DaggerNavigationFragment)
        fun requestFadeInScreen(fragment: DaggerNavigationFragment)
        fun requestAttachScreen(fragment: DaggerNavigationFragment)
        fun restartCurrentScreen()
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