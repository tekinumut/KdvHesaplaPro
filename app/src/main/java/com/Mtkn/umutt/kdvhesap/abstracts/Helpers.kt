package com.Mtkn.umutt.kdvhesap.abstracts

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.preference.PreferenceManager
import com.Mtkn.umutt.kdvhesap.R
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

abstract class Helpers {

    companion object {

        //123456789.12345 gibi bir veriyi 13,456,789.12 gibi bir veriye çevirir
        //Virgülden sonra kaç 0 geleceğini ayarlardan çekiyoruz
        fun numberFormat(mContext: Context?, number: Double?): String? {
            val pref: String? = PreferenceManager.getDefaultSharedPreferences(mContext)
                .getString(mContext?.getString(R.string.listOndalik_key), ".##")
            val formatter = DecimalFormat("#,###$pref")

            formatter.roundingMode =
                RoundingMode.HALF_EVEN //HALF_EVEN yuvarlar 12.446 = 12.45 ---- 14.444 = 12.44
            return formatter.format(BigDecimal.valueOf(number!!))
        }

        //Telefonun anlık zamanını al. Bunu her işlem yapıldığında veritabanına kayıt edeceğiz
        fun currentTime(): String {

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("HH:mm:ss / dd-MM-yyyy")
                current.format(formatter)
            } else {
                val date = Date()
                val formatter = SimpleDateFormat("HH:mm:ss / dd-MM-yyyy", Locale.getDefault())
                formatter.format(date)
            }
        }

        //Klavye açıksa onu gizle
        fun hideKeyboard(mContext: Context, view: View) {
            val inputMethodManager =
                mContext.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        //Text'in sonuna TL ibaresini ekle. Örn: 12 ise 12 TL olacak
        fun addTextTL(mContext: Context?, text: String?): String? {
            return mContext?.getString(R.string.sonucText, text)
        }

        fun getPref(context: Context): SharedPreferences {
            return context.getSharedPreferences("defPref", MODE_PRIVATE)
        }

    }

}