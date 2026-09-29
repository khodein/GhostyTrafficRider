package com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.factory

import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.ProfileParserParamModel
import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.ProfileParserValidator
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileParseInvalidPortFormatExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileParseInvalidPortRangeExceptionModel
import kotlin.collections.get

internal class PortProfileParserValidatorImpl : ProfileParserValidator {

    override fun isRequireValidator(param: ProfileParserParamModel): Boolean {
        return param == ProfileParserParamModel.PORT
    }

    override fun validate(config: Map<*, *>): String {
        val port = config[ProfileParserParamModel.PORT.key]?.toString().orEmpty()
        // proxies[0].port: требуется строка только из цифр, представимая как число
        val isDigitsOnly = port.isNotEmpty() && port.all(Char::isDigit)
        val portNumber = port.toIntOrNull()
        if (!isDigitsOnly || portNumber == null) throw ProfileParseInvalidPortFormatExceptionModel()
        // proxies[0].port: требуется значение от 1 до 65535
        if (portNumber !in 1..65535) throw ProfileParseInvalidPortRangeExceptionModel()
        return port
    }
}
