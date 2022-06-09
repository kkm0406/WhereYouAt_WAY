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
import android.widget.Toast
import com.example.koreantime.databinding.Demo1Binding
import com.example.koreantime.databinding.ViewsBinding

import com.sothree.slidinguppanel.SlidingUpPanelLayout

class CarouselActivity : AppCompatActivity() {
    private lateinit var binding : ViewsBinding
    private var selectedIndex: Int = 0;
    mbinding
    private val binding get() = mbinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val view = binding.root
        setContentView(view)

        val layout = intent.getIntExtra("layout_file_id", R.layout.demo1)
        setContentView(layout)

        val motionLayout = findViewById<MotionLayout>(R.id.motion_container)

        val v1 = findViewById<View>(R.id.v1)
        val v2 = findViewById<View>(R.id.v2)
        val v3 = findViewById<View>(R.id.v3)
        val btn1 = findViewById<ImageView>(R.id.Map_image1)
        val btn2 = findViewById<ImageView>(R.id.Map_image2)
        val btn3 = findViewById<ImageView>(R.id.Map_image3)
        val toggle1 = findViewById<Button>(R.id.btn_toggle1)
        val toggle2 = findViewById<Button>(R.id.btn_toggle2)
        val toggle3 = findViewById<Button>(R.id.btn_toggle3)

        btn1.setOnClickListener{
            val btn1intent = Intent(this, KakaoMap::class.java)
            startActivity(btn1intent)
        }
        btn2.setOnClickListener {
            val btn2intent = Intent(this, KakaoMap::class.java)
            startActivity(btn2intent)
        }
        btn3.setOnClickListener{
            val btn3intent = Intent(this, KakaoMap::class.java)
            startActivity(btn3intent)
        }

        toggle1.setOnClickListener{
            val tg1intent = Intent(this, Meetingpage::class.java)
            startActivity(tg1intent)
        }

        toggle2.setOnClickListener{
            val tg2intent = Intent(this, Meetingpage::class.java)
            startActivity(tg2intent)
        }

        toggle3.setOnClickListener{
            val tg3intent = Intent(this, Meetingpage::class.java)
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