# Целевая архитектура (полная, не текущий скоуп)

> Это справочный документ. Он описывает конечное видение продукта — VPN-клиент
> поверх Mihomo/Clash с загрузкой профилей серверов и пользовательской
> маршрутизацией. **Сейчас в разработке только MVP** — см. [docs/mvp/SPEC.md](mvp/SPEC.md)
> и [docs/mvp/TASKS.md](mvp/TASKS.md). Разделы ниже реализуются позже, по мере
> готовности контуров, и могут корректироваться.

## Общая схема

```text
                   ┌──────────────────────┐
                   │      ТВОЙ API        │
                   │  отдаёт proxy config │
                   └──────────┬───────────┘
                              │
                              ▼
                   ┌──────────────────────┐
                   │ Config Downloader    │
                   └──────────┬───────────┘
                              │
                              ▼
                   ┌──────────────────────┐
                   │ Encrypted Storage    │
                   │ ProxyProfile objects │
                   └──────────┬───────────┘
                              │
                ┌─────────────┴─────────────┐
                │                           │
                ▼                           ▼
      API-профили серверов         Пользовательские YAML
                                           │
                                           ▼
                                      YAML parser
                                           │
                                           ▼
                                      ProxyProfile
                └─────────────┬─────────────┘
                              │
                              ▼
                   Выбранный ProxyProfile
                              +
                    пользовательские rules
                              │
                              ▼
                   RuntimeConfigBuilder
                              │
                              ▼
               cacheDir/mihomo/config.yaml
                              │
                              ▼
                       Android VpnService
                              │
                         TUN descriptor
                              │
                              ▼
                         Mihomo Core
                              │
                         rule matching
                    ┌─────────┴─────────┐
                    ▼                   ▼
                 DIRECT               PROXY
                                         │
                                         ▼
                                 выбранный сервер
```

## 1. Android-проект

```text
Kotlin
Jetpack Compose
minSdk — по требованиям
```

Условное разделение:

```text
app/
├── ui/
├── profile/
├── routing/
├── config/
├── storage/
├── vpn/
└── mihomo/
```

> Текущий проект использует другую структуру (build-logic + `:framework` +
> feature-модули), см. корневой `settings.gradle.kts`. Разделы выше — концептуальные,
> не буквальный список модулей.

## 2. Внутренняя модель профиля

YAML не должен быть основной моделью приложения.

```kotlin
data class ProxyProfile(
    val id: String,
    val name: String,
    val type: String,
    val server: String,
    val port: Int,
    val cipher: String?,
    val password: String?,
    val plugin: String?,
    val wsHost: String?,
    val wsPath: String?,
    val tls: Boolean
)
```

## 3. Загрузка профилей с API

```text
API → скачать YAML → валидировать → распарсить → ProxyProfile → зашифрованное хранилище
```

Исходный YAML после успешного импорта можно не хранить.

## 4. Пользовательские профили

```text
Создать профиль / Импортировать YAML → парсер → валидация → ProxyProfile → Encrypted Storage
```

Для VPN-системы нет разницы, пришёл профиль с API или создан пользователем.

## 5. Защищённое хранилище

```text
ProxyProfile → serialize → plaintext bytes → AES-GCM → encrypted bytes → internal app storage
```

Ключ AES — через Android Keystore.

## 6. Модель правил

```kotlin
data class RoutingSettings(
    val proxyPackages: Set<String>,
    val directPackages: Set<String>,
    val proxyDomains: Set<String>,
    val directDomains: Set<String>,
    val fallback: RouteTarget
)

enum class RouteTarget { DIRECT, PROXY }
```

## 7. Выбор приложений

Через `PackageManager` — список установленных приложений (иконка + имя), пользователь
выбирает, приложение сохраняет package name → target (`PROXY`/`DIRECT`).

## 8. Domain rules

Минимально: `DOMAIN-SUFFIX`. Позже: `DOMAIN`, `DOMAIN-KEYWORD`, `IP-CIDR`, `GEOIP`, `GEOSITE`.

