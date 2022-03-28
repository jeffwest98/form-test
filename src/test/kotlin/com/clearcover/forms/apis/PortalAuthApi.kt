package com.clearcover.forms.apis

import com.clearcover.forms.config.TestConfig

class PortalAuthApi (val config: TestConfig){
    fun login(): String?{
        return khttp.post(
            url = "${config.portalAuthDomain}/oauth/token",
            data = mapOf(
                "grant_type" to config.authGrantType,
                "client_id" to config.authClientId,
                "audience" to config.authAudience,
                "username" to config.authUserName,
                "password" to config.authPassword
            )
        ).jsonObject["access_token"]?.toString()
    }
}