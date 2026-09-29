package com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.factory

import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.ProfileParserParamModel
import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.ProfileParserValidator
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileParseMissingNameExceptionModel
import kotlin.collections.get

internal class NameProfileParserValidatorImpl : ProfileParserValidator {

    override fun isRequireValidator(param: ProfileParserParamModel): Boolean {
        return param == ProfileParserParamModel.NAME
    }

    // proxies[0].name: требуется непустая строка
    override fun validate(config: Map<*, *>): String {
        val value = config[ProfileParserParamModel.NAME.key] as? String
        if (value.isNullOrBlank()) throw ProfileParseMissingNameExceptionModel()
        return value
    }
}
