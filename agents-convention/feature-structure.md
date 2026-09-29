# Структура папок фичи

## Назначение
Задаёт единую раскладку каталогов и пакетов внутри `api`- и `impl`-модулей фичи — единый источник знаний о том, где лежит какой класс. Остальные конвенции фичи ссылаются на этот файл вместо того, чтобы повторять структуру папок.

## Когда применять
При создании новой фичи, при добавлении нового слоя или пакета в существующую фичу, при проверке, что структура фичи соответствует принятой.

## Что разрешено
Корень `feature-<name>/` — контейнер из двух Gradle-модулей, `api` и `impl` (Gradle-конфигурация — `docs/MODULES.md`), собственного кода не содержит.

`api`, пакет `com.ghosty.traffic.rider.feature.<name>`:
- `domain/model` — domain-модели, см. `feature-layer-domain.md`.
- `domain/exception` — доменные исключения фичи.
- `domain/usecase` — интерфейсы UseCase, см. `feature-layer-domain.md`.
- `router` — интерфейс `<Feature>Router`, см. `feature-router.md`.

`impl`, тот же корневой пакет `com.ghosty.traffic.rider.feature.<name>`:
- `di` — агрегатор DI-модулей фичи, см. `feature-di.md`.
- `data/dto`, `data/mapper`, `data/repository/<name>` — data-слой, см. `feature-layer-data.md`.
- `domain/usecase` — реализации UseCase, см. `feature-layer-domain.md`.
- `router`, `router/keys` — `Router.Provider`/`<Feature>RouterImpl`/DI и `NavKey`-маршруты, см. `feature-router.md`.
- `presentation/mapper` — presentation-мапперы вне конкретного Block, см. `feature-layer-presentation.md`.
- `presentation/screen/<name>` — экран (`Screen`, `ViewModel`, `state/`), см. `feature-layer-presentation.md`.
- `presentation/screen/<name>/blocks/<section>` — независимая секция экрана (Block, `mapper/`, `state/`, `widget/`), см. `feature-layer-presentation.md`.

## Что не разрешено
- Классы data/domain/presentation/router-слоя вне пакетов, описанных выше.
- Повторное описание этой раскладки в других конвенциях фичи — они ссылаются на `feature-structure.md`.
- Разная раскладка пакетов у разных фич без предметной причины.

## Названия
- Модуль фичи: `feature-<name>` (kebab-case), состоит из `feature-<name>/api` и `feature-<name>/impl`.
- Kotlin-пакет: `com.ghosty.traffic.rider.feature.<name>` — без дефисов, `<name>` в одно слово (`selfprofile`, а не `self-profile`).

## Пример
`feature-selfprofile/`:
```text
feature-selfprofile/
  api/.../feature/selfprofile/
    domain/
      exception/    ProfileException.kt, ProfileParseException.kt
      model/        ProfileModel.kt
      usecase/      GetAllProfileFlowUseCase.kt, SetProfileUseCase.kt, ...
    router/         ProfileRouter.kt
  impl/.../feature/selfprofile/
    data/
      dto/                    ProfileDto.kt, ProfileListDto.kt
      mapper/                 ProfileDtoToModelMapper.kt
      repository/
        profile/              ProfileRepository.kt
        parser/               ProfileParserRepository.kt
          validator/factory/  ...ValidatorImpl.kt
    di/             ProfileModule.kt
    domain/
      usecase/      GetAllProfileFlowUseCaseImpl.kt, SetProfileUseCaseImpl.kt, ...
    presentation/
      mapper/       ProfileErrorMapper.kt, ProfileParseErrorMapper.kt
      screen/list/
        ProfileListScreen.kt
        ProfileListViewModel.kt
        state/      ProfileListState.kt
        blocks/
          list/     ProfileListBlock.kt + mapper/ state/ widget/
          topbar/   ProfileListTopBarBlock.kt + mapper/ state/ widget/
          bottombar/ ProfileListBottomBarBlock.kt + mapper/ state/ widget/
    router/
      ProfileRouterImpl.kt
      ProfileRouterProvider.kt
      ProfileRouterModule.kt
      keys/         ProfileListKey.kt, ProfileListDialogKey.kt, ProfileEditorKey.kt
```

## Связанные конвенции
- `feature-di.md`
- `feature-router.md`
- `feature-layer-data.md`
- `feature-layer-domain.md`
- `feature-layer-presentation.md`
