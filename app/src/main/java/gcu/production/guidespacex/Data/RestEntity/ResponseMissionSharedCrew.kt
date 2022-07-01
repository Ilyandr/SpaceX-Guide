@file:Suppress("PackageName")
package gcu.production.guidespacex.Data.RestEntity

import android.os.Parcel
import android.os.Parcelable

internal data class ResponseMissionSharedCrew(
     val largeIcon: String? = null
    , val details: String?  = null
    , val crew: List<String>? = null
): Parcelable
{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int)
    {
        parcel.writeString(largeIcon)
        parcel.writeString(details)
        parcel.writeStringList(crew)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<ResponseMissionSharedCrew> {
        override fun createFromParcel(parcel: Parcel): ResponseMissionSharedCrew {
            return ResponseMissionSharedCrew(parcel)
        }

        override fun newArray(size: Int): Array<ResponseMissionSharedCrew?> {
            return arrayOfNulls(size)
        }
    }

}