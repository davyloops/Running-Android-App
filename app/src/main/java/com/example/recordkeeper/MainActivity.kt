package com.example.recordkeeper

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
import com.example.recordkeeper.track.MapsFragment
import com.example.recordkeeper.track.TrackFragment

class MainActivity : AppCompatActivity(), OnItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnItemSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.reset_cycling -> {
//                Toast.makeText(this, "Cycling records have been reset.", Toast.LENGTH_LONG).show()
//                return true
//            }
//            R.id.reset_running -> {
//                Toast.makeText(this, "Running records have been reset.", Toast.LENGTH_LONG).show()
//                return true
//            }
//            R.id.reset_all_records -> {
//                Toast.makeText(this, "All records have been reset.", Toast.LENGTH_LONG).show()
//                return true
//            }
//            else -> {
//                return super.onOptionsItemSelected(item)
//            }
//        }
        return true
    }

    private fun onRecordsClicked() {
        supportFragmentManager.commit {
            replace(R.id.frame_content, RunningFragment())
        }
    }

    private fun onTrackClicked() {
        supportFragmentManager.commit {
            replace(R.id.frame_content, MapsFragment())
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_track -> {
                onTrackClicked()
                return true
            }
            R.id.nav_records -> {
                onRecordsClicked()
                return true
            }
            else -> return false
        }
    }
}