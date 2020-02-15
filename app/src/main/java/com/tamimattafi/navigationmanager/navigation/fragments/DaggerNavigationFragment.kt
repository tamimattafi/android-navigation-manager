package com.tamimattafi.navigationmanager.navigation.fragments

import android.util.Log
import com.tamimattafi.navigationmanager.navigation.DaggerNavigationContract.*
import com.tamimattafi.navigationmanager.navigation.animation.AnimationSet
import javax.inject.Inject

abstract class DaggerNavigationFragment : BaseFragment(), SelectionListener {

    @Inject
    lateinit var navigator: Navigator

    abstract var fragmentName: String
    open var animationSet: AnimationSet? = AnimationSet.DEFAULT

    override fun onSelected() {
        Log.i(fragmentName, "Selected")
    }

    protected fun DaggerNavigationFragment.navigate(attachToBackStack: Boolean = false) {
        navigator.navigateTo(this, attachToBackStack)
    }

    protected fun DaggerNavigationFragment.switch(attachToBackStack: Boolean = false) {
        navigator.switchTo(this, attachToBackStack)
    }

    protected fun DaggerNavigationFragment.restartNavigation() {
        navigator.restartNavigationFrom(this)
    }

    fun restart() {
        navigator.restartCurrentFragment()
    }

}