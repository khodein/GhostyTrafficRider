package com.ghosty.traffic.rider.db

import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal object AppDatabaseModule {
    fun get() = module {
//        single {
//            Room.databaseBuilder(
//                context = androidContext(),
//                klass = AppDatabase::class.java,
//                name = "ghosty_traffic_rider_db"
//            )
//                .fallbackToDestructiveMigration(false)
//                .build()
//        }
//        single<RoomDatabase> { get<AppDatabase>() }
    }
}
