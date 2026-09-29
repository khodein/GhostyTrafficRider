package com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser

import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.ProfileParserValidator
import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.ProfileParserParamModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileParseBlankInputExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileParseInvalidYamlExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileParseMissingProxyExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileModel
import org.snakeyaml.engine.v2.api.Dump
import org.snakeyaml.engine.v2.api.DumpSettings
import org.snakeyaml.engine.v2.api.Load
import org.snakeyaml.engine.v2.api.LoadSettings
import org.snakeyaml.engine.v2.common.FlowStyle
import kotlin.uuid.Uuid

internal class ProfileParserRepository(
    private val validatorList: List<ProfileParserValidator>
) {
    suspend fun parse(text: String): ProfileModel {
        if (text.isBlank()) throw ProfileParseBlankInputExceptionModel()
        val config = getConfig(text = text)
        validate(ProfileParserParamModel.MODE, config)
        val proxy = firstProxy(config)
        val values = PROXY_FIELD_PARAMS.associateWith { param -> validate(param, proxy) }
        validate(ProfileParserParamModel.PROXY_GROUPS, config)
        val uuid = Uuid.random()
        val raw = getDump().dumpToString(config)
        return ProfileModel(
            id = uuid.toString(),
            name = values.getValue(ProfileParserParamModel.NAME),
            type = values.getValue(ProfileParserParamModel.TYPE),
            server = values.getValue(ProfileParserParamModel.SERVER),
            port = values.getValue(ProfileParserParamModel.PORT),
            raw = raw
        )
    }

    private fun validate(param: ProfileParserParamModel, config: Map<*, *>): String {
        return validatorList.first { it.isRequireValidator(param) }.validate(config)
    }

    private fun getDumpSettings(): DumpSettings {
        return DumpSettings.builder().setDefaultFlowStyle(FlowStyle.BLOCK).build()
    }

    private fun getDump(): Dump {
        val settings = getDumpSettings()
        return Dump(settings)
    }

    private fun firstProxy(config: Map<*, *>): Map<*, *> {
        val proxies = config[PROXIES_PARAM] as? List<*>
        return proxies?.firstOrNull() as? Map<*, *> ?: throw ProfileParseMissingProxyExceptionModel()
    }

    private fun getLoadSettings(): LoadSettings {
        return LoadSettings.builder()
            .setAllowDuplicateKeys(false)
            .setCodePointLimit(CODE_POINT_LIMIT)
            .build()
    }

    private fun getConfig(
        text: String,
    ): Map<*, *> {
        val loadSettings = getLoadSettings()
        val load = Load(loadSettings)
        return load.loadFromString(text) as? Map<*, *> ?: throw ProfileParseInvalidYamlExceptionModel()
    }

    private companion object {
        const val CODE_POINT_LIMIT = 1_048_576

        // Список серверов-прокси; берём только первый — приложение поддерживает один активный сервер на профиль
        const val PROXIES_PARAM = "proxies"

        // Поля первого прокси, которые нужно провалидировать и вытащить в ProfileModel
        val PROXY_FIELD_PARAMS = listOf(
            ProfileParserParamModel.NAME,
            ProfileParserParamModel.TYPE,
            ProfileParserParamModel.SERVER,
            ProfileParserParamModel.PORT,
        )
    }
}
