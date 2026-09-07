package com.ghosty.traffic.rider.framework.tools.res

import android.content.Context
import androidx.annotation.StringRes

interface ResProvider {
    fun getString(@StringRes id: Int): String
    fun getString(@StringRes id: Int, vararg args: Any): String
}

internal class ResProviderImpl(private val context: Context) : ResProvider {
    override fun getString(@StringRes id: Int): String = context.getString(id)
    override fun getString(@StringRes id: Int, vararg args: Any): String = context.getString(id, *args)
}
