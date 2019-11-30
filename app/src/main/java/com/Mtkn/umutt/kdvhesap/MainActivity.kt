package com.Mtkn.umutt.kdvhesap

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.Mtkn.umutt.kdvhesap.abstracts.Calculates
import com.Mtkn.umutt.kdvhesap.abstracts.Helpers
import com.Mtkn.umutt.kdvhesap.fragments.HakkindaFR
import com.Mtkn.umutt.kdvhesap.fragments.KayitlarimFR
import com.Mtkn.umutt.kdvhesap.fragments.SettingsFR
import com.Mtkn.umutt.kdvhesap.model.KayitlarimModel
import com.Mtkn.umutt.kdvhesap.viewModel.KayitlarimViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    //  private lateinit var mInterstitialAd: InterstitialAd //Geçiş reklamı
    private lateinit var currentRadioButton: RadioButton //Seçili olan radio button
    private lateinit var kayitlarimViewModel: KayitlarimViewModel //MVVM :)
    private var x1: Float = 0.0f    //ekranı sağa sola çekerken kullanacağız

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //başta null dönmesin diye seçili olan radioButton KDV Hariç butonu olacak
        currentRadioButton = findViewById(R.id.radioHaric)
        //Veritabanına veri eklemek için ViewModel'a erişeceğiz
        kayitlarimViewModel = ViewModelProvider(this).get(KayitlarimViewModel::class.java)


        MobileAds.initialize(this, getString(R.string.ad_publish_id)) //reklamı init ediyoruz

        //Banner reklam yükle
        adViewBanner.loadAd(AdRequest.Builder().build())

        /*    mInterstitialAd = InterstitialAd(this)
            mInterstitialAd.adUnitId = getString(R.string.initial_ad_unit_id)
            // mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712" //test api
            mInterstitialAd.loadAd(AdRequest.Builder().build())
    */
        //Butonlara tıkladığımda ele alınacak işlemler
        //Buton'un textini alarak hangi butona tıkladığımızı anlayacağız
        //Mesela %1 butonuna tıkladık bu texti('%1') startCalculate metoduna gönderiyoruz.
        button1.setOnClickListener { v ->
            onButtonClick(v)
        }
        button8.setOnClickListener { v ->
            onButtonClick(v)
        }
        button18.setOnClickListener { v ->
            onButtonClick(v)
        }
        buttonDiger.setOnClickListener { v ->
            onButtonClick(v)
        }

        //edittext2'e her tıkladığımda eğer içeriğinde % var ise onu sil
        editTextOran.setOnFocusChangeListener { _, b ->
            if (b) {
                editTextOran.setText(delPercent(editTextOran.text.toString()))
            }
        }

        //mevcut radio button değiştiğinde ona göre otomatik olarak işlem gerçekleştir.
        radioGroup.setOnCheckedChangeListener { _, buttonID ->

            //Seçili radioButton'u currentRadioButton değişkenine atıyoruz
            currentRadioButton = findViewById(buttonID)

            //Seçili radio Button'a göre textview'ın değeri değişecek
            if (currentRadioButton.text.toString() == getString(R.string.kdv_dahil)) {
                toplamTutarText.text = getString(R.string.kdv_haric_text)
            } else if (currentRadioButton.text.toString() == getString(R.string.kdv_haric)) {
                toplamTutarText.text = getString(R.string.kdv_dahil_text)
            }
            if (textsNotEmpty()) {
                //edittext1 ve edittext2 dolu ise sanki diğer butonuna basmış gibi işlem yaptırıyoruz.
                //Böylece radio button değiştiğinde kullanıcı tekrar hesap yapmak zorunda kalmaz.
                startCalculate(getString(R.string.yuzdeDiger), false)
            }
        }

        //Klavyede edittext2'de iken klavyede DONE tuşunu tıklarsa aşağıdaki işlemleri yap
        //hesaplama işlemlerini gerçekleştir
        editTextOran.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                startCalculate(getString(R.string.yuzdeDiger), true)
                Helpers.hideKeyboard(this, v)

                return@setOnEditorActionListener true
            }
            false
        }

        //ekran döndüğünde verileri tekrar yükle
        if (savedInstanceState != null) {

            radioGroup.check(savedInstanceState.getInt("radiobutton"))

            editTextTutar.setText(savedInstanceState.getString("tutar"))
            editTextOran.setText(savedInstanceState.getString("oran"))
            tutarSonuc.text = savedInstanceState.getString("tutarsonuc")
            kdvTutarSonuc.text = savedInstanceState.getString("kdvtutari")
            toplamTutarSonuc.text = savedInstanceState.getString("toplamtutar")
            toplamTutarText.text = savedInstanceState.getString("toplamTutarText")

            //Eğer edittext 1 ve edittext2'de veri varsa tekrar hesap yap. //Ki gerek yok zaten tüm verileri çektik
            if (textsNotEmpty()) {
                startCalculate(getString(R.string.yuzdeDiger), false)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("tutar", editTextTutar.text.toString())
        outState.putString("oran", editTextOran.text.toString())
        outState.putString("tutarsonuc", tutarSonuc.text.toString())
        outState.putString("kdvtutari", kdvTutarSonuc.text.toString())
        outState.putString("toplamtutar", toplamTutarSonuc.text.toString())
        outState.putString("toplamTutarText", toplamTutarText.text.toString())
        outState.putInt("radiobutton", currentRadioButton.id)

    }

    //Hesaplamaya başladığımız metod
    private fun startCalculate(text: String?, dbEkle: Boolean) {

        //Eğer edittext1 boş ise hata ver ve işlemden çık
        if (editTextTutar.text.isNullOrEmpty()) {
            textInputTutar.error = getString(R.string.empty_error)

        } else {//edittext1 dolu ise devam et.

            if (text == getString(R.string.yuzdeDiger)) { //eğer diğer butonuna tıkladıysam

                if (editTextOran.text.isNullOrEmpty()) { // edittext 2 boş ise hata ver ve işlemden çık. Çünkü kdv oranı lazım
                    textInputOran.error = getString(R.string.empty_error)
                } else {//eğer diğer butonuna tıkladığımda bir sorun yoksa

                    //edittext2'e tıkladığımda %'ı sileceğim için onu tekrar eklemem gerek. Görsel olarak hoş gözükmesi için
                    val builder = StringBuilder() //append kullanmalıyım o yüzden stringbuilder
                    if (!editTextOran.text.toString().contains("[%]".toRegex())) { // eğer ben butona tıkladığımda edittext2'de % yok ise
                        builder.append("%")
                            .append(editTextOran.text.toString()) // mevcut kdv oranının başına % ekle
                        calculate(
                            toKdvOran(builder.toString()),
                            dbEkle
                        )         // sonra onu calculate metoduna gönderip hesaplat
                    } else {
                        calculate(
                            toKdvOran(editTextOran.text.toString()),
                            dbEkle
                        ) //eğer içeriğimde % varsa zaten sorun yok direk hesapla
                    }
                }
                //%1, %8 veya %18 butonlarından birine bastıysam
            } else {
                calculate(
                    toKdvOran(text),
                    dbEkle
                ) //butondan aldığında text değerine göre kdv oranı belirleyip işlem yap.

            }
        }

        if (!editTextTutar.text.isNullOrEmpty()) {
            textInputTutar.error = null
        }
        if (!editTextOran.text.isNullOrEmpty()) {
            textInputOran.error = null
        }
    }

    private fun toKdvOran(textOfButton: String?): Double? {
        //Butona tıkladığımda onu edittext'e yaz
        editTextOran.setText(textOfButton)
        //Eğer text içerisinde % var ise onları da sil. Çünkü hesap yapılacak.
        return delPercent(textOfButton)?.toDouble()
    }

    //Eğer gelen string'de % var ise onları yok et.
    private fun delPercent(text: String?): String? {
        return text?.replace("[%]".toRegex(), "")
    }

    //Değerler alındıktan sonra son aşama işlem aşaması
    //Sonuçlar bulunduktan sonra texte yazdırılıyorlar
    private fun calculate(oran: Double?, dbEkle: Boolean) {
        //Hangi radio buton aktifse ona göre işlem yapacağız.
        val mAvaibleRadio = findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
        val tutarAsString: String = editTextTutar.text.toString() //tutarımız zaten biliniyor.
        var toplamTutar: Double? = null //kdv hesabı yapıldıktan sonra çıkacak sonuç
        var kdvOran: Double? = null     //tutarın kdv oranının değeri

        //eğer mevcut radio butonu dahil'de ise
        //toplamtutar ve kdvOran değerlerini buluyoruz.
        if (mAvaibleRadio.text == getString(R.string.kdv_dahil)) {
            toplamTutar = Calculates.totalTutarDahil(tutarAsString.toDouble(), oran)
            kdvOran = Calculates.kdvTutarDahil(toplamTutar, oran)

        } else if (mAvaibleRadio.text == getString(R.string.kdv_haric)) {
            toplamTutar = Calculates.totalTutarHaric(tutarAsString.toDouble(), oran)
            kdvOran = Calculates.kdvTutarHaric(tutarAsString.toDouble(), oran)
        }

        tutarSonuc.text = Helpers.addTextTL(
            applicationContext,
            Helpers.numberFormat(this, tutarAsString.toDouble())
        )
        kdvTutarSonuc.text =
            Helpers.addTextTL(applicationContext, Helpers.numberFormat(this, kdvOran))
        toplamTutarSonuc.text =
            Helpers.addTextTL(applicationContext, Helpers.numberFormat(this, toplamTutar))

        //Ayarlar menüsünde kullanıcının yaptığı seçime göre database kaydını aç veya kapat
        val dbEkleSettings: Boolean = PreferenceManager.getDefaultSharedPreferences(this)
            .getBoolean(getString(R.string.switchSave_key), true)

        //Eğer dbEkle true ise yapılan işlemleri kayıtlarım sekmesine ekle
        //Ekran döndüğünde ve radioButon'a tıklandığında veri eklenmeyecek
        //Kullanıcı ayarlar menüsünden de kaydı aktif etmeli
        if (dbEkle && dbEkleSettings) {

            val model = KayitlarimModel(
                currentRadioButton.text.toString(),
                tutarSonuc.text.toString(),
                editTextOran.text.toString(),
                kdvTutarSonuc.text.toString(),
                toplamTutarSonuc.text.toString(),
                Helpers.currentTime()
            )

            kayitlarimViewModel.insert(model)
        }
    }

    //Menüyü oluştur
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Menü butonlarının click işlemleri
    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when (item.itemId) {
            R.id.menu_kayitlarim -> {

                //     if (mInterstitialAd.isLoaded)
                //         mInterstitialAd.show()

                openFragment(KayitlarimFR())
            }

            R.id.menu_ayarlar -> {
                openFragment(SettingsFR())
            }
            R.id.menu_hakkinda -> {
                openFragment(HakkindaFR())
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun openFragment(fragment: Fragment) {

        val currentFR: Fragment? = supportFragmentManager.findFragmentById(R.id.main_container)

        //Fragment'ın birden fazla açılmasını engeller
        if (currentFR?.javaClass != fragment.javaClass) {
            supportFragmentManager.beginTransaction().replace(R.id.main_container, fragment)
                .addToBackStack("mainActivity").commit()
        }
    }

    //Ekranı sağa ve sola çektiğimizde harekete geçen sınıf. Hazır kod
    //Her harekette radioButton'un ChangeListener metodunu çalıştırıyoruz. Böylece tekrar hesap yapıyor
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = event.x
            }
            MotionEvent.ACTION_UP -> {
                val x2 = event.x
                val deltaX = x2 - x1
                if (abs(deltaX) > 150) {
                    // Left to Right swipe action
                    if (x2 > x1) {
                        radioHaric.performClick()
                    } else {
                        radioDahil.performClick()
                    }// Right to left swipe action
                }
            }
        }
        super.dispatchTouchEvent(event)
        return true
    }

    //Eğer edittext1 ve edittext2 boş değilse
    private fun textsNotEmpty(): Boolean {
        return !editTextTutar.text.isNullOrEmpty() && !editTextOran.text.isNullOrEmpty()
    }

    //%1,%8,%18 ve Diğer butonlarına bastığımda yapılacak işlemler
    private fun onButtonClick(v: View) {
        editTextTutar.clearFocus() //Her butona tıkladığımızda text'lerdeki odaklanmayı kaldırıyoruz
        editTextOran.clearFocus()
        Helpers.hideKeyboard(applicationContext, v)  //Her butona tıkladığımda klavyeyi gizle.

        startCalculate((v as Button).text.toString(), true) //işlemler başlıyor.
    }

    //Gruptaki her butona tek tek ulaşmak için bu metodu kullanıyoruz.
    //Butonları gruba aldığımızda fragment açılışlarında butonlar bir bugdan dolayı ekranda kalıyor.
    //Bu hatadan dolayı butonlara grup yöntemi ile erişme işlemi iptal edildi
    /*   private fun Group.setAllOnClickListener(listener: View.OnClickListener?) {
           referencedIds.forEach { id ->
               rootView.findViewById<View>(id).setOnClickListener(listener)
           }
       }
   */

    public override fun onResume() {
        super.onResume()
        // Resume the AdView.
        adViewBanner.resume()
    }

    public override fun onPause() {
        // Pause the AdView.
        adViewBanner.pause()
        super.onPause()
    }

    public override fun onDestroy() {
        // Destroy the AdView.
        adViewBanner.destroy()
        super.onDestroy()
    }


}
