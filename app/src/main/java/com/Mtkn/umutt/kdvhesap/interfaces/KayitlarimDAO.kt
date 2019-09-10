package com.Mtkn.umutt.kdvhesap.interfaces

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.Mtkn.umutt.kdvhesap.model.KayitlarimModel

@Dao
interface KayitlarimDAO {

   @Insert
   fun addRecords(values: KayitlarimModel)

   @Query("Select * from Kayitlarim ORDER BY eklenmeTarihi DESC")
   fun getAllRecords(): LiveData<List<KayitlarimModel>>

   @Delete
   fun delRecord(value: KayitlarimModel)

   @Query("Delete from Kayitlarim")
   fun delAllRecords()

}