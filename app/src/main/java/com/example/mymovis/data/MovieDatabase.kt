package com.example.mymovis.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Movie::class, FavouriteMovie::class], version = 2, exportSchema = false)
abstract class MovieDatabase:RoomDatabase() {

    abstract fun movieDao() : MovieDao

    companion object {

        private var database: MovieDatabase? = null

        private val DB_NAME = "movies.db"
        private val LOCK: Any = Any()

        fun getIstance(context: Context): MovieDatabase {
            synchronized(LOCK) {
                if (database == null) {
                    database = Room.databaseBuilder(
                        context.applicationContext,
                        MovieDatabase::class.java,
                        DB_NAME
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return database ?: throw IllegalStateException("Database instance is null")
        }
    }
}