package com.clearcover.forms.models

import java.util.*

data class Vehicle(
    val vin: String,
    val make: String,
    val model: String,
    val year: Int,
    val bodyStyle: String,
    val ownership: String,
    val vehicleUse: String,
    val rideShare: Boolean,
    val annualMileage: Int,
    var vehcileCoverage: Array<RatingCoverage>,
    val endorsements: String? = null
)
{
    val id = UUID.randomUUID().toString()
}