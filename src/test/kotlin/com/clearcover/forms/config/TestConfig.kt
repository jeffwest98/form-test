package com.clearcover.forms.config

data class TestConfig(
    val rateDomain: String,
    val paymentsDomain: String,
    val policiesDomain: String,
    val formsDomain: String,
    val portalAuthDomain: String,
    val authGrantType: String,
    val authClientId: String,
    val authAudience: String,
    val authUserName: String,
    val authPassword: String,
    val rateAuthToken: String,
    val policiesAuthToken: String,
    val documentWaitRetries: Int
){

}