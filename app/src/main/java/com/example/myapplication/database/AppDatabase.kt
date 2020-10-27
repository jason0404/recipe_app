package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.database.entities.User
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized


@Database(
    entities = [User::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getUserDao() : UserDao

    @InternalCoroutinesApi
    companion object{

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()


        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance?: buildDatabase(context)
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "MyDatabase.database"
            ).build()
    }
}