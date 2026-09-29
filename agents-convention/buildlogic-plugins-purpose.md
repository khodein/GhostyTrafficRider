# Назначение плагинов build-logic

## Назначение
Фиксирует единственную зону ответственности каждого плагина `build-logic`, чтобы при добавлении зависимости было понятно, в какой плагин её класть, и чтобы не заводить новый плагин под то, что уже покрыто существующим.

## Когда применять
При выборе, какой плагин подключить к модулю, при добавлении новой зависимости технологии и при решении, нужен ли для неё новый плагин.

## Что разрешено
- `ghostytrafficrider.android.application` — базовая конфигурация `app`-модуля (compileSdk, defaultConfig, buildTypes, compileOptions).
- `ghostytrafficrider.android.library` — базовая конфигурация Android-library-модуля (compileSdk, defaultConfig, compileOptions).
- `ghostytrafficrider.android.core` — базовые AndroidX-зависимости (`appcompat`, `core-ktx`, `lifecycle-runtime-ktx`, `lifecycle-viewmodel-ktx`).
- `ghostytrafficrider.android.compose` — включает Compose (`buildFeatures.compose`, `kotlin.plugin.compose`) и подключает Compose-зависимости (material3, foundation, ui и т. п.).
- `ghostytrafficrider.android.test` — тестовые зависимости (JUnit, Espresso, Compose UI test).
- `ghostytrafficrider.android.datastore` — зависимость `androidx-datastore-preferences`.
- `ghostytrafficrider.android.navigation3` — зависимости Jetpack Navigation 3.
- `ghostytrafficrider.android.room` — зависимости Room и плагин `com.google.devtools.ksp`.
- `ghostytrafficrider.android.feature` — составной плагин `impl`-модуля фичи: подключает `android.library`, `android.core`, `android.compose`, `koin`, `ktor`, `coil`, `android.room`, `android.datastore` и зависимости на модули `framework`.
- `ghostytrafficrider.android.feature.api` — составной плагин `api`-модуля фичи: подключает `android.library`, `android.core`, `kotlin.plugin.compose` и минимальные Compose-зависимости (`runtime`, `ui`) для контрактов, использующих Compose-типы.
- `ghostytrafficrider.koin` — зависимости Koin (DI).
- `ghostytrafficrider.ktor` — зависимости Ktor Client и плагин `kotlin.plugin.serialization`.
- `ghostytrafficrider.coil` — зависимости Coil (загрузка изображений).

## Что не разрешено
- Добавлять зависимость технологии, не входящей в зону ответственности плагина, в чужой плагин (например, зависимость Room — в `ghostytrafficrider.koin`).
- Заводить новый плагин под технологию, уже покрытую существующим (сверяться со списком выше перед созданием).
- Плагин без единственной, ясно сформулированной ответственности из этого списка.

## Названия
Имя файла и id плагина — по `buildlogic-plugin-naming.md`.

## Пример
`ghostytrafficrider.android.feature` — единственный плагин, который `impl`-модуль фичи подключает в `plugins { }`; сам он агрегирует `android.library`, `android.core`, `android.compose`, `koin`, `ktor`, `coil`, `android.room`, `android.datastore` — модулю фичи не нужно подключать их по отдельности.

## Связанные конвенции
- `buildlogic-plugin-naming.md`
- `buildlogic-plugin-structure.md`
