package com.bryanadams.fetchnames.network

import com.bryanadams.fetchnames.model.Name
import retrofit2.http.GET

interface NetworkInterface {

    @GET("/hiring.json")
    suspend fun getNames(): List<Name>
}