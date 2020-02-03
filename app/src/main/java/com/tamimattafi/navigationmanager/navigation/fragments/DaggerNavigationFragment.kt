package com.tamimattafi.navigationmanager.navigation.fragments

import android.content.res.Configuration
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tamimattafi.mvputils.KeyboardUtils.hideKeyboardImplicit
import com.tamimattafi.navigationmanager.navigation.DaggerNavigationContract
import javax.inject.Inject

abstract class DaggerNavigationFragment : DaggerBaseFragment(),
    DaggerNavigationContract.SelectionListener {

    @Inject
    lateinit var navigator: DaggerNavigationContract.NavigationManager

    open var fragmentName: String = this.javaClass.simpleName

    open var handleViewOrientation: Boolean = false

    override fun onDestroyView() {
        super.onDestroyView()
        navigationActivity.hideKeyboardImplicit()
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
            LayoutInflater.from(navigationActivity)
                .inflate(layoutId, this)
        }
    }

    fun restart() {
        navigator.restartCurrentScreen()
    }

    fun finish() {
        navigator.finishScreen(this)
    }
}