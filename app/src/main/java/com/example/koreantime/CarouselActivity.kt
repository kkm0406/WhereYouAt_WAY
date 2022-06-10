package com.example.koreantime

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.*
import kotlinx.android.synthetic.main.views.*

import com.example.koreantime.DTO.DTO_group
import com.example.koreantime.DTO.DTO_schecule
import com.example.koreantime.databinding.Demo1Binding
import com.example.koreantime.databinding.ViewsBinding
import com.google.firebase.firestore.FirebaseFirestore

import com.sothree.slidinguppanel.SlidingUpPanelLayout

class CarouselActivity : AppCompatActivity() {
    private var selectedIndex: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val secondIntent = intent
        val groupname = secondIntent.getStringExtra("groupname")
        val membernick=secondIntent.getStringArrayListExtra("membernick")
        val groupid=secondIntent.getStringExtra("groupid")

        val memberssssss=ArrayList<String>()
        val meetingssss=ArrayList<DTO_schecule>()
        val db=FirebaseFirestore.getInstance()
        if (membernick != null) {
            for(i in 0 .. membernick.size-1 step (1)){
                db.collection("user").document(membernick.get(i)).get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                Log.d("hihiii", document.data!!.get("nickname") as String)
                                memberssssss.add(document.data!!.get("nickname") as String)
                                if(i+1==membernick.size){
                                    val members = findViewById<TextView>(R.id.members)
                                    var temp=""
                                    if (memberssssss != null) {
                                        for(i in 0 .. memberssssss.size-1 step (1)){
                                            temp=temp+" "+memberssssss.get(i)
                                            Log.d("hihiisdsi", temp)
                                        }
                                        members.text = temp
                                    }

                                }
                            } else {
                                Log.d("hihiii", "No such document")
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d("hihiii", "get failed with ", exception)
                        }
            }
        }

        Log.d("inner group",groupname.toString());
        Log.d("inner group",membernick.toString());


        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val layout = intent.getIntExtra("layout_file_id", R.layout.demo1)
        setContentView(layout)

        val motionLayout = findViewById<MotionLayout>(R.id.motion_container)

        val v1 = findViewById<View>(R.id.v1)
        val v2 = findViewById<View>(R.id.v2)
        val v3 = findViewById<View>(R.id.v3)
        val groupName = findViewById<TextView>(R.id.textView)
        val delbtn = findViewById<ImageButton>(R.id.delete)


       db.collection("group").document(groupid.toString()).collection("schedule")
               .get()
               .addOnSuccessListener { result ->
                   for (document in result) {
                       val meeting= DTO_schecule(document.data.get("host").toString(),
                               document.data.get("location").toString()
                               ,document.data.get("punishment_alarm").toString()
                               ,document.data.get("punishment_vibrate").toString()
                               ,document.data.get("time").toString()
                               ,document.data.get("date").toString()
                               ,document.data.get("name").toString())
                       Log.d("meeitngfdff", meeting.date)
                       meetingssss.add(meeting)
                   }
               }
               .addOnFailureListener { exception ->
                   Log.d("meeitngfdff", "Error getting documents: ", exception)
               }



        groupName.setText(groupname);
        delbtn.setOnClickListener {
            db.collection("group").document(groupid.toString())
                    .delete()
                    .addOnSuccessListener {
                        Log.d("삭제 성공", "DocumentSnapshot successfully deleted!")
                        val result_intent = intent
                        setResult(1, result_intent);
                        finish()
                    }
                    .addOnFailureListener { e -> Log.w("삭제 실패", "Error deleting document", e) }
        }
        Map_image1.setOnClickListener{
            val btn1intent = Intent(this, IntroActivity::class.java) //카카오 맵 넣으시고요
            startActivity(btn1intent)
        }
        Map_image2.setOnClickListener {
            val btn2intent = Intent(this, IntroActivity::class.java)
            startActivity(btn2intent)
        }
        Map_image3.setOnClickListener{
            val btn3intent = Intent(this, IntroActivity::class.java)// 요기까지
            startActivity(btn3intent)
        }

        btn_toggle1.setOnClickListener{
            val tg1intent = Intent(this, IntroActivity::class.java)// 여기서부턴 Meetingpage
            startActivity(tg1intent)
        }

        btn_toggle2.setOnClickListener{
            val tg2intent = Intent(this, IntroActivity::class.java)
            startActivity(tg2intent)
        }

        btn_toggle3.setOnClickListener{
            val tg3intent = Intent(this, IntroActivity::class.java) // 여기까지
            startActivity(tg3intent)
        }


        v1.setOnClickListener {
            if (selectedIndex == 0) return@setOnClickListener


            motionLayout.setTransition(R.id.s2, R.id.s1) //orange to blue transition
            motionLayout.transitionToEnd()
            selectedIndex = 0;
        }
        v2.setOnClickListener {
            if (selectedIndex == 1) return@setOnClickListener


            if (selectedIndex == 2) {
                motionLayout.setTransition(R.id.s3, R.id.s2)  //red to orange transition
            } else {
                motionLayout.setTransition(R.id.s1, R.id.s2) //blue to orange transition
            }
            motionLayout.transitionToEnd()
            selectedIndex = 1;
        }
        v3.setOnClickListener {
            if (selectedIndex == 2) return@setOnClickListener


            motionLayout.setTransition(R.id.s2, R.id.s3) //orange to red transition
            motionLayout.transitionToEnd()
            selectedIndex = 2;
        }

    }

}