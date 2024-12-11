package com.digitalsoch.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.digitalsoch.R
import com.digitalsoch.dashboard.DashboardActivity
import com.digitalsoch.databinding.ActivityLoginBinding
import com.digitalsoch.utils.Constants.ROLE
import com.digitalsoch.utils.Constants.login

class LoginActivity : AppCompatActivity() {
    private var binding: ActivityLoginBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding!!.textView.animation = AnimationUtils.loadAnimation(this, R.anim.icon_anim)
        binding!!.cardView.animation = AnimationUtils.loadAnimation(this,R.anim.icon_anim)
        binding!!.btnLogin.animation = AnimationUtils.loadAnimation(this, R.anim.text_animation3)
        binding!!.edtEmailInput.animation = AnimationUtils.loadAnimation(this,R.anim.text_animation)
        binding!!.edtPassInput.animation = AnimationUtils.loadAnimation(this,R.anim.text_animation1)
        binding!!.txtForget.animation = AnimationUtils.loadAnimation(this,R.anim.text_animation2)
        binding!!.btnLogin.setOnClickListener {
            var emailTxt = binding?.edtEmail?.text.toString()
            when (emailTxt) {
                "admin" -> {
                    login = true
                    ROLE = 1
                }
                "employee" -> {
                    login = true
                    ROLE= 2
                }
                else -> {
                    login = false
                    Toast.makeText(this,"Enter admin for Admin/ employee for Employee Login",Toast.LENGTH_SHORT).show()
                }
            }

            if (login){
                startActivity(Intent(this,DashboardActivity::class.java))
                finish()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val dialogView = layoutInflater.inflate(R.layout.dialog_logout, null)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirm)
        val titleTxt = dialogView.findViewById<TextView>(R.id.title)
        titleTxt.text = "Are you sure you want to exit?"
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            finish()
            dialog.dismiss()
        }

        dialog.show()
    }
}