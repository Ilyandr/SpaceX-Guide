@file:Suppress("PackageName")
package gcu.production.guidespacex.Service.Rest

import gcu.production.guidespacex.Data.PaginationData
import gcu.production.guidespacex.Service.Rest.Response.ResponseImpl

internal interface RestRepositoryImpl
{
    infix fun launchGetAllMissions(paginationData: PaginationData)
    infix fun launchGetDetailsInfo(missionID: String)
    infix fun setNewResponseService(restRepositoryImpl: ResponseImpl) // Запасной план должен быть всегда!
}