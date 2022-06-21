package com.kesimilim.newsmap.model

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject

data class VKUser(
    val id: Long = 0,
    val firstName: String = "",
    val lastName: String = "",
    val photo: String = "",
    val city: String = "",
    val country: String = "",
    val deactivated: Boolean = false) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(photo)
        parcel.writeString(city)
        parcel.writeString(country)
        parcel.writeByte(if (deactivated) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VKUser> {
        override fun createFromParcel(parcel: Parcel): VKUser {
            return VKUser(parcel)
        }

        override fun newArray(size: Int): Array<VKUser?> {
            return arrayOfNulls(size)
        }

        fun parse(json: JSONObject) = VKUser(
            id = json.optLong("id", 0),
            firstName = json.optString("first_name", ""),
            lastName = json.optString("last_name", ""),
            photo = json.optString("photo_200", ""),
            city = json.optString("city", ""),
            country = json.optString("country", ""),
            deactivated = json.optBoolean("deactivated", false)
        )
    }
}