package org.zmolnar.rest.requests

import com.google.gson.Gson
import org.apache.http.HttpHost
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.unit.Fuzziness
import org.elasticsearch.common.unit.TimeValue
import org.elasticsearch.index.query.MatchQueryBuilder
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.sort.ScoreSortBuilder
import org.elasticsearch.search.sort.SortOrder
import org.zmolnar.rest.common.STORE_NAME
import org.zmolnar.rest.model.DescriptionIndexDocument
import org.zmolnar.rest.model.TERM
import java.util.concurrent.TimeUnit

class DescriptionSearchRequest constructor(_typeId: String, _term: String){

    val typeId = _typeId
    val term = _term

    fun execute(duration: Long, timeUnit: TimeUnit): List<DescriptionIndexDocument> {
        val client = RestHighLevelClient(RestClient.builder(
                HttpHost("localhost", 9200, "http"),
                HttpHost("localhost", 9201, "http")
        ))

        val searchRequest = SearchRequest(STORE_NAME)
        searchRequest.types("description")

        val sourceBuilder = SearchSourceBuilder()
        sourceBuilder.timeout(TimeValue(duration, timeUnit))
        if (!term.isEmpty()) {
            val queryBuilder = MatchQueryBuilder(TERM, term)
            // TODO: Use boolQueryBuilder
            queryBuilder.fuzziness(Fuzziness.AUTO)
            queryBuilder.prefixLength(5)
            queryBuilder.maxExpansions(10)
            sourceBuilder.query(queryBuilder)
        }

        sourceBuilder.sort(ScoreSortBuilder().order(SortOrder.DESC))
        searchRequest.source(sourceBuilder)

        if (!typeId.isEmpty()) {

        }

        val response = client.search(searchRequest, RequestOptions.DEFAULT)
        val gson = Gson()
        val documents = response.hits.hits.asIterable().map {
            val jsonResponse = it.sourceAsString
            val document = gson.fromJson(jsonResponse, DescriptionIndexDocument::class.java)
            document.score = it.score
            document
        }.toCollection(ArrayList())
        client.close()
        return documents
    }

}
