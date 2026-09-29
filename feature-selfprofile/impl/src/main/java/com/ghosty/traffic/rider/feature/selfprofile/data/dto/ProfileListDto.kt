package com.ghosty.traffic.rider.feature.selfprofile.data.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class ProfileListDto(
    val profiles: List<ProfileDto> = emptyList(),
    val activeProfileId: String? = null,
)
