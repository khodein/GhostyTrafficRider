package com.ghosty.traffic.rider.vpn

import com.ghosty.traffic.rider.feature.profile.model.ProxyProfile
import java.io.File
import org.snakeyaml.engine.v2.api.Dump
import org.snakeyaml.engine.v2.api.DumpSettings
import org.snakeyaml.engine.v2.api.Load
import org.snakeyaml.engine.v2.api.LoadSettings
import org.snakeyaml.engine.v2.common.FlowStyle

internal class RuntimeConfigBuilder {
    fun write(profile: ProxyProfile, directory: File): File {
        val parsed = Load(LoadSettings.builder().build()).loadFromString(profile.raw) as? Map<*, *>
            ?: error("Некорректная конфигурация")
        val config = parsed.entries.associate { (key, value) -> key.toString() to value }.toMutableMap()
        if (!config.containsKey("rules")) config["rules"] = listOf("MATCH,DIRECT")
        // The Android service owns TUN; the core must not create another interface.
        config["tun"] = mapOf("enable" to false)
        config["log-level"] = "info"
        if (!config.containsKey("find-process-mode")) config["find-process-mode"] = "always"
        check(directory.isDirectory || directory.mkdirs())
        return File(directory, "config.yaml").also {
            it.writeText(Dump(DumpSettings.builder().setDefaultFlowStyle(FlowStyle.BLOCK).build()).dumpToString(config))
        }
    }
}
