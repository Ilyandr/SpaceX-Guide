@file:Suppress("PackageName")
package gcu.production.guidespacex.ViewModels

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import gcu.production.guidespacex.App
import gcu.production.guidespacex.Data.LoadingState
import gcu.production.guidespacex.Data.ObservableList
import gcu.production.guidespacex.Data.default
import gcu.production.guidespacex.Data.set
import gcu.production.guidespacex.GeneralImpl.NetworkActions
import gcu.production.guidespacex.R
import gcu.production.guidespacex.Service.NetworkConnection
import gcu.production.guidespacex.Data.RestEntity.PeopleData
import gcu.production.guidespacex.Data.RestEntity.SingleMissionListEntity
import gcu.production.guidespacex.Service.Rest.RestRepositoryImpl
import gcu.production.guidespacex.UI.CustomInterface.CustomLoadingDialog
import gcu.production.guidespacex.UI.CustomInterface.MissionDataFragmentInflator
import gcu.production.guidespacex.databinding.FragmentMissionDataBinding
import javax.inject.Inject

internal class MissionDataViewModel(
    private val singleMissionListEntity: SingleMissionListEntity
    , contextCall: FragmentActivity
    , viewParentBinding: FragmentMissionDataBinding
): MissionDataFragmentInflator(
    contextCall
    , singleMissionListEntity
    , viewParentBinding
), NetworkActions
{
    @Inject
    lateinit var restRepositoryImpl: RestRepositoryImpl

    @Inject
    lateinit var observableList: ObservableList<PeopleData>

    private val customLoadingDialog: CustomLoadingDialog
            by lazy { CustomLoadingDialog(contextCall) }

    private val state =
        MutableLiveData<LoadingState>()
            .default(
                initialState = LoadingState.DefaultLoadingState
            )

    init
    {
        (contextCall.applicationContext as App)
            .detailsComponent
            .inject(this)
    }

    private fun loadDataForList()
    {
        this.observableList.wrapped.clear()
        this.state.set(LoadingState.LoadingProcessState)

        this.observableList.setActionChange {
            this inflateView observableList
            this.state.set(LoadingState.LoadingSuccessLoadingState)
        }

        singleMissionListEntity
            .responseMissionSharedCrew
            ?.crew
            ?.forEach {
                this.restRepositoryImpl.launchGetDetailsInfo(it)
            }
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
        (state.value as LoadingState.LoadingErrorLoadingState).showMessageError()
    }

    override fun launchWithCheckNetworkConnection()
    {
        if (this.singleMissionListEntity
                .responseMissionSharedCrew!!
                .crew!!
                .isNotEmpty())
            NetworkConnection
                .checkingAccessWithActions(
                    actionSuccess = ::loadDataForList,
                    actionFault = ::networkFaultConnection,
                    actionsLoadingAfterAndBefore = Pair(
                        Runnable { this.customLoadingDialog.startLoadingDialog() },
                        Runnable { this.customLoadingDialog.stopLoadingDialog() }),
                    listenerForFailConnection = this
                )
        else
        {
            this.state.set(LoadingState.LoadingSuccessLoadingState)
            this inflateView observableList
        }
    }
}