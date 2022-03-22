package com.clearcover.forms.apis

import com.clearcover.forms.config.TestConfig
import com.clearcover.forms.helpers.TestHelpers
import khttp.responses.Response
import org.json.JSONObject

class PaymentPlanApi(val config: TestConfig) {
    fun getPaymentPlan(startDate: String, ratesResponse: Response): Response{
        val jsonParam = TestHelpers.readFile("./src/test/kotlin/com/clearcover/forms/templates/payment_plan.json")
        return khttp.post(
            url = "${config.paymentsDomain}/api/v1/insurance/plans/calculate",
            json = makePaymentPlanJson(jsonParam, startDate, ratesResponse)
        );
    }

    private fun makePaymentPlanJson(jsonTemplate: String, startDate: String, rateResponse: Response) : JSONObject {
        var stringResult = jsonTemplate.replace("{{premium}}", rateResponse.jsonObject["totalPremium"].toString())
        stringResult = stringResult.replace("{{startDate}}", startDate)
        return JSONObject(stringResult)
    }
}