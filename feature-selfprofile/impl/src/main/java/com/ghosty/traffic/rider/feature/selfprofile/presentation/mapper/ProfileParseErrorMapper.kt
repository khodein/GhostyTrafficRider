package com.ghosty.traffic.rider.feature.selfprofile.presentation.mapper

import androidx.annotation.StringRes
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileParseBlankInputExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileParseExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileParseInvalidModeExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileParseInvalidPortFormatExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileParseInvalidPortRangeExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileParseInvalidYamlExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileParseMissingNameExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileParseMissingProxyExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileParseMissingProxyGroupExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileParseMissingServerExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileParseMissingTypeExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.impl.R
import com.ghosty.traffic.rider.framework.tools.res.ResProvider

internal class ProfileParseErrorMapper(
    private val resProvider: ResProvider
) {

    fun map(exception: ProfileParseExceptionModel): String {
        return resProvider.getString(messageRes(exception))
    }

    @StringRes
    private fun messageRes(exception: ProfileParseExceptionModel): Int = when (exception) {
        is ProfileParseBlankInputExceptionModel -> R.string.profile_parse_error_blank_input
        is ProfileParseInvalidYamlExceptionModel -> R.string.profile_parse_error_invalid_yaml
        is ProfileParseInvalidModeExceptionModel -> R.string.profile_parse_error_invalid_mode
        is ProfileParseMissingProxyExceptionModel -> R.string.profile_parse_error_missing_proxy
        is ProfileParseMissingNameExceptionModel -> R.string.profile_parse_error_missing_name
        is ProfileParseMissingTypeExceptionModel -> R.string.profile_parse_error_missing_type
        is ProfileParseMissingServerExceptionModel -> R.string.profile_parse_error_missing_server
        is ProfileParseInvalidPortFormatExceptionModel -> R.string.profile_parse_error_invalid_port_format
        is ProfileParseInvalidPortRangeExceptionModel -> R.string.profile_parse_error_invalid_port_range
        is ProfileParseMissingProxyGroupExceptionModel -> R.string.profile_parse_error_missing_proxy_group
    }
}
