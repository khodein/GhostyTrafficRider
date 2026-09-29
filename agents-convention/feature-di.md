# Структура DI-модуля фичи

## Назначение
Задаёт единый способ регистрации Koin-биндингов фичи в `impl`-модуле: один агрегатор в пакете `di` и отдельный модуль на каждый слой (data, domain, presentation, router), сгруппированный по типу биндингов.

## Когда применять
При создании DI-регистрации для новой фичи в `impl`-модуле или при добавлении новых биндингов в существующую фичу.

## Что разрешено
Расположение пакета `di` и пакетов слоёв (`data/`, `domain/`, `presentation/`, `router/`) — см. `feature-structure.md`.
- Пакет `di` содержит только один файл — агрегатор `<Feature>Module`, который собирает `Module` из модулей слоёв.
- Модуль слоя (`<Feature><Layer>Module`) лежит в корне пакета этого слоя, а не в `di/`.
- Модуль слоя группирует биндинги по типу в приватных extension-функциях `Module.addXxx()` (`addMappers`, `addRepository`, `addUseCase`, `addViewModels`, `addBlocks`, `addProviders`, `addValidator` и т. п.).
- `fun get(module: Module) = with(module) { addXxx(); ... }` в модуле слоя — единственная публичная точка входа.
- Агрегатор вызывает `<Layer>Module.get(this)` для каждого присутствующего в фиче слоя и не регистрирует биндинги напрямую.
- Использовать `singleOf` / `factoryOf` / `viewModelOf` вместо `single { Impl(...) }`, когда конструктор не требует ручной сборки параметров; `bind` — для привязки реализации к интерфейсу.

## Что не разрешено
- Регистрировать биндинги напрямую в агрегаторе `di/<Feature>Module`, минуя модуль слоя.
- Класть модуль слоя внутрь пакета `di`.
- Один плоский модуль на всю фичу без разбивки по слоям и без группировки `addXxx()`.
- Публичная (не `internal`) видимость у модулей — все `object` DI-модулей фичи объявляются `internal`.

## Названия
- Агрегатор: файл `di/<Feature>Module.kt`, `internal object <Feature>Module`, метод `fun get(): Module`.
- Модуль слоя: файл `<layer>/<Feature><Layer>Module.kt`, `internal object <Feature><Layer>Module` (`<Layer>` = `Data`, `Domain`, `Presentation`, `Router` и т. п.), метод `fun get(module: Module)`.
- Группирующая функция: `private fun Module.add<Группа>()`, где `<Группа>` — во множественном числе тип биндинга (`Mappers`, `UseCase`, `ViewModels`, `Blocks`, `Providers`, `Validator`).

## Пример
`feature-selfprofile/impl/.../di/ProfileModule.kt`:
```kotlin
internal object ProfileModule {
    fun get() = module {
        ProfileDataModule.get(this)
        ProfileDomainModule.get(this)
        ProfileRouterModule.get(this)
        ProfilePresentationModule.get(this)
    }
}
```

`feature-selfprofile/impl/.../data/ProfileDataModule.kt`:
```kotlin
internal data object ProfileDataModule {
    fun get(module: Module) = with(module) {
        addMappers()
        addRepository()
        addValidator()
    }

    private fun Module.addMappers() {
        singleOf(::ProfileDtoToModelMapper)
    }
    // addRepository(), addValidator() аналогично
}
```

## Связанные конвенции
- `feature-structure.md`
- `feature-router.md`
- `feature-layer-data.md`
- `feature-layer-domain.md`
- `feature-layer-presentation.md`
