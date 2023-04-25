package com.example.recordkeeper

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.commit
import com.example.recordkeeper.databinding.ActivityMainBinding
import com.example.recordkeeper.running.RunningFragment
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import com.example.recordkeeper.cycling.CyclingFragment
import com.example.recordkeeper.editrecord.EditRecordActivity
import com.example.recordkeeper.track.MapsFragment
import com.example.recordkeeper.track.TrackFragment

class MainActivity : AppCompatActivity(), OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ModelPreferencesManager.with(application)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnItemSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        if (menu != null) {
            this.menu = menu
        }
        showMenuToolbar(false)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.reset_running -> {
                Toast.makeText(this, "Running records have been reset.", Toast.LENGTH_LONG).show()
            }
            R.id.add_run -> {
                val runningLogFrag = supportFragmentManager.fragments[1] as RunningLogFragment
                val selectedDate = runningLogFrag.getSelectedDate()

                val intent = Intent(applicationContext, EditRecordActivity::class.java)
                intent.putExtra("Date", selectedDate)
                startActivity(intent)
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
        return true
    }

    private fun onRunningLogClicked() {
        supportFragmentManager.commit {
            replace(R.id.frame_content, RunningLogFragment())
        }
        showMenuToolbar(true)
    }

    private fun onTrackClicked() {
        supportFragmentManager.commit {
            replace(R.id.frame_content, MapsFragment())
        }
        showMenuToolbar(false)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_track -> {
                onTrackClicked()
                return true
            }
            R.id.nav_running_log -> {
                onRunningLogClicked()
                return true
            }
            else -> return false
        }
    }

    private fun showMenuToolbar(shouldShow: Boolean) {
        if (shouldShow) {
            menu.findItem(R.id.reset_running).isVisible = true
            menu.findItem(R.id.add_run).isVisible = true
        }
        else {
            menu.findItem(R.id.reset_running).isVisible = false
            menu.findItem(R.id.add_run).isVisible = false
        }
    }
}