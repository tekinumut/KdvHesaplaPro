package com.Mtkn.umutt.kdvhesap.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.Mtkn.umutt.kdvhesap.R
import com.Mtkn.umutt.kdvhesap.viewModel.KayitlarimViewModel


class SettingsFR : PreferenceFragmentCompat() {

    private lateinit var switchDel: SwitchPreferenceCompat
    private lateinit var switchSave: SwitchPreferenceCompat
    private lateinit var kayitlarimViewModel: KayitlarimViewModel

    //Layout oluşturulduğund arka planı beyaz yap çünkü fragment mainActivity'nin üzerinde olmalı
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(ContextCompat.getColor(context!!.applicationContext, R.color.white))
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.settings_preference, rootKey)

        switchSave = findPreference(getString(R.string.switchSave_key))!!
        switchDel = findPreference(getString(R.string.switchDel_key))!!
        kayitlarimViewModel = ViewModelProvider(this).get(KayitlarimViewModel::class.java)

        switchSaveSummary(switchSave.isChecked)

        switchDel.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->

                if (newValue as Boolean) {
                    callDialogBuilder().show()
                }
                true
            }

        switchSave.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->

                switchSaveSummary(newValue as Boolean)

                true
            }

    }


    //Eğer kullanıcı tüm verileri silmek isterse ona bir dialog paneli göster
    //Daha sonra buttonu tekrar pasif hale getir.
    private fun callDialogBuilder(): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        builder.setTitle(getString(R.string.switchDel_title))
            .setMessage(getString(R.string.sure_about_del_all_records))

            .setPositiveButton(getString(R.string.evet)) { _, _ ->
                kayitlarimViewModel.deleteAllRecords()
                switchDel.isChecked = false
                Toast.makeText(context, getString(R.string.values_removed), Toast.LENGTH_SHORT)
                    .show()
            }.setNegativeButton(getString(R.string.hayir)) { dialogInterface, _ ->
                dialogInterface.dismiss()
                switchDel.isChecked = false //eğer hayır derse switch'i tekrar pasif yap
            }
        return builder.create()
    }

    private fun switchSaveSummary(newValue: Boolean) {
        if (newValue) {
            switchSave.summary = getString(R.string.switchSave_summary)
        } else {
            switchSave.summary = getString(R.string.switchSave_summary_off)

        }
    }
}