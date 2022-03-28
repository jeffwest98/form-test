package com.clearcover.forms.apis

import com.clearcover.forms.config.TestConfig
import com.clearcover.forms.models.Document
import org.json.JSONArray
import java.io.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

// will not return result until policy has the expected number of document with document name
class PolicyDocumentsApi(val config: TestConfig) {
    /*
        Will not return result until policy has the expected number of document with document name or
        expected number of all documents if documentName is null
     */
    fun getPolicyDocumentAssetList(
        policyId: String,
        authToken: String,
        expectedNumber: Int,
        documentName: String? = null,
        retries: Int? = null
    ): List<Document>{
        var maxTry = retries ?: config.documentWaitRetries;
        val requestUrl = "${config.policiesDomain}/api/v1/policies/$policyId/mailableDocuments"
        var documentList: JSONArray?
        while(true){
            documentList = khttp.get(
                url = requestUrl,
                headers = mapOf(
                    "Authorization" to "Bearer $authToken"
                )
            ).jsonObject.getJSONArray("documents")

            val listSize = getDocumentAssetIdList(documentList, documentName).size
            if(listSize >= expectedNumber || maxTry == 0){
                break
            }

            Thread.sleep(1000)
            maxTry--
        }
        var list = documentList?.let { getDocumentAssetIdList(it) }!!
        return ArrayList(list.sortedBy{ LocalDate.parse(it.createdOn, DateTimeFormatter.ISO_OFFSET_DATE_TIME)})
    }

    // starts at index 1
    fun getDocument(list: List<Document> ,documentName: String, index: Int): Document?{
        var documentList: ArrayList<Document> = ArrayList()
        for(document in list){
            if(document.formName == documentName){
                documentList.add(document)
            }
        }
        if(documentList.size >= index){
            return documentList[index - 1]
        }

        return null
    }

    fun downloadAsset(assetId: String, destFileName: String){
        var inputStream: InputStream? = null;
        var outputStream: OutputStream? = null;
        try{
            var fileResponse = khttp.get(
                url = "${config.formsDomain}/api/v1/assets/$assetId/content", stream = true
            );

            var resultFile = File(destFileName)
            var fileReader = ByteArray(4096)

            inputStream = fileResponse.raw
            outputStream = FileOutputStream(resultFile)

            while(true){
                var read: Int = inputStream.read(fileReader)

                if(read == -1){
                    break
                }
                outputStream.write(fileReader,0,read)
            }
            outputStream.flush()
        }

        catch(ex: IOException){
            throw ex
        }
        finally {
            inputStream?.close()
            outputStream?.close()
        }
    }

    private fun getDocumentAssetIdList(jsonArray:JSONArray ,documentName: String? = null): List<Document>{
        var documentList: ArrayList<Document> = ArrayList()
        for(i in 0 until jsonArray.length()){
            val document = jsonArray.getJSONObject(i)
            if(document["formName"] == documentName || documentName == null)
            {
                documentList.add(
                    Document(
                        document["id"].toString(),
                        document["formName"].toString(),
                        document["createdOn"].toString()
                    )
                )
            }
        }
        return documentList
    }
}