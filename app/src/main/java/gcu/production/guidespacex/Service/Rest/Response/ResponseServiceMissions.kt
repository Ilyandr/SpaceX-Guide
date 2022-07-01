@file:Suppress("UNCHECKED_CAST", "PackageName")
package gcu.production.guidespacex.Service.Rest.Response

import com.google.gson.Gson
import gcu.production.guidespacex.Data.ObservableList
import gcu.production.guidespacex.Data.RestEntity.ResponseMissionSharedCrew
import gcu.production.guidespacex.Data.RestEntity.SingleMissionListEntity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


internal class ResponseServiceMissions(
    private val observableList: ObservableList<SingleMissionListEntity>
) : ResponseImpl
{
    override fun <ResponseType, ReturnType> successResponse(
        response: ResponseType?
    ): ReturnType?
    {
        JSONObject(
            Gson().toJson(response)
        ).getJSONArray("docs")
            .also {
                for (index in 0..it.length())
                    try
                    {
                        this.observableList.add(
                            getSingleEntity(
                                it.getJSONObject(index)
                            )
                        )
                    } catch (ignored: JSONException) {}
            }

        return Unit as ReturnType
    }

    override fun errorResponse() {}

    // Когда мама принесла полные пакеты с магазина и ты бежишь их разбирать (но в моём случае я уже сижу точу конфеты).
    private fun getSingleEntity(singleData: JSONObject) =
        SingleMissionListEntity(
            missionStatus = singleData.getBoolean("success")
            , missionStartDate = singleData.getString("date_local")
            , missionName = singleData.getString("name")
            , missionID = singleData.getString("id")
            , missionIconLinkSmall = singleData
                .getJSONObject("links")
                .getJSONObject("patch")
                .getString("small")
            , countUseFirstStage = singleData
                .getJSONArray("cores")
                .getJSONObject(0)
                .getLong("flight")
            , responseMissionSharedCrew = ResponseMissionSharedCrew(
                crew = this getStringArray singleData.getJSONArray("crew")
                , largeIcon = singleData
                    .getJSONObject("links")
                    .getJSONObject("patch")
                    .getString("large")
                , details = singleData.getString("details")
            )
        )

    @Throws(JSONException::class)
    private infix fun getStringArray(jsonArray: JSONArray?): List<String>
    {
        return if (jsonArray != null)
        {
            val stringsArray =
                arrayOfNulls<String>(jsonArray.length())

            for (i in 0 until jsonArray.length())
                stringsArray[i] = jsonArray.getString(i)

            val result = mutableListOf<String>()

            stringsArray.forEach {
                result.add(it ?: return emptyList())
            }
            result
        } else emptyList()
    }
}