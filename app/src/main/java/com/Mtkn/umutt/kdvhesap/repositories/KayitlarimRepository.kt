package com.Mtkn.umutt.kdvhesap.repositories

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.Mtkn.umutt.kdvhesap.database.KayitlarimDB
import com.Mtkn.umutt.kdvhesap.interfaces.KayitlarimDAO
import com.Mtkn.umutt.kdvhesap.model.KayitlarimModel


//Room Database işlemleri için
class KayitlarimRepository(application: Application) {

    private var kayitlarimDAO: KayitlarimDAO

    private var allRecords: LiveData<List<KayitlarimModel>>

    init {
        val database: KayitlarimDB = KayitlarimDB.getInstance(application.applicationContext)!!
        kayitlarimDAO = database.myDao()
        allRecords = kayitlarimDAO.getAllRecords()
    }

    fun insert(model: KayitlarimModel) {
        InsertRecordsAsync(kayitlarimDAO).execute(model)
    }

    fun delete(model: KayitlarimModel) {
        DeleteRecordsAsync(kayitlarimDAO).execute(model)
    }

    fun deleteAll() {
        DeleteAllRecordsAsync(kayitlarimDAO).execute()
    }

    fun getAllRecords(): LiveData<List<KayitlarimModel>> {
        return allRecords
    }


    private class InsertRecordsAsync(val myDao: KayitlarimDAO) :
        AsyncTask<KayitlarimModel, Unit, Unit>() {

        override fun doInBackground(vararg p0: KayitlarimModel?) {
            myDao.addRecords(p0[0]!!)
        }
    }

    private class DeleteRecordsAsync(val myDao: KayitlarimDAO) :
        AsyncTask<KayitlarimModel, Unit, Unit>() {

        override fun doInBackground(vararg p0: KayitlarimModel?) {
            myDao.delRecord(p0[0]!!)
        }
    }

    private class DeleteAllRecordsAsync(val myDao: KayitlarimDAO) : AsyncTask<Unit, Unit, Unit>() {
        override fun doInBackground(vararg p0: Unit?) {
            myDao.delAllRecords()
        }
    }


}