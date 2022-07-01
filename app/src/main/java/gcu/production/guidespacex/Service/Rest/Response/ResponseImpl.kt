@file:Suppress("PackageName")

package gcu.production.guidespacex.Service.Rest.Response

internal interface ResponseImpl
{
    fun <ResponseType, ReturnType> successResponse(
        response: ResponseType?
    ): ReturnType?

    fun errorResponse()
}