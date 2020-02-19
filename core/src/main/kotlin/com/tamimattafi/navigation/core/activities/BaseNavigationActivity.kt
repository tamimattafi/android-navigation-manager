@file:Suppress("UNCHECKED_CAST")

package com.tamimattafi.navigation.core.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.tamimattafi.navigation.core.NavigationContract.*
import com.tamimattafi.navigation.core.fragments.BaseNavigationFragment
import com.tamimattafi.navigation.core.utils.NavigationUtils.handleAnimationSet
import com.tamimattafi.navigation.core.utils.NavigationUtils.handleBackStack
import com.tamimattafi.navigation.core.utils.NavigationUtils.inTransaction


abstract class BaseNavigationActivity<F: BaseNavigationFragment> : AppCompatActivity(), Navigator<F> {

    /**
    * LayoutId of the desired view that will be passed to setContentView(...)
    */
    abstract val layoutId: Int

    /**
    * RootId of the desired view that will be replaced by fragments during navigation
    */
    abstract val rootId: Int

    /**
    * Current visible child fragment
    * If the current visible fragment is the base fragment or no fragment at all, a null is returned instead
    */
    final override val currentFragment: F?
        get() = (supportFragmentManager.findFragmentById(rootId) as? F)

    /**
    * Current base fragment
    * If no base fragment was attached, a null is returned instead
    */
    final override var baseFragment: F? = null

    /**
    * Current ActivityResultReceiver, if a call back is returned from startActivityForResult
    * fragment's fun onReceiveActivityResult(requestCode, resultCode, data) will be triggered
    */
    private var currentResultReceiver: ActivityResultReceiver? = null

    /**
    * Method will be called right before super.onCreate(...)
    * This is a suitable place to inject dependencies
    */
    open fun onActivityLaunched() {
        Log.d(TAG, ACTIVITY_LAUNCHED_MESSAGE)
    }

    /**
    * Method will be called right after super.onCreate(...) and before the view is visible to the user
    * This is a suitable place to change themes and apply new styles
    */
    open fun onActivityCreated() {
        Log.d(TAG, ACTIVITY_CREATED_MESSAGE)
    }

    /**
    * Method will be called after the view is visible to the user
    * This is a suitable place to start your logic processing and navigation
    */
    open fun onViewCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, VIEW_CREATED_MESSAGE)
    }

    final override fun onCreate(savedInstanceState: Bundle?) {
        onActivityLaunched()
        super.onCreate(savedInstanceState)
        onActivityCreated()
        setContentView(layoutId)
        setUpBackStackListener()
        onViewCreated(savedInstanceState)
    }

    final override fun restartCurrentFragment() {
        (currentFragment ?: baseFragment)?.let { fragment ->
            remove(fragment)
            reAttach(fragment)
        }
    }

    @CallSuper
    override fun onBackPressed() {
        (currentFragment as? BackPressController)?.let {
            if (it.onBackPressed()) super.onBackPressed()
        } ?: super.onBackPressed()
    }

    final override fun performBackPress() {
        onBackPressed()
    }

    final override fun setActivityReceiver(resultReceiver: ActivityResultReceiver) {
        this.currentResultReceiver = resultReceiver
    }

    final override fun requestActivityForResult(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
    }

    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        currentResultReceiver?.onReceiveActivityResult(requestCode, resultCode, data)
    }

    final override fun switchTo(fragment: F, addCurrentToBackStack: Boolean) {
        startTransaction {
            handleAnimationSet(fragment.animationSet)
                .replace(rootId, fragment, fragment.fragmentName)
                .handleBackStack(addCurrentToBackStack, currentFragment?.fragmentName)
        }
    }

    final override fun navigateTo(fragment: F, addCurrentToBackStack: Boolean) {
        startTransaction {
            handleAnimationSet(fragment.animationSet)
                .add(rootId, fragment, fragment.fragmentName)
                .handleBackStack(addCurrentToBackStack, currentFragment?.fragmentName)
        }
    }

    final override fun restartNavigationFrom(fragment: F) {
        startTransaction {
            handleAnimationSet(fragment.animationSet).replace(rootId, fragment)
        }
    }

    final override fun remove(fragment: F) {
        startTransaction { remove(fragment) }
        popBackStack()
    }

    final override fun restartActivity() {
        finish()
        startActivity(intent)
    }

    open fun onFragmentAttached(fragment: F) {
        Log.d(fragment.fragmentName, FRAGMENT_ATTACHED_MESSAGE)
    }

    private fun popBackStack() {
        supportFragmentManager.popBackStack()
    }

    private fun reAttach(fragment: F) {
        startTransaction {
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

    private fun startTransaction(transaction: FragmentTransaction.() -> FragmentTransaction) {
        supportFragmentManager.inTransaction(transaction)
    }

    companion object {
        private const val TAG = "NavigationActivity"
        private const val VIEW_CREATED_MESSAGE = "View Created"
        private const val ACTIVITY_CREATED_MESSAGE = "Activity Created"
        private const val ACTIVITY_LAUNCHED_MESSAGE = "Activity Launched"
        private const val FRAGMENT_ATTACHED_MESSAGE = "Navigation Fragment Attached"
    }

}