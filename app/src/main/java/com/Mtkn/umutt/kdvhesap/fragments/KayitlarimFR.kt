package com.Mtkn.umutt.kdvhesap.fragments

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.Mtkn.umutt.kdvhesap.R
import com.Mtkn.umutt.kdvhesap.adapter.KayitlarimAdapter
import com.Mtkn.umutt.kdvhesap.model.KayitlarimModel
import com.Mtkn.umutt.kdvhesap.viewModel.KayitlarimViewModel


class KayitlarimFR : Fragment() {

    private lateinit var rootView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var kayitlarimViewModel: KayitlarimViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)

        rootView = inflater.inflate(R.layout.fr_kayitlarim, container, false)

        init()


        kayitlarimViewModel = ViewModelProvider(this).get(KayitlarimViewModel::class.java)

        kayitlarimViewModel.getAllRecords().observe(viewLifecycleOwner, Observer<List<KayitlarimModel>> { model ->
            recyclerView.adapter = KayitlarimAdapter(context, this, model)
        })

        return rootView
    }

    private fun init() {

        recyclerView = rootView.findViewById(R.id.rec_kayitlarim)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context, DividerItemDecoration.VERTICAL
            )
        )

    }

}