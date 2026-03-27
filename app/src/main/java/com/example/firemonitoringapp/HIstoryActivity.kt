package com.example.firemonitoringapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.*

class HistoryActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var btnClear: MaterialButton
    private lateinit var databaseReference: DatabaseReference
    private val historyList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        listView = findViewById(R.id.listHistory)
        btnClear = findViewById(R.id.btnClearHistory)

        databaseReference = FirebaseDatabase.getInstance().getReference("history")

        btnClear.setOnClickListener {
            showDeleteConfirmation()
        }

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                historyList.clear()
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        val suhu = data.child("suhu").value.toString()
                        val waktu = data.child("waktu").value.toString()
                        historyList.add("🔥 $waktu\nSuhu: $suhu °C")
                    }
                    historyList.reverse()
                }
                
                val adapter = ArrayAdapter(
                    this@HistoryActivity,
                    android.R.layout.simple_list_item_1,
                    historyList
                )
                listView.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Hapus Riwayat")
            .setMessage("Apakah Anda yakin ingin menghapus semua riwayat?")
            .setPositiveButton("Ya") { _, _ ->
                databaseReference.removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Riwayat dihapus", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Tidak", null)
            .show()
    }
}