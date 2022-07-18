package com.kesimilim.newsmap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kesimilim.newsmap.database.entity.RoomCopyHistory

@Dao
interface CopyHistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCopyHistory(copyHistory: RoomCopyHistory)

//    @Query("SELECT * FROM cory_history_table WHERE copy_history_post_id = :postId")
//    fun getCopyHistory(postId: Int): List<RoomCopyHistory>
}