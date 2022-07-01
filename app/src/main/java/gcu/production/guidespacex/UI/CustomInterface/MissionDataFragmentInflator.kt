@file:Suppress("PackageName", "BlockingMethodInNonBlockingContext")
package gcu.production.guidespacex.UI.CustomInterface

import android.annotation.SuppressLint
import android.content.Context
import android.text.SpannableStringBuilder
import android.text.method.ScrollingMovementMethod
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


internal abstract class MissionDataFragmentInflator(
    private val context: Context
    , private val singleMissionListEntity: SingleMissionListEntity
    , private val viewParentBinding: FragmentMissionDataBinding)
{
    @SuppressLint("SetTextI18n")
    @OptIn(DelicateCoroutinesApi::class)
    protected infix fun inflateView(observableList: ObservableList<PeopleData>
    ) = GlobalScope.launch(Dispatchers.Main)
    {
        singleMissionListEntity
            .responseMissionSharedCrew?.let { currentEntity ->
                viewParentBinding.largeMissionIcon
                    .load(uri = currentEntity.largeIcon) {
                        crossfade(true)
                    }

                val descriptionColor =
                    ContextCompat.getColor(
                        context
                        , R.color.white
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

                viewParentBinding.missionDetails.movementMethod =
                    ScrollingMovementMethod()

                viewParentBinding.crewData.text = ""

                if (observableList.isNotEmpty())
                    observableList.forEach {
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