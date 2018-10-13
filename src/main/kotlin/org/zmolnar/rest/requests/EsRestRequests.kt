package org.zmolnar.rest.requests

import org.apache.http.HttpHost
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.zmolnar.rest.common.DESCRIPTION_INDEX_NAME
import org.zmolnar.rest.common.FULLY_SPECIFIED_NAME
import org.zmolnar.rest.common.STORE_NAME

class EsRestRequests {

    private fun indexDocument() {
        val client = RestHighLevelClient(RestClient.builder(
                HttpHost("localhost", 9200, "http"),
                HttpHost("localhost", 9201, "http")
        ))

        val descriptionDocumentJson = hashMapOf(
            "id" to 6,
            "effectivetime" to 20120830,
            "active" to true,
            "moduleId" to "moduleId",
            "languageCode" to "languageCode",
            "typeId" to FULLY_SPECIFIED_NAME,
            "term" to "Another fully specified name",
            "caseSignificanceId" to "case significance"
        )
        val request = IndexRequest(STORE_NAME, DESCRIPTION_INDEX_NAME, "6")
        request.source(descriptionDocumentJson)
        client.index(request, RequestOptions.DEFAULT)
        client.close()
    }


}