package com.tamimattafi.navigationmanager.navigation.fragments

import android.util.Log
import com.tamimattafi.navigationmanager.navigation.DaggerNavigationContract.*
import com.tamimattafi.navigationmanager.navigation.animation.AnimationSet
import javax.inject.Inject

abstract class DaggerNavigationFragment : BaseFragment(), SelectionListener {


    /*
    * #DESCRIPTION
    *
    * This is an instance of the navigation host activity that will be provided each time the fragment was attached
    * An injection module should be provided for Dagger in order to make it able to provide this dependency
    * For more information, visit our github page: https://github.com/tamimattafi/android-dagger-navigationmanager
    *
    * */
    @Inject
    protected lateinit var navigator: Navigator


    /*
    * #DESCRIPTION
    *
    * This is fragment name that will be saved in back-stack and used for logging, make sure to have a unique name for each fragment type
    * Calling this.javaClass.name is a good idea if your fragment classes have unique names
    *
    * */
    abstract var fragmentName: String


    /*
    * #DESCRIPTION
    *
    * This is the animation that will be played during each transaction.
    *
    * */
    open var animationSet: AnimationSet? = AnimationSet.DEFAULT


    /*
    * #DESCRIPTION
    *
    * onSelected() will be called when a navigation to that fragment happens.
    * be careful from using any view components because they might still be null when this method is called
    *
    * */
    override fun onSelected() {
        Log.i(fragmentName, "Selected")
    }


    /*
    * #DESCRIPTION
    *
    * This is a short-cut for navigating to a fragment that can be used as myFragment.navigate(attachToBackStack)
    *
    * */
    protected fun DaggerNavigationFragment.navigate(attachToBackStack: Boolean = false) {
        navigator.navigateTo(this, attachToBackStack)
    }


    /*
    * #DESCRIPTION
    *
    * This is a short-cut for switching to a fragment that can be used as myFragment.switch(attachToBackStack)
    *
    * */
    protected fun DaggerNavigationFragment.switch(attachToBackStack: Boolean = false) {
        navigator.switchTo(this, attachToBackStack)
    }


    /*
    * #DESCRIPTION
    *
    * This is a short-cut for restarting navigation from a fragment that can be used as myFragment.restartNavigation()
    *
    * */
    protected fun DaggerNavigationFragment.restartNavigation() {
        navigator.restartNavigationFrom(this)
    }


    /*
    * #DESCRIPTION
    *
    * This is a short-cut for restarting the current fragment if it's visible to the user
    *
    * */
    fun restartIfVisible() {
        navigator.restartCurrentFragment()
    }

}