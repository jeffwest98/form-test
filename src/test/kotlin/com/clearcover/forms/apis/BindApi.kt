package com.clearcover.forms.apis

import com.clearcover.forms.config.TestConfig
import com.clearcover.forms.helpers.TestHelpers
import com.clearcover.forms.models.BindCoverage
import com.clearcover.forms.models.Driver
import com.clearcover.forms.models.Vehicle
import com.clearcover.forms.parameters.PlanType
import khttp.responses.Response
import org.json.JSONObject

class BindApi(val config: TestConfig) {
    fun bindPolicy(
        startDate: String,
        endDate: String,
        vehicles: Array<Vehicle>,
        drivers: Array<Driver>,
        ratesResponse: Response,
        paymentPlanResponse: Response,
        planType: PlanType
    ) : Response
    {
        val jsonParam = TestHelpers.readFile("./src/test/kotlin/com/clearcover/forms/templates/bind.json")
        return khttp.post(
            url = "${config.policiesDomain}/api/v1/policies/bind",
            json = makeBindJson(
                jsonParam,
                startDate,
                endDate,
                vehicles,
                drivers,
                ratesResponse,
                paymentPlanResponse,
                planType
            ),
            headers = mapOf(
                "Clearcover-Partner-Authorization-Token" to config.policiesAuthToken
            )
        )
    }

    private fun makeBindJson(
        jsonTemplate: String,
        startDate: String,
        endDate: String,
        vehicles: Array<Vehicle>,
        drivers: Array<Driver>,
        rateResponse: Response,
        paymentPlanResponse: Response,
        planType: PlanType) : JSONObject
    {
        var stringResult = jsonTemplate.replace("{{vehicles}}", makeVehicleBindJson(vehicles))
        stringResult = stringResult.replace("{{drivers}}", makeDriverBindJson(drivers))
        stringResult = stringResult.replace("{{coverages}}", makeCoverageRateBindJson(rateResponse))
        stringResult = stringResult.replace("{{premium}}", rateResponse.jsonObject["totalPremium"].toString())
        stringResult = stringResult.replace("{{paymentPlanDef}}",findPaymentPlanDefinitionFromResponse(paymentPlanResponse,planType).toString())
        stringResult = stringResult.replace("{{paymentPlanId}}", findPaymentPlanDefinitionFromResponse(paymentPlanResponse,planType)?.get("planId").toString())
        stringResult = stringResult.replace("{{discounts}}", rateResponse.jsonObject.getJSONObject("discountSummary").getJSONArray("discounts").toString())
        stringResult = stringResult.replace("{{email}}", drivers[0].email)
        stringResult = stringResult.replace("{{rateId}}", rateResponse.jsonObject["rateId"].toString())
        stringResult = stringResult.replace("{{startDate}}", startDate)
        stringResult = stringResult.replace("{{endDate}}", endDate)
        return JSONObject(stringResult)
    }

    private fun makeVehicleBindJson(vehicles: Array<Vehicle>) : String{
        var vehicleJson = ""
        for(vehicle in vehicles){
            vehicleJson +=
                """
                                {
                                        "identifier": "${vehicle.id}",
                                        "itemType": "vehicle",
                                        "startDate": "{{startDate}}T07:00:00-06:00",
                                        "endorsementTypes": [],
                                        "locations": [
                                            {
                                                "address": {
                                                    "addressLine1": "14579 SE Creekside Dr",
                                                    "city": "Robertsdale",
                                                    "state": "OR",
                                                    "postalCode": "97267",
                                                    "country": "US"
                                                },
                                                "locationType": "garaging"
                                            }
                                        ],
                                        "vehicle": {
                                            "detail": {
                                                "identifier": "${vehicle.id}",
                                                "antiTheft": {
                                                    "alarm": false,
                                                    "activeDisable": false,
                                                    "passiveDisable": false,
                                                    "recovery": false
                                                },
                                                "identification": {
                                                    "vin": "${vehicle.vin}",
                                                    "partialVin": null,
                                                    "year": ${vehicle.year},
                                                    "make": "${vehicle.make}",
                                                    "model": "${vehicle.model}",
                                                    "bodyStyle": "${vehicle.bodyStyle}",
                                                    "vehicleServicesId": "v1_WzIwMTcsIkhvbmRhIiwiRklUIExYIiwiSENIQksgNEQiLCIzSEdHSzVHNVx1MDAyNkgiXQ==",
                                                    "vinFromSvc": null,
                                                    "hasSeenVinMismatchDisclaimer": false
                                                },
                                                "mileage": {
                                                    "annual": ${vehicle.annualMileage}
                                                },
                                                "rideshareUse": ${vehicle.rideShare},
                                                "usage": "${vehicle.vehicleUse}"
                                            },
                                            "ownershipType": "${vehicle.ownership}",
                                            "lienholders": []
                                        },
                                        "insurableItemAmounts": []
                                },
                       """
        }
        return vehicleJson.substringBeforeLast(',').trim()
    }

