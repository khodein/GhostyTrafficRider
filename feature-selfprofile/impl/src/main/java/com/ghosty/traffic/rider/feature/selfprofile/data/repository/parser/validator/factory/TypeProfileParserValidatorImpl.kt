package com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.factory

import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.ProfileParserParamModel
import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.ProfileParserValidator
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileParseMissingTypeExceptionModel
import kotlin.collections.get

internal class TypeProfileParserValidatorImpl : ProfileParserValidator {

    override fun isRequireValidator(param: ProfileParserParamModel): Boolean {
        return param == ProfileParserParamModel.TYPE
    }

    // proxies[0].type: требуется непустая строка
    override fun validate(config: Map<*, *>): String {
        val value = config[ProfileParserParamModel.TYPE.key] as? String
        if (value.isNullOrBlank()) throw ProfileParseMissingTypeExceptionModel()
        return value
    }
}
