@file:Suppress("PackageName")
package gcu.production.guidespacex.DI.Component

import dagger.Component
import gcu.production.guidespacex.DI.Module.DataDetailsModule
import gcu.production.guidespacex.ViewModels.MissionDataViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [DataDetailsModule::class])
internal interface DetailsComponent
{
    fun inject(missionDataViewModel: MissionDataViewModel)
}