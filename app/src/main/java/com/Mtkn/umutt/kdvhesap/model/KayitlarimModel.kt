package com.Mtkn.umutt.kdvhesap.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Kayitlarim")
data class KayitlarimModel(
    var kdvType: String,
    var tutar: String,
    var kdv_oran: String,
    var kdvTutarSonuc: String,
    var toplamTutarSonuc: String,
    var eklenmeTarihi: String) {


    @PrimaryKey(autoGenerate = true)
    var recordID: Int = 0

}
