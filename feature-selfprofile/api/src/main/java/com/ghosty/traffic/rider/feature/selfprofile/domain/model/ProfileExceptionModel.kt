package com.ghosty.traffic.rider.feature.selfprofile.domain.model

sealed class ProfileExceptionModel : IllegalArgumentException()

// В хранилище на диске оказались профили с повторяющимся id
class ProfileDuplicateIdExceptionModel : ProfileExceptionModel()

// activeProfileId в хранилище ссылается на несуществующий профиль
class ProfileMissingActiveExceptionModel : ProfileExceptionModel()

// Операция вызвана с id профиля, которого нет в хранилище
class ProfileNotFoundExceptionModel : ProfileExceptionModel()

// Не удалось создать директорию для файла хранилища перед записью
class ProfileStorageDirectoryExceptionModel : ProfileExceptionModel()