package com.bryanadams.fetchnames.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bryanadams.fetchnames.R
import com.bryanadams.fetchnames.model.PickerData
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var adapter: ExpandablePickerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val model: MainActivityViewModel by viewModels()
        initRecyclerView()
        model.getPickerDataObjects().observe(this) {
            renderData(it)
        }
    }



    private fun renderData(pickerData: List<PickerData>) {
        adapter = ExpandablePickerAdapter(
            object : ExpandablePickerAdapter.RecyclerAdapterListener {
                override fun onClick(item: PickerData) {
//
                }

            })
        recyclerview.adapter = adapter
        adapter.setList(pickerData)
    }

    private fun initRecyclerView() {
        recyclerview.layoutManager = LinearLayoutManager(this)
    }
}