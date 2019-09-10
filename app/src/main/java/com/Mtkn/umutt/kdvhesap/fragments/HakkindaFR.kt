package com.Mtkn.umutt.kdvhesap.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.Mtkn.umutt.kdvhesap.R
import kotlinx.android.synthetic.main.fr_hakkinda.*
import kotlinx.android.synthetic.main.fr_hakkinda.view.*

class HakkindaFR : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val rootView: View = inflater.inflate(R.layout.fr_hakkinda, container, false)

        //Textview'ı genişletme ve daraltma işlemlerinin yapıldığı metot
        rootView.click_to_expand.setOnClickListener {

            if (rootView.kullanim_kilavuzu_text.maxLines == getString(R.string.maxLinesLess).toInt()) {
                onLess()

            } else if (kullanim_kilavuzu_text.maxLines == Integer.MAX_VALUE) {
                onExpand()
            }
        }


        //Geri bildirim gönder. ( Mail yolu ile )
        rootView.send_feedback.setOnClickListener {

            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.myGmail)))
            startActivity(Intent.createChooser(intent, "Mail gönder"))

        }

        //Uygulamayı değerlendirme sayfasına git
        rootView.rate_app.setOnClickListener {

            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context?.packageName)
                    )
                )
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + context?.packageName)
                    )
                )
            }

        }

        //Github'da kodları görüntüle
        rootView.github_source_code.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse(getString(R.string.github_address))
                )
            )
        }

        return rootView
    }

    //Eğer Genişletme butonuna tıkladıysam
    private fun onLess() {
        click_to_expand.text = getString(R.string.expand_less)
        click_to_expand.setCompoundDrawablesRelativeWithIntrinsicBounds(
            R.drawable.ic_expand_less, 0, 0, 0
        )
        kullanim_kilavuzu_text.maxLines = Integer.MAX_VALUE
    }

    //Eğer Daraltma butonuna tıkladıysam
    private fun onExpand() {
        click_to_expand.text = getString(R.string.expand_more)
        click_to_expand.setCompoundDrawablesRelativeWithIntrinsicBounds(
            R.drawable.ic_expand_more, 0, 0, 0
        )
        kullanim_kilavuzu_text.maxLines = getString(R.string.maxLinesLess).toInt()
    }
}