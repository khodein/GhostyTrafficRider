package com.ghosty.traffic.rider.feature.selfprofile.data.mapper

import com.ghosty.traffic.rider.feature.selfprofile.data.dto.ProfileDto
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileModel

internal class ProfileDtoToModelMapper {

    fun toModel(dto: ProfileDto) = ProfileModel(
        dto.id,
        dto.name,
        dto.type,
        dto.server,
        dto.port,
        dto.raw,
    )

    fun toDto(profile: ProfileModel) = ProfileDto(
        profile.id,
        profile.name,
        profile.type,
        profile.server,
        profile.port,
        profile.raw,
    )
}