package com.example.airpay.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.airpay.R
import com.example.airpay.model.Currency
class CurrencyAdapter(private val currencies: List<Currency>) :
    RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_country, parent, false)
        return CurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = currencies[position]
        holder.bind(currency)
    }

    override fun getItemCount(): Int {
        return currencies.size
    }

    inner class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val currencyNameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val countryNameTextView: TextView = itemView.findViewById(R.id.currencyTextView)

        fun bind(currency: Currency) {
            currencyNameTextView.text = currency.currencyName
            countryNameTextView.text = currency.countryName
        }
    }
}
