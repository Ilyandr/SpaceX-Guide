@file:Suppress("PackageName")

package gcu.production.guidespacex.ViewModels

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gcu.production.guidespacex.App
import gcu.production.guidespacex.Data.*
import gcu.production.guidespacex.Data.LoadingState
import gcu.production.guidespacex.Data.ObservableList
import gcu.production.guidespacex.Data.PaginationData
import gcu.production.guidespacex.Service.Rest.RestRepositoryImpl
import gcu.production.guidespacex.GeneralImpl.NetworkActions
import gcu.production.guidespacex.R
import gcu.production.guidespacex.Service.ActivityOnBackPressed
import gcu.production.guidespacex.Service.NavigationApp
import gcu.production.guidespacex.Service.NetworkConnection
import gcu.production.guidespacex.Data.RestEntity.SingleMissionListEntity
import gcu.production.guidespacex.UI.CustomInterface.CustomLoadingDialog
import gcu.production.guidespacex.UI.MissionDataFragment
import javax.inject.Inject
import kotlin.system.exitProcess

internal class MissionListViewModel(
    contextCall: FragmentActivity
) : ViewModel(), NetworkActions, ActivityOnBackPressed
{
    @Inject
    lateinit var observableList: ObservableList<SingleMissionListEntity>

    @Inject
    lateinit var restRepositoryImpl: RestRepositoryImpl

    val navigationApp: NavigationApp by lazy {
        NavigationApp(
            R.id.generalView
            , MissionDataFragment()
            , contextCall
        )
    }

    private val customLoadingDialog: CustomLoadingDialog
         by lazy { CustomLoadingDialog(contextCall) }

    val state =
        MutableLiveData<LoadingState>()
            .default(
                initialState = LoadingState.DefaultLoadingState
            )

    init
    {
        (contextCall.applicationContext as App)
            .missionsComponent
            .inject(this)
    }

    private fun loadDataForList()
    {
        state.set(LoadingState.LoadingProcessState)

        observableList.setActionChange {
            // Хъюстон, кажется у нас больше нет проблем!
            state.set(LoadingState.LoadingSuccessLoadingState)
        }

        restRepositoryImpl launchGetAllMissions  PaginationData()
    }

    infix fun checkCompleteList(list: MutableList<*>) =
        list.all { singleEntity ->
             if (singleEntity is SingleMissionListEntity)
                 singleEntity.missionName != null
            else false
        }

    override fun networkFaultConnection()
    {
        state.set(
            LoadingState.LoadingErrorLoadingState(
                messageError = Toast.makeText(
                    this.customLoadingDialog.context
                    , R.string.toastNetworkError
                    , Toast.LENGTH_SHORT)
            )
        )
        // Хъюстон, кажется у нас снова проблемы!
        (state.value as LoadingState.LoadingErrorLoadingState).showMessageError()
    }

    override fun launchWithCheckNetworkConnection() =
        NetworkConnection
            .checkingAccessWithActions(
                actionSuccess = ::loadDataForList
                , actionFault = ::networkFaultConnection
                , actionsLoadingAfterAndBefore = Pair(
                    Runnable { this.customLoadingDialog.startLoadingDialog() }
                    , Runnable { this.customLoadingDialog.stopLoadingDialog() })
                ,  listenerForFailConnection = this
            )

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
}