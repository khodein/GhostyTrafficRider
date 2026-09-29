package com.ghosty.traffic.rider.feature.vpn.config

import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileModel
import com.ghosty.traffic.rider.feature.vpn.domain.model.VpnInvalidConfigExceptionModel
import com.ghosty.traffic.rider.feature.vpn.domain.model.VpnRuntimeDirectoryExceptionModel
import java.io.File
import org.snakeyaml.engine.v2.api.Dump
import org.snakeyaml.engine.v2.api.DumpSettings
import org.snakeyaml.engine.v2.api.Load
import org.snakeyaml.engine.v2.api.LoadSettings
import org.snakeyaml.engine.v2.common.FlowStyle

internal class RuntimeConfigBuilder {
    fun write(profile: ProfileModel, directory: File): File {
        val parsed = Load(LoadSettings.builder().build()).loadFromString(profile.raw) as? Map<*, *>
            ?: throw VpnInvalidConfigExceptionModel()
        val config = parsed.entries.associate { (key, value) -> key.toString() to value }.toMutableMap()
        if (!config.containsKey("rules")) config["rules"] = listOf("MATCH,DIRECT")
        config["tun"] = mapOf("enable" to false)
        config["log-level"] = "info"
        if (!config.containsKey("find-process-mode")) config["find-process-mode"] = "always"
        if (!directory.isDirectory && !directory.mkdirs()) {
            throw VpnRuntimeDirectoryExceptionModel()
        }
        return File(directory, "config.yaml").also {
            val settings = DumpSettings.builder().setDefaultFlowStyle(FlowStyle.BLOCK).build()
            it.writeText(Dump(settings).dumpToString(config))
        }
    }
}
