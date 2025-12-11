package com.teamadn.partyfinder.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.teamadn.partyfinder.features.auth.data.datasource.AuthRemoteDataSource
import com.teamadn.partyfinder.features.auth.data.repository.AuthRepository
import com.teamadn.partyfinder.features.auth.domain.repository.IAuthRepository
import com.teamadn.partyfinder.features.auth.domain.usecase.LoginUseCase
import com.teamadn.partyfinder.features.auth.domain.usecase.LogoutUseCase
import com.teamadn.partyfinder.features.auth.domain.usecase.RegisterUseCase
import com.teamadn.partyfinder.features.auth.presentation.LoginViewModel
import com.teamadn.partyfinder.features.auth.presentation.RegisterViewModel
import com.teamadn.partyfinder.features.favorites.data.datasource.FavoriteLocalDataSource
import com.teamadn.partyfinder.features.favorites.data.repository.FavoriteRepository
import com.teamadn.partyfinder.features.favorites.domain.repository.IFavoriteRepository
import com.teamadn.partyfinder.features.favorites.domain.usecase.GetFavoritesUseCase
import com.teamadn.partyfinder.features.favorites.domain.usecase.ToggleFavoriteUseCase
import com.teamadn.partyfinder.features.favorites.presentation.FavoriteViewModel
import com.teamadn.partyfinder.features.party.data.database.AppRoomDatabase
import com.teamadn.partyfinder.features.party.data.datasource.PartyLocalDataSource
import com.teamadn.partyfinder.features.party.data.datasource.PartyRealTimeRemoteDataSource
import com.teamadn.partyfinder.features.party.data.repository.PartyRepository
import com.teamadn.partyfinder.features.party.domain.repository.IPartyRepository
import com.teamadn.partyfinder.features.party.domain.usecase.GetPartiesUseCase
import com.teamadn.partyfinder.features.party.presentation.PartyViewModel
import com.teamadn.partyfinder.navigation.NavigationViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
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

    // --- Remote Config ---
    single {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 10
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        // Valores por defecto (opcional pero recomendado)
        remoteConfig.setDefaultsAsync(mapOf("is_maintenance_mode" to false))
        remoteConfig
    }

    // Database
    single { AppRoomDatabase.getDatabase(androidContext()) }

    // Firebase
    single { FirebaseAuth.getInstance() } // <-- NUEVO: Proveedor de Firebase Auth

    // Party Feature Dependencies
    single { get<AppRoomDatabase>().partyDao() }
    single { PartyRealTimeRemoteDataSource() }
    single { PartyLocalDataSource(get()) }
    single<IPartyRepository> { PartyRepository(get(), get()) }
    factory { GetPartiesUseCase(get()) }
    //viewModel { PartyViewModel(get()) }
    viewModel { PartyViewModel(get(), get(), get(), get()) }


    // --- Favorites Feature Dependencies ---
    single { get<AppRoomDatabase>().favoriteDao() }
    single { FavoriteLocalDataSource(get()) }
    single<IFavoriteRepository> { FavoriteRepository(get()) }
    factory { GetFavoritesUseCase(get()) }
    factory { ToggleFavoriteUseCase(get()) }
    viewModel { FavoriteViewModel(get(), get()) }


    // MODIFICADO: Auth Feature Dependencies
    single { AuthRemoteDataSource(get()) }
    single<IAuthRepository> { AuthRepository(get()) }
    factory { RegisterUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { LogoutUseCase(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }


    viewModel { NavigationViewModel(get()) }}