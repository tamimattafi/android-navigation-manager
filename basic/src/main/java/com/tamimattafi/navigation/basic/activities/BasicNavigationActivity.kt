package com.tamimattafi.navigation.basic.activities

import com.tamimattafi.navigation.basic.BasicNavigator
import com.tamimattafi.navigation.core.activities.BaseNavigationActivity
import com.tamimattafi.navigation.basic.fragments.BasicNavigationFragment


abstract class BasicNavigationActivity : BaseNavigationActivity<BasicNavigationFragment>(), BasicNavigator {

    override fun onFragmentAttached(fragment: BasicNavigationFragment) {
        super.onFragmentAttached(fragment)
        fragment.navigator = this
    }

}