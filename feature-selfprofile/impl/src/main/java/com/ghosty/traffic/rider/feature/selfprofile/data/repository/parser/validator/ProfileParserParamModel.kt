package com.ghosty.traffic.rider.feature.selfprofile.data.repository.parser.validator

internal enum class ProfileParserParamModel(val key: String) {
    // Режим маршрутизации трафика clash/mihomo: направлять запросы по правилам (proxy-groups + rules), а не глобально через один прокси
    MODE("mode"),

    // Именованные группы прокси, на которые ссылаются правила маршрутизации (rules) в конфиге
    PROXY_GROUPS("proxy-groups"),

    // Отображаемое имя прокси-сервера
    NAME("name"),

    // Протокол прокси-сервера (vmess, trojan, ss и т.п.)
    TYPE("type"),

    // Адрес (хост) прокси-сервера
    SERVER("server"),

    // Порт прокси-сервера
    PORT("port"),
}