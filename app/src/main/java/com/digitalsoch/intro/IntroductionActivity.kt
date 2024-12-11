package com.digitalsoch.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.digitalsoch.R
import com.digitalsoch.auth.LoginActivity
import com.digitalsoch.dashboard.DashboardActivity
import com.digitalsoch.databinding.ActivityIntoductionBinding

class IntroductionActivity : AppCompatActivity() {
    lateinit var binding: ActivityIntoductionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntoductionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var btnSkip = binding.btnSkip
        var btnStart = binding.btnStart
        var btnNext = binding.btnNext
        var viewPager = binding.viewPager
        val images = listOf(
            R.drawable.slide_1, // Replace with your actual drawable resources
            R.drawable.slide_2,
            R.drawable.slide_3
        )

        val adapter = IntroPagerAdapter(images)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        btnSkip.visibility = View.VISIBLE
                        btnNext.visibility = View.VISIBLE
                        btnStart.visibility = View.GONE
                    }

                    1 -> {
                        btnSkip.visibility = View.GONE
                        btnNext.visibility = View.VISIBLE
                        btnStart.visibility = View.GONE
                    }

                    2 -> {
                        btnSkip.visibility = View.GONE
                        btnNext.visibility = View.GONE
                        btnStart.visibility = View.VISIBLE
                    }
                }
            }
        })

        btnSkip.setOnClickListener {
            // Handle skip action, e.g., navigate to the main activity
            navigateToMainActivity()
        }

        btnNext.setOnClickListener {
            viewPager.currentItem = viewPager.currentItem + 1
        }

        btnStart.setOnClickListener {
            // Handle start action, e.g., navigate to the main activity
            navigateToMainActivity()
        }
    }

    private fun navigateToMainActivity() {
        // Intent to navigate to the main activity
        val intent = Intent(this, LoginActivity::class.java) // Replace with your main activity
        startActivity(intent)
        finish()
    }
}
