package com.example.jstmcpandroid.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jstmcpandroid.databinding.RowItemBinding
import com.example.jstmcpandroid.db.DataEntity

class DataAdapter(private var dataList: List<DataEntity>) :
    RecyclerView.Adapter<DataAdapter.DataViewHolder>() {

    inner class DataViewHolder(private val binding: RowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: DataEntity) {
            with(binding) {
                textCategory.text = item.kategori
                textValue1.text = item.nilai1.toString()
                textValue2.text = item.nilai2.toString()
                textValue3.text = item.nilai3.toString()
                textValue4.text = item.nilai4.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = RowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    fun updateData(newList: List<DataEntity>) {
        dataList = newList
        notifyDataSetChanged()
    }
}