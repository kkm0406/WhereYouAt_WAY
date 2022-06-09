package com.example.koreantime

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)


        val layoutList = arrayListOf(R.layout.demo1)
        val demoList = arrayListOf<DemoAdapter.Demo>()
        for (i in 0 until layoutList.size) {
            demoList.add(DemoAdapter.Demo("Group ${i + 1}", layoutList[i]))
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,LinearLayoutManager.VERTICAL
            )
        )
        recyclerView.layoutManager =
            LinearLayoutManager(this)
        recyclerView.adapter = DemoAdapter(demoList)
    }

    fun delete_doc(){
        db.collection("group").document("DC")
                .delete()
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    fun start(layoutFileId: Int) {
        val intent = Intent(this, CarouselActivity::class.java).apply {
            putExtra("layout_file_id", layoutFileId)
        }
        startActivity(intent)
    }
}
