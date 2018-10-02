package org.zmolnar.rest.requests

import com.google.gson.Gson
import org.apache.http.HttpHost
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.unit.Fuzziness
import org.elasticsearch.common.unit.TimeValue
import org.elasticsearch.index.query.BoolQueryBuilder
import org.elasticsearch.index.query.MatchQueryBuilder
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.sort.ScoreSortBuilder
import org.elasticsearch.search.sort.SortOrder
import org.zmolnar.rest.common.DESCRIPTION_INDEX_NAME
import org.zmolnar.rest.common.STORE_NAME
import org.zmolnar.rest.model.DescriptionIndexDocument
import org.zmolnar.rest.model.TERM
import org.zmolnar.rest.model.TYPE_ID
import java.util.concurrent.TimeUnit

class DescriptionSearchRequest constructor(_typeId: String, _term: String){

    private val typeId = _typeId
    private val term = _term

    fun execute(duration: Long, timeUnit: TimeUnit): List<DescriptionIndexDocument> {
        val client = RestHighLevelClient(RestClient.builder(
                HttpHost("localhost", 9200, "http"),
                HttpHost("localhost", 9201, "http")
        ))

        val boolQueryBuilder = BoolQueryBuilder()
        if (!term.isEmpty()) {
            addTermFilterQuery(boolQueryBuilder)
        }

        if (!typeId.isEmpty()) {
            boolQueryBuilder.must(QueryBuilders.matchQuery(TYPE_ID, typeId))
        }

        val sourceBuilder = SearchSourceBuilder()
        sourceBuilder.sort(ScoreSortBuilder().order(SortOrder.DESC))
        sourceBuilder.timeout(TimeValue(duration, timeUnit))
        sourceBuilder.query(boolQueryBuilder)

        val searchRequest = SearchRequest(STORE_NAME)
        searchRequest.types(DESCRIPTION_INDEX_NAME)
        searchRequest.source(sourceBuilder)

        return executeSearch(searchRequest, client)
    }

    private fun addTermFilterQuery(boolQueryBuilder: BoolQueryBuilder) {
        boolQueryBuilder.should(addFuzzyPrefixQuery())
        boolQueryBuilder.should(QueryBuilders.termQuery(TERM, term))
    }

    private fun addFuzzyPrefixQuery() : MatchQueryBuilder {
        val matchQuery = QueryBuilders.matchQuery(TERM, term)
        matchQuery.boost(0.1F)
        matchQuery.fuzziness(Fuzziness.AUTO)
        matchQuery.prefixLength(5)
        matchQuery.maxExpansions(10)

        return matchQuery
    }

    private fun executeSearch(searchRequest: SearchRequest, client: RestHighLevelClient) : List<DescriptionIndexDocument> {
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
