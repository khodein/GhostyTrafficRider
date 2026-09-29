package com.ghosty.traffic.rider.feature.selfprofile.presentation.mapper

import androidx.annotation.StringRes
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileDuplicateIdExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileMissingActiveExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileNotFoundExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileStorageDirectoryExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.impl.R
import com.ghosty.traffic.rider.framework.tools.res.ResProvider

internal class ProfileErrorMapper(
    private val resProvider: ResProvider
) {
    fun map(exception: ProfileExceptionModel): String {
        return resProvider.getString(messageRes(exception))
    }

    @StringRes
    private fun messageRes(exception: ProfileExceptionModel): Int = when (exception) {
        is ProfileDuplicateIdExceptionModel -> R.string.profile_error_duplicate_id
        is ProfileMissingActiveExceptionModel -> R.string.profile_error_missing_active
        is ProfileNotFoundExceptionModel -> R.string.profile_error_not_found
        is ProfileStorageDirectoryExceptionModel -> R.string.profile_error_storage_directory
    }

    fun general(): String {
        return resProvider.getString(R.string.profile_error_load_generic)
    }
}
