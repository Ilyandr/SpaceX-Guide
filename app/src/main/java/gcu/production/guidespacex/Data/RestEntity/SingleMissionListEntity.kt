@file:Suppress("PackageName")
package gcu.production.guidespacex.Data.RestEntity

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import java.text.SimpleDateFormat

@Keep
internal data class SingleMissionListEntity(
   val missionID: String? = null,
   val missionIconLinkSmall: String? = null,
   val missionName: String? = null,
   val countUseFirstStage: Long?  = null,
   val missionStatus: Boolean? = null,
   val missionStartDate: String? = null,
   var responseMissionSharedCrew: ResponseMissionSharedCrew? = null
): Parcelable
{
   constructor(parcel: Parcel) : this(
      parcel.readString(),
      parcel.readString(),
      parcel.readString(),
      parcel.readValue(Long::class.java.classLoader) as? Long,
      parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
      parcel.readString(),
      parcel.readParcelable(ResponseMissionSharedCrew::class.java.classLoader)
   )

   // Утекай, в подворотне нас ждёт SimpleDateFormat..
   @SuppressLint("SimpleDateFormat")
   fun getMissionStartDateFormatted(itsLageData: Boolean): String? =
      if (!itsLageData)
         this.missionStartDate?.let {
            SimpleDateFormat("yyyy-MM-dd")
               .parse(it.split("T")[0])
         }?.let {
            SimpleDateFormat("dd-MM-yyyy").format(it)
         }
   else
      this.missionStartDate?.let {
         SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            .parse(it.split(".")[0]
               .replaceFirst("T", " ")
            )
      }?.let {
         SimpleDateFormat("hh:mm dd-MM-yyyy").format(it)
      }

   override fun writeToParcel(parcel: Parcel, flags: Int)
   {
      parcel.writeString(missionID)
      parcel.writeString(missionIconLinkSmall)
      parcel.writeString(missionName)
      parcel.writeValue(countUseFirstStage)
      parcel.writeValue(missionStatus)
      parcel.writeString(missionStartDate)
      parcel.writeParcelable(responseMissionSharedCrew, flags)
   }

   override fun describeContents() = 0

   companion object CREATOR : Parcelable.Creator<SingleMissionListEntity> {
      override fun createFromParcel(parcel: Parcel): SingleMissionListEntity {
         return SingleMissionListEntity(parcel)
      }

      override fun newArray(size: Int): Array<SingleMissionListEntity?> {
         return arrayOfNulls(size)
      }
   }
}