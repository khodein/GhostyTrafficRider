# Контур 1 — задачи

## Порядок для быстрого результата

Буквы стадий группируют задачи по области, а не задают строгий порядок.
Сначала собираем минимальный сквозной путь, затем завершаем весь MVP.

1. **Первое рабочее подключение:** A → B → C → D1/D4/D5 (минимальный UI
   вставки и сохранения одного активного профиля) → E → G → H → I/J → K1.
   В G домены пока передаются пустым списком. I и J выполняются совместно:
   разрешение, запуск, STOP и обработка ошибок нужны в одном проходе.
   Проверяем предоставленный пользователем тип подключения: Shadowsocks +
   v2ray-plugin + WebSocket/TLS и маршрутизацию Android-приложений.
2. **Завершение MVP:** F (экран доменов) → D2/D3 и завершение D5
   (список профилей, удаление, импорт файла, выбор из нескольких) → K2.
3. **Необязательная полировка:** L после готовности MVP.

Первый проход — промежуточный результат; готовый MVP включает оба прохода.
Зависимости задачи должны быть готовы до её проверки. Gradle-компиляцию,
сборку и тесты запускает пользователь; агент выполняет статические проверки,
если пользователь отдельно не запросил конкретную Gradle-команду.

Статусы: `[ ]` не начато, `[~]` в работе, `[x]` готово. Отмечай по ходу —
это рабочий чеклист, не архив.

## Стадия A — Каркас модулей

Каркас создан. Пользователь подтвердил успешный Gradle Sync после
перехода на convention-плагины. Сборка агентом не запускалась.

### [x] A1. Создать модули `feature-selfprofile:api` и `feature-selfprofile:impl`
- Структура по [../MODULES.md](../MODULES.md): пустой `api` с
  `android.feature.api`, `impl` зависит от `api`. Namespace API:
  `com.ghosty.traffic.rider.feature.selfprofile.api`.
- `feature-selfprofile/impl/build.gradle.kts` с плагином `android.feature`
  (см. SPEC → "Модули"), `namespace = "com.ghosty.traffic.rider.feature.selfprofile.impl"`.
- `feature-selfprofile/impl/src/main/AndroidManifest.xml` — пустой (`<manifest />`),
  как у `:framework:router`/`:framework:tools`.
- Пустой Koin-модуль `ProfileFeatureModule` (пока без биндингов) в
  `feature-selfprofile/impl/src/main/java/.../feature/profile/ProfileFeatureModule.kt`.
- **Готово, когда**: `./gradlew :feature-selfprofile:impl:assembleDebug` (или
  синхронизация в Android Studio) проходит без ошибок, модуль виден в
  Project view.

### [x] A2. Создать модули `feature-routing:api` и `feature-routing:impl`
- API namespace: `com.ghosty.traffic.rider.feature.routing.api`.
- То же самое, `namespace = "com.ghosty.traffic.rider.feature.routing.impl"`.
- **Готово, когда**: аналогично A1.

### [x] A3. Создать модуль `vpn`
- `vpn/build.gradle.kts`: `android.library` + `android.core` (без Compose —
  это не-UI модуль), `namespace = "com.ghosty.traffic.rider.vpn"`.
- **Готово, когда**: модуль виден, пустой, собирается.

## Стадия B — Модель и парсинг YAML

B1–B3 и C1 реализованы; статус `[~]` до проверки пользователем.
По решению пользователя тесты пока отложены: тестовые критерии ниже
остаются ориентирами для последующей проверки. Gradle не запускался.

> Формат входа решён (см. SPEC → "Данные"): пользователь приносит **весь**
> Mihomo-конфиг (`mode`/`dns`/`proxies`/`proxy-groups`/...) целиком, без
> `rules:` — `rules:` всегда генерирует приложение. Если `rules:` в YAML
> всё же есть — вырезать молча (см. SPEC → "Данные").

### [~] B1. Добавить YAML-зависимость
- Добавлен SnakeYAML Engine 2.9 (альтернатива архивированному `kaml`) в `gradle/libs.versions.toml`
  (`[versions]` + `[libraries]`) и в `feature-selfprofile/impl/build.gradle.kts`.
- **Готово, когда**: пустой тестовый вызов парсера компилируется и линкуется
  (проверка пользователем; тесты пока отложены).

