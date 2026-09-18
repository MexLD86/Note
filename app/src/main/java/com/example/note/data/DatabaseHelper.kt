package com.example.note.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_NAME = "notes.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NOTES = "notes"

        const val COLUMN_ID = "id"
        const val COLUMN_TEXT = "text"
        const val COLUMN_DATE_TIME = "date_time"
        const val COLUMN_IS_CHECKED = "is_checked"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_NOTES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TEXT TEXT NOT NULL,
                $COLUMN_DATE_TIME INTEGER NOT NULL,
                $COLUMN_IS_CHECKED INTEGER DEFAULT 0 
            )  
        """.trimIndent()
        db?.execSQL(createTable)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
    }

    //CRUD operations

    fun insertNote(note: Note): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TEXT, note.text)
            put(COLUMN_DATE_TIME, note.dateTime)
            put(COLUMN_IS_CHECKED, if (note.isChecked) 1 else 0)
        }
        val id = db.insert(TABLE_NOTES, null, values)
        android.util.Log.d("DB", "Вставлена заметка id=$id, text=${note.text}")
        return id
    }

    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val db = writableDatabase
        val cursor = db.query(
            TABLE_NOTES,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_DATE_TIME DESC"
        )
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                val text = getString(getColumnIndexOrThrow(COLUMN_TEXT))
                val dateTime = getLong(getColumnIndexOrThrow(COLUMN_DATE_TIME))
                val isChecked = getInt(getColumnIndexOrThrow(COLUMN_IS_CHECKED)) == 1

                notes.add(
                    Note(
                        id = id,
                        text = text,
                        dateTime = dateTime,
                        isChecked = isChecked
                    )
                )
            }
            close()
        }
        android.util.Log.d("DB_CHECK", "getAllNotes вернул ${notes.size} записей")
        return notes
    }

    fun getNoteById(id: Long): Note? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NOTES,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        return with(cursor) {
            if (moveToFirst()) {
                val note = Note(
                    id = getLong(getColumnIndexOrThrow(COLUMN_ID)),
                    text = getString(getColumnIndexOrThrow(COLUMN_TEXT)),
                    dateTime = getLong(getColumnIndexOrThrow(COLUMN_DATE_TIME)),
                    isChecked = getInt(getColumnIndexOrThrow(COLUMN_IS_CHECKED)) == 1
                )
                close()
                note
            } else {
                close()
                null
            }
        }
    }

    fun updateNote(note: Note): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TEXT, note.text)
            put(COLUMN_DATE_TIME, note.dateTime)
            put(COLUMN_IS_CHECKED, if (note.isChecked) 1 else 0)
        }
        return db.update(
            TABLE_NOTES,
            values,
            "$COLUMN_ID = ?",
            arrayOf(note.id.toString())
        )
    }

    fun deleteNote(id: Long): Int {
        val db = writableDatabase
        return db.delete(
            TABLE_NOTES,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun deleteAllNotes() : Int {
        val db = writableDatabase
        return db.delete(TABLE_NOTES, null,null)
    }

    fun getNotesCount(): Int {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NOTES,
            arrayOf("COUNT(*)"),
            null,
            null,
            null,
            null,
            null
        )
        return with(cursor) {
            moveToFirst()
            val count = getInt(0)
            close()
            count
        }
    }
}