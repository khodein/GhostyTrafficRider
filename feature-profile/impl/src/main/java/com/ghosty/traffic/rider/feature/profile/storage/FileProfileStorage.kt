package com.ghosty.traffic.rider.feature.profile.storage

import com.ghosty.traffic.rider.feature.profile.model.ProxyProfile
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

internal class FileProfileStorage(private val directory: File) : ProfileStorage {
    private val mutex = Mutex()
    private val state = MutableStateFlow<StoredProfiles?>(null)
    private val json = Json { ignoreUnknownKeys = true }
    private val file = File(directory, "profiles.json")

    override fun getAll(): Flow<List<ProxyProfile>> = observe()
        .map { stored -> stored.profiles.map { it.toProfile() } }
        .distinctUntilChanged()

    override fun getActive(): Flow<ProxyProfile?> = observe()
        .map { stored -> stored.profiles.find { it.id == stored.activeProfileId }?.toProfile() }
        .distinctUntilChanged()

    override suspend fun save(profile: ProxyProfile) = update { stored ->
        val profiles = stored.profiles.toMutableList()
        val index = profiles.indexOfFirst { it.id == profile.id }
        val entry = StoredProfile.from(profile)
        if (index < 0) profiles.add(entry) else profiles[index] = entry
        stored.copy(profiles = profiles)
    }

    override suspend fun delete(id: String) = update { stored ->
        stored.copy(
            profiles = stored.profiles.filterNot { it.id == id },
            activeProfileId = stored.activeProfileId.takeUnless { it == id },
        )
    }

    override suspend fun select(id: String?) = update { stored ->
        require(id == null || stored.profiles.any { it.id == id }) { "Профиль не найден" }
        stored.copy(activeProfileId = id)
    }

    private fun observe(): Flow<StoredProfiles> = flow {
        withContext(Dispatchers.IO) { mutex.withLock { load() } }
        emitAll(state.filterNotNull())
    }

    private fun load(): StoredProfiles {
        state.value?.let { return it }
        val stored = if (file.exists()) {
            json.decodeFromString<StoredProfiles>(file.readText())
        } else {
            StoredProfiles()
        }
        require(stored.profiles.map { it.id }.distinct().size == stored.profiles.size) {
            "Хранилище содержит повторяющиеся профили"
        }
        require(stored.activeProfileId == null || stored.profiles.any { it.id == stored.activeProfileId }) {
            "Активный профиль отсутствует в хранилище"
        }
        state.value = stored
        return stored
    }

    private suspend fun update(transform: (StoredProfiles) -> StoredProfiles) {
        withContext(Dispatchers.IO) {
            mutex.withLock {
                val next = transform(load())
                check(directory.isDirectory || directory.mkdirs()) { "Не удалось создать хранилище" }
                val temporary = File(directory, "profiles.json.tmp")
                try {
                    FileOutputStream(temporary).use { output ->
                        output.write(json.encodeToString(StoredProfiles.serializer(), next).toByteArray(Charsets.UTF_8))
                        output.fd.sync()
                    }
                    Files.move(
                        temporary.toPath(),
                        file.toPath(),
                        StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING,
                    )
                    state.value = next
                } finally {
                    temporary.delete()
                }
            }
        }
    }
}

@Serializable
private data class StoredProfiles(
    val profiles: List<StoredProfile> = emptyList(),
    val activeProfileId: String? = null,
)

@Serializable
private data class StoredProfile(
    val id: String,
    val name: String,
    val type: String,
    val server: String,
    val port: Int,
    val raw: String,
) {
    fun toProfile() = ProxyProfile(id, name, type, server, port, raw)

    companion object {
        fun from(profile: ProxyProfile) = StoredProfile(
            profile.id, profile.name, profile.type, profile.server, profile.port, profile.raw,
        )
    }
}