    private fun makeDriverBindJson(drivers: Array<Driver>) : String {
        var driverJson = ""
        for (driver in drivers) {
            driverJson +=
                """{
                        "identifier": "${driver.id}",
                        "partyType": "driver",
                        "policyPartyRelationship": "${driver.policyRelationship}",
                        "person": {
                            "dateOfBirth": "${driver.dob}",
                            "primaryLanguage": "en-US",
                            "phoneNumbers": [
                                {
                                    "phoneType": "landline",
                                    "phoneFormat": "north_american_numbering_plan",
                                    "number": "1112221234"
                                }
                            ],
                            "addresses": [
                                {
                                    "addressLine1": "17830 Baldwin Farms Pl",
                                    "city": "Robertsdale",
                                    "state": "OR",
                                    "postalCode": "36567",
                                    "country": "US"
                                }
                            ],
                            "email": "${driver.email}",
                            "legalIdentifiers": [
                                {
                                    "identifierType": "License Number",
                                    "identifierValue": "${driver.license}",
                                    "identifierJurisdiction": {
                                        "country": "USA",
                                        "body": "${driver.licenseState}-DMV"
                                    }
                                }
                            ],
                            "firstName": "${driver.firstName}",
                            "middleName": "${driver.middleName}",
                            "lastName": "${driver.lastName}",
                            "gender": "${driver.gender}",
                            "maritalStatus": "${driver.maritalStatus}",
                            "source": "confirmed_adpf"
                        },
                        "startDate": "{{startDate}}T07:00:00-06:00",
                        "driver": {
                            "detail": {
                                "identifier": "${driver.id}",
                                "violations": [],
                                "sr22s": [],
                                "accidents": [],
                                "suspensions": [],
                                "license": {
                                    "status": "active",
                                    "geographicLocation": "US-${driver.licenseState}",
                                    "verified": true,
                                    "source": {
                                        "kind": "mvr",
                                        "reportId": "883d668e-d48c-4031-bec7-93dfcd70ce6f"
                                    },
                                    "number": "${driver.license}"
                                },
                                "financialResponsibility": {
                                    "kind": "noHit",
                                    "source": {
                                        "kind": "ncf",
                                        "reportId": "d516c421-2e50-4537-867f-d45694e3851c"
                                    }
                                },
                                "ageFirstLicensed": 16,
                                "bankruptcies": [],
                                "coverageHistory": {},
                                "creditProfile": {
                                    "date": "2021-02-26",
                                    "source": "ncf_report",
                                    "insuranceScoreNcfStatus": "Credit bureau no-hit"
                                },
                                "drivingCourse": {
                                    "courseTaken": true,
                                    "completionDate": "2017-01-08"
                                },
                                "education": {
                                    "currentLevel": "bachelors_degree"
                                },
                                "homeOwnership": {
                                    "homeOwner": true,
                                    "monthsOwned": 16,
                                    "ownershipType": "own"
                                },
                                "kinship": "${driver.kinship}",
                                "militaryService": {
                                    "serviceMember": true,
                                    "militaryRank": "O-4",
                                    "isDeployed": true
                                },
                                "profession": "Actuary",
                                "specialFilings": [],
                                "priorInsurance": {
                                    "currentlyInsuredMonths": 0,
                                    "isCurrentlyInsured": false,
                                    "reportHit": true,
                                    "source": {
                                        "kind": "currentCarrier",
                                        "reportId": "a85509c0-fa86-4a09-bb5c-a6082e608ebd"
                                    },
                                    "currentCarrierName": "COMPANY99017",
                                    "uninsuredReason": "other",
                                    "daysSinceLastInsured": 31,
                                    "previousBiLevel": "30M/60M"
                                }
                            }
                        }
                    },
                """.trimIndent()
        }
        return driverJson.substringBeforeLast(',')
    }

