package com.tamimattafi.navigation.dagger.fragments

import com.tamimattafi.navigation.core.NavigationContract.NavigationGuest
import com.tamimattafi.navigation.core.fragments.BaseNavigationFragment
import com.tamimattafi.navigation.dagger.DaggerNavigator
import javax.inject.Inject

abstract class DaggerNavigationFragment : BaseNavigationFragment(), NavigationGuest<DaggerNavigationFragment, DaggerNavigator> {

    /**
    * Instance of the navigation host activity that will be provided each time the fragment was attached
    * An injection module should be provided for Dagger in order to make it able to provide this dependency
    * For more information, visit our github page: https://github.com/tamimattafi/android-dagger-navigationmanager
    */
    @Inject
    final override lateinit var navigator: DaggerNavigator

}