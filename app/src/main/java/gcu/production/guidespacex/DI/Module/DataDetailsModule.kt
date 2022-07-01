@file:Suppress("PackageName")
package gcu.production.guidespacex.DI.Module

import dagger.Module
import dagger.Provides
import gcu.production.guidespacex.Data.ObservableList
import gcu.production.guidespacex.Service.Rest.Response.ResponseImpl
import gcu.production.guidespacex.Service.Rest.Response.ResponseServiceDetails
import gcu.production.guidespacex.Data.RestEntity.PeopleData
import gcu.production.guidespacex.Service.Rest.RestRepository
import gcu.production.guidespacex.Service.Rest.RestRepositoryImpl
import javax.inject.Singleton

@Module
internal class DataDetailsModule
{
    @Provides
    @Singleton
    fun provideObservableList():
            ObservableList<PeopleData> =
        ObservableList(
            wrapped = mutableListOf()
        )

    @Provides
    fun provideResponseServiceService(
        list: ObservableList<PeopleData>
    ): ResponseImpl =
        ResponseServiceDetails(
            observableList = list
        )

    @Provides
    fun provideRestRepositoryImpl(
        responseImpl: ResponseImpl
    ): RestRepositoryImpl =
        RestRepository(
            responseService = responseImpl
        )
}