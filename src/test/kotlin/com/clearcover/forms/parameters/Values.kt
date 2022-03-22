package com.clearcover.forms.parameters

// used for rates
enum class RelationshipToApplicant(val value: String) {
    APPLICANT("applicant"),
    SPOUSE("spouse"),
    OTHER_RELATIVE("otherRelative")
}

enum class PolicyRelationship(val value: String){
    NAMED_INSURED("named_insured"),
    COVERED_PERSON("covered_person"),
    EXCLUDED_PERSON("excluded_person")
}

enum class MaritalStatus(val value: String){
    SINGLE("single"),
    MARRIED("married"),
    WIDOWED("widowed"),
    DIVORCED("divorced"),
    SEPARATED("separated"),
    CIVIL_UNION("civilUnion"),
    DOMESTIC_PARTNERSHIP("domesticPartnership")
}

// used for bind
enum class Kinship(val value: String){
    APPLICANT("applicant"),
    SPOUSE("spouse"),
    OTHER_RELATIVE("other_relative"),
    NON_RELATIVE("non_relative")
}

enum class PlanType(val value: String) {
    CC_FULL("full_pay_cc"),
    CC_MONTHLY("six_monthly_equal_payment_cc")
}

enum class VehicleOwnership(val value: String){
    OWNED("owned"),
    FINANCED("financed"),
    LEASED("leased")
}

enum class VehicleUse(val value: String){
    BUSINESS("business"),
    COMMUTING("commuting"),
    PLEASURE("pleasure"),
    ARTISAN("artisan"),
    FARM("farm")
}

enum class DocumentNames(val value: String){
    AUTO_APPLICATION("AUTO_APPLICATION")
}

enum class OregonBICoverageLevel(val value: String){
    FIFTY_ONE_HUNDRED("50M/100M")
}

enum class OregonPDCoverageLevel(val value: String){
    TWENTY("20M")
}

enum class OregonMEDCoverageLevel(val value: String){
    TWO_THOUSAND("2000")
}

enum class OregonPIPCoverageLevel(val value: String){
    FIFTEEN_THOUSAND("OR15M")
}

enum class OregonUIUMCoverageLevel(val value: String){
    TWENTY_FIVE_FIFTY("25M/50M")
}
enum class OregonUMPDCoverageLevel(val value: String){
    TWENTY("20M")
}

enum class OregonATCoverageLevel(val value: String){
    THIRTY_NINE_HUNDRED("30/900")
}

enum class OregonCollCoverageLevel(val value: String){
    TWO_HUNDRED("200"),
    FIVE_HUNDRED("500")
}

enum class OregonCompCoverageLevel(val value: String){
    TWO_HUNDRED("200"),
    FIVE_HUNDRED("500")
}

enum class OregonRACoverageLevel(val value: String){
    ONE_HUNDRED("100")
}