package com.bryanadams.fetchnames.network

import com.bryanadams.fetchnames.model.Name
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NameRetriever {

    private val networkInterface: NetworkInterface

    companion object {
        const val BaseURL = "https://fetch-hiring.s3.amazonaws.com"
    }

    init {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        networkInterface = retrofit.create(NetworkInterface::class.java)
    }

    suspend fun getNames(): List<Name> {
        return networkInterface.getNames()
    }
}