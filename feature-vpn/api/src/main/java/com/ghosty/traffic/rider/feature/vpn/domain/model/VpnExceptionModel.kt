package com.ghosty.traffic.rider.feature.vpn.domain.model

sealed class VpnExceptionModel(cause: Throwable? = null) : IllegalStateException(cause)

class VpnActiveProfileMissingExceptionModel : VpnExceptionModel()

class VpnInvalidConfigExceptionModel : VpnExceptionModel()

class VpnRuntimeDirectoryExceptionModel : VpnExceptionModel()

class VpnEngineSetupTimeoutExceptionModel : VpnExceptionModel()

class VpnEngineConfigRejectedExceptionModel : VpnExceptionModel()

class VpnEngineUnavailableExceptionModel : VpnExceptionModel()

class VpnTunStartExceptionModel : VpnExceptionModel()

class VpnUnknownExceptionModel(cause: Throwable) : VpnExceptionModel(cause)
