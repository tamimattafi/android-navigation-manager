package com.tamimattafi.navigation.dagger

import com.tamimattafi.navigation.core.NavigationContract.Navigator
import com.tamimattafi.navigation.dagger.fragments.DaggerNavigationFragment


/**
* For documentations visit NavigationContract.Navigator<T> class
* An instance of this interface will be injected in all your DaggerNavigationFragments
* All behaviours from NavigationContract.Navigator<T> apply to this interface
*/
interface DaggerNavigator : Navigator<DaggerNavigationFragment>