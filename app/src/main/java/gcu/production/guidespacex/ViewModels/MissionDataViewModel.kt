@file:Suppress("PackageName")
package gcu.production.guidespacex.ViewModels

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gcu.production.guidespacex.App
import gcu.production.guidespacex.Data.ObservableList
import gcu.production.guidespacex.Service.NetworkConnection
import gcu.production.guidespacex.Data.RestEntity.PeopleData
import gcu.production.guidespacex.Data.RestEntity.SingleMissionListEntity
import gcu.production.guidespacex.Service.Rest.RestRepositoryImpl
import gcu.production.guidespacex.CustomInterface.CustomLoadingDialog
import gcu.production.guidespacex.CustomInterface.ViewInflator
import gcu.production.guidespacex.GeneralImpl.ViewModelImpl
import gcu.production.guidespacex.Model.LoadingStateModule
import gcu.production.guidespacex.Model.default
import gcu.production.guidespacex.Model.set
import gcu.production.guidespacex.R
import javax.inject.Inject

internal class MissionDataViewModel(
    private val singleMissionListEntity: SingleMissionListEntity
    , contextCall: FragmentActivity
): ViewModel(), ViewModelImpl
{
    @Inject
    lateinit var restRepositoryImpl: RestRepositoryImpl

    @Inject
    lateinit var observableList: ObservableList<PeopleData>

    private val customLoadingDialog: CustomLoadingDialog by lazy {
        CustomLoadingDialog(contextCall)
    }

    private val loadingStateMutableModule =
        MutableLiveData<LoadingStateModule>()
           .default(LoadingStateModule.DefaultLoadingStateModule)

     val loadingStateModule: LiveData<LoadingStateModule> =
         this.loadingStateMutableModule

    init
    {
        (contextCall.applicationContext as App)
            .detailsComponent
            .inject(this)
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
        this.observableList.clear()

        if (this.singleMissionListEntity
                .responseMissionSharedCrew!!
                .crew!!
                .isNotEmpty()
            && observableList.wrapped.isEmpty()
        ) {
            NetworkConnection
                .checkingAccessWithActions(
                    actionSuccess =
                    {
                        this.observableList.setActionChange {
                            this.loadingStateMutableModule.set(
                                LoadingStateModule.LoadingSuccessLoadingStateModule
                            )
                        }

                        singleMissionListEntity
                            .responseMissionSharedCrew
                            ?.crew
                            ?.forEach {
                                this.restRepositoryImpl.launchGetDetailsInfo(it)
                            }
                    },
                    actionFault =
                    {
                        this.loadingStateMutableModule.set(
                            LoadingStateModule.LoadingErrorLoadingStateModule
                        )
                    },
                    actionsLoadingAfterAndBefore = Pair(
                        Runnable { this.customLoadingDialog.startLoadingDialog() },
                        Runnable { this.customLoadingDialog.stopLoadingDialog() })
                )
        }
        else
        {
            this.loadingStateMutableModule.set(
                LoadingStateModule
                    .LoadingSuccessLoadingStateModule
            )
        }
    }

    override fun actionStateSuccessLoading(viewInflator: ViewInflator?) {
        viewInflator?.inflateView(this.observableList)
    }
}