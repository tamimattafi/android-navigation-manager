package com.tamimattafi.navigation.core.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.tamimattafi.navigation.core.NavigationContract.*
import com.tamimattafi.navigation.core.animation.AnimationSet
import com.tamimattafi.navigation.core.fragments.BaseNavigationFragment


abstract class BaseNavigationActivity<F: BaseNavigationFragment> : AppCompatActivity(), Navigator<F> {

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
    * This is the current visible child fragment
    * If the current visible fragment is the base fragment or no fragment at all, a null is returned instead
    *
    * */
    final override val currentFragment: F?
        get() = (supportFragmentManager.findFragmentById(rootId) as? F)


    /*
    * #DESCRIPTION
    *
    * This is the current base fragment
    * If no base fragment was attached, a null is returned instead
    *
    * */
    final override var baseFragment: F? = null


    /*
    * #DESCRIPTION
    *
    * This is the current ActivityResultReceiver, if a call back is returned from startActivityForResult
    * fragment's fun onReceiveActivityResult(requestCode, resultCode, data) will be triggered
    *
    * */
    private var currentResultReceiver: ActivityResultReceiver? = null


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onActivityCreated()
        setContentView(layoutId)
        setUpBackStackListener()
        onViewCreated(savedInstanceState)

    }

    override fun restartCurrentFragment() {
        (currentFragment ?: baseFragment)?.let { fragment ->
            remove(fragment)
            reAttach(fragment)
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

    override fun switchTo(fragment: F, addCurrentToBackStack: Boolean) {
        supportFragmentManager.inTransaction {
            handleAnimationSet(fragment.animationSet).replace(rootId, fragment, fragment.fragmentName).handleBackStack(addCurrentToBackStack)
        }
    }

    override fun navigateTo(fragment: F, addCurrentToBackStack: Boolean) {
        supportFragmentManager.inTransaction {
            handleAnimationSet(fragment.animationSet).add(rootId, fragment, fragment.fragmentName).handleBackStack(addCurrentToBackStack)
        }
    }

    override fun remove(fragment: F) {
        supportFragmentManager.inTransaction { remove(fragment) }
        popBackStack()
    }

    override fun restartActivity() {
        finish()
        startActivity(intent)
    }

    private fun popBackStack() {
        supportFragmentManager.popBackStack()
    }

    private fun reAttach(fragment: F) {
        supportFragmentManager.inTransaction {
            replace(rootId, fragment.javaClass.newInstance().also { it.arguments = fragment.arguments }, fragment.fragmentName)
        }
    }

    private fun setUpBackStackListener() {
        supportFragmentManager.addOnBackStackChangedListener {
            notifyCurrentFragmentSelection()
        }
    }

    private fun notifyCurrentFragmentSelection() {
        (currentFragment as? SelectionListener)?.onSelected()
    }

    protected fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction): FragmentManager
            = this.also {
                beginTransaction().func().commit()
                onFragmentAttached(currentFragment!!)
            }

    open fun onFragmentAttached(fragment: F) {
        Log.d(fragment.fragmentName, "Attached")
    }

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