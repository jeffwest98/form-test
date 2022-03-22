package com.clearcover.forms.tests
import com.clearcover.forms.config.TestConfig
import com.clearcover.forms.helpers.TestHelpers
import org.json.JSONObject
import org.junit.jupiter.api.BeforeAll

open class FormTests {
    companion object{
        lateinit var config : TestConfig
        @BeforeAll
        @JvmStatic
        internal fun setUp()
        {
            val curDir = System.getProperty("user.dir")
            val configJson = JSONObject(TestHelpers.readFile("$curDir/src/test/kotlin/config.json"))
            config = TestConfig(
                configJson.getString("rateDomain"),
                configJson.getString("paymentsDomain"),
                configJson.getString("policiesDomain"),
                configJson.getString("formsDomain"),
                configJson.getString("portalAuthDomain"),
                configJson.getString("authGrantType"),
                configJson.getString("authClientId"),
                configJson.getString("authAudience"),
                configJson.getString("authUserName"),
                configJson.getString("authPassword"),
                configJson.getString("rateAuthToken"),
                configJson.getString("policiesAuthToken"),
                configJson.getInt("documentWaitRetries")
            )
        }
    }
}