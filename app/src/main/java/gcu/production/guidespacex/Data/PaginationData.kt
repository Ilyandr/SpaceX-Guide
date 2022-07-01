@file:Suppress("PackageName")
package gcu.production.guidespacex.Data

internal data class PaginationData(
    var limit: Int = 10
    , var isLoading: Boolean = false
    , var page: Int = 1
)