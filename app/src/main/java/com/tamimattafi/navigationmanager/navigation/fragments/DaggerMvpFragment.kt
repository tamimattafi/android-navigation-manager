package com.tamimattafi.navigationmanager.navigation.fragments

import com.tamimattafi.mvp.MvpBaseContract
import javax.inject.Inject

abstract class DaggerMvpFragment<P : MvpBaseContract.Presenter> : DaggerNavigationFragment() {

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