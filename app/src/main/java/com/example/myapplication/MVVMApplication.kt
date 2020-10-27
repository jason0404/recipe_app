package com.example.myapplication

import android.app.Application
import com.example.myapplication.auth.AuthViewModelFactory
import com.example.myapplication.common.Model
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.FavList
import com.example.myapplication.database.Recipe
import com.example.myapplication.network.MyAPI
import com.example.myapplication.network.NetworkConnectionInterceptor
import com.example.myapplication.respositories.UserRepository
import kotlinx.coroutines.InternalCoroutinesApi
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

@InternalCoroutinesApi
class MVVMApplication : Application(), KodeinAware{
    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@MVVMApplication))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { MyAPI(instance()) }
        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { UserRepository(instance(), instance()) }
        bind() from singleton { FavList(instance()) }
        bind() from singleton { Recipe(instance()) }
        bind() from singleton { Model(instance(), instance(), instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }
    }
}