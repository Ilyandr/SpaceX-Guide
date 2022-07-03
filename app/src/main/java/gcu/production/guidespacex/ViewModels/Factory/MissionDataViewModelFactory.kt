@file:Suppress("PackageName", "UNCHECKED_CAST")
package gcu.production.guidespacex.ViewModels.Factory

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gcu.production.guidespacex.Data.RestEntity.SingleMissionListEntity
import gcu.production.guidespacex.ViewModels.MissionDataViewModel

internal class MissionDataViewModelFactory(
    private val singleMissionListEntity: SingleMissionListEntity
    , private val contextCall: FragmentActivity
) : ViewModelProvider.NewInstanceFactory()
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(MissionDataViewModel::class.java))
        {
            return MissionDataViewModel(
                singleMissionListEntity
                , contextCall
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}