@file:Suppress("PackageName")

package gcu.production.guidespacex.ViewModels

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gcu.production.guidespacex.App
import gcu.production.guidespacex.Model.LoadingStateModule
import gcu.production.guidespacex.Data.ObservableList
import gcu.production.guidespacex.Data.PaginationData
import gcu.production.guidespacex.Service.Rest.RestRepositoryImpl
import gcu.production.guidespacex.Service.ActivityOnBackPressed
import gcu.production.guidespacex.Service.NetworkConnection
import gcu.production.guidespacex.Data.RestEntity.SingleMissionListEntity
import gcu.production.guidespacex.CustomInterface.CustomLoadingDialog
import gcu.production.guidespacex.CustomInterface.ViewInflator
import gcu.production.guidespacex.GeneralImpl.ViewModelImpl
import gcu.production.guidespacex.Model.default
import gcu.production.guidespacex.Model.set
import gcu.production.guidespacex.R
import javax.inject.Inject
import kotlin.system.exitProcess

internal class MissionListViewModel(
    contextCall: FragmentActivity
) : ViewModel(), ActivityOnBackPressed, ViewModelImpl
{
    @Inject
    lateinit var observableList: ObservableList<SingleMissionListEntity>

    @Inject
    lateinit var restRepositoryImpl: RestRepositoryImpl

    private val customLoadingDialog: CustomLoadingDialog
         by lazy { CustomLoadingDialog(contextCall) }

    private val loadingStateMutableModule =
        MutableLiveData<LoadingStateModule>()
            .default(LoadingStateModule.DefaultLoadingStateModule)

    val loadingStateModule: LiveData<LoadingStateModule> =
        this.loadingStateMutableModule

    init
    {
        (contextCall.applicationContext as App)
            .missionsComponent
            .inject(this)
    }

    private fun loadDataForList()
    {
        actionStateSuccessLoading()

        this.observableList.setActionChange {
            // Хъюстон, кажется у нас больше нет проблем!
            this.loadingStateMutableModule.set(
                LoadingStateModule.LoadingSuccessLoadingStateModule)
        }
    }

    infix fun checkCompleteList(list: MutableList<*>) =
        list.all { singleEntity ->
             if (singleEntity is SingleMissionListEntity)
                 singleEntity.missionName != null
            else false
        }

    fun actionBarOptions(titleBar: String, actionBar: ActionBar?) =
        actionBar?.let {
            it.title = titleBar
            it.setDisplayHomeAsUpEnabled(true)
            it.setBackgroundDrawable(
                ColorDrawable(
                    Color.parseColor(
                        "#373636"
                    )
                )
            )
        }

    override fun callOnBackPressed(activity: FragmentActivity) =
        activity
            .supportFragmentManager
            .fragments
            .let {
                if (it.isEmpty())
                    exitProcess(0)
                else
                    activity
                        .supportFragmentManager
                        .beginTransaction()
                        .remove(it[0])
                        .commit()
            }

    override fun actionStateFaultLoading()
    {
        Toast.makeText(
            this.customLoadingDialog.context
            , R.string.toastNetworkError
            , Toast.LENGTH_SHORT
        ).show()

        this.loadingStateMutableModule.set(
            LoadingStateModule.DefaultLoadingStateModule
        )
    }

    override fun actionStateDefaultLoading()
    {
        NetworkConnection
            .checkingAccessWithActions(
                actionSuccess = ::loadDataForList
                , actionFault =
                {
                   this.loadingStateMutableModule.set(
                       LoadingStateModule.LoadingErrorLoadingStateModule)
                }
                , actionsLoadingAfterAndBefore = Pair(
                    Runnable { this.customLoadingDialog.startLoadingDialog() }
                    , Runnable { this.customLoadingDialog.stopLoadingDialog() })
            )
    }

    override fun actionStateSuccessLoading(viewInflator: ViewInflator?) {
        this.restRepositoryImpl launchGetAllMissions PaginationData()
    }
}