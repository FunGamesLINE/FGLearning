package com.example.fglearning.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fglearning.database.dao.AccentDao
import com.example.fglearning.database.dao.FlashcardDao
import com.example.fglearning.database.dao.InsertLetterDao
import com.example.fglearning.database.dao.PackageDao
import com.example.fglearning.database.dao.PackageItemDao
import com.example.fglearning.database.entity.Accent
import com.example.fglearning.database.entity.Flashcard
import com.example.fglearning.database.entity.InsertLetter
import com.example.fglearning.database.entity.Package
import com.example.fglearning.database.entity.PackageItem

@Database(
    entities = [
        Package::class,
        PackageItem::class,
        Flashcard::class,
        Accent::class,
        InsertLetter::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class MaterialsDatabase : RoomDatabase() {
    abstract fun packageDao(): PackageDao
    abstract fun packageItemDao(): PackageItemDao

    abstract fun flashcardDao(): FlashcardDao
    abstract fun accentDao(): AccentDao
    abstract fun insertLetterDao(): InsertLetterDao

    companion object {
        @Volatile
        private var INSTANCE: MaterialsDatabase? = null

        fun getInstance(context: Context): MaterialsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MaterialsDatabase::class.java,
                    "material_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}