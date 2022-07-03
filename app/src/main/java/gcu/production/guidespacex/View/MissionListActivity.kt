@file:Suppress( "PackageName")
package gcu.production.guidespacex.View


import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gcu.production.guidespacex.Adapters.CustomAdapter
import gcu.production.guidespacex.App
import gcu.production.guidespacex.Data.ObservableList
import gcu.production.guidespacex.GeneralImpl.GeneralUIImpl
import gcu.production.guidespacex.R
import gcu.production.guidespacex.Service.NavigationApp.Companion.NAVIGATE_KEY
import gcu.production.guidespacex.Data.RestEntity.SingleMissionListEntity
import gcu.production.guidespacex.Model.LoadingStateModule
import gcu.production.guidespacex.Service.NavigationApp
import gcu.production.guidespacex.ViewModels.Factory.MissionListViewModelFactory
import gcu.production.guidespacex.ViewModels.MissionListViewModel
import javax.inject.Inject

internal class MissionListActivity : AppCompatActivity(), GeneralUIImpl
{
    @Inject
    lateinit var observableList: ObservableList<SingleMissionListEntity>
    @Inject
    lateinit var recyclerAdapter: CustomAdapter

    private lateinit var recyclerView: RecyclerView
    private val missionListViewModel by viewModels<MissionListViewModel> {
        MissionListViewModelFactory(this)
    }

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

        this.recyclerView =
            findViewById(R.id.missionsList)

        this.missionListViewModel.actionBarOptions(
            getString(R.string.appName)
            , supportActionBar
        )

        recyclerViewOptions()
    }

    private fun recyclerViewOptions()
    {
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        this.recyclerView.layoutManager = linearLayoutManager
        this.recyclerView.adapter = this.recyclerAdapter

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

        this.recyclerAdapter.setActionClick {
            NavigationApp(
                R.id.generalView
                , MissionDataFragment()
                , this
            ).navigate(NAVIGATE_KEY to it)
        }

        this.missionListViewModel
            .loadingStateModule
            .observe(this)
            {
                when (it)
                {
                    LoadingStateModule.DefaultLoadingStateModule ->
                        this.missionListViewModel.actionStateDefaultLoading()

                    LoadingStateModule.LoadingSuccessLoadingStateModule ->
                        if(this.observableList.wrapped.isEmpty())
                            this.missionListViewModel.actionStateSuccessLoading()

                    LoadingStateModule.LoadingErrorLoadingStateModule ->
                        this.missionListViewModel.actionStateFaultLoading()

                    else -> return@observe
                }
            }
    }

    override fun onBackPressed()
    {
        this.viewModelStore.clear()
        this.missionListViewModel.callOnBackPressed(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}