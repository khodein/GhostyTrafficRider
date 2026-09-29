# Создание feature-модулей

## Разделение API и реализации

### Назначение
Другие модули используют публичный контракт фичи, не завися от её UI,
хранилища и конкретных классов реализации.

### Делает
- Каждая новая фича создаётся как `feature-<name>/api` и `feature-<name>/impl`.
- `api` содержит публичные модели, интерфейсы и необходимые контракты навигации.
- `impl` содержит экраны, ViewModel, парсеры, хранилища и DI-регистрацию.
- `impl` зависит от своего `api` через `implementation(project(...))`.
- Классы и функции реализации объявляются `internal`. Само имя `impl`
  не ограничивает видимость Kotlin-кода.
- Публичным в `impl` остаётся объект сборки Koin-модуля с `get()`, чтобы
  `app` мог зарегистрировать зависимости. Его сигнатура не раскрывает
  конкретные классы реализации.
- Другие фичи и `vpn` зависят только от `api`. `api` не зависит от `impl`.
- `app` собирает приложение: подключает `api`/`impl` и регистрирует Koin-модули.
  Автоматическое добавление Gradle-зависимости не регистрирует Koin-модуль.

### Не делает
- Не создаёт дополнительные слои, интерфейсы и зависимости без потребности.
- Не требует разделять инфраструктурный модуль `vpn` на `api`/`impl`.
- Не дублирует плагины и зависимости, уже предоставленные convention-плагинами.

### Пример

```text
feature-selfprofile/
  api/
    build.gradle.kts
    src/main/AndroidManifest.xml
    src/main/java/com/ghosty/traffic/rider/feature/profile/model/
  impl/
    build.gradle.kts
    src/main/AndroidManifest.xml
    src/main/java/com/ghosty/traffic/rider/feature/profile/
```

Корневой каталог фичи — контейнер, собственного `build.gradle.kts` и `src`
у него нет. Kotlin package может оставаться предметным (`feature.selfprofile`):
границу видимости задаёт Gradle-модуль, а не суффикс пакета.

## Gradle-конфигурация

### Назначение
Подключать фичи через существующие механизмы проекта без ручного дублирования.

### Делает
- Для `api` использует минимальный шаблон:

```kotlin
plugins {
    id("ghostytrafficrider.android.feature.api")
}

android {
    namespace = "com.ghosty.traffic.rider.feature.selfprofile.api"
}
```

- Для `impl` с UI использует шаблон:

```kotlin
plugins {
    id("ghostytrafficrider.android.feature")
}

android {
    namespace = "com.ghosty.traffic.rider.feature.selfprofile.impl"
}

dependencies {
    implementation(project(":feature-selfprofile:api"))
}
```

- Использует `ghostytrafficrider.android.feature.api` для `api` и
  `ghostytrafficrider.android.feature` для `impl`. Общие зависимости
  задаются в этих convention-плагинах в `build-logic`; зависимость
  `impl` от своего `api` добавляется явно.
- В обоих подмодулях создаёт пустой `src/main/AndroidManifest.xml`.
- Проверяет уникальность namespace; для новой фичи заменяет `profile`
  и `feature-selfprofile` в шаблоне.
- Использует автообнаружение в `settings.gradle.kts` и
  `implementationFeatureModules()` в `app/build.gradle.kts`: они уже
  поддерживают `feature-*/api` и `feature-*/impl`.
- При появлении биндингов добавляет Koin-модуль фичи в `AppModule.get()`.
- Проверяет структуру и зависимости статически. Gradle Sync, сборку
  и тесты выполняет пользователь, если отдельно не поручил агенту
  конкретную Gradle-команду.

### Не делает
- Не добавляет вручную `include` или зависимости фичи в `app`, когда
  существующее автообнаружение уже подходит.
- Не подключает в `api` дополнительные зависимости реализации поверх
  стандартного `android.feature.api`.
