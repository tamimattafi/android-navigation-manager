@file:Suppress("UNCHECKED_CAST")

package com.tamimattafi.navigation.core.activities

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import android.util.Log
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.tamimattafi.navigation.core.NavigationContract.*
import com.tamimattafi.navigation.core.fragments.BaseNavigationFragment
import com.tamimattafi.navigation.core.values.Values.ACTIVITY_CREATED_MESSAGE
import com.tamimattafi.navigation.core.values.Values.ACTIVITY_LAUNCHED_MESSAGE
import com.tamimattafi.navigation.core.values.Values.BACK_STACK_CHANGE_MESSAGE
import com.tamimattafi.navigation.core.values.Values.BASE_FRAGMENT_CHANGE_MESSAGE
import com.tamimattafi.navigation.core.values.Values.FRAGMENT_ATTACHED_MESSAGE
import com.tamimattafi.navigation.core.values.Values.RESULT_RECEIVER_CHANGE_MESSAGE
import com.tamimattafi.navigation.core.values.Values.VIEW_CREATED_MESSAGE


abstract class BaseNavigationActivity<F: BaseNavigationFragment> : AppCompatActivity(), Navigator<F> {


    /**
    * LayoutId of the desired view that will be passed to setContentView(...)
    */
    protected abstract val layoutId: Int


    /**
    * RootId of the desired view that will be replaced by fragments during navigation
    */
    protected abstract val rootId: Int


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
    set(value) {
        field = value
        onBaseFragmentChange(value)
    }


    /**
    * Current ActivityResultReceiver, if a call back is returned from startActivityForResult
    * fragment's fun onReceiveActivityResult(requestCode, resultCode, data) will be triggered
    */
    private var currentResultReceiver: ActivityResultReceiver? = null
    set(value) {
        field = value
        onResultReceiverChange(value)
    }


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


    /**
    * Method will be called if base fragment's instance was changed
    * This is a suitable place to reset some states related to back-stack
    */
    open fun onBaseFragmentChange(newBaseFragment: F?) {
        Log.d(TAG, BASE_FRAGMENT_CHANGE_MESSAGE)
    }


    /**
    * Method will be called if the result receiver was change
    * This is a suitable place to stop any result handlers in your previous receiver
    */
    open fun onResultReceiverChange(newResultReceiver: ActivityResultReceiver?) {
        Log.d(TAG, RESULT_RECEIVER_CHANGE_MESSAGE)
    }


    /**
    * Method will be called if the back stack was change
    * This is a suitable place to check for currentFragment changes
    */
    open fun onBackStackChanged() {
        Log.d(TAG, BACK_STACK_CHANGE_MESSAGE)
    }


    /**
    * Method will be called after any navigation transaction
    * This is a suitable place to apply some behaviours to the fragment
    */
    open fun onFragmentAttached(fragment: F) {
        Log.d(fragment.fragmentName, FRAGMENT_ATTACHED_MESSAGE)
    }

    final override fun onCreate(savedInstanceState: Bundle?) {
        onActivityLaunched()
        super.onCreate(savedInstanceState)
        onActivityCreated()
        setContentView(layoutId)
        setUpBackStackListener()
        onViewCreated(savedInstanceState)
    }

    final override fun restartCurrentFragment(attachToBackStack: Boolean, withAnimation: Boolean) {
        (currentFragment ?: baseFragment)?.let { fragment ->
            remove(fragment)
            reAttach(fragment, attachToBackStack, withAnimation)
        }
    }

    @CallSuper
    override fun onBackPressed() {
        (currentFragment as? BackPressController)?.let {
            if (it.onBackPressed()) super.onBackPressed()
        } ?: super.onBackPressed()
    }

    final override fun finishActivity() {
        finish()
    }

    final override fun openActivity(intent: Intent) {
        startActivity(intent)
    }

    final override fun launchService(intent: Intent, foreground: Boolean) {
        if (foreground && SDK_INT >= O) {
            startForegroundService(intent)
        } else startService(intent)
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

    final override fun switchTo(
        fragment: F,
        addCurrentToBackStack: Boolean,
        withAnimation: Boolean
    ) {
        startAttachTransaction(fragment, addCurrentToBackStack, withAnimation) {
            replace(fragment)
        }
    }

    final override fun navigateTo(
        fragment: F,
        addCurrentToBackStack: Boolean,
        withAnimation: Boolean
    ) {
        startAttachTransaction(fragment, addCurrentToBackStack, withAnimation) {
            add(fragment)
        }
    }

    final override fun restartNavigationFrom(fragment: F, withAnimation: Boolean) {
        supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        startAttachTransaction(fragment, false, withAnimation) {
            replace(rootId, fragment)
        }

        baseFragment = fragment
    }

    final override fun remove(fragment: F) {
        if (fragment == currentFragment) {
            popBackStack()
        } else supportFragmentManager.commit {
            remove(fragment)
        }
    }

    final override fun restartActivity() {
        finish()
        startActivity(intent)
    }

    private fun popBackStack() {
        supportFragmentManager.popBackStack()
    }

    private fun reAttach(
        fragment: F,
        addCurrentToBackStack: Boolean,
        withAnimation: Boolean
    ) {
        startAttachTransaction(fragment, addCurrentToBackStack, withAnimation) {
            replace(fragment.javaClass.newInstance().apply { arguments = fragment.arguments })
        }
    }

    private fun setUpBackStackListener() {
        supportFragmentManager.addOnBackStackChangedListener {
            notifyCurrentFragmentSelection()
            onBackStackChanged()
        }
    }

    private fun notifyCurrentFragmentSelection() {
        (currentFragment as? SelectionListener)?.onSelected()
    }

    private fun startAttachTransaction(
        fragment: F,
        addCurrentToBackStack: Boolean,
        withAnimation: Boolean,
        transaction: FragmentTransaction.() -> FragmentTransaction
    ) {
        val oldFragment = supportFragmentManager.findFragmentByTag(fragment.fragmentName)
        supportFragmentManager.commit {
            if (oldFragment != null) {
                remove(oldFragment)
            }

            if (withAnimation) fragment.animationSet?.apply {
                setCustomAnimations(
                    enterAnimation,
                    exitAnimation,
                    popEnterAnimation,
                    popExitAnimation
                )
            }

            transaction()
            setReorderingAllowed(true)

            if (addCurrentToBackStack && oldFragment != currentFragment) {
                addToBackStack(currentFragment?.fragmentName)
            }
        }

        onFragmentAttached(fragment)
    }

    private fun FragmentTransaction.add(fragment: F): FragmentTransaction
        = add(rootId, fragment, fragment.fragmentName)

    private fun FragmentTransaction.replace(fragment: F): FragmentTransaction
        = replace(rootId, fragment, fragment.fragmentName)

    companion object {
        private const val TAG = "NavigationActivity"
    }
}