package com.ghosty.traffic.rider.feature.selfprofile.data.dto

import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileModel
import kotlinx.serialization.Serializable

@Serializable
internal data class ProfileDto(
    val id: String,
    val name: String,
    val type: String,
    val server: String,
    val port: String,
    val raw: String,
) {
}