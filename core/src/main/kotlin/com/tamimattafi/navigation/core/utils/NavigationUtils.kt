package com.tamimattafi.navigation.core.utils

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.tamimattafi.navigation.core.animation.AnimationSet

internal object NavigationUtils {

    fun FragmentManager.inTransaction(
        transaction: FragmentTransaction.() -> FragmentTransaction
    ): FragmentManager = this.also {
        beginTransaction().transaction().commit()
    }

    fun FragmentTransaction.handleBackStack(
        addCurrentToBackStack: Boolean,
        backStackName: String?
    ): FragmentTransaction = if (addCurrentToBackStack) {
        addToBackStack(backStackName)
    } else this

    fun FragmentTransaction.handleAnimationSet(
        animationSet: AnimationSet?,
        applyAnimation: Boolean
    ): FragmentTransaction = animationSet?.takeIf { applyAnimation }?.let { set ->
        setCustomAnimations(
            set.enterAnimation,
            set.exitAnimation,
            set.popEnterAnimation,
            set.popExitAnimation
        )
    } ?: this
}