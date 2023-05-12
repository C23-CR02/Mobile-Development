package com.bangkit.cloudraya.database

import androidx.room.*

@Dao
interface sitesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSites(sites: Sites)

    @Query("SELECT * FROM sites")
    fun getAllSites() : List<Sites>

    @Query("DELETE FROM sites")
    suspend fun deleteAll()

}