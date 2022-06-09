package com.example.koreantime

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import kotlinx.android.synthetic.main.views.*

import android.widget.Toast
import com.example.koreantime.databinding.Demo1Binding
import com.example.koreantime.databinding.ViewsBinding

import com.sothree.slidinguppanel.SlidingUpPanelLayout

class CarouselActivity : AppCompatActivity() {
    private var selectedIndex: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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