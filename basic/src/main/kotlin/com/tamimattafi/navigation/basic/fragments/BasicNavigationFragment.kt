package com.tamimattafi.navigation.basic.fragments

import com.tamimattafi.navigation.basic.BasicNavigator
import com.tamimattafi.navigation.core.NavigationContract.NavigationGuest
import com.tamimattafi.navigation.core.fragments.BaseNavigationFragment

abstract class BasicNavigationFragment : BaseNavigationFragment(), NavigationGuest<BasicNavigationFragment, BasicNavigator> {

    /**
    * Instance of the navigation host activity that will be provided each time the fragment was attached
    */
    final override lateinit var navigator: BasicNavigator

}