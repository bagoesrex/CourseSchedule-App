package com.dicoding.courseschedule.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var viewModel: AddCourseViewModel
    private lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
        supportActionBar?.title = getString(R.string.add_course)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = AddCourseViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory)[AddCourseViewModel::class.java]

        viewModel.saved.observe(this) {
            if (it.getContentIfNotHandled() == true)
                finish()
            else {
                val message = getString(R.string.input_empty_message)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_insert -> {
                val courseName = findViewById<TextInputEditText>(R.id.ed_course_name).text.toString().trim()
                val lecturer = findViewById<TextInputEditText>(R.id.ed_lecturer).text.toString().trim()
                val day = findViewById<Spinner>(R.id.spinner_day).selectedItemPosition
                val startTime = findViewById<TextView>(R.id.tv_add_start_time).text.toString().trim()
                val endTime = findViewById<TextView>(R.id.tv_add_end_time).text.toString().trim()
                val note = findViewById<TextInputEditText>(R.id.ed_note).text.toString().trim()

                viewModel.insertCourse(courseName, day, startTime, endTime, lecturer, note)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showStartTimePicker(view: View) {
        val dialog = TimePickerFragment()
        dialog.show(supportFragmentManager, "START_TIME")
        this.view = view
    }

    fun showEndTimePicker(view: View) {
        val dialog = TimePickerFragment()
        dialog.show(supportFragmentManager, "END_TIME")
        this.view = view
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedTime = timeFormat.format(calendar.time)

        when (tag) {
            "START_TIME" -> findViewById<TextView>(R.id.tv_add_start_time).text = formattedTime
            "END_TIME" -> findViewById<TextView>(R.id.tv_add_end_time).text = formattedTime
        }
    }
}