package com.clearcover.forms.models

import java.util.*

class Driver (val dob: String,
              val gender: String,
              val maritalStatus: String,
              val relationShipToApplicant: String,
              val violation: Boolean,
              val policyRelationship: String,
              val kinship: String,
              val firstName: String,
              val middleName: String,
              val lastName: String,
              val license: String,
              val licenseState: String
)
{
    var violationString = ""
    var email = "test+${UUID.randomUUID().toString()}@clearcover.com"
    val id = UUID.randomUUID().toString()

    init{
        if(this.violation) {
            violationString =
                """
                   {
                      "id": "ab327abe-beea-4c45-b5be-c6eca27d7af4",
                      "violationDate": "2021-11-04",
                      "convictionDate": null,
                      "kind": "speedingOther",
                      "source": {
                        "kind": "userReported"
                      }
                   } 
                """.trimIndent()
        }
    }
}