### [~] B2. Определить `ProxyProfile` и парсер
- `data class ProxyProfile` (см. SPEC → "Данные") в `feature-selfprofile:api`
  (пакет `.../feature/profile/model/`).
- `internal` функция/класс `ProxyProfileParser` в `feature-selfprofile:impl`, принимает сырой YAML-текст целиком,
  возвращает `Result<ProxyProfile>` (или sealed class
  `ParseResult.Success/Error`). Логика:
  1. распарсить YAML в дерево (YAML-узел, не строгий data class — конфиг
     пользователя может содержать что угодно из Clash-конфига);
  2. прочитать `proxies[0].name/type/server/port` для отображения в UI;
  3. если на верхнем уровне есть ключ `rules` — удалить его из дерева;
  4. сериализовать дерево обратно в YAML-текст → это `raw`.
- **Готово, когда**: unit-тест на 2–3 примерах YAML (ss, vmess, битый файл)
  проходит — успешный парсинг даёт корректные `name/type/server/port` и
  `raw` без `rules:` (даже если во входном файле `rules:` был), битый YAML
  даёт `Error`, не кидает необработанное исключение.

### [~] B3. Валидация обязательных полей
- После парсинга: `proxies` не пустой, у `proxies[0]` есть
  `name`/`type`/`server`/`port`, иначе `ParseResult.Error` с понятным
  сообщением (какого поля не хватает).
- Проверить `mode: rule` и наличие группы `PROXY` (контракт входа из SPEC).
- **Готово, когда**: unit-тест на YAML без `proxies` (например) даёт
  предсказуемую ошибку, а не NPE/ClassCastException.

## Стадия C — Хранение

### [~] C1. Файловое хранилище профилей
- Контракт `ProfileStorage` в `feature-selfprofile:api`, `internal` реализация
  в `feature-selfprofile:impl`: читает/пишет
  `profiles.json` в `filesDir` через `kotlinx.serialization`.
- API: `getAll(): Flow<List<ProxyProfile>>`, `save(profile: ProxyProfile)`,
  `delete(id: String)`, `getActive(): Flow<ProxyProfile?>`, `select(id: String?)`.
- Профили и `activeProfileId` записываются атомарно в один JSON-файл.
  Повреждённое хранилище возвращает ошибку, а не заменяется пустым.
  Удаление активного профиля сбрасывает сохранённый выбор.
- **Готово, когда**: unit/instrumented-тест — сохранить профиль, пересоздать
  `ProfileStorage` (эмулируя перезапуск процесса), профиль на месте.

### C2. Хранилище правил (`ProxyPackage`/`ProxyDomain`)
- Модели и контракт — в `feature-routing:api`, `internal` файловая
  реализация — в `feature-routing:impl`: `proxy_packages.json`, `proxy_domains.json`.
- API: `getPackages(): Flow<Set<ProxyPackage>>`, запись/обновление правила
  по package name с `RouteTarget`, удаление правила; аналогично для доменов.
- Смена режима заменяет правило, не создаёт второе для того же ключа.
- **Готово, когда**: тот же тест на переживание "перезапуска" для обеих
  сущностей.

## Стадия D — Экран Profiles

Реализованы ViewModel, стартовый экран списка, диалог вставки YAML,
выбор активного профиля и удаление. Импорт файла D3 ещё не реализован.
Статус `[~]` — ожидает компиляции и ручной проверки пользователем.
Строки MVP заданы в коде, иконки — из имеющегося Material Icons.

### [~] D1. Koin-биндинги + `ProfileListViewModel`
- `ViewModel : BaseViewModel<ProfileListState>` (см. SPEC → базовые классы
  из `:framework`), состояние — список профилей + статус загрузки.
- Регистрация во `ProfileFeatureModule` (Koin), подписка на
  `ProfileStorage.getAll()`.
- **Готово, когда**: ViewModel отдаёт актуальный список при изменении
  хранилища (проверить логированием/тестом, экран ещё не обязателен).

### [~] D2. UI списка профилей
- Экран со списком (`name`, `type`, `server:port`), пустое состояние
  ("нет профилей, импортируй файл или вставь YAML"), кнопки "Импорт файла" /
  "Вставить YAML", удаление профиля (свайп или иконка), использует
  `AppTheme` (см. SPEC).
