package com.ghosty.traffic.rider.feature.selfprofile.domain.model

data class ProfileModel(
    val id: String,
    val name: String,
    val type: String,
    val server: String,
    val port: String,
    val raw: String,
) {
    // Дефолтный data class toString() вывел бы server/port/raw (в raw может быть пароль/uuid прокси) — исключаем их, чтобы не утекали в логи
    override fun toString(): String = "ProfileModel(id=$id, name=$name, type=$type)"
}
