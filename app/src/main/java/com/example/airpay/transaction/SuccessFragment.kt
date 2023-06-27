package com.example.airpay.transaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.airpay.R
import com.example.airpay.databinding.FragmentSentMoneyBinding
import com.example.airpay.databinding.FragmentSuccessBinding

class SuccessFragment : Fragment() {

    private lateinit var binding: FragmentSuccessBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    companion object {
    }
}