package com.tamimattafi.navigationmanager.navigation

import android.content.Intent
import android.util.Log
import com.tamimattafi.navigationmanager.utils.KeyboardUtils
import javax.inject.Inject


interface NavigationContract {

    interface NavigationManager {
        fun requestAttachBaseScreen(fragment: NavigationFragment)
        fun requestSlideLeftScreen(fragment: NavigationFragment)
        fun requestSlideRightScreen(fragment: NavigationFragment)
        fun requestFadeInScreen(fragment: NavigationFragment)
        fun requestAttachScreen(fragment: NavigationFragment)
        fun restartCurrent()
        fun requestBackPress()
        fun requestActivityForResult(
            resultReceiver: ActivityResultReceiver,
            intent: Intent,
            requestCode: Int
        )

        fun requestRestart()
    }

    abstract class NavigationFragment : BaseFragment(),
        SelectionListener {

        @Inject
        lateinit var navigationManager: NavigationManager

        abstract var fragmentName: String

        override fun onDestroyView() {
            super.onDestroyView()
            KeyboardUtils.hide(context!!)
        }

        override fun onSelected() {
            Log.i(fragmentName, "Selected")
        }

        fun restart() {
            navigationManager.restartCurrent()
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