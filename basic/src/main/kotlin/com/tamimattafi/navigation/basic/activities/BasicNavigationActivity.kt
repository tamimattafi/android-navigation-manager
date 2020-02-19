package com.tamimattafi.navigation.basic.activities

import androidx.annotation.CallSuper
import com.tamimattafi.navigation.basic.BasicNavigator
import com.tamimattafi.navigation.core.activities.BaseNavigationActivity
import com.tamimattafi.navigation.basic.fragments.BasicNavigationFragment


abstract class BasicNavigationActivity : BaseNavigationActivity<BasicNavigationFragment>(), BasicNavigator {

    @CallSuper
    override fun onFragmentAttached(fragment: BasicNavigationFragment) {
        fragment.navigator = this
    }

}