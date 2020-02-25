package com.tamimattafi.navigation.core.animation

import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import com.tamimattafi.navigation.core.R.anim.*

class AnimationSet() {

    @AnimatorRes @AnimRes
    var enterAnimation: Int = enter
    private set

    @AnimatorRes @AnimRes
    var exitAnimation: Int = exit
    private set

    @AnimatorRes @AnimRes
    var popEnterAnimation: Int = pop_enter
    private set

    @AnimatorRes @AnimRes
    var popExitAnimation: Int = pop_exit
    private set

    constructor(enterAnimation: Int, exitAnimation: Int, popEnterAnimation: Int, popExitAnimation: Int): this() {
        this.enterAnimation = enterAnimation
        this.exitAnimation = exitAnimation
        this.popEnterAnimation = popEnterAnimation
        this.popExitAnimation = popExitAnimation
    }

    fun rebuild(): Builder
        = Builder(this)

    fun copy(): AnimationSet
        = AnimationSet(
            enterAnimation,
            exitAnimation,
            popEnterAnimation,
            popExitAnimation
        )

    companion object {

        class Builder {

            private val instance: AnimationSet

            constructor() {
                instance = AnimationSet()
            }

            constructor(default: AnimationSet) {
                instance = default.copy()
            }

            fun setEnterAnimation(@AnimatorRes @AnimRes enterAnimation: Int): Builder
                = this.also { instance.enterAnimation = enterAnimation }

            fun setExitAnimation(@AnimatorRes @AnimRes exitAnimation: Int): Builder
                = this.also { instance.exitAnimation = exitAnimation }

            fun setPopEnterAnimation(@AnimatorRes @AnimRes popEnterAnimation: Int): Builder
                = this.also { instance.popEnterAnimation = popEnterAnimation }

            fun setPopExitAnimation(@AnimatorRes @AnimRes popExitAnimation: Int): Builder
                = this.also { instance.popExitAnimation = popExitAnimation }

            fun build(): AnimationSet = instance

        }

        var DEFAULT: AnimationSet? = AnimationSet()

        val SLIDE_LEFT: AnimationSet
        get() = AnimationSet(
            enter,
            exit,
            pop_enter,
            pop_exit
        )

        val SLIDE_RIGHT: AnimationSet
        get() = AnimationSet(
            pop_enter,
            pop_exit,
            enter,
            exit
        )

    }
}