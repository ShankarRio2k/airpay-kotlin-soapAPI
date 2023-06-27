package com.example.airpay.login

import android.Manifest.permission.SEND_SMS
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.airpay.MainActivity
import com.example.airpay.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var phoneNumber: String
    private lateinit var otp: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.loginButton.setOnClickListener {
            phoneNumber = binding.phoneNumberEditText.text.toString()
            if (isValidPhoneNumber(phoneNumber)) {
                otp = generateOTP()
                sendOTP(phoneNumber, otp) // Send OTP
            } else {
                Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show()
            }
        }
    }

        private fun isValidPhoneNumber(phoneNumber: String): Boolean {
            val pattern = Regex("^\\d{10}$") // Regular expression pattern for 10-digit phone numbers
            return pattern.matches(phoneNumber)
        }


        private fun sendOTP(phoneNumber: String, otp: String) {
            // Store the generated OTP and phone number
            this.phoneNumber = phoneNumber
            this.otp = otp

            val permission = SEND_SMS
            val granted = PackageManager.PERMISSION_GRANTED
            if (ContextCompat.checkSelfPermission(this, permission) != granted) {
                ActivityCompat.requestPermissions(this, arrayOf(permission), 0)
            } else {
                val message = "Your pin for AirPay : $otp"
                val smsManager = SmsManager.getDefault()
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                Toast.makeText(this, "OTP sent successfully", Toast.LENGTH_SHORT).show()
                binding.pinview.visibility = View.VISIBLE
                binding.loginButton.text = "Login"

                // Handle OTP verification
                binding.loginButton.setOnClickListener {
                    val enteredOTP = binding.pinview.text.toString()
                    if (enteredOTP == otp && isValidPhoneNumber(phoneNumber)) {
                        // OTP verification successful, proceed to the next screen
                        val nextActivity = MainActivity::class.java
                        startActivity(Intent(this@LoginActivity, nextActivity))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid OTP", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        private fun generateOTP(): String {
            val otp = (1000..9999).random().toString()
            return otp
        }

    }
