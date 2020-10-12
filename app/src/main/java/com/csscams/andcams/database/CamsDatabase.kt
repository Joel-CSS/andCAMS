package com.csscams.andcams.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PayPeriod::class],version=1)
abstract class CamsDatabase : RoomDatabase(){
    //TODO: abstract val camsDAO : CamsDAO

    companion object{
        @Volatile
        private var INSTANCE: CamsDatabase? = null
        fun getInstance(context: Context): CamsDatabase{
            synchronized(this){
                var instance: CamsDatabase? = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CamsDatabase::class.java,
                        "cams_database"
                    ).build()
                }
                return instance
            }

        }
    }
}

