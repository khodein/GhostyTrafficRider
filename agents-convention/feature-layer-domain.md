# Структура domain-слоя фичи

## Назначение
Задаёт единый способ организации domain-слоя фичи: неизменяемая domain-модель и UseCase — интерфейс на одно действие в `api`-модуле с единственной реализацией в `impl`-модуле.

## Когда применять
При создании domain-слоя для новой фичи, при добавлении нового доменного понятия (модели) или нового действия (UseCase) в существующую фичу.

## Что разрешено
Расположение пакетов (`api/domain/model`, `api/domain/usecase`, `impl/domain/usecase`) — см. `feature-structure.md`.
- `<Concept>Model` (api) — публичный `data class`, которым фича обменивается с другими модулями (через UseCase, `<Feature>Router` и т. п.).
- Поля модели — примитивы и другие domain-модели, без ссылок на Dto, Room-сущности или Compose/UI-типы.
- Переопределение `toString()`, если часть полей чувствительна (пароль, токен, сырой конфиг) и не должна попадать в логи.
- Несколько независимых domain-моделей на фичу, каждая в своём файле, если они выражают разные понятия, а не варианты одного и того же.
- `<Action><Concept>UseCase` (api) — интерфейс на одно действие с единственным методом `operator fun invoke(...)`; если действие асинхронное — `suspend operator fun invoke(...)`.
- UseCase-подписка (`invoke(): Flow<T>`) называется с суффиксом `Flow` — `<Action><Concept>FlowUseCase`; одноразовое получение значения (`suspend invoke(): T`) — без `Flow` в имени.
- `<Action><Concept>UseCaseImpl` (impl) — единственная реализация интерфейса; внутри вызывает Repository (см. `feature-layer-data.md`) и/или другие UseCase.
- Регистрация UseCase в DI — через модуль слоя `domain`, см. `feature-di.md` (`addUseCase`, `factoryOf(::Impl) bind Interface::class`).

## Что не разрешено
- Аннотации сериализации или persistence (`@Serializable`, Room-аннотации) на domain-модели — это ответственность Dto, см. `feature-layer-data.md`.
- Поля или варианты модели, привязанные к конкретному источнику данных (например, поле, которое есть только при чтении из Room и всегда null при чтении из сети).
- Compose/UI-типы (`State`, `Color`, `@Composable`-лямбды и т. п.) в domain-модели.
- UseCase с несколькими публичными методами вместо одного `invoke` — один UseCase выполняет одно действие.
- UseCase без интерфейса в `api`-модуле или больше одной реализации одного интерфейса.
- Обращение к Repository напрямую из presentation-слоя в обход UseCase.

## Названия
- Модель: `<Concept>Model`, файл `domain/model/<Concept>Model.kt`, `data class`.
- UseCase (api): `<Action><Concept>UseCase`, файл `domain/usecase/<Action><Concept>UseCase.kt`, `interface` с `operator fun invoke(...)`. Если `invoke` возвращает `Flow<T>` (подписка) — имя `<Action><Concept>FlowUseCase`.
- UseCase (impl): `<Action><Concept>UseCaseImpl` (или `<Action><Concept>FlowUseCaseImpl` для подписки), файл `domain/usecase/<Action><Concept>UseCaseImpl.kt`, `internal class`, реализует одноимённый интерфейс без суффикса `Impl`.

## Пример
`feature-selfprofile/api/.../domain/model/ProfileModel.kt`:
```kotlin
data class ProfileModel(
    val id: String,
    val name: String,
    val type: String,
    val server: String,
    val port: String,
    val raw: String,
) {
    // Дефолтный data class toString() вывел бы server/port/raw (в raw может быть пароль/uuid прокси) — исключаем их, чтобы не утекали в логи
    override fun toString(): String = "ProfileModel(id=$id, name=$name, type=$type)"
}
```

`feature-selfprofile/api/.../domain/usecase/GetActiveProfileFlowUseCase.kt`:
```kotlin
interface GetActiveProfileFlowUseCase {
    operator fun invoke(): Flow<ProfileModel?>
}
```

`feature-selfprofile/impl/.../domain/usecase/GetActiveProfileFlowUseCaseImpl.kt`:
```kotlin
internal class GetActiveProfileFlowUseCaseImpl(
    private val repository: ProfileRepository
) : GetActiveProfileFlowUseCase {
    override fun invoke(): Flow<ProfileModel?> {
        return repository.getActiveFlow()
    }
}
```

`feature-selfprofile/api/.../domain/usecase/DeleteProfileUseCase.kt`:
```kotlin
interface DeleteProfileUseCase {
    suspend operator fun invoke(id: String)
}
```

`feature-selfprofile/impl/.../domain/usecase/DeleteProfileUseCaseImpl.kt`:
```kotlin
internal class DeleteProfileUseCaseImpl(
    private val profileRepository: ProfileRepository
) : DeleteProfileUseCase {
    override suspend fun invoke(id: String) {
        return withContext(Dispatchers.IO) {
            profileRepository.delete(id)
        }
    }
}
```

## Связанные конвенции
- `feature-structure.md`
- `feature-layer-data.md`
- `feature-di.md`
