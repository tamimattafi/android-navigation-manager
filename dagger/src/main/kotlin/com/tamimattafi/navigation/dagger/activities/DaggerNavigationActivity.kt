package com.tamimattafi.navigation.dagger.activities

import androidx.annotation.CallSuper
import com.tamimattafi.navigation.core.activities.BaseNavigationActivity
import com.tamimattafi.navigation.dagger.DaggerNavigator
import com.tamimattafi.navigation.dagger.fragments.DaggerNavigationFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


abstract class DaggerNavigationActivity : BaseNavigationActivity<DaggerNavigationFragment>(), HasAndroidInjector, DaggerNavigator {

    @Inject
    protected lateinit var androidInjector: DispatchingAndroidInjector<Any?>

    @CallSuper
    override fun onActivityLaunched() {
        super.onActivityLaunched()
        AndroidInjection.inject(this)
    }

    final override fun androidInjector(): AndroidInjector<Any?> {
        return androidInjector
    }

}