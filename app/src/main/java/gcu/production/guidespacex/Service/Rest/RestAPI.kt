@file:Suppress("PackageName")
package gcu.production.guidespacex.Service.Rest

import gcu.production.guidespacex.Data.RestEntity.CallBodyEntity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

internal interface RestAPI
{
    @POST("/v4/launches/query")
    fun getMissionsList( @Body callBodyEntity: CallBodyEntity): Call<Any?>

    @GET("/v4/crew/{id}")
    fun getSingleMissionData(
        @Path("id") missionID: String
    ): Call<Any?>

    companion object {
        internal const val BASE_URL = "https://api.spacexdata.com"
    }
}