# Структура presentation-слоя фичи

## Назначение
Задаёт единый способ организации presentation-слоя фичи в `impl`-модуле: экран собирается из независимых Block, каждый Block инкапсулирует свою бизнес-логику и UI-состояние, а рендеринг вынесен в чистые Widget без доступа к ViewModel/UseCase.

## Когда применять
При создании экрана для новой фичи, при добавлении новой независимой секции (Block) на существующий экран или при изменении состава presentation-слоя.

## Что разрешено
Расположение пакетов (`presentation/mapper`, `presentation/screen/<name>`, `presentation/screen/<name>/blocks/<section>`) — см. `feature-structure.md`.
- Экран — `<Screen>Screen` (Composable), `<Screen>ViewModel`, `state/<Screen>State`.
- `<Screen>Screen` получает ViewModel через `koinViewModel<...>()`, подписывается на `viewState`, раскладывает готовые Widget по кускам `state`; сам не содержит бизнес-логики.
- `<Screen>ViewModel` наследует `BaseViewModel<<Screen>State>`, в конструкторе принимает Block'и экрана, регистрирует их через `registerBlocks { add(...) }`, в `updateViewState()` собирает `<Screen>State` из `block.blockState.value`; методы, которые видно снаружи (например, `fetch()`), делегируют в конкретный Block. ViewModel не вызывает UseCase/Repository напрямую, если состояние экрана разбито на Block'и.
- `<Screen>State` — агрегат состояний всех Block экрана, наследует `UiState` (framework).
- Независимая секция экрана (список, топ-бар, боттом-бар и т. п.):
  - `<Screen><Section>Block` наследует `Block<State, Provider>` (framework), содержит бизнес-логику секции: вызывает UseCase, обрабатывает действия пользователя, обновляет состояние через `updateState`.
  - `<Screen><Section>UiState` — чистая проекция под UI: примитивы, `StableEvent`/`StableClickEvent`, вложенные `Item`/`ClickEvent`; без domain-моделей и Dto.
  - `<Screen><Section>BlockMapper` конвертирует domain-модель (см. `feature-layer-domain.md`) в `<Section>UiState`, заворачивает обработчики кликов в `StableEvent`/`StableClickEvent`.
  - `<Screen><Section>Widget` — чистый рендеринг по `uiState`, без обращения к Block/ViewModel/UseCase/Koin; действия пользователя идут через `uiState.clickEvent.invoke(...)`.
- Presentation-mapper, не привязанный к одному Block (например, маппер доменного исключения в текст ошибки через `ResProvider`), — вне пакета конкретного Block.
- Регистрация ViewModel/Block/Mapper в DI — через модуль слоя `presentation`, см. `feature-di.md` (`addViewModels`, `addBlocks`, `addMappers`).

## Что не разрешено
- Domain-модели или Dto в `UiState` — только примитивы и стабильные обёртки (`StableEvent` и т. п.).
- Widget, который сам обращается к ViewModel, Block, UseCase или Koin.
- Бизнес-логика в `Screen` или `Widget` вместо Block.
- Класс секции экрана, который не наследует `Block` из framework.
- Несколько `ViewModel` на один экран.

## Названия
- Screen: `<Screen>Screen`, файл `presentation/screen/<name>/<Screen>Screen.kt`, `@Composable internal fun`.
- ViewModel: `<Screen>ViewModel`, файл `presentation/screen/<name>/<Screen>ViewModel.kt`, `internal class`, наследует `BaseViewModel<<Screen>State>`.
- Состояние экрана: `<Screen>State`, файл `presentation/screen/<name>/state/<Screen>State.kt`, `@Immutable data class`, наследует `UiState`.
- Block: `<Screen><Section>Block`, файл `.../blocks/<section>/<Screen><Section>Block.kt`, `internal class`, наследует `Block<State, Provider>`.
- Состояние Block: `<Screen><Section>UiState`, файл `.../blocks/<section>/state/<Screen><Section>UiState.kt`, `@Immutable data class`.
- Mapper Block: `<Screen><Section>BlockMapper`, файл `.../blocks/<section>/mapper/<Screen><Section>BlockMapper.kt`, `internal class`.
- Widget Block: `<Screen><Section>Widget`, файл `.../blocks/<section>/widget/<Screen><Section>Widget.kt`, `@Composable internal fun`.
- Presentation-mapper вне Block: `<Feature><Purpose>Mapper`, файл `presentation/mapper/<Feature><Purpose>Mapper.kt`, `internal class`.

