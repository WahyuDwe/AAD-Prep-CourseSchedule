package com.dicoding.courseschedule.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.ui.list.ListViewModelFactory
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {
    private lateinit var viewModel: AddCourseViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ListViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory)[AddCourseViewModel::class.java]

        viewModel.saved.observe(this) {
            if (it.getContentIfNotHandled() == true) {
                Toast.makeText(this, "Successfully add Course", Toast.LENGTH_SHORT).show()
                onBackPressed()
            } else {
                Toast.makeText(this, getString(R.string.input_empty_message), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        showTimePicker()
    }

    private fun showTimePicker() {
        val startTime = findViewById<ImageButton>(R.id.btn_start_time)
        val endTime = findViewById<ImageButton>(R.id.btn_end_time)

        startTime.setOnClickListener {
            val timePickerFragment = TimePickerFragment()
            timePickerFragment.show(supportFragmentManager, TIME_PICKER_START)
        }

        endTime.setOnClickListener {
            val timePickerFragment = TimePickerFragment()
            timePickerFragment.show(supportFragmentManager, TIME_PICKER_END)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                addCourse()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        when (tag) {
            TIME_PICKER_START -> {
                val tvStartTimeValue = findViewById<TextView>(R.id.tv_start_time_value)
                tvStartTimeValue.text = timeFormat.format(calendar.time)
            }

            TIME_PICKER_END -> {
                val tvEndTimeValue = findViewById<TextView>(R.id.tv_end_time_value)
                tvEndTimeValue.text = timeFormat.format(calendar.time)
            }
        }
    }

    private fun addCourse() {
        val courseName =
            findViewById<TextInputEditText>(R.id.et_course_name).text?.trim().toString()
        val day = findViewById<Spinner>(R.id.spinner_day).selectedItemPosition
        val startTime = findViewById<TextView>(R.id.tv_start_time_value).text.toString()
        val endTime = findViewById<TextView>(R.id.tv_end_time_value).text.toString()
        val lecturerName = findViewById<TextInputEditText>(R.id.et_lecturer).text?.trim().toString()
        val note = findViewById<TextInputEditText>(R.id.et_note).text?.trim().toString()

        viewModel.insertCourse(courseName, day, startTime, endTime, lecturerName, note)

    }

    companion object {
        private const val TIME_PICKER_START = "time_picker_start"
        private const val TIME_PICKER_END = "time_picker_end"
    }
}