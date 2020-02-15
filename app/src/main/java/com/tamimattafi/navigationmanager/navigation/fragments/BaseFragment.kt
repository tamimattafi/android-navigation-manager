package com.tamimattafi.navigationmanager.navigation.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tamimattafi.navigationmanager.navigation.values.Deprecation.PROPERTY_REMOVAL
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {

    protected abstract val layoutId: Int

    @Deprecated(PROPERTY_REMOVAL, level = DeprecationLevel.WARNING, replaceWith = ReplaceWith("view!!"))
    protected val fragmentView: View
    get() = view!!

    @Deprecated(PROPERTY_REMOVAL, level = DeprecationLevel.WARNING, replaceWith = ReplaceWith("activity!!.applicationContext"))
    protected val applicationContext: Context
    get() = activity!!.applicationContext

    @Deprecated(PROPERTY_REMOVAL, level = DeprecationLevel.WARNING, replaceWith = ReplaceWith("activity!!"))
    protected val navigationActivity: Activity
    get() = activity!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(layoutId, container, false)

}