package com.bryanadams.fetchnames.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bryanadams.fetchnames.model.LineItemObject
import com.bryanadams.fetchnames.model.ListItem
import com.bryanadams.fetchnames.network.NameRetriever
import kotlinx.coroutines.*

class MainActivityViewModel : ViewModel() {
    private val nameRetriever: NameRetriever = NameRetriever()
    private val lineItemObjects: MutableLiveData<List<LineItemObject>> by lazy {
        MutableLiveData<List<LineItemObject>>().also {
            loadNames()
        }
    }

    fun getLineItemObjects(): LiveData<List<LineItemObject>> {
        return lineItemObjects
    }

    private fun loadNames() {
        val namesFetchJob = Job()
        val scope = CoroutineScope(namesFetchJob + Dispatchers.Main)

        scope.launch {
            val nameResponse = nameRetriever.getNames().sortedBy { it.listId }
                .filter { it.name != "" && it.name != null }
            val groupedNames = nameResponse.groupBy { it.listId }
            val presentedList: MutableList<LineItemObject> = mutableListOf()
            groupedNames.forEach { (key, value) ->
                key?.let { ListItem(it) }?.let { presentedList.add(it) }
                presentedList.addAll(value.sortedBy { it.name })
                lineItemObjects.postValue(presentedList)
            }

        }
    }
}