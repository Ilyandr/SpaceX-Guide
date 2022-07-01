@file:Suppress("PackageName")
package gcu.production.guidespacex.DI.Module

import dagger.Module
import dagger.Provides
import gcu.production.guidespacex.Adapters.CustomAdapter
import gcu.production.guidespacex.Data.ObservableList
import gcu.production.guidespacex.Data.PaginationData
import gcu.production.guidespacex.Service.Rest.Response.ResponseImpl
import gcu.production.guidespacex.Service.Rest.Response.ResponseServiceMissions
import gcu.production.guidespacex.Data.RestEntity.SingleMissionListEntity
import gcu.production.guidespacex.Service.Rest.RestRepository
import gcu.production.guidespacex.Service.Rest.RestRepositoryImpl
import javax.inject.Singleton

@Module
internal class DataMissionsModule
{
    @Provides
    @Singleton
    fun provideObservableList():
            ObservableList<SingleMissionListEntity> =
        ObservableList(
            wrapped = mutableListOf()
        )

    @Provides
    fun provideResponseServiceService(
        list: ObservableList<SingleMissionListEntity>
    ): ResponseImpl =
        ResponseServiceMissions(
            observableList = list
        )

    @Provides
    fun provideRestRepositoryImpl(
        responseImpl: ResponseImpl
    ): RestRepositoryImpl =
        RestRepository(
            responseService = responseImpl
        )

    @Provides
    fun providePaginationData() = PaginationData()

    @Provides
    fun provideRecyclerAdapter() =
        CustomAdapter(mutableListOf())
} // И всё же - Kodein DI приятнее!