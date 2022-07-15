package com.kesimilim.newsmap.model

import android.os.Parcel
import android.os.Parcelable
import com.vk.sdk.api.wall.dto.WallWallpostAttachment
import com.vk.sdk.api.wall.dto.WallWallpostFull
import org.json.JSONObject

data class VKWall(
    val postId: Int? = null,
    val text: String? = null,
    val attachments: List<WallWallpostAttachment>? = null,
    val copyHistory: List<WallWallpostFull>? = null,
)

//129580073
//) : Parcelable {
//
//    constructor(parcel: Parcel) : this(
//
//    )
//
//    override fun describeContents(): Int {
//    }
//
//    override fun writeToParcel(dest: Parcel?, flags: Int) {
//    }
//
//    companion object CREATOR : Parcelable.Creator<VKWall> {
//        override fun createFromParcel(parcel: Parcel): VKWall {
//            return VKWall(parcel)
//        }
//
//        override fun newArray(size: Int): Array<VKWall?> {
//            return arrayOfNulls(size)
//        }
//
//        fun parse(json: JSONObject) = VKWall()
//
////            VKUser(
////            id = json.optLong("id", 0),
////            firstName = json.optString("first_name", ""),
////            lastName = json.optString("last_name", ""),
////            photo = json.optString("photo_200", ""),
////            deactivated = json.optBoolean("deactivated", false))
//    }
//
//}
