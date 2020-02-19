package com.tamimattafi.navigation.dagger.fragments

import android.content.Context
import androidx.annotation.CallSuper
import com.tamimattafi.navigation.core.NavigationContract.NavigationGuest
import com.tamimattafi.navigation.core.fragments.BaseNavigationFragment
import com.tamimattafi.navigation.dagger.DaggerNavigator
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class DaggerNavigationFragment : BaseNavigationFragment(), NavigationGuest<DaggerNavigationFragment, DaggerNavigator>, HasAndroidInjector {

    /**
    * Instance of the navigation host activity that will be provided each time the fragment was attached
    * An injection module should be provided for Dagger in order to make it able to provide this dependency
    * For more information, visit our github page: https://github.com/tamimattafi/android-dagger-navigationmanager
    */
    @Inject
    final override lateinit var navigator: DaggerNavigator

    @Inject
    protected lateinit var androidInjector: DispatchingAndroidInjector<Any?>

    @CallSuper
    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    final override fun androidInjector(): AndroidInjector<Any?> {
        return androidInjector
    }

}