## 9. RuntimeConfigBuilder

Вход: выбранный `ProxyProfile` + `RoutingSettings` + статический YAML template.
Выход: готовый Clash/Mihomo YAML.

```yaml
mode: rule
log-level: info
find-process-mode: always

dns:
  enable: true
  enhanced-mode: redir-host
  nameserver:
    - 1.1.1.1
    - 8.8.8.8

proxies:
  - name: "ACTIVE"
    type: ss
    server: 1.2.3.4
    port: 443
    cipher: chacha20-ietf-poly1305
    password: "..."

proxy-groups:
  - name: PROXY
    type: select
    proxies:
      - ACTIVE
      - DIRECT

rules:
  - PROCESS-NAME,com.google.android.youtube,PROXY
  - PROCESS-NAME,org.telegram.messenger,PROXY
  - DOMAIN-SUFFIX,youtube.com,PROXY
  - DOMAIN-SUFFIX,googlevideo.com,PROXY
  - MATCH,DIRECT
```

## 10. Порядок rules

```text
1. BLOCK
2. explicit DIRECT app rules
3. explicit PROXY app rules
4. explicit DIRECT domains
5. explicit PROXY domains
6. IP rules
7. MATCH
```

`MATCH` всегда последний.

## 11. YAML создаётся только при запуске

```text
Encrypted Profile → decrypt + RoutingSettings → RuntimeConfigBuilder → cacheDir/mihomo/config.yaml
```

Постоянных YAML-файлов с credentials нет.

## 12. Mihomo

```text
Kotlin → JNI/binding → Mihomo native core
```

Core получает: config path, TUN fd, runtime directories, callback/protect mechanism.

## 13. VpnService

```text
ProxyVpnService:
  foreground notification
  VPN lifecycle
  создание TUN
  запуск Mihomo
  остановка Mihomo
```

Routing logic туда не переносится.

## 14. VPN permission

```kotlin
VpnService.prepare(context)
```

Если требуется — системный VPN dialog → пользователь разрешает → `startForegroundService(...)`.

## 15. TUN

```text
VpnService.Builder → address/routes/dns/mtu → establish() → ParcelFileDescriptor → tunFd
```

## 16. TUN descriptor → Mihomo

```text
Android apps → Android routing → TUN → fd → Mihomo
```

## 17. `protect()`

Исходящие соединения самого Mihomo не должны снова попадать в TUN — иначе петля.
Обходятся через `VpnService.protect()`.

## 18. Реальный routing

```text
packet → destination → package/process → domain → rules
```

YouTube → PROCESS-NAME → PROXY → Shadowsocks → сервер.
Chrome → нет матча → `MATCH,DIRECT` → обычное соединение.

## 19. STOP

```text
STOP → Mihomo stop TUN → остановить core → закрыть ParcelFileDescriptor →
stopForeground() → stopSelf() → удалить cacheDir/mihomo/config.yaml
```

## 20. UI (полный продукт)

Экраны: Profiles, Apps, Domains, Main (Server/Rules/Default route/START-STOP).

## Итоговый жизненный цикл

```text
1. Выбор профиля (с API или пользовательский) → parse → encrypt → сохранить
2. Выбор приложений
3. Добавление domain rules
4. START
5. Расшифровать выбранный ProxyProfile
6. Собрать rules
7. Сгенерировать runtime config.yaml
8. Запустить foreground VpnService
9. Создать TUN, получить fd
10. Запустить Mihomo (config.yaml + TUN fd)
11. Mihomo маршрутизирует DIRECT/PROXY
12. STOP → остановить Mihomo → закрыть TUN → удалить config.yaml
```

Разделение ответственности:

```text
UI                 → что хочет пользователь
Encrypted Storage   → какие серверы доступны
RuntimeConfigBuilder → перевод настроек в Mihomo YAML
VpnService          → доступ к Android network stack
Mihomo              → DNS/proxy/routing engine
```
