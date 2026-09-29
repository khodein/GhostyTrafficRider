# AGENTS.md

Формат файлов конвенций в `agents-convention/` — `agents-convention/convention-file-structure.md`.

## Feature

Фича — Gradle-модуль `feature-<name>` из двух подмодулей, `api` и `impl` (разделение и Gradle-конфигурация — `docs/MODULES.md`). Расположение пакетов и файлов внутри `api`/`impl` по слоям, DI-регистрация и навигация описаны отдельными конвенциями в `agents-convention/`; при создании или изменении фичи руководствоваться ими, а не выводить структуру заново.

- `agents-convention/feature-structure.md` — раскладка папок и пакетов фичи, единый источник знаний о том, где лежит какой класс.
- `agents-convention/feature-di.md` — структура DI-модуля фичи: агрегатор + модуль на каждый слой.
- `agents-convention/feature-router.md` — навигация: `Router.Provider` и `NavKey` внутри фичи, интерфейс `<Feature>Router` для вызова навигации извне.
- `agents-convention/feature-layer-data.md` — data-слой: Dto, Mapper, Repository.
- `agents-convention/feature-layer-domain.md` — domain-слой: domain-модель, UseCase.
- `agents-convention/feature-layer-presentation.md` — presentation-слой: Screen, ViewModel, Block, UiState, Mapper, Widget.

## Build-logic

Общие Gradle-настройки и зависимости подключаются модулям через precompiled script plugin'ы из `build-logic/src/main/kotlin`, а не прописываются в `build.gradle.kts` модулей напрямую. Именование, внутренняя структура и назначение каждого плагина описаны отдельными конвенциями в `agents-convention/`; при создании или изменении плагина руководствоваться ими.

- `agents-convention/buildlogic-plugin-naming.md` — как называть файл плагина и его id.
- `agents-convention/buildlogic-plugin-structure.md` — из чего состоит плагин: version catalog, `add<Название>Dependencies()`, `pluginManager.withPlugin(...)`.
- `agents-convention/buildlogic-plugins-purpose.md` — зона ответственности каждого существующего плагина.
