package org.zmolnar.rest.config

import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.beans.factory.FactoryBean
import org.springframework.context.annotation.Configuration

@Configuration
class ElasticSearchConfig : FactoryBean<RestHighLevelClient> {

    var restHighLevelClient: RestHighLevelClient? = null

    override fun getObject(): RestHighLevelClient {
        return createHighLevelClient()
    }

    override fun isSingleton(): Boolean {
        return true
    }


    override fun getObjectType(): Class<*> {
        return RestHighLevelClient::class.java
    }


    private fun createHighLevelClient() : RestHighLevelClient {
        restHighLevelClient = RestHighLevelClient(RestClient.builder(
                HttpHost("localhost", 9200, "http"),
                HttpHost("localhost", 9201, "http")
        ))

        return restHighLevelClient as RestHighLevelClient
    }

}