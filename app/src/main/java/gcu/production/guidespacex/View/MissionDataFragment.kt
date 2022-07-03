@file:Suppress("PackageName")
package gcu.production.guidespacex.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import gcu.production.guidespacex.CustomInterface.MissionDataFragmentInflator
import gcu.production.guidespacex.CustomInterface.ViewInflator
import gcu.production.guidespacex.GeneralImpl.GeneralUIImpl
import gcu.production.guidespacex.Model.LoadingStateModule
import gcu.production.guidespacex.Service.NavigationApp
import gcu.production.guidespacex.ViewModels.Factory.MissionDataViewModelFactory
import gcu.production.guidespacex.ViewModels.MissionDataViewModel
import gcu.production.guidespacex.databinding.FragmentMissionDataBinding

internal class MissionDataFragment : Fragment(), GeneralUIImpl
{
    private lateinit var viewBinding: FragmentMissionDataBinding

    private val viewModel by viewModels<MissionDataViewModel> {
        MissionDataViewModelFactory(
            requireArguments()
                .getParcelable(NavigationApp.NAVIGATE_KEY)!!
            , requireActivity()
        )
    }

    private lateinit var viewInflator: ViewInflator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View
    {
        initObjects()
        launchBasicLogic()
        return this.viewBinding.root
    }

    override fun initObjects()
    {
        this.viewBinding =
            FragmentMissionDataBinding.inflate(layoutInflater)

        this.viewInflator =
            MissionDataFragmentInflator(
                requireActivity()
                , requireArguments()
                    .getParcelable(NavigationApp.NAVIGATE_KEY)!!
                , this.viewBinding
            )
    }

    override fun launchBasicLogic()
    {
        this.viewModel
            .loadingStateModule
            .observe(requireActivity())
            {
                when (it)
                {
                    LoadingStateModule.LoadingErrorLoadingStateModule ->
                        this.viewModel.actionStateFaultLoading()

                    LoadingStateModule.LoadingSuccessLoadingStateModule ->
                        this.viewModel.actionStateSuccessLoading(this.viewInflator)

                    LoadingStateModule.DefaultLoadingStateModule ->
                        this.viewModel.actionStateDefaultLoading()

                    else -> return@observe
                }
            }
    }
}