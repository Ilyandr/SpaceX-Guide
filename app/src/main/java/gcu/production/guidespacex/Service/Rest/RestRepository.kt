@file:Suppress("PackageName")
package gcu.production.guidespacex.Service.Rest

import gcu.production.guidespacex.Data.PaginationData
import gcu.production.guidespacex.Service.Rest.Response.ResponseImpl
import gcu.production.guidespacex.Service.Rest.RestAPI.Companion.BASE_URL
import gcu.production.guidespacex.Data.RestEntity.buildBodyEntity
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

internal class RestRepository(
    private var responseService: ResponseImpl
    ) : Callback<Any?>, RestRepositoryImpl
{
    override infix fun launchGetAllMissions(paginationData: PaginationData) =
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RestAPI::class.java)
            .getMissionsList(paginationData.buildBodyEntity())
            .enqueue(this)

    override infix fun launchGetDetailsInfo(missionID: String) =
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RestAPI::class.java)
            .getSingleMissionData(missionID)
            .enqueue(this)

    override fun setNewResponseService(restRepositoryImpl: ResponseImpl) {
        this.responseService = restRepositoryImpl
    }

    override fun onResponse(
        call: Call<Any?>
        , response: Response<Any?>)
    {
        this.responseService.successResponse<Any, Unit>(
            response = response.body()
        )
    }

    override fun onFailure(
        call: Call<Any?>
        , t: Throwable
    ) = this.responseService.errorResponse()
}