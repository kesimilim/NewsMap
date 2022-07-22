package com.kesimilim.newsmap.repository.friendDataSource.remote

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.kesimilim.newsmap.database.entity.RoomFriend
import com.kesimilim.newsmap.model.GeoPoint
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.sdk.api.friends.FriendsService
import com.vk.sdk.api.friends.dto.FriendsGetFieldsResponse
import com.vk.sdk.api.users.dto.UsersFields
import com.vk.sdk.api.users.dto.UsersUserFull
import java.io.IOException
import kotlin.coroutines.suspendCoroutine

class FriendRemoteDataSourceImpl(private val context: Context): FriendRemoteDataSource {
    override suspend fun getFriendList(): List<RoomFriend> {
        val request = FriendsService().friendsGet(fields = listOf(
            UsersFields.PHOTO_200,
            UsersFields.CITY,
            UsersFields.HOME_TOWN,
            UsersFields.COUNTRY
        ))

        return suspendCoroutine { list ->
            VK.execute(
                request,
                object: VKApiCallback<FriendsGetFieldsResponse> {
                    override fun fail(error: Exception) {
                        list.resumeWith(Result.failure(error))
                    }
                    override fun success(result: FriendsGetFieldsResponse) {
                        val friendList = arrayListOf<RoomFriend>()
                        val friends = result.items

                        if (friends.isNotEmpty()) {
                            friends.map { friend ->
                                val item = getRoomFriend(friend)
                                if (item != null) friendList.add(item)
                            }
                            list.resumeWith(Result.success(friendList))
                        }
                    }
                }
            )
        }
    }

    private fun getRoomFriend(friend: UsersUserFull): RoomFriend? {
        val city = city(friend)
        if (city != null) {
            val geo = getLocationFromAddress(city)
            if (geo != null) {
                var latitude = geo.latitude
                var longitude = geo.longitude

                if (city == "Uren") {
                    latitude = 57.455218599999995
                    longitude = 45.7910542
                }

                if (latitude != 0.0 && latitude != null) {
                    return RoomFriend(
                        id = 0,
                        userId = friend.id.value,
                        firstName = friend.firstName ?: "",
                        lastName = friend.lastName ?: "",
                        photo = friend.photo200 ?: "",
                        city = RoomFriend.RoomCity(city, latitude, longitude),
                        vkUserId = friend.id
                    )
                }
            }
        }
        return null
    }

    private fun city(friend: UsersUserFull): String? {
        return if (friend.city != null) {
            friend.city?.title
        } else if (friend.homeTown != null) {
            friend.homeTown
        } else if (friend.country != null) {
            friend.country?.title
        } else {
            null
        }
    }

    private fun getLocationFromAddress(strAddress: String): GeoPoint? {
        if (strAddress != "") {
            val coder = Geocoder(context)
            val address: List<Address>
            try {
                address = coder.getFromLocationName(strAddress, 5)
                if (address == null || address.isEmpty()) {
                    return null
                }
                val location: Address = address[0]
                return GeoPoint(
                    location.latitude,
                    location.longitude
                )
            } catch (e: IOException) {
                return null
            }
        }
        return null
    }
}