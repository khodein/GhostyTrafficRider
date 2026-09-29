package com.ghosty.traffic.rider.feature.selfprofile.data.repository.profile

import android.content.Context
import com.ghosty.traffic.rider.feature.selfprofile.data.dto.ProfileListDto
import com.ghosty.traffic.rider.feature.selfprofile.data.mapper.ProfileDtoToModelMapper
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileDuplicateIdExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileMissingActiveExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileNotFoundExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileStorageDirectoryExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.collections.map

internal class ProfileRepository(
    private val json: Json,
    private val context: Context,
    private val mapper: ProfileDtoToModelMapper,
) {
    private val mutex = Mutex()
    private val cache = MutableStateFlow(ProfileListDto())
    private val activeProfile = MutableStateFlow<ProfileModel?>(null)
    private val directory: File
        get() = context.filesDir
    private val file = File(directory, DIRECTORY_NAME_PATH)

    // Чистая подписка на уже загруженное состояние — ничего не читает с диска и не бросает исключения
    fun getAllFlow(): Flow<List<ProfileModel>> =
        cache
            .map { stored -> stored.profiles.map(mapper::toModel) }
            .distinctUntilChanged()

    fun getActiveFlow(): StateFlow<ProfileModel?> = activeProfile.asStateFlow()

    // Явная загрузка с диска — вызывающий сам решает, когда её дёргать (старт экрана, pull-to-refresh) и как обрабатывать ошибку
    suspend fun load(): List<ProfileModel> {
        val stored = mutex.withLock { loadLocked() }
        return stored.profiles.map(mapper::toModel)
    }

    suspend fun getById(id: String): ProfileModel? {
        val stored = mutex.withLock { loadLocked() }
        return stored.profiles.find { it.id == id }?.let(mapper::toModel)
    }

    suspend fun save(model: ProfileModel) = update { stored ->
        val dtoList = stored.profiles.toMutableList()
        val index = dtoList.indexOfFirst { it.id == model.id }
        val dto = mapper.toDto(model)
        if (index < 0) dtoList.add(dto) else dtoList[index] = dto
        stored.copy(profiles = dtoList)
    }

    suspend fun delete(id: String) = update { stored ->
        stored.copy(
            profiles = stored.profiles.filterNot { it.id == id },
            activeProfileId = stored.activeProfileId.takeUnless { it == id },
        )
    }

    suspend fun select(id: String?) = update { stored ->
        if (id != null && stored.profiles.none { it.id == id }) throw ProfileNotFoundExceptionModel()
        stored.copy(activeProfileId = id)
    }

    private fun loadLocked(): ProfileListDto {
        val stored = if (file.exists()) {
            json.decodeFromString<ProfileListDto>(file.readText())
        } else {
            ProfileListDto()
        }
        if (stored.profiles.map { it.id }.distinct().size != stored.profiles.size) {
            throw ProfileDuplicateIdExceptionModel()
        }
        if (stored.activeProfileId != null && stored.profiles.none { it.id == stored.activeProfileId }) {
            throw ProfileMissingActiveExceptionModel()
        }
        updateCache(stored)
        return stored
    }

    private suspend fun update(transform: (ProfileListDto) -> ProfileListDto) {
        withContext(Dispatchers.IO) {
            mutex.withLock {
                val next = transform(loadLocked())
                if (!directory.isDirectory && !directory.mkdirs()) throw ProfileStorageDirectoryExceptionModel()
                val temporary = File(directory, "${DIRECTORY_NAME_PATH}.tmp")
                try {
                    FileOutputStream(temporary).use { output ->
                        output.write(
                            json.encodeToString(ProfileListDto.serializer(), next)
                                .toByteArray(Charsets.UTF_8)
                        )
                        output.fd.sync()
                    }
                    Files.move(
                        temporary.toPath(),
                        file.toPath(),
                        StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING,
                    )
                    updateCache(next)
                } finally {
                    temporary.delete()
                }
            }
        }
    }

    private fun updateCache(stored: ProfileListDto) {
        cache.value = stored
        activeProfile.value = stored.profiles
            .find { it.id == stored.activeProfileId }
            ?.let(mapper::toModel)
    }

    private companion object {
        const val DIRECTORY_NAME_PATH = "self_profiles.json"
    }
}
