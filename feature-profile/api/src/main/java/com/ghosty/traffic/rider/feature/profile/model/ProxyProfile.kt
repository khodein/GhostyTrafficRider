package com.ghosty.traffic.rider.feature.profile.model

data class ProxyProfile(
    val id: String,
    val name: String,
    val type: String,
    val server: String,
    val port: Int,
    val raw: String,
) {
    override fun toString(): String = "ProxyProfile(id=$id, name=$name, type=$type)"
}
