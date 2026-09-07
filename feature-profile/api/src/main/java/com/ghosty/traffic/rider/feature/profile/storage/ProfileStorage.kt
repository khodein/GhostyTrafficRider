package com.ghosty.traffic.rider.feature.profile.storage

import com.ghosty.traffic.rider.feature.profile.model.ProxyProfile
import kotlinx.coroutines.flow.Flow

interface ProfileStorage {
    fun getAll(): Flow<List<ProxyProfile>>
    fun getActive(): Flow<ProxyProfile?>
    suspend fun save(profile: ProxyProfile)
    suspend fun delete(id: String)
    suspend fun select(id: String?)
}
