@file:Suppress("PackageName")
package gcu.production.guidespacex.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import gcu.production.guidespacex.App
import gcu.production.guidespacex.Data.PaginationData
import gcu.production.guidespacex.GeneralImpl.NetworkActions
import gcu.production.guidespacex.R
import gcu.production.guidespacex.Service.NetworkConnection
import gcu.production.guidespacex.Data.RestEntity.SingleMissionListEntity
import gcu.production.guidespacex.Service.Rest.RestRepositoryImpl
import gcu.production.guidespacex.databinding.SingleItemRecycleViewBinding
import kotlinx.coroutines.*
import javax.inject.Inject

internal typealias delegateUnitFunction =
        ((singleEntity: SingleMissionListEntity) -> Unit)?

internal class CustomAdapter(
    private val missionsList: MutableList<SingleMissionListEntity>
    , private var actionClick: delegateUnitFunction = null
) : RecyclerView.Adapter<CustomAdapter.MyViewHolder>()
{
    private lateinit var singleViewBinding: SingleItemRecycleViewBinding

    inner class MyViewHolder(
        itemView: View,
        private val binding: SingleItemRecycleViewBinding,
    ) : RecyclerView.ViewHolder(itemView)
    {
        @SuppressLint("SetTextI18n")
        fun drawItem(item: SingleMissionListEntity)
        {
            this.binding.missionName.text =
                item.missionName

            this.binding.firstCount.text =
                "Count:${item.countUseFirstStage}"

            this.binding.missionDate.text =
                item.getMissionStartDateFormatted(itsLageData = false)

            this.binding.missionStatus.text =
                if (item.missionStatus!!) "Success"
                else "Fault"

            this.binding.smallImage
                .load(uri = item.missionIconLinkSmall) {
                    crossfade(true)
                }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup
        , viewType: Int
    ): MyViewHolder
    {
        this.singleViewBinding =
            SingleItemRecycleViewBinding.inflate(
                LayoutInflater.from(parent.context)
                , parent
                , false
            )

        return MyViewHolder(
            singleViewBinding.root, singleViewBinding
        )
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        val singleMissionListEntity =
            this.missionsList[position]

        holder.drawItem(singleMissionListEntity)

        holder.itemView.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                this@CustomAdapter
                    .actionClick
                    ?.invoke(singleMissionListEntity)
            }
        }
    }

    override fun getItemCount() =
        this.missionsList.size

    @SuppressLint("NotifyDataSetChanged")
    infix fun updateAdapter(items: List<SingleMissionListEntity>)
    {
        this.missionsList.clear()
        this.missionsList.addAll(
            items.distinctBy { it.missionID }
                .sortedBy { it.missionStartDate }
                .reversed()
        )
        notifyDataSetChanged()
    }

    infix fun setActionClick(action: delegateUnitFunction) {
        this.actionClick = action
    }


    // И на миг, позабыв, что такое любовь - мы с тобою начнём.. Танцы вдвоём, странные танцы.
    inner class PaginationController(
        contextCall: Context
        , private val progressBar: ProgressBar
        ): NetworkActions
    {
        @Inject
        lateinit var paginationData: PaginationData
        @Inject
        lateinit var restRepositoryImpl: RestRepositoryImpl

        init
        {
            (contextCall.applicationContext as App)
                .missionsComponent
                .inject(this)
        }

        fun recyclerViewOptions(
            recyclerView: RecyclerView
            , linearLayoutManager: LinearLayoutManager
        ) {
            recyclerView.addOnScrollListener(
                object: RecyclerView.OnScrollListener()
                {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
                    {
                        if (dy > 0)
                        {
                            val visibleItemCount =
                                linearLayoutManager.childCount

                            val pastVisibleItem =
                                linearLayoutManager.findFirstCompletelyVisibleItemPosition()

                            val totalVisibleItem =
                                this@CustomAdapter.itemCount

                            if (!paginationData.isLoading)
                            {
                                if (visibleItemCount + pastVisibleItem >= totalVisibleItem)
                                {
                                    paginationData.page++
                                    launchWithCheckNetworkConnection()
                                }
                            }
                        }
                    }
                })
        }

        @OptIn(DelicateCoroutinesApi::class)
        private fun getNewSinglePage() =
            GlobalScope.launch(Dispatchers.IO) {
                restRepositoryImpl.launchGetAllMissions(
                    this@PaginationController.paginationData
                )
            }

        override fun networkFaultConnection() =
            Toast.makeText(
                this.progressBar.context
                , R.string.toastNetworkError
                , Toast.LENGTH_SHORT)
                .show()

        override fun launchWithCheckNetworkConnection() =
            NetworkConnection
                .checkingAccessWithActions(
                    actionSuccess = ::getNewSinglePage
                    , actionFault = ::networkFaultConnection
                    , actionsLoadingAfterAndBefore = Pair(
                        Runnable
                        {
                            paginationData.isLoading = true
                            progressBar.visibility = View.VISIBLE
                        }
                        , Runnable
                        {
                            paginationData.isLoading = false
                            progressBar.visibility = View.GONE
                        })
                    ,  listenerForFailConnection = this
                )
    }
}