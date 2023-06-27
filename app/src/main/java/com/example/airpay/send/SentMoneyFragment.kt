package com.example.airpay.send

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.airpay.R
import com.example.airpay.databinding.FragmentHomeBinding
import com.example.airpay.databinding.FragmentSentMoneyBinding
import com.example.airpay.transaction.SuccessFragment

class SentMoneyFragment : Fragment() {

    private lateinit var binding: FragmentSentMoneyBinding
    private lateinit var phoneNumber: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSentMoneyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitButton.setOnClickListener {
            phoneNumber = binding.recipientNumberEditText.text.toString()
            if (isValidPhoneNumber(phoneNumber) && binding.sendingAmount.text != null ) {
                val amount = binding.sendingAmount.text.toString().toDoubleOrNull()
                if (amount != null && amount > 0) {
                    val fragment = SuccessFragment() // Create an instance of the SentMoneyFragment
                    val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                    val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.fragmentContainerView, fragment) // Replace `R.id.fragmentContainer` with the ID of the container in your layout
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                } else {
                    Toast.makeText(requireContext(), "Invalid amount", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(requireContext(), "Invalid phone number", Toast.LENGTH_SHORT).show()
            }
        }

        }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val pattern = Regex("^\\d{10}$") // Regular expression pattern for 10-digit phone numbers
        return pattern.matches(phoneNumber)
    }

}