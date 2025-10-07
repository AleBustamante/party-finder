package com.teamadn.partyfinder.di

import com.teamadn.partyfinder.features.party.data.database.AppRoomDatabase
import com.teamadn.partyfinder.features.party.data.datasource.PartyLocalDataSource
import com.teamadn.partyfinder.features.party.data.datasource.PartyRealTimeRemoteDataSource
import com.teamadn.partyfinder.features.party.data.repository.PartyRepository
import com.teamadn.partyfinder.features.party.domain.repository.IPartyRepository
import com.teamadn.partyfinder.features.party.domain.usecase.GetPartiesUseCase
import com.teamadn.partyfinder.features.party.presentation.PartyViewModel
import com.teamadn.partyfinder.navigation.NavigationViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.concurrent.TimeUnit


object NetworkConstants {
    const val RETROFIT_GITHUB = "RetrofitGithub"
    const val GITHUB_BASE_URL = "https://api.github.com/"
    const val RETROFIT_MOVIE = "RetrofitMovie"
    const val MOVIE_BASE_URL = "https://api.themoviedb.org/"
}

val appModule = module {

    // OkHttpClient
    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }


    // Party Feature Dependencies
    single(named("partyDao")) { get<AppRoomDatabase>().partyDao() }
    single { PartyRealTimeRemoteDataSource() }
    single { PartyLocalDataSource(get(named("partyDao"))) }
    single<IPartyRepository> { PartyRepository(get(), get()) }
    factory { GetPartiesUseCase(get()) }
    viewModel { PartyViewModel(get()) }


    viewModel { NavigationViewModel() }
}
