package ru.hamlet.lolquiz.listlolitems

import retrofit2.http.GET
import ru.hamlet.lolquiz.LolItem


interface LolItemsAPI {
    @GET("/items")
    suspend fun getItems() : List<LolItem>


}