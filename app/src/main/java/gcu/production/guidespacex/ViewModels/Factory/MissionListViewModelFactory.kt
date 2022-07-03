@file:Suppress("PackageName", "UNCHECKED_CAST")
package gcu.production.guidespacex.ViewModels.Factory

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gcu.production.guidespacex.ViewModels.MissionListViewModel

internal class MissionListViewModelFactory(
    private val contextCall: FragmentActivity
): ViewModelProvider.NewInstanceFactory()
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        if (modelClass.isAssignableFrom(MissionListViewModel::class.java))
        {
            return MissionListViewModel(
                contextCall
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}