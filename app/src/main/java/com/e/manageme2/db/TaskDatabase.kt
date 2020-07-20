package com.e.manageme2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Task::class],
    version = 12,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun getTaskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        private val Lock = Any()

        //Runs whenever i write: TaskDatabase(application!!)
        //if INSTANCE is null, create the database, else return INSTANCE
        operator fun invoke(context: Context) = INSTANCE ?: synchronized(Lock){
            INSTANCE ?: buildDatabase(context).also {
                INSTANCE = it
            }
        }

        //builds the actual database
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            TaskDatabase::class.java,
            "taskdatabase"
        ).fallbackToDestructiveMigration().build()

    }

}