# Структура data-слоя фичи

## Назначение
Задаёт единый способ организации data-слоя фичи в `impl`-модуле: Dto под сырую структуру источника данных, свой Mapper на каждую Dto-структуру и Repository, который инкапсулирует источник и отдаёт наружу только domain-модель.

## Когда применять
При создании data-слоя для новой фичи, при подключении нового источника данных (сеть, Room, файл и т. п.) или при добавлении новой Dto-структуры в существующую фичу.

## Что разрешено
Расположение пакетов (`data/dto`, `data/mapper`, `data/repository/<name>`) — см. `feature-structure.md`.
- Dto — `data class` с суффиксом `Dto`, описывающий сырую структуру источника данных (сетевой response, Room-сущность, файл и т. п.).
- Аннотация на Dto (`@Serializable`, Room-аннотации и т. п.) зависит от источника и не фиксируется этой конвенцией — её проставляет тот, кто подключает источник.
- Обёрточный Dto (например, список записей + метаданные, как `ProfileListDto`) — тоже имеет суффикс `Dto`.
- На каждую Dto-структуру, которая читается/пишется через domain-модель, — свой Mapper. Сама domain-модель (`<Feature>Model`) описывается отдельной конвенцией — см. `feature-layer-domain.md`.
- `<Feature>Repository` инкапсулирует доступ к одному источнику данных и отдаёт наружу только domain-модель (через Mapper).
- Регистрация Dto/Mapper/Repository в DI — через модуль слоя `data`, см. `feature-di.md` (`addMappers`, `addRepository`).

## Что не разрешено
- Возвращать Dto из публичных методов Repository или пробрасывать его в domain/presentation-слой.
- Dto без суффикса `Dto` или вне пакета `data/dto`.
- Один Mapper на несколько независимых Dto-структур — на каждую структуру свой Mapper.
- Логика маппинга Dto ↔ Model внутри Repository вместо отдельного Mapper — кроме репозитория, который ничего не хранит, а выполняет разовое преобразование (например, парсинг ввода в domain-модель); для такого репозитория Dto и Mapper не обязательны.

## Названия
- Dto: `<Concept>Dto`, файл `data/dto/<Concept>Dto.kt`, `data class`.
- Mapper: `<Dto>ToModelMapper`, файл `data/mapper/<Dto>ToModelMapper.kt`, `internal class`, методы `toModel(dto: <Dto>): <Model>` и `toDto(model: <Model>): <Dto>`.
- Repository: `<Feature>Repository`, файл `data/repository/<name>/<Feature>Repository.kt`, `internal class`, публичные методы возвращают domain-модель или `Flow<Model>`, не Dto.

## Пример
`feature-selfprofile/impl/.../data/dto/ProfileDto.kt`:
```kotlin
@Serializable
internal data class ProfileDto(
    val id: String,
    val name: String,
    val type: String,
    val server: String,
    val port: String,
    val raw: String,
)
```

`feature-selfprofile/impl/.../data/dto/ProfileListDto.kt`:
```kotlin
@Serializable
data class ProfileListDto(
    val profiles: List<ProfileDto> = emptyList(),
    val activeProfileId: String? = null,
)
```

`feature-selfprofile/impl/.../data/mapper/ProfileDtoToModelMapper.kt`:
```kotlin
internal class ProfileDtoToModelMapper {

    fun toModel(dto: ProfileDto) = ProfileModel(
        dto.id, dto.name, dto.type, dto.server, dto.port, dto.raw,
    )

    fun toDto(profile: ProfileModel) = ProfileDto(
        profile.id, profile.name, profile.type, profile.server, profile.port, profile.raw,
    )
}
```

`feature-selfprofile/impl/.../data/repository/profile/ProfileRepository.kt` (сокращённо, полный файл — в проекте):
```kotlin
internal class ProfileRepository(
    private val json: Json,
    private val context: Context,
    private val mapper: ProfileDtoToModelMapper,
) {
    private val cache = MutableStateFlow(ProfileListDto())

    fun getAllFlow(): Flow<List<ProfileModel>> =
        cache.map { stored -> stored.profiles.map(mapper::toModel) }.distinctUntilChanged()

    suspend fun save(model: ProfileModel) = update { stored ->
        val dto = mapper.toDto(model)
        // найти по id в stored.profiles и заменить/добавить dto
        stored
    }

    // load(), getById(), delete(), select() — читают/пишут ProfileListDto через json и mapper,
    // наружу возвращают только ProfileModel
}
```

## Связанные конвенции
- `feature-structure.md`
- `feature-di.md`
- `feature-layer-domain.md`
