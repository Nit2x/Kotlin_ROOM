package com.example.kotlin_room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.kotlin_room.database.Note
import com.example.kotlin_room.database.NoteRoomDatabase
import com.example.kotlin_room.helper.DateHelper.getCurrentDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TambahNote : AppCompatActivity() {

    val DB = NoteRoomDatabase.getDatabase(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_note)

        val _btnTambah = findViewById<Button>(R.id.btnTambah)
        val _btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val _etJudul = findViewById<EditText>(R.id.etJudul)
        val _etDeskripsi = findViewById<EditText>(R.id.etDeskripsi)

        var tanggal : String = getCurrentDate()
        _btnTambah.setOnClickListener {
            CoroutineScope(Dispatchers.IO).async {
                DB.noteDao().insert(
                    Note(0,_etJudul.text.toString(), _etDeskripsi.text.toString(), tanggal)
                )
                finish()
            }
        }

        var iID : Int = 0
        var iAddEdit : Int = 0

        iID = intent.getIntExtra("noteId",0)
        iAddEdit = intent.getIntExtra("addEdit",0)

        if (iAddEdit==0) {
            _btnTambah.visibility = View.VISIBLE
            _btnUpdate.visibility = View.GONE
            _etJudul.isEnabled = true
        } else {
            _btnTambah.visibility = View.GONE
            _btnUpdate.visibility = View.VISIBLE
            _etJudul.isEnabled = false

            CoroutineScope(Dispatchers.IO).async {
                val noteitem = DB.noteDao().getNote(iID)
                _etJudul.setText(noteitem.judul)
                _etDeskripsi.setText(noteitem.deskripsi)
            }
        }

        _btnUpdate.setOnClickListener {
            CoroutineScope(Dispatchers.IO).async {
                DB.noteDao().update(
                    _etJudul.text.toString(), _etDeskripsi.text.toString(), iID
                )
                finish()
            }
        }
    }
}