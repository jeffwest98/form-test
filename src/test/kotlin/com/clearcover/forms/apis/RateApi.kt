package com.clearcover.forms.apis

import com.clearcover.forms.config.TestConfig
import com.clearcover.forms.helpers.TestHelpers
import com.clearcover.forms.models.Driver
import com.clearcover.forms.models.RatingCoverage
import com.clearcover.forms.models.Vehicle
import khttp.responses.Response
import org.json.JSONObject
import java.util.*

class RateApi(val config: TestConfig) {
    fun getRate(startDate: String, vehicles: Array<Vehicle>, drivers: Array<Driver>, policyCoverages: Array<RatingCoverage>): Response{
        var jsonParam = TestHelpers.readFile("./src/test/kotlin/com/clearcover/forms/templates/or_rate.json")
        return khttp.post(
            url = "${config.rateDomain}/v2/rate?mocked=false",
            json = makeRateJson(jsonParam, startDate, vehicles, drivers, policyCoverages),
            headers = mapOf("Authorization" to "${config.rateAuthToken}")
        );
    }

    private fun makeRateJson(jsonTemplate: String, startDate: String, vehicles: Array<Vehicle>, drivers: Array<Driver>, policyCoverages: Array<RatingCoverage>) : JSONObject {
        var stringResult = jsonTemplate.replace("{{vehicles}}", makeVehicleRateJson(vehicles))
        stringResult = stringResult.replace("{{drivers}}", makeDriverRateJson(drivers))
        stringResult = stringResult.replace("{{policyCoverages}}", makeCoverageRateJson(policyCoverages))
        stringResult = stringResult.replace("{{application_id}}", UUID.randomUUID().toString())
        stringResult = stringResult.replace("{{startDate}}", startDate)
        return JSONObject(stringResult)
    }

    private fun makeVehicleRateJson(vehicles: Array<Vehicle>) : String{
        var vehicleJson = ""
        for(vehicle in vehicles){
            vehicleJson +=
                """{
                      "id": "${vehicle.id}",
                      "kind": "vehicle",
                      "vin": "${vehicle.vin}",
                      "make": "${vehicle.make}",
                      "model": "${vehicle.model}",
                      "year": ${vehicle.year},
                      "bodyStyle": "${vehicle.bodyStyle}",
                      "driverReportedCharacteristics": {
                        "annualMileage": ${vehicle.annualMileage},
                        "hasActiveDisablingDevice": false,
                        "hasPassiveDisablingDevice": false,
                        "hasAlarm": false,
                        "hasVehicleRecovery": false,
                        "rideshareUse": ${vehicle.rideShare},
                        "vehicleUse": "${vehicle.vehicleUse}"
                      },
                      "endorsementDate": null,
                      "endorsements": [
                        ${vehicle.endorsements ?: ""}
                      ],
                      "coverages": [
                        ${makeCoverageRateJson(vehicle.vehcileCoverage)}
                      ],
                      "ownership": "${vehicle.ownership}"
                },"""
        }
        return vehicleJson.substringBeforeLast(',').trim()
    }

    private fun makeDriverRateJson(drivers: Array<Driver>) : String{
        var driverJson = ""
        for(driver in drivers){
            driverJson +=
                """{
                      "id": "${driver.id}",
                      "endorsementDate": null,
                      "dateOfBirth": "${driver.dob}",
                      "defensiveDrivingCompletedDate": null,
                      "ageFirstLicensed": 16,
                      "educationLevel": "bachelorsDegree",
                      "gender": "${driver.gender}",
                      "maritalStatus": "${driver.maritalStatus}",
                      "relationshipToApplicant": "${driver.relationShipToApplicant}",
                      "accidents": [],
                      "profession": "Nursing Instructors and Teachers, Postsecondary",
                      "kind": "driver",
                      "violations": [
                        ${driver.violationString}
                      ],
                      "sr22s": [],
                      "suspensions": [],
                      "bankruptcies": [],
                      "financialResponsibilityScore": {
                        "kind": "noHit",
                        "source": {
                          "kind": "userReported"
                        }
                      },
                      "license": {
                        "status": "active",
                        "geographicLocation": "US-CO",
                        "verified": false,
                        "source": {
                          "kind": "userReported"
                        }
                      }
                },""".trimIndent()
        }
        return driverJson.substringBeforeLast(',')
    }

    private fun makeCoverageRateJson(coverages: Array<RatingCoverage>): String{
        var coverageJson = ""
        for(coverage in coverages){
            if(!coverage.waived) {
                coverageJson +=
                    """{
                        "identifier": "${coverage.identifier}",
                        "waived": false,
                        "coverageLevel": {
                          "identifier": "${coverage.coverageLevelIdentifier}"
                        }
                    },""".trimIndent()
            }
        }
        return coverageJson.substringBeforeLast(',')
    }
}