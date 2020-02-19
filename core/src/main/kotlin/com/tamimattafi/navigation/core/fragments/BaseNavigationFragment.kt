package com.tamimattafi.navigation.core.fragments

import android.util.Log
import com.tamimattafi.navigation.core.NavigationContract.SelectionListener
import com.tamimattafi.navigation.core.animation.AnimationSet

abstract class BaseNavigationFragment : BaseFragment(), SelectionListener {

    /**
    * Fragment name that will be saved in back-stack and used for logging, make sure to have a unique name for each fragment type
    * Calling this.javaClass.name is a good idea if your fragment classes have unique names
    */
    abstract val fragmentName: String

    /**
    * Animation that will be played during each transaction.
    */
    open val animationSet: AnimationSet? = AnimationSet.DEFAULT

    /**
    * Will be called when a navigation to that fragment happens.
    * be careful from using any view components because they might still be null when this method is called
    */
    override fun onSelected() {
        Log.d(fragmentName, SELECTED_MESSAGE)
    }


    companion object {
        const val SELECTED_MESSAGE = "Navigation Fragment Selected"
    }

}