- Зарегистрирован в навигации через `Router.Provider` в
  `ProfileFeatureModule`.
- **Готово, когда**: экран открывается из `MainActivity`, показывает пустое
  состояние на чистой установке.

### D3. Импорт файла
- `ActivityResultContracts.OpenDocument()` (MIME `*/*` или `application/x-yaml`,
  фильтровать по расширению после выбора, т.к. YAML MIME ненадёжен),
  читает содержимое через `ContentResolver`, прогоняет через
  `ProxyProfileParser` (B2), при успехе — `ProfileStorage.save`.
- Ошибка парсинга — снэкбар/диалог с понятным текстом (не обязательно через
  `uikit`, простой `AlertDialog`/`Text`, свой для этого контура).
- **Готово, когда**: выбор валидного `.yaml` файла добавляет профиль в
  список D2; выбор битого файла показывает ошибку, список не меняется.

### [~] D4. Вставка YAML вручную
- Экран/bottom sheet с `TextField(singleLine = false)` + кнопка "Сохранить",
  тот же `ProxyProfileParser` + `ProfileStorage.save`.
- **Готово, когда**: вставленный текст того же YAML, что и в D3, даёт
  идентичный профиль в списке.

### [~] D5. Выбор активного профиля
- В списке — отметка "выбран" (radio/чекбокс), хранится либо как поле в
  `ProfileStorage` (`activeProfileId`), либо отдельным маленьким
  файлом/DataStore-значением.
- **Готово, когда**: выбор профиля переживает перезапуск процесса.

### [~] D6. Редактирование профиля
- Иконка редактирования открывает диалог с сохранённым YAML.
- Сохранение повторно валидирует YAML и обновляет профиль с прежним ID;
  активный выбор сохраняется, дубликат не создаётся.
- Отмена и ошибка валидации оставляют сохранённый профиль без изменений.
- Реализовано; ожидает ручной проверки. Gradle и тесты не запускались.
- **Готово, когда**: изменение имени/сервера видно в списке и сохраняется
  после перезапуска; активный профиль остаётся выбранным, отмена и битый
  YAML не меняют ранее сохранённые данные.

## Стадия E — Экран Apps

### E1. Список установленных приложений
- Через `PackageManager` (`getInstalledApplications` +
  `ApplicationInfo.loadLabel`/`loadIcon`), с фильтрацией системных приложений
  без launcher intent (иначе список будет огромным и бесполезным).
- `AppsListViewModel : BaseViewModel<...>` — состояние: список
  `{ packageName, label, icon, target: RouteTarget? }`.
- **Готово, когда**: экран показывает реальные установленные приложения с
  иконкой и именем.

### E2. Выбор приложения и режима + сохранение
- Пользователь отмечает нужные приложения и выбирает для каждого «С VPN»
  (`PROXY`) или «Без VPN» (`DIRECT`). Режимы взаимоисключающие.
- Снятие отметки удаляет правило (`target = null`); отсутствие правила
  не равно явному DIRECT, поскольку остаётся проверка доменов.
- **Готово, когда**: выбор и режимы переживают перезапуск; снятие отметки
  удаляет правило, смена режима обновляет его. Активный VPN не перезапускается.

## Стадия F — Экран Domains

### F1. Список + добавление
- Отдельный экран со списком доменов (`DOMAIN-SUFFIX`),
  поле ввода/вставки + кнопка "Добавить",
  примитивная проверка формата (не пустая строка, без пробелов — не более
  того для контура 1).
- Для каждого домена — взаимоисключающий выбор «С VPN» (`PROXY`) /
  «Без VPN» (`DIRECT`), сохранение выбранного режима.
- **Готово, когда**: домен добавляется, режим меняется и оба сохраняются
  после перезапуска; изменения не затрагивают уже работающий VPN.

### F2. Удаление домена
- Свайп/иконка на элементе списка.
- **Готово, когда**: удаление персистится.

## Стадия G — RuntimeConfigBuilder

