package com.example.playlistmaker.sharing.di

import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.domain.impl.SharingInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharingModule = module {

    single{
        ExternalNavigator(androidContext())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }
}