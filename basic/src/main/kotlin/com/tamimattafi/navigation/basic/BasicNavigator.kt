package com.tamimattafi.navigation.basic

import com.tamimattafi.navigation.core.NavigationContract.Navigator
import com.tamimattafi.navigation.basic.fragments.BasicNavigationFragment


/**
* For documentations visit NavigationContract.Navigator<T> class
* An instance of this interface will be passed to all of your BasicNavigationFragments
* All behaviours from NavigationContract.Navigator<T> apply to this interface
*/
interface BasicNavigator : Navigator<BasicNavigationFragment>