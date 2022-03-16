package ru.hamlet.lolquiz

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.hamlet.lolquiz.listlolitems.ListViewModel
import ru.hamlet.lolquiz.listlolitems.LolItemsRepository

//тут регистрируем зависимости
val repositoryModule = module {
    single { LolItemsRepository(get()) } //get это магическая ф только у koin, находит нужное по типу
}

val uiModule = module {
    viewModel { ListViewModel(get()) }
}

//переменные глобальные

