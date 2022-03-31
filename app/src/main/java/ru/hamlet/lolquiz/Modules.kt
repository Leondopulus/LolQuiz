package ru.hamlet.lolquiz

import android.content.Context
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.hamlet.lolquiz.listlolitems.*
import java.util.concurrent.TimeUnit

//тут регистрируем зависимости
val repositoryModule = module {
    single { LolItemsRepository(get(named("Smart")) ) }  //get это магическая ф только у koin, находит нужное по типу
    single { createRetrofit() }
    single { createLolItemsAPI(get()) }
    single<LolItemsService>(named("Retrofit")) { RetrofitLolItemService(get()) }
    single<LolItemsService>(named("Cache")) { CacheLolItemService(get()) }
    single<LolItemsService>(named("Stream")) { StreamLolItemService() }
    single<LolItemsService>(named("Smart")) { SmartLolItemService(get(), get()) }
    }

val uiModule = module {
    viewModel { ListViewModel(get()) }
}

fun createRetrofit() : Retrofit{
    val httpClient = OkHttpClient.Builder()
        .readTimeout(120, TimeUnit.SECONDS)
        .connectTimeout(120, TimeUnit.SECONDS)
        .build()



    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("http://c93c-5-166-192-37.ngrok.io")
        .client(httpClient)
        .build()

    return retrofit
}

fun createLolItemsAPI(retrofit :Retrofit): LolItemsAPI{
    return retrofit.create(LolItemsAPI::class.java)
}



//переменные глобальные

