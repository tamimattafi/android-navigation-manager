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

    /*
    * #DESCRIPTION
    *
    * This is the layoutId of the desired view that will be passed to setContentView(...)
    *
    * */
    abstract val layoutId: Int



    /*
    * #DESCRIPTION
    *
    * This is the rootId of the desired view that will be replaced by fragments during navigation
    *
    * */
    abstract var rootId: Int


    /*
    * #DESCRIPTION
    *
    * This method will be called after the view is visible to the user
    * This is a suitable place to start your logic processing and navigation
    *
    * */
    abstract fun onViewCreated(savedInstanceState: Bundle?)

    /*
    * #DESCRIPTION
    *
    * This method will be called right after super.onCreate(...) and before the view is visible to the user
    * This is a suitable place to change themes and apply new styles
    *
    * */
    open fun onActivityCreated() {}



    /*
    * #DESCRIPTION
    *
    * This is the current visible child fragment
    * If the current visible fragment is the base fragment or no fragment at all, a null is returned instead
    *
    * */
    final override val currentFragment: DaggerNavigationFragment?
        get() = (supportFragmentManager.findFragmentById(rootId) as? DaggerNavigationFragment)


    /*
    * #DESCRIPTION
    *
    * This is the current base fragment
    * If no base fragment was attached, a null is returned instead
    *
    * */
    final override var baseFragment: DaggerNavigationFragment? = null


    /*
    * #DESCRIPTION
    *
    * This is the current ActivityResultReceiver, if a call back is returned from startActivityForResult
    * fragment's fun onReceiveActivityResult(requestCode, resultCode, data) will be triggered
    *
    * */
    private var currentResultReceiver: ActivityResultReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onActivityCreated()
        setContentView(layoutId)
        setUpBackStackListener()
        onViewCreated(savedInstanceState)

    }

    private fun setUpBackStackListener() {
        supportFragmentManager.addOnBackStackChangedListener {
            notifyCurrentFragmentSelection()
        }
    }

    private fun notifyCurrentFragmentSelection() {
        (currentFragment as? SelectionListener)?.onSelected()
    }

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
        (currentFragment ?: baseFragment)?.let { fragment ->
            remove(fragment)
            reAttach(fragment)
        }
    }

    private fun popBackStack() {
        supportFragmentManager.popBackStack()
    }

    private fun reAttach(fragment: DaggerNavigationFragment) {
        supportFragmentManager.inTransaction {
            replace(rootId, fragment.javaClass.newInstance().also { it.arguments = fragment.arguments }, fragment.fragmentName)
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
            handleAnimationSet(fragment.animationSet).replace(rootId, fragment, fragment.fragmentName).handleBackStack(addCurrentToBackStack)
        }
    }

    override fun navigateTo(fragment: DaggerNavigationFragment, addCurrentToBackStack: Boolean) {
        supportFragmentManager.inTransaction {
            handleAnimationSet(fragment.animationSet).add(rootId, fragment, fragment.fragmentName).handleBackStack(addCurrentToBackStack)
        }
    }

    override fun remove(fragment: DaggerNavigationFragment) {
        supportFragmentManager.inTransaction { remove(fragment) }
        popBackStack()
    }

    override fun restartActivity() {
        finish()
        startActivity(intent)
    }


    protected fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction): FragmentManager
            = this.also { beginTransaction().func().commit() }

    @Deprecated(Deprecation.NAVIGATE_TO_REPLACEMENT, level = DeprecationLevel.WARNING)
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