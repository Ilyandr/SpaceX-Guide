@file:Suppress("PackageName")
package gcu.production.guidespacex.Data.RestEntity

import android.os.Parcel
import android.os.Parcelable

internal data class PeopleData(
    val fullName: String? = null
    , val agency: String? = null
    , val status: String? = null
): Parcelable
{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fullName)
        parcel.writeString(agency)
        parcel.writeString(status)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<PeopleData> {
        override fun createFromParcel(parcel: Parcel): PeopleData {
            return PeopleData(parcel)
        }

        override fun newArray(size: Int): Array<PeopleData?> {
            return arrayOfNulls(size)
        }
    }
}