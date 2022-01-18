package com.brix.windows

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*

import android.app.DatePickerDialog
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


class MainActivity : AppCompatActivity() {

    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0

    private val TAG = "MainActivity"

    private lateinit var txtDiffFrom: TextView
    private lateinit var txtDiffTo: TextView
    private lateinit var txtDiffResult: TextView
    private lateinit var txtAddSubFrom: TextView

    private lateinit var spinner: Spinner

    private lateinit var layoutDiff: LinearLayout
    private lateinit var layoutAddSub: LinearLayout


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txtDiffFrom = findViewById(R.id.txtDiffFrom)
        txtDiffTo = findViewById(R.id.txtDiffTo)
        txtDiffResult = findViewById(R.id.txtDiffResult)
        txtAddSubFrom = findViewById(R.id.txtAddSubFrom)

        spinner = findViewById(R.id.spinner)

        layoutDiff = findViewById(R.id.layoutDiff)
        layoutAddSub = findViewById(R.id.layoutAddSub)


//      setting value to calendar
        val c = Calendar.getInstance()
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]

//      setting current date to the text view
        txtDiffFrom.text = getCurrentDate()
        txtDiffTo.text = getCurrentDate()
        txtAddSubFrom.text = getCurrentDate()

        onDataChange()
        getDifference()
    }

    private fun onDataChange() {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (spinner.selectedItem.toString() == "Difference between dates") {
                    layoutAddSub.visibility = View.GONE
                    layoutDiff.visibility = View.VISIBLE
                }
                if (spinner.selectedItem.toString() == "Add or subtract days") {
                    layoutAddSub.visibility = View.VISIBLE
                    layoutDiff.visibility = View.GONE
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        txtDiffFrom.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun afterTextChanged(p0: Editable?) {
                getDifference()
            }

        })
        txtDiffTo.addTextChangedListener(object : TextWatcher {
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
        val result =
            period.years.toString() + " years \n" + period.months.toString() + " months \n" + weeks.toString() + " weeks \n" + days + " days"
        txtDiffResult.text = result
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
}