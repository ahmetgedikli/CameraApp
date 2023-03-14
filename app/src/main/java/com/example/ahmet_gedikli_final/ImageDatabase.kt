package com.example.ahmet_gedikli_final

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private const val DATABASE = "images"

@Database(
    entities = [Image::class],
    version = 1
)
abstract class ImageDatabase : RoomDatabase() {

    abstract fun ImageDao(): UserDao

    companion object {

        // For Singleton instantiation
        @Volatile
        private var INSTANCE: ImageDatabase? = null

        fun getInstance(context: Context): ImageDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?:Room.databaseBuilder(
                    context.applicationContext,
                    ImageDatabase::class.java,
                    "image_database"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                    .also { INSTANCE = it }
            }
        }

    }
}