package com.example.kotlin_room

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_room.database.Note
import com.example.kotlin_room.database.NoteDao
import com.example.kotlin_room.database.NoteRoomDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var DB :NoteRoomDatabase
    private var arNote : MutableList<Note> = mutableListOf()
    private var note : Note? = null
    private lateinit var adapterN : adapterNote

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DB = NoteRoomDatabase.getDatabase(this)
        adapterN = adapterNote(arNote)


        val _fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)
        _fabAdd.setOnClickListener {
            startActivity(Intent(this, TambahNote::class.java))
        }

        val _rvNotes = findViewById<RecyclerView>(R.id.rvNotes)
        _rvNotes.layoutManager = LinearLayoutManager(this)
        _rvNotes.adapter = adapterN

        adapterN.setOnItemClickCallback(object: adapterNote.OnItemClickCallback {
            override fun delData(dtnote: Note) {

                CoroutineScope(Dispatchers.IO).async {
                    DB.noteDao().delete(dtnote)
                    val note = DB.noteDao().selectall()
                    Log.d("data ROOM2", note.toString())

                    withContext(Dispatchers.Main) {
                        adapterN.isiData(note)
                    }
                }
            }
        })

    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.IO).async {
            val note = DB.noteDao().selectall()
            Log.d("data ROOM",note.toString())
            adapterN.isiData(note)
        }
    }

}