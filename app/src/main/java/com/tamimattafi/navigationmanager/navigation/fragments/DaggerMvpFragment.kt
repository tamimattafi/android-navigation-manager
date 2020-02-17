package com.tamimattafi.navigationmanager.navigation.fragments

import com.tamimattafi.mvp.MvpBaseContract
import javax.inject.Inject

abstract class DaggerMvpFragment<P : MvpBaseContract.Presenter> : DaggerNavigationFragment() {


    /*
    * #DESCRIPTION
    *
    * This is an instance of the presenter passed as a generic P
    * An injection module should be provided for Dagger in order to make it able to provide this dependency
    * For more information, visit our github page: https://github.com/tamimattafi/android-dagger-navigationmanager
    *
    * */
    @Inject
    protected lateinit var presenter: P


    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroyView()
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }


    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

}