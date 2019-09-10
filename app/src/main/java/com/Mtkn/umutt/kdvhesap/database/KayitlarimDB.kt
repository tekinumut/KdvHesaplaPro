package com.Mtkn.umutt.kdvhesap.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.Mtkn.umutt.kdvhesap.R
import com.Mtkn.umutt.kdvhesap.interfaces.KayitlarimDAO
import com.Mtkn.umutt.kdvhesap.model.KayitlarimModel

@Database(entities = [KayitlarimModel::class], version = 1)

abstract class KayitlarimDB : RoomDatabase() {

   abstract fun myDao(): KayitlarimDAO

   companion object {
      private var instance: KayitlarimDB? = null

      fun getInstance(mContext: Context?): KayitlarimDB? {
         if (instance == null) {
            synchronized(KayitlarimDB::class) {
               instance = Room.databaseBuilder(
                  mContext!!.applicationContext,
                  KayitlarimDB::class.java,
                  mContext.getString(R.string.kayitlarimDB)
               ).fallbackToDestructiveMigration()
                  // .addCallback(roomCallback)
                  .build()
            }
         }
         return instance
      }
   }
}