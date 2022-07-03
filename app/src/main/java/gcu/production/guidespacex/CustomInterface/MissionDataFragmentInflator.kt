@file:Suppress("PackageName", "BlockingMethodInNonBlockingContext")
package gcu.production.guidespacex.CustomInterface

import android.content.Context
import android.text.SpannableStringBuilder
import androidx.core.content.ContextCompat
import androidx.core.text.color
import coil.load
import gcu.production.guidespacex.Data.ObservableList
import gcu.production.guidespacex.R
import gcu.production.guidespacex.Data.RestEntity.PeopleData
import gcu.production.guidespacex.Data.RestEntity.SingleMissionListEntity
import gcu.production.guidespacex.databinding.FragmentMissionDataBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class MissionDataFragmentInflator(
    private val context: Context
    , private val singleMissionListEntity: SingleMissionListEntity
    , private val viewParentBinding: FragmentMissionDataBinding
    ): ViewInflator
{
    @OptIn(DelicateCoroutinesApi::class)
    override fun <T> inflateView(observableList: ObservableList<T>)
    {
        GlobalScope.launch(Dispatchers.Main)
        {
            singleMissionListEntity
                .responseMissionSharedCrew?.let { currentEntity ->
                    viewParentBinding.largeMissionIcon
                        .load(uri = currentEntity.largeIcon) {
                            crossfade(true)
                        }

                    val descriptionColor =
                        ContextCompat.getColor(
                            context, R.color.white
                        )

                    viewParentBinding.missionDate.text =
                        SpannableStringBuilder()
                            .color(descriptionColor) { append("Date: ") }
                            .append(singleMissionListEntity.getMissionStartDateFormatted(true))

                    viewParentBinding.missionName.text =
                        SpannableStringBuilder()
                            .color(descriptionColor) { append("Mission: ") }
                            .append(singleMissionListEntity.missionName)

                    viewParentBinding.missionStatus.text =
                        SpannableStringBuilder()
                            .color(descriptionColor) { append("Status: ") }
                            .append(
                                if (singleMissionListEntity.missionStatus!!)
                                    "Success"
                                else "Fault"
                            )

                    viewParentBinding.firstCount.text =
                        SpannableStringBuilder()
                            .color(descriptionColor) { append("Count first stage usage: ") }
                            .append(singleMissionListEntity.countUseFirstStage.toString())

                    viewParentBinding.missionDetails.text =
                        SpannableStringBuilder()
                            .color(descriptionColor) { append("Details: ") }
                            .append(currentEntity.details)

                    viewParentBinding.crewData.text = ""

                    if (observableList.isNotEmpty())
                        observableList.forEach {
                            if (it is PeopleData)
                                viewParentBinding.crewData.text =
                                    "${viewParentBinding.crewData.text}" +
                                            "\n${it.fullName} - ${it.agency} - ${it.status}"
                        }

                    viewParentBinding.crewData.text =
                        SpannableStringBuilder()
                            .color(descriptionColor) { append("Astronauts: ") }
                            .append(viewParentBinding.crewData.text.ifEmpty { "unknown" })
                }
        }
    }
}

internal interface ViewInflator
{
    infix fun <T> inflateView(
        observableList: ObservableList<T>
    )
}