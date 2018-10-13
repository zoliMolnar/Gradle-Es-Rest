package org.zmolnar.rest.requests

import com.google.gson.Gson
import org.apache.http.HttpHost
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.unit.Fuzziness
import org.elasticsearch.common.unit.TimeValue
import org.elasticsearch.index.query.*
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.sort.ScoreSortBuilder
import org.elasticsearch.search.sort.SortOrder
import org.springframework.beans.factory.annotation.Autowired
import org.zmolnar.rest.common.DESCRIPTION_INDEX_NAME
import org.zmolnar.rest.common.STORE_NAME
import org.zmolnar.rest.model.DescriptionIndexDocument
import java.util.concurrent.TimeUnit

private const val MAX_SIZE = 10_000

class DescriptionSearchRequest constructor(_typeId: String, _term: String){

    private val typeId = _typeId
    private val term = _term

    @Autowired
    private lateinit var client: RestHighLevelClient

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
            boolQueryBuilder.filter(QueryBuilders.termQuery(DescriptionIndexDocument.TYPE_ID, typeId))
        }

        val sourceBuilder = SearchSourceBuilder()
        sourceBuilder.sort(ScoreSortBuilder().order(SortOrder.DESC))
        sourceBuilder.timeout(TimeValue(duration, timeUnit))
        sourceBuilder.size(MAX_SIZE)
        sourceBuilder.query(createDisMaxQuery(boolQueryBuilder))

        val searchRequest = SearchRequest(STORE_NAME)
        searchRequest.types(DESCRIPTION_INDEX_NAME)
        searchRequest.source(sourceBuilder)

        return executeSearch(searchRequest, client)
    }

    private fun addTermFilterQuery(boolQueryBuilder: BoolQueryBuilder) {
        boolQueryBuilder.should(addPrefixQuery())
        boolQueryBuilder.should(addCommonTermsQuery())
        boolQueryBuilder.should(addExactTermQuery())
        boolQueryBuilder.should(addPhraseQuery())
    }

    private fun addPrefixQuery() : PrefixQueryBuilder {
        val prefixQuery = QueryBuilders.prefixQuery(DescriptionIndexDocument.TERM, term)
        prefixQuery.boost(20F)

        return prefixQuery
    }

    private fun addCommonTermsQuery(): QueryBuilder? {
        val termsQuery = QueryBuilders.commonTermsQuery(DescriptionIndexDocument.TERM, term)
        termsQuery.analyzer("simple")
        termsQuery.cutoffFrequency(0.001F)
        termsQuery.highFreqMinimumShouldMatch("1")
        termsQuery.lowFreqMinimumShouldMatch("1")
        termsQuery.boost(50F)
        return termsQuery
    }

    private fun addExactTermQuery() : TermQueryBuilder {
        val exactTermQuery = QueryBuilders.termQuery(DescriptionIndexDocument.TERM, term)
        exactTermQuery.boost(100F)
        return exactTermQuery
    }

    private fun addPhraseQuery(): MatchPhraseQueryBuilder? {
        return QueryBuilders.matchPhraseQuery(DescriptionIndexDocument.TERM, term).analyzer("simple").boost(20F)
    }

    private fun createDisMaxQuery(boolQueryBuilder: BoolQueryBuilder): DisMaxQueryBuilder? {
        val disMaxQuery = QueryBuilders.disMaxQuery()
        disMaxQuery.boost(1.2F)
        disMaxQuery.tieBreaker(0.9F)
        disMaxQuery.add(boolQueryBuilder)

        return disMaxQuery
    }

    private fun executeSearch(searchRequest: SearchRequest, client: RestHighLevelClient) : List<DescriptionIndexDocument> {
        val response = client.search(searchRequest, RequestOptions.DEFAULT)
        val gson = Gson()
        client.close()
        return response.hits.hits.asIterable().map {
            val jsonResponse = it.sourceAsString
            val document = gson.fromJson(jsonResponse, DescriptionIndexDocument::class.java)
            document.score = it.score
            document
        }.toCollection(ArrayList())

    }

}
