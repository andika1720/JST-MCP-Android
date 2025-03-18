package com.example.jstmcpandroid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jstmcpandroid.databinding.RowItemBinding
import com.example.jstmcpandroid.data.TableItem

class TableAdapter(private val items: List<TableItem>) :
    RecyclerView.Adapter<TableAdapter.TableViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val binding = RowItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TableViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class TableViewHolder(private val binding: RowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TableItem) {
            binding.apply {
                textCategory.text = item.category

                if (item.values.isNotEmpty()) textValue1.text = item.values[0]
                if (item.values.size > 1) textValue2.text = item.values[1]
                if (item.values.size > 2) textValue3.text = item.values[2]
                if (item.values.size > 3) textValue4.text = item.values[3]
            }
        }
    }
}