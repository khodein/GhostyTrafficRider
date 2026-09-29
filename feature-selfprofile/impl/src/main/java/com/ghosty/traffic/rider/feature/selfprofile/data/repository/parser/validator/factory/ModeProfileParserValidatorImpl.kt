package com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.factory

import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.ProfileParserParamModel
import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.ProfileParserValidator
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileParseInvalidModeExceptionModel
import kotlin.collections.get

internal class ModeProfileParserValidatorImpl : ProfileParserValidator {

    override fun isRequireValidator(param: ProfileParserParamModel): Boolean {
        return param == ProfileParserParamModel.MODE
    }

    override fun validate(config: Map<*, *>): String {
        val value = config[ProfileParserParamModel.MODE.key] as? String
        if (value != MODE_RULE) throw ProfileParseInvalidModeExceptionModel()
        return value
    }

    private companion object {
        // Единственный поддерживаемый режим маршрутизации — по правилам (proxy-groups + rules)
        const val MODE_RULE = "rule"
    }
}
