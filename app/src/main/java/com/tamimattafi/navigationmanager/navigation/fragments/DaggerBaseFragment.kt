package com.tamimattafi.navigationmanager.navigation.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class DaggerBaseFragment : DaggerFragment() {

    abstract val layoutId: Int
    protected lateinit var fragmentView: View

    @Inject
    protected lateinit var applicationContext: Context

    @Inject
    protected lateinit var navigationActivity: Activity


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(layoutId, container, false).also { fragmentView = it }

}