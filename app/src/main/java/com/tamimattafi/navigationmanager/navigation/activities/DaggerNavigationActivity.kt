package com.tamimattafi.navigationmanager.navigation.activities

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.tamimattafi.navigationmanager.R
import com.tamimattafi.navigationmanager.navigation.DaggerNavigationContract.*
import com.tamimattafi.navigationmanager.navigation.animation.AnimationSet
import com.tamimattafi.navigationmanager.navigation.fragments.DaggerNavigationFragment
import com.tamimattafi.navigationmanager.navigation.values.Deprecation
import dagger.android.support.DaggerAppCompatActivity


abstract class DaggerNavigationActivity : DaggerAppCompatActivity(), Navigator {

    abstract val layoutId: Int
    abstract var rootId: Int

    abstract fun onViewCreated(savedInstanceState: Bundle?)

    final override val currentFragment: DaggerNavigationFragment?
        get() = (supportFragmentManager.findFragmentById(rootId) as? DaggerNavigationFragment)

    final override var baseFragment: DaggerNavigationFragment? = null

    private var currentResultReceiver: ActivityResultReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onActivityCreated()
        setContentView(layoutId)
        onViewCreated(savedInstanceState)
        supportFragmentManager.addOnBackStackChangedListener {
            (currentFragment as? SelectionListener)?.onSelected()
        }
    }

    open fun onActivityCreated() {}


    @Deprecated(Deprecation.NAVIGATE_TO_REPLACEMENT, level = DeprecationLevel.WARNING)
    override fun requestAttachBaseScreen(fragment: DaggerNavigationFragment) {
        supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager.inTransaction {
            replace(rootId, fragment)
        }
        baseFragment = fragment
    }

    @Deprecated(Deprecation.NAVIGATE_TO_REPLACEMENT, level = DeprecationLevel.WARNING)
    override fun requestSlideLeftScreen(fragment: DaggerNavigationFragment, addToBackStack: Boolean, replace: Boolean) {
        supportFragmentManager.inTransaction {
            setCustomAnimations(
                R.anim.enter,
                R.anim.exit,
                R.anim.pop_enter,
                R.anim.pop_exit
            ).handleTransitionType(fragment, replace).handleBackStack(addToBackStack)
        }
    }


    @Deprecated(Deprecation.NAVIGATE_TO_REPLACEMENT, level = DeprecationLevel.WARNING)
    override fun requestSlideRightScreen(fragment: DaggerNavigationFragment, addToBackStack: Boolean, replace: Boolean) {
        supportFragmentManager.inTransaction {
            setCustomAnimations(
                R.anim.pop_enter,
                R.anim.pop_exit,
                R.anim.enter,
                R.anim.exit
            ).handleTransitionType(fragment, replace).handleBackStack(addToBackStack)
        }
    }

    @Deprecated(Deprecation.NAVIGATE_TO_REPLACEMENT, level = DeprecationLevel.WARNING)
    override fun requestFadeInScreen(fragment: DaggerNavigationFragment, addToBackStack: Boolean, replace: Boolean) {
        supportFragmentManager.inTransaction {
            setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            ).handleTransitionType(fragment, replace).handleBackStack(addToBackStack)
        }
    }

    @Deprecated(Deprecation.NAVIGATE_TO_REPLACEMENT, level = DeprecationLevel.WARNING)
    override fun requestAttachScreen(fragment: DaggerNavigationFragment, addToBackStack: Boolean, replace: Boolean) {
        supportFragmentManager.inTransaction {
            handleTransitionType(fragment, replace).handleBackStack(addToBackStack)
        }
    }

    override fun restartCurrentFragment() {
        currentFragment?.let {
            supportFragmentManager.inTransaction { remove(it) }
                .apply { popBackStack() }
                .inTransaction { add(rootId, it.javaClass.newInstance()).addToBackStack(it.fragmentName) }
        }
    }

    override fun onBackPressed() {
        (currentFragment as? BackPressController)?.let {
            if (it.onBackPressed()) super.onBackPressed()
        } ?: super.onBackPressed()
    }

    override fun performBackPress() {
        onBackPressed()
    }

    override fun setActivityReceiver(resultReceiver: ActivityResultReceiver) {
        this.currentResultReceiver = resultReceiver
    }

    override fun requestActivityForResult(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        currentResultReceiver?.onReceiveActivityResult(requestCode, resultCode, data)
    }

    override fun switchTo(fragment: DaggerNavigationFragment, addCurrentToBackStack: Boolean) {
        supportFragmentManager.inTransaction {
            handleAnimationSet(fragment.animationSet).handleTransitionType(fragment, true).handleBackStack(addCurrentToBackStack)
        }
    }

    override fun navigateTo(fragment: DaggerNavigationFragment, addCurrentToBackStack: Boolean) {
        supportFragmentManager.inTransaction {
            handleAnimationSet(fragment.animationSet).handleTransitionType(fragment, false).handleBackStack(addCurrentToBackStack)
        }
    }

    override fun restartActivity() {
        finish()
        startActivity(intent)
    }

    protected fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction): FragmentManager
            = this.also { beginTransaction().func().commit() }

    private fun FragmentTransaction.handleTransitionType(fragment: DaggerNavigationFragment, replace: Boolean): FragmentTransaction
            = if (replace) replace(rootId, fragment, fragment.fragmentName) else add(rootId, fragment, fragment.fragmentName)

    private fun FragmentTransaction.handleBackStack(addCurrentToBackStack: Boolean): FragmentTransaction
            = if (addCurrentToBackStack) addToBackStack(currentFragment?.fragmentName) else this

    private fun FragmentTransaction.handleAnimationSet(animationSet: AnimationSet?): FragmentTransaction
            = animationSet?.let { set ->
                setCustomAnimations(
                    set.enterAnimation,
                    set.exitAnimation,
                    set.popEnterAnimation,
                    set.popExitAnimation
                )
            } ?: this
}