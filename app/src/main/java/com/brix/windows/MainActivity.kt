package com.brix.windows

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*

import android.app.DatePickerDialog
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import android.widget.RadioGroup

import android.widget.RadioButton
import kotlin.collections.ArrayList
import android.widget.ArrayAdapter


class MainActivity : AppCompatActivity() {

    private lateinit var txtDiffFrom: TextView
    private lateinit var txtDiffTo: TextView
    private lateinit var txtDiffDay: TextView
    private lateinit var txtDiffResult: TextView
    private lateinit var txtAddSubFrom: TextView
    private lateinit var txtAddSubResult: TextView
    private lateinit var spinner: Spinner
    private lateinit var layoutDiff: LinearLayout
    private lateinit var layoutAddSub: LinearLayout
    private lateinit var radioGroup: RadioGroup
    private lateinit var spinnerYear: Spinner
    private lateinit var spinnerMonth: Spinner
    private lateinit var spinnerDay: Spinner
    private lateinit var radioButtonAdd: RadioButton
    private lateinit var radioButtonSub: RadioButton

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txtDiffFrom = findViewById(R.id.txtDiffFrom)
        txtDiffTo = findViewById(R.id.txtDiffTo)
        txtDiffDay = findViewById(R.id.txtDiffDay)
        txtDiffResult = findViewById(R.id.txtDiffResult)
        txtAddSubFrom = findViewById(R.id.txtAddSubFrom)
        txtAddSubResult = findViewById(R.id.txtAddSubResult)
        spinner = findViewById(R.id.spinner)
        layoutDiff = findViewById(R.id.layoutDiff)
        layoutAddSub = findViewById(R.id.layoutAddSub)
        radioGroup = findViewById(R.id.radioGroup)
        spinnerYear = findViewById(R.id.spinnerYear)
        spinnerMonth = findViewById(R.id.spinnerMonth)
        spinnerDay = findViewById(R.id.spinnerDay)
        radioButtonAdd = findViewById(R.id.radioButtonAdd)
        radioButtonSub = findViewById(R.id.radioButtonSub)

//      setting current date to the text view
        txtDiffFrom.text = getCurrentDate()
        txtDiffTo.text = getCurrentDate()
        txtAddSubFrom.text = getCurrentDate()

