package com.example.koreantime

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.koreantime.DTO.DTO_schecule
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.views.*

class CarouselActivity : AppCompatActivity() {
    private var selectedIndex: Int = 0;
    var groupid: String="" //쓸수있는 var
    var db: FirebaseFirestore? =null
    val meetingssss=ArrayList<DTO_schecule>()
    var membernick=ArrayList<String>()
    var useremail=""
    var meetings_id=ArrayList<String>()

    fun update_list(){
        db!!.collection("group").document(groupid).collection("schedule")
                .get()
                .addOnSuccessListener { result ->
                    var meeting_count=0
                    meetingssss.clear()
                    meetings_id.clear()
                    for (document in result) {
                        val meeting= DTO_schecule(document.data.get("host").toString(),
                                document.data.get("location").toString()
                                ,document.data.get("punishment_alarm").toString()
                                ,document.data.get("punishment_vibrate").toString()
                                ,document.data.get("time").toString()
                                ,document.data.get("date").toString()
                                ,document.data.get("name").toString())
                        Log.d("meeitngfdff", meeting.date)
                        meetingssss.add(meeting)//스케줄 다 가져와서 저장
                        meetings_id.add(document.id)
                        meeting_count += 1
                    }
                    Log.d("temptemp","tejote")
                    if(meeting_count>0){
                        if(meetingssss.size==1){
                            datebox1.text=meetingssss.get(0).date
                            timebox1.text=meetingssss.get(0).time
                            datebox2.text=""
                            timebox2.text=""
                            datebox3.text=""
                            timebox3.text=""
                            btn_toggle1.setOnClickListener{
                                val tg1intent = Intent(this, Meetingpage::class.java)// 여기서부턴 Meetingpage
                                tg1intent.putExtra("id",meetings_id.get(0))
                                tg1intent.putExtra("gid",groupid)
                                startActivity(tg1intent)
                            }
                            Map_image2.setOnClickListener{
                                val btn1intent = Intent(this, KakaoMap::class.java) //카카오 맵 넣으시고요
                                btn1intent.putExtra("useremail", useremail)
                                btn1intent.putExtra("groupname", groupid)
                                val items = membernick?.toArray(arrayOfNulls<String>(membernick.size))
                                btn1intent.putExtra("groupmember", items)
                                startActivityForResult(btn1intent, 1)
                            }
                            Map_image3.setOnClickListener {
                                val btn1intent = Intent(this, KakaoMap::class.java) //카카오 맵 넣으시고요
                                btn1intent.putExtra("useremail", useremail)
                                btn1intent.putExtra("groupname", groupid)
                                val items = membernick?.toArray(arrayOfNulls<String>(membernick.size))
                                btn1intent.putExtra("groupmember", items)
                                startActivityForResult(btn1intent, 1)
                            }
                        }
                        if(meetingssss.size==2){
                            datebox1.text=meetingssss.get(0).date
                            timebox1.text=meetingssss.get(0).time
                            datebox2.text=meetingssss.get(1).date
                            timebox2.text=meetingssss.get(1).time
                            datebox3.text=""
                            timebox3.text=""
                            btn_toggle1.setOnClickListener{
                                val tg1intent = Intent(this, Meetingpage::class.java)// 여기서부턴 Meetingpage
                                startActivity(tg1intent)
                            }

                            btn_toggle2.setOnClickListener{
                                val tg2intent = Intent(this, Meetingpage::class.java)
                                startActivity(tg2intent)
                            }
                            Map_image3.setOnClickListener{
                                val btn1intent = Intent(this, KakaoMap::class.java) //카카오 맵 넣으시고요
                                btn1intent.putExtra("useremail", useremail)
                                btn1intent.putExtra("groupname", groupid)
                                val items = membernick?.toArray(arrayOfNulls<String>(membernick.size))
                                btn1intent.putExtra("groupmember", items)
                                startActivityForResult(btn1intent, 1)
                            }
                        }
                        if(meetingssss.size==3){
                            datebox1.text=meetingssss.get(0).date
                            timebox1.text=meetingssss.get(0).time
                            datebox2.text=meetingssss.get(1).date
                            timebox2.text=meetingssss.get(1).time
                            datebox3.text=meetingssss.get(2).date
                            timebox3.text=meetingssss.get(2).time
                            btn_toggle1.setOnClickListener{
                                val tg1intent = Intent(this, Meetingpage::class.java)// 여기서부턴 Meetingpage
                                startActivity(tg1intent)
                            }

                            btn_toggle2.setOnClickListener{
                                val tg2intent = Intent(this, Meetingpage::class.java)
                                startActivity(tg2intent)
                            }

                            btn_toggle3.setOnClickListener{
                                val tg3intent = Intent(this, Meetingpage::class.java) // 여기까지
                                startActivity(tg3intent)
                            }
                        }
                    }
                    else{
                        datebox1.text=""
                        timebox1.text=""
                        datebox2.text=""
                        timebox2.text=""
                        datebox3.text=""
                        timebox3.text=""
                        Map_image1.setOnClickListener{
                            val btn1intent = Intent(this, KakaoMap::class.java) //카카오 맵 넣으시고요
                            btn1intent.putExtra("useremail", useremail)
                            btn1intent.putExtra("groupname", groupid)
                            val items = membernick?.toArray(arrayOfNulls<String>(membernick.size))
                            btn1intent.putExtra("groupmember", items)
                            startActivityForResult(btn1intent, 1)
                        }
                        Map_image2.setOnClickListener {
                            val btn1intent = Intent(this, KakaoMap::class.java) //카카오 맵 넣으시고요
                            btn1intent.putExtra("useremail", useremail)
                            btn1intent.putExtra("groupname", groupid)
                            val items = membernick?.toArray(arrayOfNulls<String>(membernick.size))
                            btn1intent.putExtra("groupmember", items)
                            startActivityForResult(btn1intent, 1)
                        }
                        Map_image3.setOnClickListener{
                            val btn1intent = Intent(this, KakaoMap::class.java) //카카오 맵 넣으시고요
                            btn1intent.putExtra("useremail", useremail)
                            btn1intent.putExtra("groupname", groupid)
                            val items = membernick?.toArray(arrayOfNulls<String>(membernick.size))
                            btn1intent.putExtra("groupmember", items)
                            startActivityForResult(btn1intent, 1)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("meeitngfdff", "Error getting documents: ", exception)
                }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            1->{
                if(resultCode==1) {
                    Log.d("hihihihihihi","success")
                    update_list()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val secondIntent = intent
        val groupname = secondIntent.getStringExtra("groupname")
        useremail = secondIntent.getStringExtra("useremail").toString()
        membernick= secondIntent.getStringArrayListExtra("membernick")!!
        groupid= secondIntent.getStringExtra("groupid").toString()

        val memberssssss=ArrayList<String>()

        db=FirebaseFirestore.getInstance()
        if (membernick != null) {
            for(i in 0 .. membernick.size-1 step (1)){
                db!!.collection("user").document(membernick.get(i)).get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                Log.d("hihiii", document.data!!.get("nickname") as String)
                                memberssssss.add(document.data!!.get("nickname") as String)
                                Log.d("hihiii", memberssssss.toString())
                                Log.d("hihiii", membernick.size.toString())
                                if(i+2==membernick.size){//
                                    val members = findViewById<TextView>(R.id.members)
                                    var temp=""
                                    if (memberssssss != null) {
                                        for(k in 0 .. memberssssss.size-1 step (1)){
                                            temp=temp+" "+memberssssss.get(k)
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
        val datebox1 = findViewById<TextView>(R.id.datebox1)
        val timebox1 = findViewById<TextView>(R.id.timebox1)
        val datebox2 = findViewById<TextView>(R.id.datebox2)
        val timebox2 = findViewById<TextView>(R.id.timebox2)
        val datebox3 = findViewById<TextView>(R.id.datebox3)
        val timebox3 = findViewById<TextView>(R.id.timebox3)
        val delbtn = findViewById<ImageButton>(R.id.delete)



        update_list()



        groupName.setText(groupname);
        delbtn.setOnClickListener {
            db!!.collection("group").document(groupid.toString())
                    .delete()
                    .addOnSuccessListener {
                        Log.d("삭제 성공", "DocumentSnapshot successfully deleted!")
                        val result_intent = intent
                        setResult(1, result_intent);
                        finish()
                    }
                    .addOnFailureListener { e -> Log.w("삭제 실패", "Error deleting document", e) }
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