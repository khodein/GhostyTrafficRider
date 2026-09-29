package com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator

import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.ProfileParserParamModel

internal interface ProfileParserValidator {
    fun isRequireValidator(param: ProfileParserParamModel): Boolean
    fun validate(config: Map<*, *>): String
}