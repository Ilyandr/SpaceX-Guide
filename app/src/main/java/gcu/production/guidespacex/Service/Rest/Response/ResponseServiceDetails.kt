@file:Suppress("PackageName", "UNCHECKED_CAST")
package gcu.production.guidespacex.Service.Rest.Response

import com.google.gson.Gson
import gcu.production.guidespacex.Data.ObservableList
import gcu.production.guidespacex.Data.RestEntity.PeopleData
import org.json.JSONException
import org.json.JSONObject

internal class ResponseServiceDetails(
    private val observableList: ObservableList<PeopleData>
): ResponseImpl
{
    override fun <ResponseType, ReturnType> successResponse(
        response: ResponseType?
    ): ReturnType?
    {
        try
        {
            this.observableList.add(
                getSingleEntity(
                    JSONObject(
                        Gson().toJson(response)
                    )
                )
            )
        } catch (ignored: JSONException) {}

        return Unit as ReturnType
    }

    override fun errorResponse() {}

    private infix fun getSingleEntity(singleData: JSONObject) =
        PeopleData(
            fullName = singleData.getString("name")
            , agency = singleData.getString("agency")
            , status = singleData.getString("status")
        )
}