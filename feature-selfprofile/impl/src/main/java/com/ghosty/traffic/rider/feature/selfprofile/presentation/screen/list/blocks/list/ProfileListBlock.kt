package com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.list

import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileExceptionModel
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.DeleteProfileUseCase
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.GetActiveProfileFlowUseCase
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.GetAllProfileFlowUseCase
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.LoadProfileUseCase
import com.ghosty.traffic.rider.feature.selfprofile.domain.usecase.SelectProfileUseCase
import com.ghosty.traffic.rider.feature.selfprofile.presentation.mapper.ProfileErrorMapper
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.list.mapper.ProfileListBlockMapper
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.list.state.ProfileListUiState
import com.ghosty.traffic.rider.framework.UiStatus
import com.ghosty.traffic.rider.framework.block.Block
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

internal class ProfileListBlock(
    private val getAllProfileUseCase: GetAllProfileFlowUseCase,
    private val getActiveProfileUseCase: GetActiveProfileFlowUseCase,
    private val selectProfileUseCase: SelectProfileUseCase,
    private val deleteProfileUseCase: DeleteProfileUseCase,
    private val loadProfileUseCase: LoadProfileUseCase,
    private val profileListMapper: ProfileListBlockMapper,
    private val profileErrorMapper: ProfileErrorMapper,
) : Block<ProfileListUiState, Unit>() {

    private var loadJob: Job? = null
    private var selectJob: Job? = null
    private var deleteJob: Job? = null

    override fun getInitialUiState(): ProfileListUiState {
        return ProfileListUiState()
    }

    override fun onStartBlock() {
        blockScope?.launch {
            combine(
                getAllProfileUseCase.invoke(),
                getActiveProfileUseCase.invoke(),
            ) { all, active ->
                active to all
            }.collect { result ->
                updateState { state ->
                    state.copy(
                        activeId = result.first?.id.orEmpty(),
                        items = profileListMapper.map(
                            list = result.second,
                            activeModel = result.first,
                            onClickDelete = ::onClickDelete,
                            onClickEdit = ::onClickEdit,
                            onClickSelect = ::onClickSelect
                        )
                    )
                }
            }
        }
    }

    fun fetch() {
        loadJob?.cancel()
        loadJob = blockScope?.launch {
            updateStatus(UiStatus.Loading)
            runCatching {
                loadProfileUseCase.invoke()
            }.onSuccess {
                updateStatus(UiStatus.Success)
            }.onFailure { error ->
                updateError(error)
                updateStatus(UiStatus.Error)
            }
        }
    }

    private fun updateStatus(status: UiStatus) {
        updateState { state -> state.copy(status = status) }
    }

    private fun updateError(error: Throwable) {
        val message = if (error is ProfileExceptionModel) {
            profileErrorMapper.map(error)
        } else {
            error.message
        }
        updateState { state ->
            state.copy(
                error = message ?: profileErrorMapper.general()
            )
        }
    }

    private fun onClickDelete(profileId: String) {
        deleteJob?.cancel()
        deleteJob = blockScope?.launch {
            runCatching {
                deleteProfileUseCase.invoke(profileId)
            }.onFailure {

            }
        }
    }

    private fun onClickEdit(profileId: String) {
        // Добавить переход на экран редактирования профиля
    }

    private fun onClickSelect(profileId: String) {
        selectJob?.cancel()
        selectJob = blockScope?.launch {
            runCatching {
                selectProfileUseCase.invoke(profileId)
            }.onFailure {

            }
        }
    }
}