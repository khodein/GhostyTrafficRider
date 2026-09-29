package com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.list.mapper

import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileModel
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.list.state.ProfileListUiState
import com.ghosty.traffic.rider.feature.selfprofile.presentation.screen.list.blocks.list.state.ProfileUiState
import com.ghosty.traffic.rider.framework.stable.StableEvent

internal class ProfileListBlockMapper {

    fun map(
        list: List<ProfileModel>,
        activeModel: ProfileModel?,
        onClickDelete: (id: String) -> Unit,
        onClickEdit: (id: String) -> Unit,
        onClickSelect: (id: String) -> Unit,
    ): List<ProfileListUiState.Item> {
        return list.mapIndexed { index, model ->
            mapModelToItem(
                index = index,
                model = model,
                activeModel = activeModel,
                onClickDelete = onClickDelete,
                onClickEdit = onClickEdit,
                onClickSelect = onClickSelect,
            )
        }
    }

    private fun mapModelToItem(
        index: Int,
        activeModel: ProfileModel?,
        model: ProfileModel,
        onClickDelete: (id: String) -> Unit,
        onClickEdit: (id: String) -> Unit,
        onClickSelect: (id: String) -> Unit,
    ): ProfileListUiState.Item {
        return ProfileUiState(
            id = model.id,
            isSelected = activeModel?.id == model.id,
            name = model.name,
            type = model.type,
            server = model.server,
            port = model.port,
            clickEvent = StableEvent(
                stableKey = "click${index}${model.id}",
                action = { event ->
                    when (event) {
                        ProfileListUiState.ClickEvent.Select -> onClickSelect.invoke(model.id)
                        ProfileListUiState.ClickEvent.Edit -> onClickEdit.invoke(model.id)
                        ProfileListUiState.ClickEvent.Delete -> onClickDelete.invoke(model.id)
                    }
                }
            ),
        )
    }
}