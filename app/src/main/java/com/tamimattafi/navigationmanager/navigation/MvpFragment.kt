package com.tamimattafi.navigationmanager.navigation

import com.tamimattafi.mvp.MvpBaseContract
import com.tamimattafi.mvp.navigation.global.NavigationContract
import com.tamimattafi.navigationmanager.navigation.NavigationContract.NavigationFragment
import javax.inject.Inject

abstract class MvpFragment<P : MvpBaseContract.Presenter> : NavigationFragment(),
    NavigationContract.MvpFragment<P> {

    @Inject
    lateinit var presenter: P

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