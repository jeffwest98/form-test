package com.clearcover.forms.helpers

import java.io.File

class TestHelpers {
    companion object{
        fun readFile(fileName: String) : String = File(fileName).readText()
        fun getCurrentTimestamp(): String{
            val tsLong = System.currentTimeMillis() / 1000
            return tsLong.toString()
        }
        fun extractTestName(testName: String): String{
            return testName.replace("(TestInfo)", "")
        }
    }
}