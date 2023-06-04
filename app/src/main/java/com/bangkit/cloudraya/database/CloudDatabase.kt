package com.bangkit.cloudraya.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Sites::class], version = 2, exportSchema = false)
abstract class CloudDatabase:RoomDatabase() {
    abstract fun sitesDao() : sitesDao
}