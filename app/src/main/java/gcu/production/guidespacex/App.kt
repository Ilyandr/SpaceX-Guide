package gcu.production.guidespacex

import android.app.Application
import gcu.production.guidespacex.DI.Component.DaggerDetailsComponent
import gcu.production.guidespacex.DI.Component.DaggerMissionsComponent
import gcu.production.guidespacex.DI.Component.MissionsComponent
import gcu.production.guidespacex.DI.Component.DetailsComponent

internal class App: Application()
{
    lateinit var missionsComponent: MissionsComponent
    lateinit var detailsComponent: DetailsComponent

    override fun onCreate()
    {
        super.onCreate()
        this.missionsComponent =
            DaggerMissionsComponent.create()
        this.detailsComponent =
            DaggerDetailsComponent.create()
    }
}