package com.Mtkn.umutt.kdvhesap.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.Mtkn.umutt.kdvhesap.R
import com.Mtkn.umutt.kdvhesap.abstracts.Helpers
import com.Mtkn.umutt.kdvhesap.model.KayitlarimModel
import com.Mtkn.umutt.kdvhesap.viewModel.KayitlarimViewModel
import kotlinx.android.synthetic.main.rec_in_kayitlarim.view.*


//Burada viewModel'a erişmek için fragment göndermek zorunda kaldım.
// Çok doğru bir yol değil, o yüzden çözüm bulunursa farklı bir yöntem denenmeli.
class KayitlarimAdapter(
    private val mContext: Context?,
    private val fragment: Fragment,
    private val recyclerList: List<KayitlarimModel>?

) : RecyclerView.Adapter<KayitlarimAdapter.ViewHolder>() {

    private lateinit var kayitlarimViewModel: KayitlarimViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(fragment.context).inflate(
                R.layout.rec_in_kayitlarim, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return recyclerList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: KayitlarimModel = recyclerList!![position]

        kayitlarimViewModel = ViewModelProvider(fragment).get(KayitlarimViewModel::class.java)

        holder.kdvType.text = model.kdvType

        //toplamTutarText değerini KDV Dahil veya Hariç olma durumuna göre değiştir
        if (model.kdvType == fragment.context?.getString(R.string.kdv_dahil)) {
            holder.toplamTutarText.text = fragment.context?.getString(R.string.kdv_haric_text)
        } else if (model.kdvType == fragment.context?.getString(R.string.kdv_haric)) {
            holder.toplamTutarText.text = fragment.context?.getString(R.string.kdv_dahil_text)
        }

        holder.tutarSonuc.text = model.tutar
        holder.kdvTutarSonuc.text = model.kdvTutarSonuc
        holder.toplamTutarSonuc.text = model.toplamTutarSonuc
        holder.eklenmeTarihiSonuc.text = model.eklenmeTarihi

        holder.kdvTutarText.text =
            mContext?.getString(R.string.rec_in_kdv_tutari_text, model.kdv_oran)

        holder.imageView.setOnClickListener {

            if (Helpers.getPref(mContext!!).getBoolean("myRecordRemoveDialog", true)) {
                callDialogBuilder(model).show()
            } else {
                delRecord(model)
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val kdvType: TextView = view.rec_in_kdvType
        val tutarSonuc: TextView = view.rec_in_tutarSonuc
        val kdvTutarSonuc: TextView = view.rec_in_kdvTutarSonuc
        val toplamTutarSonuc: TextView = view.rec_in_toplamTutarSonuc
        val eklenmeTarihiSonuc: TextView = view.rec_in_eklenmeTarihiSonuc
        val toplamTutarText: TextView =
            view.rec_in_toplamTutar //kdvType'a göre Text değeri değişecek
        val kdvTutarText: TextView = view.rec_in_kdvTutar //KDV oranına göre textView değişecek

        val imageView: ImageView = view.rec_in_delete_image

    }

    private fun callDialogBuilder(model: KayitlarimModel): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(mContext)

        builder.setTitle(mContext?.getString(R.string.switchDel_title))
            .setMessage(mContext?.getString(R.string.sure_del_record))
            .setPositiveButton(mContext?.getString(R.string.evet)) { _, _ ->
                delRecord(model)
            }.setNegativeButton(mContext?.getString(R.string.hayir)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }.setNeutralButton("Bir Daha Sorma") { _, _ ->
                Helpers.getPref(mContext!!).edit().putBoolean("myRecordRemoveDialog", false).apply()
                delRecord(model)
            }

        return builder.create()
    }

    private fun delRecord(model: KayitlarimModel) {
        kayitlarimViewModel.deleteRecord(model)
        Toast.makeText(
            mContext, mContext?.getString(R.string.value_removed), Toast.LENGTH_SHORT
        ).show()
    }

}