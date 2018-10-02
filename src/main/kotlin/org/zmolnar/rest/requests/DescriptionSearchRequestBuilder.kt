package org.zmolnar.rest.requests

class DescriptionSearchRequestBuilder {

    private var typeId: String = ""
    private var term: String = ""

    fun filterByType(typeId: String) = apply { this.typeId = typeId}
    fun filterByTerm(term: String) = apply {this.term = term}

    fun build() = DescriptionSearchRequest(typeId, term)

}