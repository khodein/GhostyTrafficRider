# Структура плагина build-logic

## Назначение
Задаёт единую внутреннюю структуру precompiled script plugin'а в `build-logic`, чтобы подключение зависимостей одной технологии выглядело одинаково независимо от того, кто и когда написал плагин.

## Когда применять
При создании нового precompiled script plugin в `build-logic/src/main/kotlin` или при добавлении зависимостей в существующий.

## Что разрешено
- `val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")` — получение version catalog, если плагин подключает зависимости из `libs.versions.toml`.
- Одна extension-функция `DependencyHandler.add<Название>Dependencies()`, которая через `add("implementation"/"ksp"/..., libs.findLibrary("...").get())` перечисляет все зависимости этой технологии.
- `pluginManager.withPlugin("com.android.application") { dependencies.add<Название>Dependencies() }` и такой же блок для `"com.android.library"` — чтобы плагин работал и в `app`, и в library-модулях.
- `plugins { id(...) }` в начале файла — только для стороннего Gradle-плагина, без которого технология не работает (`org.jetbrains.kotlin.plugin.serialization` для ktor, `com.google.devtools.ksp` для room).
- Для плагина, который конфигурирует AGP-расширение (`android.application`, `android.library`, `android.compose` и т. п.), вместо `add<Название>Dependencies()` — `extensions.configure<ApplicationExtension>("android") { ... }` / `extensions.configure<LibraryExtension>("android") { ... }`, при необходимости обёрнутое в `pluginManager.withPlugin(...)`, если плагин применяется и к `application`, и к `library`.
- Составной плагин (`android.feature`, `android.feature.api`) в `plugins { }` подключает другие плагины `build-logic` вместо повторения их содержимого, и добавляет только свои специфичные зависимости через `add<Название>Dependencies()`.

## Что не разрешено
- Безусловное `dependencies { implementation(...) }` без `pluginManager.withPlugin(...)` в плагине, который может быть применён и к `application`, и к `library`.
- Захардкоженная версия библиотеки вместо `libs.findLibrary("...")` из version catalog.
- Несколько технологий (например, Room и DataStore) в одной extension-функции `addXxxDependencies()` одного плагина.
- Дублирование содержимого другого плагина `build-logic` вместо подключения его через `plugins { id(...) }`.

## Названия
- Extension-функция зависимостей: `DependencyHandler.add<Название>Dependencies()`, где `<Название>` — технология в PascalCase без префикса `android` (`addKtorDependencies`, `addRoomDependencies`, `addFeatureDependencies`).

## Пример
`build-logic/src/main/kotlin/ghostytrafficrider.ktor.gradle.kts`:
```kotlin
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.getByType

plugins {
    id("org.jetbrains.kotlin.plugin.serialization")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun DependencyHandler.addKtorDependencies() {
    add("implementation", platform(libs.findLibrary("ktor-client-bom").get()))
    add("implementation", libs.findLibrary("ktor-client-core").get())
    // остальные ktor-зависимости аналогично
}

pluginManager.withPlugin("com.android.application") {
    dependencies.addKtorDependencies()
}

pluginManager.withPlugin("com.android.library") {
    dependencies.addKtorDependencies()
}
```

## Связанные конвенции
- `buildlogic-plugin-naming.md`
- `buildlogic-plugins-purpose.md`
