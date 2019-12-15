package com.tamimattafi.navigationmanager.navigation

import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tamimattafi.navigationmanager.utils.KeyboardUtils
import javax.inject.Inject


interface NavigationContract {

    interface NavigationManager {
        fun requestAttachBaseScreen(fragment: NavigationFragment)
        fun requestSlideLeftScreen(fragment: NavigationFragment)
        fun requestSlideRightScreen(fragment: NavigationFragment)
        fun requestFadeInScreen(fragment: NavigationFragment)
        fun requestAttachScreen(fragment: NavigationFragment)
        fun restartCurrentScreen()
        fun requestBackPress()
        fun setActivityReceiver(resultReceiver: ActivityResultReceiver)
        fun requestActivityForResult(intent: Intent, requestCode: Int)

        fun requestRestart()
    }

    abstract class NavigationFragment : BaseFragment(),
        SelectionListener {

        @Inject
        lateinit var navigationManager: NavigationManager

        abstract var fragmentName: String

        open var handleViewOrientation: Boolean = false

        override fun onDestroyView() {
            super.onDestroyView()
            KeyboardUtils.hide(context!!)
        }

        override fun onSelected() {
            Log.i(fragmentName, "Selected")
        }

        override fun onConfigurationChanged(newConfig: Configuration) {
            super.onConfigurationChanged(newConfig)
            if (handleViewOrientation) populateViewForOrientation()
        }

        private fun populateViewForOrientation() {
            (fragmentView as ViewGroup).apply {
                removeAllViewsInLayout()
                LayoutInflater.from(appActivity).inflate(layoutId, this)
            }
        }

        fun restart() {
            navigationManager.restartCurrentScreen()
        }

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