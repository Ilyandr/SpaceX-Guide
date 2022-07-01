@file:Suppress("PackageName")
package gcu.production.guidespacex.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gcu.production.guidespacex.GeneralImpl.GeneralUIImpl
import gcu.production.guidespacex.Service.NavigationApp.Companion.NAVIGATE_KEY
import gcu.production.guidespacex.ViewModels.MissionDataViewModel
import gcu.production.guidespacex.databinding.FragmentMissionDataBinding

internal class MissionDataFragment : Fragment(), GeneralUIImpl
{
    private lateinit var viewBinding: FragmentMissionDataBinding
    private lateinit var viewModel: MissionDataViewModel

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

        this.viewModel =
            MissionDataViewModel(
                requireArguments().getParcelable(NAVIGATE_KEY)!!
                , requireActivity()
                , this.viewBinding
            )
    }

    override fun launchBasicLogic()
    {
        viewModel.launchWithCheckNetworkConnection()
    }
}