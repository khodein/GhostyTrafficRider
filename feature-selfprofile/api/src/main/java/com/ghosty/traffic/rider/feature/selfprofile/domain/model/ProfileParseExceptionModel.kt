package com.ghosty.traffic.rider.feature.selfprofile.domain.model

sealed class ProfileParseExceptionModel : IllegalArgumentException()

// Пустой текст вставлен в поле ввода конфигурации
class ProfileParseBlankInputExceptionModel : ProfileParseExceptionModel()

// Текст не распарсился как YAML-объект (невалидный синтаксис или корень не map)
class ProfileParseInvalidYamlExceptionModel : ProfileParseExceptionModel()

// В конфиге отсутствует или не равно "rule" поле mode
class ProfileParseInvalidModeExceptionModel : ProfileParseExceptionModel()

// В конфиге нет ни одного прокси-сервера в proxies
class ProfileParseMissingProxyExceptionModel : ProfileParseExceptionModel()

// У первого прокси в proxies отсутствует или пустое поле name
class ProfileParseMissingNameExceptionModel : ProfileParseExceptionModel()

// У первого прокси в proxies отсутствует или пустое поле type
class ProfileParseMissingTypeExceptionModel : ProfileParseExceptionModel()

// У первого прокси в proxies отсутствует или пустое поле server
class ProfileParseMissingServerExceptionModel : ProfileParseExceptionModel()

// У первого прокси в proxies отсутствует поле port или оно состоит не только из цифр
class ProfileParseInvalidPortFormatExceptionModel : ProfileParseExceptionModel()

// У первого прокси в proxies поле port вне диапазона 1..65535
class ProfileParseInvalidPortRangeExceptionModel : ProfileParseExceptionModel()

// В proxy-groups нет группы с именем PROXY, на которую опираются правила маршрутизации
class ProfileParseMissingProxyGroupExceptionModel : ProfileParseExceptionModel()