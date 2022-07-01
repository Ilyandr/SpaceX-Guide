@file:Suppress( "PackageName")
package gcu.production.guidespacex.UI

import android.content.res.Configuration

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gcu.production.guidespacex.Adapters.CustomAdapter
import gcu.production.guidespacex.App
import gcu.production.guidespacex.Data.LoadingState
import gcu.production.guidespacex.Data.ObservableList
import gcu.production.guidespacex.GeneralImpl.GeneralUIImpl
import gcu.production.guidespacex.R
import gcu.production.guidespacex.Service.NavigationApp.Companion.NAVIGATE_KEY
import gcu.production.guidespacex.Data.RestEntity.SingleMissionListEntity
import gcu.production.guidespacex.ViewModels.MissionListViewModel
import javax.inject.Inject

internal class MissionListActivity : AppCompatActivity(), GeneralUIImpl
{
    @Inject lateinit var observableList: ObservableList<SingleMissionListEntity>
    @Inject lateinit var recyclerAdapter: CustomAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var missionListViewModel: MissionListViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initObjects()
        launchBasicLogic()
    }

    override fun initObjects()
    {
        (applicationContext as App)
            .missionsComponent
            .inject(this)

        this.missionListViewModel =
            MissionListViewModel(this)

        this.recyclerView =
            findViewById(R.id.missionsList)

        recyclerViewOptions()
        this.missionListViewModel.actionBarOptions(
            getString(R.string.appName)
            , supportActionBar
        )
    }

    private fun recyclerViewOptions()
    {
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        this.recyclerView.layoutManager = linearLayoutManager
        this.recyclerView.adapter = this.recyclerAdapter

        if(this.observableList.wrapped.isNotEmpty())
            this.recyclerAdapter.updateAdapter(
                this.observableList.wrapped
            )

        this.recyclerAdapter.PaginationController(
            this, findViewById(R.id.processLoadingData))
            .recyclerViewOptions(
                this.recyclerView
                , linearLayoutManager
            )
    }

    override fun launchBasicLogic()
    {
        this.observableList.setActionChange {
           if (missionListViewModel checkCompleteList it)
                this.recyclerAdapter.updateAdapter(
                    it as List<SingleMissionListEntity>
                )
        }

        if(this.observableList.wrapped.isEmpty())
            this.missionListViewModel.launchWithCheckNetworkConnection()

        this.recyclerAdapter.setActionClick {
            if (this.missionListViewModel.state.value
                        is LoadingState.LoadingSuccessLoadingState)
                this.missionListViewModel
                    .navigationApp
                    .navigate(NAVIGATE_KEY to it)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration)
    {
        super.onConfigurationChanged(newConfig)
        if(this.observableList.wrapped.isNotEmpty())
            recyclerViewOptions()
    }

    override fun onBackPressed() {
        this.missionListViewModel.callOnBackPressed(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}