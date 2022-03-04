package com.bryanadams.fetchnames.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bryanadams.fetchnames.R
import com.bryanadams.fetchnames.model.LineItemObject
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val model: MainActivityViewModel by viewModels()
        initRecyclerView()
        model.getLineItemObjects().observe(this) {
            renderData(it)
        }
    }



    private fun renderData(lineItemObjects: List<LineItemObject>) {
        recyclerview.adapter = NamesAdapter(lineItemObjects)
    }

    private fun initRecyclerView() {
        recyclerview.layoutManager = LinearLayoutManager(this)
    }
}