package com.Mtkn.umutt.kdvhesap.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.Mtkn.umutt.kdvhesap.model.KayitlarimModel
import com.Mtkn.umutt.kdvhesap.repositories.KayitlarimRepository

class KayitlarimViewModel(application: Application) : AndroidViewModel(application) {

   private var repository: KayitlarimRepository = KayitlarimRepository(application)

   private var allRecords: LiveData<List<KayitlarimModel>> = repository.getAllRecords()

   fun insert(model: KayitlarimModel) {
      repository.insert(model)
   }

   fun deleteRecord(model: KayitlarimModel) {
      repository.delete(model)
   }

   fun deleteAllRecords() {
      repository.deleteAll()
   }

   fun getAllRecords(): LiveData<List<KayitlarimModel>> {
      return allRecords
   }
}