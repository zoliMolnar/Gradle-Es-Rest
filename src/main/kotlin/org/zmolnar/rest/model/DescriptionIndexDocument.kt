package org.zmolnar.rest.model



const val TERM = "term"
const val TYPE_ID = "typeId"

/**
 * Simple data class to represent a SNOMEDCT description
 */
data class DescriptionIndexDocument(
        val id: String,
        val effectiveTime: Long,
        val active: Boolean,
        val moduleId: String,
        val conceptId: String,
        val languageCode: String,
        val typeId: String,
        val term: String,
        val caseSignificanceId: String
) {
    var score: Float = (-1).toFloat()

}