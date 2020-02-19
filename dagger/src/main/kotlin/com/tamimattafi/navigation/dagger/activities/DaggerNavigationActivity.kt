package com.tamimattafi.navigation.dagger.activities

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
    lateinit var androidInjector: DispatchingAndroidInjector<Any?>

    override fun onActivityCreated() {
        super.onActivityCreated()
        AndroidInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any?> {
        return androidInjector
    }

}