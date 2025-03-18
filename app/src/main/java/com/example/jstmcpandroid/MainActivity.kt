package com.example.jstmcpandroid

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jstmcpandroid.adapter.TableAdapter
import com.example.jstmcpandroid.data.TableItem
import com.example.jstmcpandroid.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tableAdapter: TableAdapter
    private val tableItems = mutableListOf<TableItem>()

    private val inputX1Fields = mutableListOf<EditText>()
    private val inputX2Fields = mutableListOf<EditText>()
    private var selectedLogicFunction = "AND"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtonLogic()
        setupInputFields()
        setUpRV()
        setupActionButtons()
        binding.detail.setOnClickListener {
            showAppInfoDialog()
        }
    }

    private fun setupButtonLogic() {
        binding.btnAnd.isChecked = true
        selectedLogicFunction = "AND"
        binding.btnAnd.setBackgroundColor(ContextCompat.getColor(this, R.color.orange))
        binding.btnAnd.setTextColor(ContextCompat.getColor(this, R.color.black))

        binding.toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                selectedLogicFunction = when (checkedId) {
                    R.id.btn_and -> "AND"
                    R.id.btn_or -> "OR"
                    R.id.btn_xor -> "XOR"
                    R.id.btn_nand -> "NAND"
                    R.id.btn_nor -> "NOR"
                    R.id.btn_not -> "NOT"

                    else -> "AND"
                }
                updateButtonStyles(checkedId)
                updateX2Visibility()
            }

        }
    }

    private fun setupInputFields() {
        inputX1Fields.apply {
            add(binding.inputX1Row1)
            add(binding.inputX1Row2)
            add(binding.inputX1Row3)
            add(binding.inputX1Row4)
        }

        inputX2Fields.apply {
            add(binding.inputX2Row1)
            add(binding.inputX2Row2)
            add(binding.inputX2Row3)
            add(binding.inputX2Row4)
        }
    }

    private fun setUpRV() {
        tableAdapter = TableAdapter(tableItems)
        binding.rvTable.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = tableAdapter
        }
        initialItemTable()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initialItemTable() {
        tableItems.clear()
        val categories = resources.getStringArray(R.array.table_categories)

        tableItems.add(TableItem(categories[0], "1", "2", "3", "4"))
        categories.drop(1).forEach { category ->
            tableItems.add(TableItem(category, "", "", "", ""))
        }
        tableAdapter.notifyDataSetChanged()
    }

    private fun setupActionButtons() {
        binding.btnInput.setOnClickListener {
            generateRandomInputs()
        }

        binding.btnLatih.setOnClickListener {
            trainJsf()
        }

        binding.btnReset.setOnClickListener {
            resetAllFields()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun generateRandomInputs() {
        for (i in 0 until 4) {
            inputX1Fields[i].setText(Random.nextInt(2).toString())
            inputX2Fields[i].setText(Random.nextInt(2).toString())
        }
        binding.etBobot.setText(Random.nextInt(1, 9).toString())
        binding.etThreshold.setText(Random.nextInt(1, 9).toString())
    }

    private fun resetAllFields() {
        for (i in 0 until 4) {
            inputX1Fields[i].setText("")
            inputX2Fields[i].setText("")
        }
        binding.etBobot.setText("")
        binding.etThreshold.setText("")
        binding.btnAnd.isChecked = true
        selectedLogicFunction = "AND"
        initialItemTable()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun trainJsf() {
        try {
            val weight = binding.etBobot.text.toString().toInt()
            val threshold = binding.etThreshold.text.toString().toInt()

            for (i in 0 until 4) {
                val x1Value = inputX1Fields[i].text.toString().toInt()
                val x2Value = if (selectedLogicFunction == NOT) 0 else (inputX2Fields[i].text.toString().toIntOrNull() ?: 0)

                tableItems[1].values[i] = x1Value.toString()
                tableItems[2].values[i] = if (selectedLogicFunction == NOT) "-" else x2Value.toString()
            }

            tableItems[3].values.fill(selectedLogicFunction)
            tableItems[4].values.fill(weight.toString())
            tableItems[5].values.fill(threshold.toString())

            val yValues = calculateYValue()
            tableItems[6].values = yValues.map { it.toString() }.toTypedArray()

            val trainResults = calculateTrainResult(weight)
            tableItems[7].values = trainResults.map { it.toString() }.toTypedArray()

            val outputs = calculateOutput(trainResults, threshold)
            tableItems[8].values = outputs.map { it.toString() }.toTypedArray()

            tableItems[9].values = determineResults(yValues, outputs)
            tableAdapter.notifyDataSetChanged()
            println("Training Completed")

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    private fun calculateYValue(): Array<Int> {
        val x1Values = tableItems[1].values.map { it.toInt() }.toTypedArray()
        val x2Values = tableItems[2].values.mapNotNull { it.toIntOrNull() }.toTypedArray()

    return when (selectedLogicFunction) {
            "AND" -> Array(4) { i -> if (x1Values[i] == 1 && x2Values[i] == 1) 1 else 0 }
            "OR" -> Array(4) { i -> if (x1Values[i] == 1 || x2Values[i] == 1) 1 else 0 }
            "XOR" -> Array(4) { i -> if (x1Values[i] != x2Values[i]) 1 else 0 }
            "NAND" -> Array(4) { i -> if (x1Values[i] == 1 && x2Values[i] == 1) 0 else 1 }
            "NOR" -> Array(4) { i -> if (x1Values[i] == 0 && x2Values[i] == 0) 1 else 0 }
            "NOT" -> Array(4) { i -> if (x1Values[i] == 0) 1 else 0 }
            else -> Array(4) { 0 }
        }
    }
    private fun calculateTrainResult(weight: Int): Array<Int> {
        return Array(4) { i ->
            val x1 = tableItems[1].values[i].toInt()
            val x2 = tableItems[2].values[i].toIntOrNull() ?: 0

            (x1 * weight) + (x2 * weight)
        }

    }

    private fun calculateOutput(trainingResults: Array<Int>, threshold: Int): Array<Int> {
       return Array(4) { i -> if (trainingResults[i] >= threshold) 1 else 0 }

    }

    private fun determineResults(yValues: Array<Int>, outputs: Array<Int>): Array<String> {
        return Array(4) { i -> if (yValues[i] == outputs[i]) OK else NOT_OK }

    }

    @SuppressLint("SetTextI18n")
    private fun updateX2Visibility() {
        if (selectedLogicFunction == NOT) {
            binding.X2.visibility = View.GONE
            inputX2Fields.forEach { it.visibility = View.GONE }
        } else {
            binding.X2.visibility = View.VISIBLE
            inputX2Fields.forEach { it.visibility = View.VISIBLE }
        }

        if (selectedLogicFunction == NOT){
            binding.X1.text = "X"
        } else {
            binding.X1.text = "X1"
        }

    }

    private fun updateButtonStyles(selectedId: Int) {
        val buttons = listOf(
            binding.btnAnd to R.id.btn_and,
            binding.btnOr to R.id.btn_or,
            binding.btnXor to R.id.btn_xor,
            binding.btnNand to R.id.btn_nand,
            binding.btnNor to R.id.btn_nor,
            binding.btnNot to R.id.btn_not
        )

        for ((button, id) in buttons) {
            if (id == selectedId) {
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.orange))
                button.setTextColor(ContextCompat.getColor(this, R.color.black))
            } else {
                button.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                button.setTextColor(ContextCompat.getColor(this, R.color.white))
            }
        }
    }
    private fun showAppInfoDialog() {
        val message = SpannableStringBuilder()
            .append("Selamat datang di aplikasi Mcculloch-Pitt, sebuah proyek kelompok dari kelas R88 untuk memenuhi tugas kelompok mata kuliah Jaringan Saraf Tiruan.\n\n")
            .append("Kelompok kami yang terdiri dari:\n\n")
            .append("- Andika Wahyu Saputra (202143502497)\n")
            .append("- Widhiani Luky Kurniasih (202143502524)\n")
            .append("- Gina Salma Sabilla (202143502543)\n")
            .append("- Asep Sutisna (202143502510)\n")
            .append("- Alfian Sahril mubarok (202143502505)\n")
            .append("- Dea amelia (202143502551)\n\n")
            .append("Berusaha mengembangkan aplikasi ini untuk membantu memvisualisasikan dan memahami konsep jaringan saraf tiruan dengan menggunakan model Mcculloch-Pitt.\n\n")
            .append("Kami berharap aplikasi ini dapat bermanfaat bagi pengguna dan memberikan kontribusi pada bidang jaringan saraf tiruan.")

        AlertDialog.Builder(this)
            .setTitle("Tentang Aplikasi")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }



    companion object {
        private const val NOT = "NOT"
        private const val NOT_OK = "NOT OK"
        private const val OK = "OK"
    }
}

