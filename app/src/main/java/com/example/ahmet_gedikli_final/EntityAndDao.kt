package com.example.ahmet_gedikli_final

import androidx.room.*


    @Entity(tableName = "images")
    data class Image(
        @ColumnInfo(name = "tag") val tag: String,

        @PrimaryKey
        @ColumnInfo(name = "id" ) val id: ByteArray
    )

    @Dao
    interface UserDao{
        @Query("SELECT id FROM images where tag = :searchId")
        fun getImageIdByTag(searchId: String): ByteArray

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun addImage(image: Image)

    }



