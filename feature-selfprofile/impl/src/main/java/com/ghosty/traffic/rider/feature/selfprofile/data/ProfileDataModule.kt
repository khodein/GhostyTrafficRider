package com.ghosty.traffic.rider.feature.selfprofile.data

import com.ghosty.traffic.rider.feature.selfprofile.data.mapper.ProfileDtoToModelMapper
import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.ProfileParserRepository
import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.ProfileParserValidator
import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.factory.ModeProfileParserValidatorImpl
import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.factory.NameProfileParserValidatorImpl
import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.factory.PortProfileParserValidatorImpl
import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.factory.ProxyGroupProfileParserValidatorImpl
import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.factory.ServerProfileParserValidatorImpl
import com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator.factory.TypeProfileParserValidatorImpl
import com.ghosty.traffic.rider.feature.selfprofile.data.repository.profile.ProfileRepository
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal object ProfileDataModule {

    fun get(module: Module) = with(module) {
        addMappers()
        addRepository()
        addValidator()
    }

    private fun Module.addMappers() {
        singleOf(::ProfileDtoToModelMapper)
    }

    private fun Module.addRepository() {
        single { ProfileParserRepository(validatorList = getAll()) }
        single {
            ProfileRepository(
                json = Json { ignoreUnknownKeys = true },
                context = androidContext(),
                mapper = get<ProfileDtoToModelMapper>()
            )
        }
    }

    private fun Module.addValidator() {
        factoryOf(::NameProfileParserValidatorImpl) bind ProfileParserValidator::class
        factoryOf(::ModeProfileParserValidatorImpl) bind ProfileParserValidator::class
        factoryOf(::TypeProfileParserValidatorImpl) bind ProfileParserValidator::class
        factoryOf(::ServerProfileParserValidatorImpl) bind ProfileParserValidator::class
        factoryOf(::PortProfileParserValidatorImpl) bind ProfileParserValidator::class
        factoryOf(::ProxyGroupProfileParserValidatorImpl) bind ProfileParserValidator::class
    }
}