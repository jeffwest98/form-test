package com.clearcover.forms.tests

import com.clearcover.forms.apis.*
import com.clearcover.forms.helpers.PdfHelper
import com.clearcover.forms.helpers.TestHelpers
import com.clearcover.forms.models.Driver
import com.clearcover.forms.models.RatingCoverage
import com.clearcover.forms.models.Vehicle
import com.clearcover.forms.parameters.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInfo
import kotlin.test.assertEquals


class OregonTests : FormTests() {
    @Test
    fun testOne(testInfo: TestInfo){
        var vehicleArray = arrayOf(
            Vehicle(
                "1G8ZK8278WZ117818",
                "Saturn",
                "SW2",
                1998,
                "WAGON 4D",
                VehicleOwnership.OWNED.value,
                VehicleUse.PLEASURE.value,
                false,
                10000,
                arrayOf(
                    RatingCoverage(
                        "AT",
                        false,
                        OregonATCoverageLevel.THIRTY_NINE_HUNDRED.value
                    ),
                    RatingCoverage(
                        "COLL",
                        false,
                        OregonCollCoverageLevel.TWO_HUNDRED.value
                    ),
                    RatingCoverage(
                        "COMP",
                        false,
                        OregonCompCoverageLevel.TWO_HUNDRED.value
                    ),
                    RatingCoverage(
                        "RA",
                        false,
                        OregonRACoverageLevel.ONE_HUNDRED.value
                    )
                )
            ),
            Vehicle(
                "2MELM75W6NX756950",
                "Mercury",
                "GRAND MARQUIS LS",
                1992,
                "SEDAN 4D",
                VehicleOwnership.OWNED.value,
                VehicleUse.PLEASURE.value,
                false,
                10000,
                arrayOf(
                    RatingCoverage(
                        "AT",
                        false,
                        OregonATCoverageLevel.THIRTY_NINE_HUNDRED.value
                    ),
                    RatingCoverage(
                        "COLL",
                        false,
                        OregonCollCoverageLevel.TWO_HUNDRED.value
                    ),
                    RatingCoverage(
                        "COMP",
                        false,
                        OregonCompCoverageLevel.TWO_HUNDRED.value
                    ),
                    RatingCoverage(
                        "RA",
                        false,
                        OregonRACoverageLevel.ONE_HUNDRED.value
                    )
                )
            ),
            Vehicle(
                "5FNRL6H9XJB078969",
                "Honda",
                "ODYSSEY ELITE",
                2018,
                "WAG4X24D",
                VehicleOwnership.OWNED.value,
                VehicleUse.PLEASURE.value,
                false,
                10000,
                arrayOf(
                    RatingCoverage(
                        "AT",
                        false,
                        OregonATCoverageLevel.THIRTY_NINE_HUNDRED.value
                    ),
                    RatingCoverage(
                        "COLL",
                        false,
                        OregonCollCoverageLevel.TWO_HUNDRED.value
                    ),
                    RatingCoverage(
                        "COMP",
                        false,
                        OregonCompCoverageLevel.TWO_HUNDRED.value
                    ),
                    RatingCoverage(
                        "RA",
                        false,
                        OregonRACoverageLevel.ONE_HUNDRED.value
                    )
                )
            )
        )

        var driverArray = arrayOf(
            Driver(
                "1971-04-13", "male", MaritalStatus.MARRIED.value, RelationshipToApplicant.APPLICANT.value, true,
                PolicyRelationship.NAMED_INSURED.value, Kinship.APPLICANT.value, "Computer", "A", "Oregon",
                "D2224217041801", "WI"
            ),
            Driver(
                "1972-04-13", "male", MaritalStatus.MARRIED.value, RelationshipToApplicant.SPOUSE.value, false,
                PolicyRelationship.COVERED_PERSON.value, Kinship.SPOUSE.value, "Computer2", "A", "Oregon",
                "D2224217041802", "WI"
            )
        )

        var policyCoveragesArray = arrayOf(
            RatingCoverage(
                "BI",
                false,
                OregonBICoverageLevel.FIFTY_ONE_HUNDRED.value
            ),
            RatingCoverage(
                "PD",
                false,
                OregonPDCoverageLevel.TWENTY.value
            ),
            RatingCoverage(
                "MED",
                false,
                OregonMEDCoverageLevel.TWO_THOUSAND.value
            ),
            RatingCoverage(
                "PIP",
                false,
                OregonPIPCoverageLevel.FIFTEEN_THOUSAND.value
            ),
            RatingCoverage(
                "UIUM",
                false,
                OregonUIUMCoverageLevel.TWENTY_FIVE_FIFTY.value
            ),
            RatingCoverage(
                "UMPD",
                false,
                OregonUMPDCoverageLevel.TWENTY.value
            )
        )

        val policyStartDate = "2022-01-13"
        val policyEndDate = "2022-07-13"

        var rateResponse = RateApi(config).getRate(
            policyStartDate,
            vehicleArray,
            driverArray,
            policyCoveragesArray
        )
        var paymentPlanResponse = PaymentPlanApi(config).getPaymentPlan(
            policyStartDate,
            rateResponse
        )
        var bindResponse = BindApi(config).bindPolicy(
            policyStartDate,
            policyEndDate,
            vehicleArray,
            driverArray,
            rateResponse,
            paymentPlanResponse,
            PlanType.CC_MONTHLY
        )

        println(bindResponse.jsonObject.getJSONObject("policy")["number"].toString())

        val policyDocumentApi = PolicyDocumentsApi(config)
        var documentList = policyDocumentApi.getPolicyDocumentAssetList(
            bindResponse.jsonObject.getJSONObject("policy")["id"].toString(),
            PortalAuthApi(config).login().toString(),
            1,
            DocumentNames.AUTO_APPLICATION.value
        )

        var document = policyDocumentApi.getDocument(
            documentList,
            DocumentNames.AUTO_APPLICATION.value,
            1
        )

        val curDir = System.getProperty("user.dir")
        val baselinePath = "$curDir/src/test/kotlin/com/clearcover/forms/baseline/oregon_app.pdf"
        val saveDest = "$curDir/src/test/kotlin/com/clearcover/forms/testcase/documents/${TestHelpers.extractTestName(testInfo.displayName)}" +
                       "_${TestHelpers.getCurrentTimestamp()}.pdf"

        policyDocumentApi.downloadAsset(
            document!!.id,
            saveDest
        )

        val baselineString = PdfHelper.readFile(baselinePath)
        var newPolicyString = PdfHelper.readFile(saveDest)
        newPolicyString = newPolicyString.replace(
            bindResponse.jsonObject.getJSONObject("policy")["number"].toString(),
            "OR164577"
            )
        newPolicyString = newPolicyString.replace(
            driverArray[0].email,
            "test+b976af14-a88f-4471-a0e7-d1b8762de4c5@clearcover.com"
        )
        assertEquals(baselineString, newPolicyString)
    }
}