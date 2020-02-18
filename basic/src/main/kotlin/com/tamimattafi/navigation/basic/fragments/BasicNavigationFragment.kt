package com.tamimattafi.navigation.basic.fragments

import com.tamimattafi.navigation.basic.BasicNavigator
import com.tamimattafi.navigation.core.NavigationContract.NavigationGuest
import com.tamimattafi.navigation.core.fragments.BaseNavigationFragment

abstract class BasicNavigationFragment : BaseNavigationFragment(), NavigationGuest<BasicNavigationFragment, BasicNavigator> {

    /**
    * Instance of the navigation host activity that will be provided each time the fragment was attached
    * An injection module should be provided for Dagger in order to make it able to provide this dependency
    * For more information, visit our github page: https://github.com/tamimattafi/android-dagger-navigationmanager
    */
    final override lateinit var navigator: BasicNavigator

}