## Пример
`feature-selfprofile/impl/.../presentation/screen/list/ProfileListScreen.kt`:
```kotlin
@Composable
internal fun ProfileListScreen() {
    val viewModel = koinViewModel<ProfileListViewModel>()
    val state by viewModel.viewState.collectAsState()

    LaunchedEffect(Unit) { viewModel.fetch() }

    Scaffold(
        topBar = { ProfileListTopBarWidget(uiState = state.profileListTopBarState) },
        bottomBar = { ProfileListBottomBarWidget(uiState = state.profileListBottomBarState) },
    ) { paddingValues ->
        ProfileListWidget(paddingValues = paddingValues, uiState = state.profileListState)
    }
}
```

`feature-selfprofile/impl/.../presentation/screen/list/ProfileListViewModel.kt`:
```kotlin
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

    private fun getState() = ProfileListState(
        profileListState = profileListBlock.blockState.value,
        profileListTopBarState = profileListTopBarBlock.blockState.value,
        profileListBottomBarState = profileListBottomBarBlock.blockState.value,
    )
}
```

`feature-selfprofile/impl/.../presentation/screen/list/blocks/list/ProfileListBlock.kt` (сокращённо):
```kotlin
internal class ProfileListBlock(
    private val getAllProfileFlowUseCase: GetAllProfileFlowUseCase,
    private val profileListMapper: ProfileListBlockMapper,
) : Block<ProfileListUiState, Unit>() {

    override fun getInitialUiState() = ProfileListUiState()

    override fun onStartBlock() {
        blockScope?.launch {
            getAllProfileFlowUseCase.invoke().collect { list ->
                updateState { state -> state.copy(items = profileListMapper.map(list, ...)) }
            }
        }
    }

    fun fetch() { /* loadProfileUseCase.invoke() внутри blockScope */ }
}
```

`feature-selfprofile/impl/.../presentation/screen/list/blocks/list/state/ProfileListUiState.kt`:
```kotlin
@Immutable
internal data class ProfileListUiState(
    val status: UiStatus = UiStatus.Loading,
    val items: List<Item> = emptyList(),
    val activeId: String = "",
    val error: String = "",
) {
    sealed interface Item {
        val key: String
        val contentType: String
    }
}
```

`feature-selfprofile/impl/.../presentation/screen/list/blocks/list/widget/ProfileListWidget.kt`:
```kotlin
@Composable
internal fun ProfileListWidget(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues.Zero,
    uiState: ProfileListUiState,
) {
    LazyColumn(modifier = modifier, contentPadding = paddingValues) {
        items(items = uiState.items, key = { it.key }, contentType = { it.contentType }) { item ->
            when (item) {
                is ProfileUiState -> ProfileWidget(uiState = item)
            }
        }
    }
}
```

`feature-selfprofile/impl/.../presentation/mapper/ProfileErrorMapper.kt`:
```kotlin
internal class ProfileErrorMapper(
    private val resProvider: ResProvider
) {
    fun map(exception: ProfileException): String = resProvider.getString(messageRes(exception))
    fun general(): String = resProvider.getString(R.string.profile_error_load_generic)
}
```

## Связанные конвенции
- `feature-structure.md`
- `feature-di.md`
- `feature-layer-domain.md`