    private fun makeCoverageRateBindJson(rateResponse: Response) : String{
        var coverageMap = mapOf<String, BindCoverage>(
            "BI" to BindCoverage("BI","bodily_injury", "per_person_split_limit"),
            "PD" to BindCoverage("PD","property_damage", "single_per_incident_limit"),
            "MED" to BindCoverage("MED","medical_payments", "single_per_incident_limit"),
            "UIUM" to BindCoverage("UIUM","uium_bodily_injury", "per_person_split_limit"),
            "COMP" to BindCoverage("COMP","comprehensive", "deductible"),
            "COLL" to BindCoverage("COLL","collision", "deductible"),
            "AT" to BindCoverage("AT","rental", "per_day_split_limit"),
            "RA" to BindCoverage("RA","roadside", "waivable_only"),
            "PIP" to BindCoverage("PIP","personal_injury_protection", "package")
        )

        var ratings = rateResponse.jsonObject.getJSONArray("vehicleRatings")
        var coverageJson = ""
        for(i in 0 until ratings.length()){
            var rating = ratings.getJSONObject(i)
            var vehicleId = rating["id"];
            var coverageRates = rating.getJSONArray("coverageRatings")
            for(i in 0 until coverageRates.length()){
                var coverage = coverageRates.getJSONObject(i)
                var identifier = coverage["identifier"]
                var level = coverage["level"]
                var premium = coverage["premium"]
                var coverageName = coverageMap[identifier]?.coverageName
                var sourceEntityType = coverageMap[identifier]?.sourceEntityType

                coverageJson +=
                    """
                                        {
                                                "inclusion": "coverage_included",
                                                "chosenCoverageLevel": "$level",
                                                "coveragePrices": [
                                                    {
                                                        "amountType": "writtenPremium",
                                                        "amountName": "bodily_injury",
                                                        "amount": {
                                                            "currency": {
                                                                "code": "USD"
                                                            },
                                                            "value": $premium
                                                        }
                                                    },
                                                    {
                                                        "amountType": "fullTermWrittenPremium",
                                                        "amountName": "$coverageName",
                                                        "amount": {
                                                            "currency": {
                                                                "code": "USD"
                                                            },
                                                            "value": $premium
                                                        }
                                                    }
                                                ],
                                                "insurableItemIdentifier": "$vehicleId",
                                                "identifier": "$identifier",
                                                "productCoverageRef": {
                                                    "sourceSystem": "clearcover-acquisition",
                                                    "sourceEntityId": "$identifier",
                                                    "sourceEntityType": "$sourceEntityType"
                                                },
                                                "effectivePeriod": {
                                                    "startAt": "{{startDate}}T07:00:00-06:00",
                                                    "endAt": "{{endDate}}T22:00:00-07:00"
                                                }
                                        },
                                              
                                """.trimIndent()
            }
        }
        return coverageJson.substringBeforeLast(',')
    }

    private fun findPaymentPlanDefinitionFromResponse(paymentPlanResponse: Response, planType: PlanType) : JSONObject?{
        var plans = paymentPlanResponse.jsonObject.getJSONArray("plans")
        for(i in 0 until plans.length()){
            var plan = plans.getJSONObject(i)
            if(plan["planEnum"] == planType.value){
                return plan
            }
        }
        return null
    }
}