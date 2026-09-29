package com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.factory

import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.ProfileParserParamModel
import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.ProfileParserValidator
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileParseMissingProxyGroupExceptionModel
import kotlin.collections.get

internal class ProxyGroupProfileParserValidatorImpl : ProfileParserValidator {

    override fun isRequireValidator(param: ProfileParserParamModel): Boolean {
        return param == ProfileParserParamModel.PROXY_GROUPS
    }

    override fun validate(config: Map<*, *>): String {
        val groups = config[ProfileParserParamModel.PROXY_GROUPS.key] as? List<*>
        val hasProxyGroup = groups.orEmpty().any { (it as? Map<*, *>)?.get(ProfileParserParamModel.NAME.key) == PROXY_GROUP_NAME }
        if (!hasProxyGroup) throw ProfileParseMissingProxyGroupExceptionModel()
        return PROXY_GROUP_NAME
    }

    private companion object {
        // Имя группы-селектора, на которую clash/mihomo обычно ссылается по умолчанию в правилах — без неё маршрутизация не сработает
        const val PROXY_GROUP_NAME = "PROXY"
    }
}