Профиль уже содержит весь конфиг (`ProxyProfile.raw`, собран в B2) —
`RuntimeConfigBuilder` не строит YAML с нуля, а только дописывает к `raw`
сгенерированный `rules:`. Статического шаблона не нужно (в отличие от
ARCHITECTURE-FULL.md → раздел 9, где `mode`/`dns`/`proxy-groups` были
частью builder'а — здесь их приносит пользователь).

### G1. Генерация `rules:`
- Функция, принимающая `Set<ProxyPackage>` + `Set<ProxyDomain>`, отдающая
  список строк `PROCESS-NAME,<pkg>,<target>`, затем
  `DOMAIN-SUFFIX,<domain>,<target>`, затем `MATCH,DIRECT` — порядок фиксирован
  (см. SPEC → "Runtime и VPN").
- **Готово, когда**: unit-тест на 2 пакета + 2 домена даёт ожидаемый список
  строк с обоими режимами в правильном порядке: все пакеты раньше всех
  доменов независимо от target, `MATCH,DIRECT` последний. Пустой ввод
  даёт только `MATCH,DIRECT`; неотмеченные приложения не создают правил.

### G2. Сборка и запись `config.yaml`
- `RuntimeConfigBuilder.build(profile: ProxyProfile, packages, domains): String` —
  распарсить `profile.raw` обратно в YAML-дерево (тот же механизм, что и в
  B2), добавить в него ключ `rules` со списком из G1, сериализовать в текст.
- Запись результата в `cacheDir/mihomo/config.yaml` при старте (создать
  директорию, если её нет). Использовать снимок профиля и правил на START;
  при редактировании настроек работающий конфиг не перезаписывать.
- **Готово, когда**: после вызова файл существует, валиден как YAML,
  содержит все ключи из `profile.raw` (mode/dns/proxies/proxy-groups/...)
  плюс сгенерированный `rules:`.

## Стадия H — Интеграция Mihomo

Библиотека выбрана — `libmihomo-android` (см. SPEC → "Mihomo — выбранная
библиотека"): `.aar` с готовым Kotlin/JNI фасадом `Clash` вокруг
`libclash.so`. Свой JNI-мост не пишем.

### H1. Подключить `libmihomo-android.aar` в проект
- Скачать актуальный `.aar` с [github.com/oviron/libmihomo-android](https://github.com/oviron/libmihomo-android)
  (Releases), положить в `vpn/libs/libmihomo-android.aar`.
- `vpn/build.gradle.kts`: `implementation(files("libs/libmihomo-android.aar"))`.
- **Готово, когда**: проект собирается с зависимостью, `Clash.load(applicationInfo.nativeLibraryDir)`
  отрабатывает без исключения в тестовой Activity/инструментальном тесте
  (native-библиотека реально грузится на устройстве/эмуляторе нужной ABI).

### H2. Обёртка над `Clash` (`MihomoEngine` или аналог)
- Тонкая Kotlin-обёртка в `vpn` поверх фасада `Clash`: `load()` (один раз,
  см. H1), `setup(configPath: String)` → `Clash.quickSetup(initParams, setupParams)`,
  `start(tunFd: Int, protect: (Int) -> Boolean)` → `Clash.startTUN(tunFd, callback, device, stack, address, dns, mtu)`,
  `stop()` → `Clash.stopTun()`. Без routing-логики внутри — только передача
  параметров в core.
- Сигнатуру `callback`/`device`/`stack`/`address`/`dns`/`mtu` уточнить по
  актуальной версии `.aar` (может отличаться между релизами библиотеки) —
  подогнать под неё сигнатуру `MihomoEngine`, а не наоборот.
- **Готово, когда**: класс компилируется, покрыт хотя бы одним unit-тестом
  на уровне "не падает при некорректных аргументах" (реальный сетевой тест —
  в стадии K).

## Стадия I — VpnService, разрешение, TUN

### I1. `ProxyVpnService` — каркас
- `class ProxyVpnService : VpnService()`, регистрация в
  `app/src/main/AndroidManifest.xml` (`<service>` с
  `android:permission="android.permission.BIND_VPN_SERVICE"`,
  `<intent-filter><action android:name="android.net.VpnService"/></intent-filter>`).
- Foreground notification (канал + `startForeground`) при старте.
- **Готово, когда**: сервис стартует и показывает уведомление (ещё без
  реального VPN/Mihomo).

### I2. Запрос VPN permission
- `VpnService.prepare(context)` перед первым стартом, обработка результата
  через `ActivityResultContracts.StartActivityForResult` в Main-экране
  (стадия J).
- **Готово, когда**: на чистой установке при нажатии START показывается
  системный диалог разрешения VPN; при повторном запуске (permission уже
  выдан) диалог не показывается.

### I3. Создание TUN
- `VpnService.Builder` → `addAddress`/`addRoute`/`addDnsServer`/`setMtu` (с
  разумными дефолтами, см. ARCHITECTURE-FULL.md → раздел 15) →
  `establish()` → `ParcelFileDescriptor`.
- **Готово, когда**: `establish()` возвращает не-null дескриптор при
  выданном permission (проверить логом `fd`).

### I4. Запуск Mihomo с TUN fd + config path
- Внутри `ProxyVpnService`: `MihomoEngine.load()` (H2, если ещё не
  выполнено) → вызвать `RuntimeConfigBuilder` (G2), получить путь к
  `config.yaml` → `MihomoEngine.setup(configPath)` → `MihomoEngine.start(tunFd, ::protect)`,
  где `::protect` — ссылка на `VpnService.protect()` этого сервиса (см.
  стадию K1).
- **Готово, когда**: START поднимает сервис → TUN → Mihomo без креша;
  видно в логах Mihomo, что core стартовал с переданным конфигом.

## Стадия J — Экран Main (START/STOP)

### J1. UI Main
- Активный профиль (имя), количество правил (пакеты+домены), кнопка
  START/STOP, статус (DISCONNECTED/CONNECTING/CONNECTED).
- **Готово, когда**: экран показывает актуальные данные из
  `ProfileStorage`/правил.

### J2. Связать START с VPN permission + запуском сервиса
- Нажатие START → если нет permission (I2), запросить → на согласии
  запустить `ProxyVpnService` (`startForegroundService`).
- **Готово, когда**: полный клик-путь от START до CONNECTED работает на
  реальном устройстве/эмуляторе.

### J3. STOP
- Остановка Mihomo (`MihomoEngine.stop()` → `Clash.stopTun()`) → закрытие
  `ParcelFileDescriptor` → `stopForeground()` → `stopSelf()` → удаление
  `cacheDir/mihomo/config.yaml` (см. SPEC → критерии готовности).
- **Готово, когда**: после STOP не остаётся уведомления, процесс VPN в
  системных настройках Android не значится активным, файл конфига удалён.

### J4. Ошибки запуска и применение изменений
- Ошибка запуска возвращает DISCONNECTED с понятным сообщением на Main;
  очистить созданные ресурсы, уведомление и runtime-конфиг.
- Отказ в VPN permission оставляет DISCONNECTED без запуска сервиса.
- Изменения профиля/правил сохраняются; применять только после ручного
  STOP → START, без автоматического перезапуска.
- **Готово, когда**: после ошибки нет зависшего CONNECTING, можно повторить
  START; изменение настроек не меняет текущее соединение, после STOP → START
  используется новый конфиг.

## Стадия K — `protect()` и сквозная проверка

### K1. `protect()` на сокетах Mihomo
- Убедиться, что исходящие соединения core (к реальному прокси-серверу)
  проходят через `VpnService.protect()` — иначе петля трафика (см.
  ARCHITECTURE-FULL.md → раздел 17). Проброшено через `callback`,
  переданный в `Clash.startTUN` в H2/I4 — на практике это значит, что
  `ProxyVpnService.protect(socket/fd)` должен реально вызываться на каждое
  исходящее соединение core (проверить по документации/исходникам
  `libmihomo-android` под какой именно сигнал core это ожидает).
- **Готово, когда**: с поднятым VPN устройство не теряет интернет целиком
  (нет петли/дедлока), трафик реально идёт через сервер из профиля
  (проверить сменой IP через любой "check my ip" сервис в отмеченном
  приложении).

### K2. Сквозной ручной прогон по критериям готовности
- Пройти по чеклисту из SPEC.md → "Критерии готовности контура 1" целиком,
  на реальном устройстве или эмуляторе с рабочим интернетом.
- **Готово, когда**: все пункты чеклиста подтверждены вручную.

## Стадия L — Полировка (по желанию, не блокирует "готовый контур 1")

- Обработка "профиль удалён, пока был активным" (снять активность/предупредить).
- Обработка "приложение убито системой во время активного VPN" (пересоздание
  уведомления/сервиса при перезапуске — `START_STICKY` или аналог).
