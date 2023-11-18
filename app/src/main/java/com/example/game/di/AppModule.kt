package com.example.game.di

import android.content.Context
import androidx.room.Room
import com.example.game.db.GameDao
import com.example.game.db.GameDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCartDatabase(@ApplicationContext context: Context): GameDatabase {
        return Room.databaseBuilder(
            context,
            GameDatabase::class.java,
            "cart_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCartDao(appDatabase: GameDatabase): GameDao {
        return appDatabase.gameDao()
    }
}