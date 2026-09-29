package com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.factory

import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.ProfileParserParamModel
import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.ProfileParserValidator
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileParseMissingServerExceptionModel
import kotlin.collections.get

internal class ServerProfileParserValidatorImpl : ProfileParserValidator {

    override fun isRequireValidator(param: ProfileParserParamModel): Boolean {
        return param == ProfileParserParamModel.SERVER
    }

    // proxies[0].server: требуется непустая строка
    override fun validate(config: Map<*, *>): String {
        val value = config[ProfileParserParamModel.SERVER.key] as? String
        if (value.isNullOrBlank()) throw ProfileParseMissingServerExceptionModel()
        return value
    }
}
