package com.tamimattafi.navigationmanager.navigation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.tamimattafi.navigationmanager.R
import com.tamimattafi.navigationmanager.navigation.NavigationContract.*
import dagger.android.support.DaggerAppCompatActivity


abstract class NavigationActivity : DaggerAppCompatActivity(), NavigationManager {

    abstract val layoutId: Int
    abstract var rootId: Int


    abstract fun onViewCreated(savedInstanceState: Bundle?)

    private val currentFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(rootId)

    private var baseFragment: Fragment? = null

    private var currentRequestCode: Int = -1
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

    override fun requestAttachBaseScreen(fragment: NavigationFragment) {
        supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager.inTransaction {
            replace(rootId, fragment)
        }
        baseFragment = fragment
    }

    override fun requestSlideLeftScreen(fragment: NavigationFragment) {
        supportFragmentManager.inTransaction {
            setCustomAnimations(
                R.anim.enter,
                R.anim.exit,
                R.anim.pop_enter,
                R.anim.pop_exit
            )
            add(rootId, fragment).addToBackStack(fragment.fragmentName)
        }
    }

    override fun requestSlideRightScreen(fragment: NavigationFragment) {
        supportFragmentManager.inTransaction {
            setCustomAnimations(
                R.anim.pop_enter,
                R.anim.pop_exit,
                R.anim.enter,
                R.anim.exit
            )
            add(rootId, fragment).addToBackStack(fragment.fragmentName)
        }
    }

    override fun requestFadeInScreen(fragment: NavigationFragment) {
        supportFragmentManager.inTransaction {
            setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            add(rootId, fragment).addToBackStack(fragment.fragmentName)
        }
    }

    override fun requestAttachScreen(fragment: NavigationFragment) {
        supportFragmentManager.inTransaction {
            add(rootId, fragment).addToBackStack(fragment.fragmentName)
        }
    }

    override fun restartCurrentScreen() {
        (currentFragment as? NavigationFragment)?.let {
            supportFragmentManager.inTransaction {
                remove(it)
                add(rootId, it.javaClass.newInstance()).addToBackStack(it.fragmentName)
            }
        }
    }

    override fun onBackPressed() {
        (currentFragment as? BackPressController)?.let {
            if (it.onBackPressed()) {
                super.onBackPressed()
            }
        } ?: super.onBackPressed()
    }

    override fun requestBackPress() {
        onBackPressed()
    }

    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }

    override fun requestActivityForResult(
        resultReceiver: ActivityResultReceiver,
        intent: Intent,
        requestCode: Int
    ) {
        this.currentResultReceiver = resultReceiver
        this.currentRequestCode = requestCode
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == currentRequestCode) {
            currentResultReceiver?.onReceiveActivityResult(requestCode, resultCode, data)
        }
    }

    override fun requestRestart() {
        finish()
        startActivity(intent)
    }
}