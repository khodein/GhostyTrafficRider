# Router фичи

## Назначение
Задаёт единый способ регистрации навигационных маршрутов фичи и вызова навигации в фичу извне: `Router.Provider` в `impl`-модуле маппит `NavKey`-маршруты (`Key`) фичи на экраны, а интерфейс `<Feature>Router` в `api`-модуле — единственная точка, через которую другие фичи запускают навигацию в эту фичу, не зная о её внутренних `Key`.

## Когда применять
При создании навигации для новой фичи или при добавлении нового маршрута/метода навигации в существующую фичу.

## Что разрешено
Расположение пакетов (`api/router`, `impl/router`, `impl/router/keys`) — см. `feature-structure.md`.
- `<Feature>Router` (api) — публичный интерфейс с методами навигации (`goTo...`), которые вызывают другие фичи.
- `<Feature>RouterModule` (impl) — DI, см. `feature-di.md`.
- `<Feature>RouterProvider` (impl) — единственная реализация `Router.Provider` в фиче, регистрирует в `invoke(): EntryProviderInstaller` все маршруты фичи через `entry<Key>(metadata = ...) { Screen() }`.
- `<Feature>RouterImpl` (impl) — реализация `<Feature>Router`, вызывает `Router.goTo`/`goBack` из framework с конкретными `Key`.
- `<Screen>Key` (impl) — `NavKey`-маршруты фичи.
- Для обычного экрана — `metadata = navTransitionMetadata(NavTransition.X)`.
- Для диалогового/bottom-sheet маршрута — `metadata = BottomSheetSceneStrategy.bottomSheet()`, с `@OptIn(ExperimentalMaterial3Api::class)` на `invoke()`.

## Что не разрешено
- Более одного `Router.Provider` на фичу.
- `Key` в `api`-модуле — они внутренние для `impl`, наружу фича торчит только методами `<Feature>Router`.
- Регистрация `Router.Provider` где-либо, кроме `<Feature>RouterModule` (`single<Router.Provider> { ... }`).
- Прямая навигация между экранами фичи в обход `Router` (`goTo`/`goBack`) — обращение к `NavDisplay`/backstack напрямую из фичи.
- Переход в другую фичу по её `Key` напрямую — только через её `<Feature>Router`.
- `Router.Provider` или `<Feature>RouterImpl` без привязки к DI-модулю фичи.

## Названия
- Интерфейс (api): `<Feature>Router`, файл `router/<Feature>Router.kt`.
- Реализация интерфейса (impl): `<Feature>RouterImpl`, файл `router/<Feature>RouterImpl.kt`, `internal class`, реализует `<Feature>Router`.
- DI-модуль (impl): `<Feature>RouterModule`, файл `router/<Feature>RouterModule.kt`.
- Провайдер (impl): `<Feature>RouterProvider`, файл `router/<Feature>RouterProvider.kt`, `internal class`, реализует `Router.Provider`.
- Маршруты (impl): `<Screen>Key`, файл `router/keys/<Screen>Key.kt`, `data object`/`data class`, реализует `NavKey`.

## Пример
`feature-selfprofile/api/.../router/ProfileRouter.kt`:
```kotlin
interface ProfileRouter {
    fun goToProfileList()
    fun goToProfileEditor()
}
```

`feature-selfprofile/impl/.../router/keys/ProfileListKey.kt`:
```kotlin
data object ProfileListKey : NavKey
```

`feature-selfprofile/impl/.../router/ProfileRouterImpl.kt`:
```kotlin
internal class ProfileRouterImpl(
    private val router: Router,
) : ProfileRouter {

    override fun goToProfileList() {
        router.goTo(ProfileListKey)
    }

    override fun goToProfileEditor() {
        router.goTo(ProfileEditorKey)
    }
}
```

`feature-selfprofile/impl/.../router/ProfileRouterProvider.kt`:
```kotlin
internal class ProfileRouterProvider : Router.Provider {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun invoke(): EntryProviderInstaller = {
        entry<ProfileListKey>(metadata = navTransitionMetadata(NavTransition.FADE)) {
            ProfileListScreen()
        }

        entry<ProfileListDialogKey>(
            metadata = BottomSheetSceneStrategy.bottomSheet()
        ) {
            ProfileListDialogScreen()
        }

        entry<ProfileEditorKey>(
            metadata = navTransitionMetadata(NavTransition.FADE)
        ) {

        }
    }
}
```

`feature-selfprofile/impl/.../router/ProfileRouterModule.kt`:
```kotlin
internal object ProfileRouterModule {
    fun get(module: Module) = with(module) {
        addProviders()
        addRoutes()
    }

    private fun Module.addProviders() {
        single<Router.Provider> { ProfileRouterProvider() }
    }

    private fun Module.addRoutes() {
        singleOf(::ProfileRouterImpl) bind ProfileRouter::class
    }
}
```

## Связанные конвенции
- `feature-structure.md`
- `feature-di.md`