        setSpinnerItem()
        onDataChange()
        getDifference()
    }

    private fun onDataChange() {
        val year = spinnerYear.selectedItem as Int
        val month = spinnerMonth.selectedItem as Int
        val day = spinnerDay.selectedItem as Int


        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (spinner.selectedItem.toString() == "Difference between dates") {
                    layoutAddSub.visibility = View.GONE
                    layoutDiff.visibility = View.VISIBLE
                }
                if (spinner.selectedItem.toString() == "Add or subtract days") {
                    layoutAddSub.visibility = View.VISIBLE
                    layoutDiff.visibility = View.GONE

                    if (radioButtonAdd.isChecked) {
                        spinnerAddDate(spinnerYear)
                        spinnerAddDate(spinnerMonth)
                        spinnerAddDate(spinnerDay)
                    } else {
                        spinnerSubDate(spinnerYear)
                        spinnerSubDate(spinnerMonth)
                        spinnerSubDate(spinnerDay)
                    }
//                  Text Change Listener Sub and Add from Text
                    txtAddSubFrom.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            p0: CharSequence?,
                            p1: Int,
                            p2: Int,
                            p3: Int
                        ) {
                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        }

                        @RequiresApi(Build.VERSION_CODES.O)
                        override fun afterTextChanged(p0: Editable?) {
                            if (radioButtonAdd.isChecked) {
                                addDate(year, month, day)
                            } else {
                                subDate(year, month, day)
                            }
                        }

                    })

                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        textChangeListener(txtDiffFrom)
        textChangeListener(txtDiffTo)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onRadioButtonClicked(view: View) {
        val year = spinnerYear.selectedItem as Int
        val month = spinnerMonth.selectedItem as Int
        val day = spinnerDay.selectedItem as Int

        if (view.id == radioButtonAdd.id) {
            addDate(year, month, day)
        } else {
            subDate(year, month, day)
        }
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radioButtonAdd ->
                    if (checked) {
                        spinnerAddDate(spinnerYear)
                        spinnerAddDate(spinnerMonth)
                        spinnerAddDate(spinnerDay)
                    }
                R.id.radioButtonSub ->
                    if (checked) {
                        spinnerSubDate(spinnerYear)
                        spinnerSubDate(spinnerMonth)
                        spinnerSubDate(spinnerDay)
                    }
            }
        }
    }

    private fun textChangeListener(textView: TextView) {
        textView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun afterTextChanged(p0: Editable?) {
                getDifference()
            }

        })
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDifference() {
        val from = formatTextView(txtDiffFrom)
        val to = formatTextView(txtDiffTo)

        val period = Period.between(from, to)
        val weeks = ChronoUnit.WEEKS.between(from, to)
        val days = ChronoUnit.DAYS.between(from, to)
        val result = period.years.toString() + " Year".pluralize(period.years) + "\n" +
                period.months.toString() + " Month".pluralize(period.months) + "\n" +
                weeks.toString() + " Week".pluralize(weeks.toInt()) + "\n" +
                period.days.toString() + " Day".pluralize(period.days)

        txtDiffResult.text = result
        txtDiffDay.text = days.toString() + " Day".pluralize(days.toInt())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onClickFromDiff(view: android.view.View) {
        datePicker(txtDiffFrom).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onClickToDiff(view: android.view.View) {
        datePicker(txtDiffTo).show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun datePicker(textView: TextView): DatePickerDialog {
        val localDate = formatTextView(textView)

//      Date picker Dialog
        return DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->

                textView.text = dateFormat(year, (monthOfYear + 1), dayOfMonth)
            },
            localDate.year,
            localDate.monthValue - 1,
            localDate.dayOfMonth
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateFormat(year: Int, month: Int, day: Int): String {
//      format locale date from calendar date picker
        val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-M-d")
        val toFormat = "$year-$month-$day"
        val localDate = LocalDate.parse(toFormat, firstApiFormat)

//      format locale date to date
        val defaultZoneId: ZoneId = ZoneId.systemDefault()
        val date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant())
        return date.toString("dd MMMM yyyy")
    }

    //    get Current Date from getCurrentDateTime Function
    private fun getCurrentDate(): String {
        val date = getCurrentDateTime()
        return date.toString("dd MMMM yyyy")
    }

    //    Format locale date
    private fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    //  get current date time from calendar
    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    //  plural singular
    private fun String.pluralize(count: Int): String? {
        return if (count > 1) {
            this + 's'
        } else {
            this
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatTextView(textView: TextView): LocalDate {
        //      format text to locale date
        val firstApiFormat = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        val toFormat = textView.text.toString()
        return LocalDate.parse(toFormat, firstApiFormat)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun onClickAddSub(view: android.view.View) {
        datePicker(txtAddSubFrom).show()
    }

    private fun setSpinnerItem() {
        val arrayList: ArrayList<Int?> = ArrayList()

        for (i in 0..999) {
            arrayList.add(i)
        }
        // Create an ArrayAdapter using a simple spinner layout and languages array
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList)
        // Set layout to use when the list of choices appear
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        // Set Adapter to Spinner
        spinnerYear.adapter = arrayAdapter
        spinnerMonth.adapter = arrayAdapter
        spinnerDay.adapter = arrayAdapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addDate(year: Int, month: Int, day: Int) {
//        getting date from text view ang converting tong date
        val localDate = formatTextView(txtAddSubFrom)
        val defaultZoneId: ZoneId = ZoneId.systemDefault()
        val date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant())

        val c = Calendar.getInstance()
        c.time = date

        // manipulate date
        c.add(Calendar.YEAR, year)
        c.add(Calendar.MONTH, month)
        c.add(Calendar.DATE, day)
//      setting and formatting Date for result
        val currentDatePlusOne = c.time
        txtAddSubResult.text = currentDatePlusOne.toString("EEEE, dd MMMM yyyy")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun subDate(year: Int, month: Int, day: Int) {
//        getting date from text view ang converting tong date
        val localDate = formatTextView(txtAddSubFrom)
        val defaultZoneId: ZoneId = ZoneId.systemDefault()
        val date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant())

        val c = Calendar.getInstance()
        c.time = date

        // manipulate date
        c.add(Calendar.YEAR, -year)
        c.add(Calendar.MONTH, -month)
        c.add(Calendar.DATE, -day)
//      setting and formatting Date for result
        val currentDatePlusOne = c.time
        txtAddSubResult.text = currentDatePlusOne.toString("EEEE, dd MMMM yyyy")
    }

    private fun spinnerAddDate(spinner: Spinner) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val year = spinnerYear.selectedItem as Int
                val month = spinnerMonth.selectedItem as Int
                val day = spinnerDay.selectedItem as Int

                addDate(year, month, day)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
    }

    private fun spinnerSubDate(spinner: Spinner) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val year = spinnerYear.selectedItem as Int
                val month = spinnerMonth.selectedItem as Int
                val day = spinnerDay.selectedItem as Int

                subDate(year, month, day)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
    }

}