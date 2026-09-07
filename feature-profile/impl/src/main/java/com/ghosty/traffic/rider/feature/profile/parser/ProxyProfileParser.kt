package com.ghosty.traffic.rider.feature.profile.parser

import com.ghosty.traffic.rider.feature.profile.model.ProxyProfile
import java.util.UUID
import org.snakeyaml.engine.v2.api.Dump
import org.snakeyaml.engine.v2.api.DumpSettings
import org.snakeyaml.engine.v2.api.Load
import org.snakeyaml.engine.v2.api.LoadSettings
import org.snakeyaml.engine.v2.common.FlowStyle
import org.snakeyaml.engine.v2.exceptions.YamlEngineException

internal class ProxyProfileParser {
    fun parse(text: String): Result<ProxyProfile> {
        return try {
            require(text.isNotBlank()) { "Вставьте YAML-конфигурацию" }
            val settings = LoadSettings.builder()
                .setAllowDuplicateKeys(false)
                .setCodePointLimit(1_048_576)
                .build()
            val config = Load(settings).loadFromString(text) as? Map<*, *>
                ?: throw IllegalArgumentException("YAML должен содержать объект конфигурации")
            require(config["mode"] == "rule") { "В конфигурации требуется mode: rule" }
            val proxies = config["proxies"] as? List<*>
            val proxy = proxies?.firstOrNull() as? Map<*, *>
                ?: throw IllegalArgumentException("Список proxies должен содержать сервер")
            val name = proxy.requiredString("name")
            val type = proxy.requiredString("type")
            val server = proxy.requiredString("server")
            val port = proxy["port"]?.toString()?.toIntOrNull()
            require(port != null && port in 1..65535) { "proxies[0].port: требуется порт от 1 до 65535" }
            val groups = config["proxy-groups"] as? List<*>
            require(groups.orEmpty().any { (it as? Map<*, *>)?.get("name") == "PROXY" }) {
                "В proxy-groups требуется группа с именем PROXY"
            }
            val raw = Dump(DumpSettings.builder().setDefaultFlowStyle(FlowStyle.BLOCK).build())
                .dumpToString(config)
            Result.success(ProxyProfile(UUID.randomUUID().toString(), name, type, server, port, raw))
        } catch (_: YamlEngineException) {
            Result.failure(IllegalArgumentException("Некорректный YAML: проверьте структуру и отступы"))
        } catch (error: IllegalArgumentException) {
            Result.failure(error)
        }
    }

    private fun Map<*, *>.requiredString(key: String): String {
        val value = this[key] as? String
        require(!value.isNullOrBlank()) { "proxies[0].$key: требуется непустая строка" }
        return value
    }
}
