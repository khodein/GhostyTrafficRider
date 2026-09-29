# Именование плагинов build-logic

## Назначение
Задаёт единый способ именования precompiled script plugin'ов в `build-logic`: по имени файла Gradle сам выводит id плагина, поэтому имя файла должно однозначно и предсказуемо описывать зону ответственности плагина.

## Когда применять
При создании нового precompiled script plugin в `build-logic/src/main/kotlin`.

## Что разрешено
- Имя плагина — точечный путь от общего к частному: `ghostytrafficrider.<сегмент>[.<сегмент>...]`.
- Префикс `android.` — для плагинов, которые конфигурируют AGP-расширение (`ApplicationExtension`/`LibraryExtension`) или подключают AndroidX-библиотеку (`ghostytrafficrider.android.application`, `ghostytrafficrider.android.core`, `ghostytrafficrider.android.compose`, `ghostytrafficrider.android.room`, `ghostytrafficrider.android.datastore`, `ghostytrafficrider.android.navigation3`, `ghostytrafficrider.android.test`).
- Без префикса `android.` — для плагинов, подключающих зависимости стороннней (не AndroidX) библиотеки, не привязанной концептуально к Android (`ghostytrafficrider.koin`, `ghostytrafficrider.ktor`, `ghostytrafficrider.coil`).
- Составной/специализированный плагин добавляет уточняющий сегмент в конец: `ghostytrafficrider.android.feature` → `ghostytrafficrider.android.feature.api` для варианта под `api`-модуль фичи.
- Id плагина в `plugins { id(...) }` других файлов и в `build.gradle.kts` модулей всегда совпадает с именем файла без `.gradle.kts`.

## Что не разрешено
- Общие/резиновые сегменты в имени (`utils`, `common`, `base`) вместо названия конкретной технологии или зоны ответственности.
- Префикс `android.` у плагина, который не трогает AGP-расширение и не привязан к AndroidX-библиотеке.
- Расхождение между именем файла и тем, что плагин реально настраивает (например, `ghostytrafficrider.room` для плагина, который также тянет networking-зависимости).

## Названия
Файл: `ghostytrafficrider.<сегмент>[.<сегмент>...].gradle.kts`, кладётся в `build-logic/src/main/kotlin/`. Id плагина = имя файла без `.gradle.kts`.

## Пример
`build-logic/src/main/kotlin/ghostytrafficrider.ktor.gradle.kts` → id плагина `ghostytrafficrider.ktor` (сторонняя библиотека, без `android.`).

`build-logic/src/main/kotlin/ghostytrafficrider.android.feature.api.gradle.kts` → id плагина `ghostytrafficrider.android.feature.api` (AndroidX/AGP-конфигурация + специализация `.api` поверх `ghostytrafficrider.android.feature`).

## Связанные конвенции
- `buildlogic-plugin-structure.md`
- `buildlogic-plugins-purpose.md`
