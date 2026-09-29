package com.ghosty.traffic.rider.feature.selfprofile.domain.usecase

import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.ProfileParserRepository
import com.ghosty.traffic.rider.feature.selfprofile.domain.model.ProfileModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class ParseProfileUseCaseImpl(
    private val parserRepository: ProfileParserRepository,
) : ParseProfileUseCase {
    override suspend fun invoke(text: String): ProfileModel {
        return withContext(Dispatchers.IO) {
            parserRepository.parse(text)
        }
    }
}