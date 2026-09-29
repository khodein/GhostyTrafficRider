package com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list

import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.bottombar.ProfileListBottomBarBlock
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.list.ProfileListBlock
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.topbar.ProfileListTopBarBlock
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.state.ProfileListState
import com.ghosty.traffic.rider.framework.BaseViewModel

internal class ProfileListViewModel(
    private val profileListBlock: ProfileListBlock,
    private val profileListTopBarBlock: ProfileListTopBarBlock,
    private val profileListBottomBarBlock: ProfileListBottomBarBlock,
) : BaseViewModel<ProfileListState>() {

    override fun getInitialUiState() = getState()

    init {
        registerBlocks {
            add(profileListBlock)
            add(profileListTopBarBlock)
            add(profileListBottomBarBlock)
        }
    }

    fun fetch() {
        profileListBlock.fetch()
    }

    override fun updateViewState() {
        setState { getState() }
    }

    private fun getState(): ProfileListState {
        return ProfileListState(
            profileListState = profileListBlock.blockState.value,
            profileListTopBarState = profileListTopBarBlock.blockState.value,
            profileListBottomBarState = profileListBottomBarBlock.blockState.value
        )
    }

//    override fun attach() {
//        if (observation?.isActive == true) return
//        observation = viewModelScope.launch {
//            setState { copy(status = UiStatus.Loading, error = null) }
//            try {
//                combine(storage.getAll(), storage.getActive()) { profiles, active -> profiles to active }
//                    .collect { (profiles, active) ->
//                        setState { copy(status = UiStatus.Success, profiles = profiles, activeId = active?.id) }
//                    }
//            } catch (error: CancellationException) {
//                throw error
//            } catch (_: Exception) {
//                setState { copy(status = UiStatus.Error, error = "Не удалось прочитать профили") }
//            }
//        }
//    }
//
//    fun openEditor() {
//        if (!viewState.value.busy) {
//            setState { copy(editing = true, editingId = null, draft = "", error = null) }
//        }
//    }
//
//    fun edit(id: String) {
//        if (viewState.value.busy) return
//        val profile = viewState.value.profiles.find { it.id == id } ?: return
//        setState { copy(editing = true, editingId = id, draft = profile.raw, error = null) }
//    }
//
//    fun closeEditor() {
//        if (!viewState.value.busy) setState { copy(editing = false, editingId = null, draft = "", error = null) }
//    }
//    fun changeDraft(text: String) {
//        if (!viewState.value.busy) setState { copy(draft = text, error = null) }
//    }
//
//    fun format() {
//        val text = viewState.value.draft
//        perform("Не удалось форматировать YAML") {
//            val result = withContext(Dispatchers.Default) { parser.parse(text) }
//            val profile = result.getOrNull()
//            if (profile != null) setState { copy(draft = profile.raw) }
//            else setState { copy(error = result.exceptionOrNull()?.message ?: "Некорректный YAML") }
//        }
//    }
//
//    fun save() {
//        val text = viewState.value.draft
//        val editingId = viewState.value.editingId
//        perform("Не удалось сохранить профиль") {
//            val result = withContext(Dispatchers.Default) { parser.parse(text) }
//            val profile = result.getOrNull()
//            if (profile == null) {
//                setState { copy(error = result.exceptionOrNull()?.message ?: "Некорректный YAML") }
//                return@perform
//            }
//            storage.save(if (editingId == null) profile else profile.copy(id = editingId))
//            setState { copy(editing = false, editingId = null, draft = "") }
//        }
//    }
//
//    fun select(id: String) = perform("Не удалось выбрать профиль") { storage.select(id) }
//    fun delete(id: String) = perform("Не удалось удалить профиль") { storage.delete(id) }
//
//    private fun perform(message: String, action: suspend () -> Unit) {
//        if (viewState.value.busy) return
//        setState { copy(busy = true, error = null) }
//        viewModelScope.launch {
//            try {
//                action()
//            } catch (error: CancellationException) {
//                throw error
//            } catch (_: Exception) {
//                setState { copy(error = message) }
//            } finally {
//                setState { copy(busy = false) }
//            }
//        }
//    }
}
