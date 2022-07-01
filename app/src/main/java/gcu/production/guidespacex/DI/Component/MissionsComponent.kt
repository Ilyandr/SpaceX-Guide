@file:Suppress("PackageName")
package gcu.production.guidespacex.DI.Component

import dagger.Component
import gcu.production.guidespacex.Adapters.CustomAdapter
import gcu.production.guidespacex.DI.Module.DataMissionsModule
import gcu.production.guidespacex.UI.MissionListActivity
import gcu.production.guidespacex.ViewModels.MissionListViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [DataMissionsModule::class])
internal interface MissionsComponent
{
    fun inject(missionListActivity: MissionListActivity)
    fun inject(missionListViewModel: MissionListViewModel)
    fun inject(paginationController: CustomAdapter.PaginationController)
}