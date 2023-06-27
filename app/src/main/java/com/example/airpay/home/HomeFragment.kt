package com.example.airpay.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.airpay.R
import com.example.airpay.adapter.CurrencyAdapter
import com.example.airpay.databinding.FragmentHomeBinding
import com.example.airpay.model.Currency
import com.example.airpay.send.SentMoneyFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sentMoneyBtn.setOnClickListener {
            val fragment = SentMoneyFragment() // Create an instance of the SentMoneyFragment
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment) // Replace `R.id.fragmentContainer` with the ID of the container in your layout
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        binding.balanceVisibility.setOnClickListener {
            binding.balanceVisibility.isSelected = !binding.balanceVisibility.isSelected
            if (binding.balanceVisibility.isSelected) {
                binding.balanceText.text = getString(R.string.balance)
            } else {
                binding.balanceText.text = getString(R.string.hide)
            }
        }

        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val countryAdapter = CurrencyAdapter(emptyList())
        recyclerView.adapter = countryAdapter

        // Make the HTTP request
        fetchCountryData()
    }

    private fun fetchCountryData() {
        val url = "http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso"
        val xmlData = """
        <?xml version="1.0" encoding="utf-8"?>
        <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
          <soap:Body>
            <ListOfCurrenciesByName xmlns="http://www.oorsprong.org/websamples.countryinfo">
            </ListOfCurrenciesByName>
          </soap:Body>
        </soap:Envelope>
    """.trimIndent()

        val client = OkHttpClient()
        val mediaType = "text/xml; charset=utf-8".toMediaType()
        val requestBody = xmlData.toRequestBody(mediaType)
        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "text/xml; charset=utf-8")
            .post(requestBody)
            .build()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    // Parse the response and extract the currency data
                    val currencies = responseData?.let { parseCurrencyData(it) }
                    // Update the RecyclerView on the main thread
                    launch(Dispatchers.Main) {
                        val currencyAdapter = currencies?.let { CurrencyAdapter(it) }
                        binding.recyclerview.adapter = currencyAdapter
                    }
                } else {
                    // Request failed
                    println("Request failed")
                    Toast.makeText(requireContext(), "Request failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Handle any exceptions that occurred during the request
                e.printStackTrace()
            }
        }
    }

    private fun parseCurrencyData(responseData: String): List<Currency> {
        val currencies = mutableListOf<Currency>()

        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(responseData.reader())

            var eventType = parser.eventType
            var currencyCode: String? = null
            var currencyName: String? = null

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "sISOCode" -> currencyCode = parser.nextText()
                            "sName" -> currencyName = parser.nextText()
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "tCurrency") {
                            if (currencyCode != null && currencyName != null) {
                                val currency = Currency(currencyCode, currencyName)
                                currencies.add(currency)
                            }
                            currencyCode = null
                            currencyName = null
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return currencies
    }


}
