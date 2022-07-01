@file:Suppress("PackageName")
package gcu.production.guidespacex.Data.RestEntity

import androidx.annotation.Keep
import gcu.production.guidespacex.Data.PaginationData

internal fun PaginationData.buildBodyEntity() =
    CallBodyEntity(
        options = Options(
            limit = this.limit
            , page = this.page
        )
    )

@Keep // Оберегаем данные класса от злых рук обфусификации minify (см. gradle)!
internal data class CallBodyEntity (
    var query: Query = Query(),
    var options: Options
)

@Keep
internal data class Options(
    val pagination: Boolean = true
    , val limit: Int
    , val page: Int
    )

@Keep
internal data class Query(
    val date_utc: DateInformation = DateInformation()
)

@Keep
internal data class DateInformation(
    val `$gte`: String = "2021-01-01T00:00:00.000Z"
    , val `$lte`: String = "2022-07-01T00:00:00.000Z"
)