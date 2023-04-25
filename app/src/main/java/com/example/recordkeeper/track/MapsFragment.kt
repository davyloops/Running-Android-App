package com.example.recordkeeper.track

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.os.SystemClock
import android.provider.SettingsSlicesContract.KEY_LOCATION
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.transition.Visibility
import com.example.recordkeeper.R
import com.example.recordkeeper.RunningLogFragment
import com.example.recordkeeper.databinding.FragmentMapsBinding
import com.example.recordkeeper.editrecord.EditRecordActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.NonDisposableHandle.parent
import java.text.SimpleDateFormat
import java.util.*


class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapsBinding

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var polylineOptions: PolylineOptions

    private var googleMap: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null
    private var lastKnownLocation: Location? = null

    private val defaultLocation = LatLng(29.6516, -82.3248)
    private var previousLocation = LatLng(0.0, 0.0)
    private var distance: Double = 0.0
    private var isFirstLocationResult = true
    var locationPermissionGranted = false

    private var chronometerPauseOffset: Long = 0
    private lateinit var alertDialogBuilder: AlertDialog.Builder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = "Run"

        alertDialogBuilder = AlertDialog.Builder(requireActivity())
        binding.buttonStartPause.setOnClickListener { onClickStartPauseButton() }
        binding.buttonEndRun.setOnClickListener { onClickEndRunButton() }
        polylineOptions = PolylineOptions().apply {
            color(Color.BLUE)
            jointType(JointType.ROUND)
        }

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        locationRequest = LocationRequest.create().apply {
            interval = 2000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            smallestDisplacement = 5.0F
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                for (location in p0.locations){
                    if (isFirstLocationResult) {
                        isFirstLocationResult = false
                        previousLocation = LatLng(location.latitude, location.longitude)
                        return
                    }

                    distance += distanceInMeters(location.latitude, location.longitude)
                    previousLocation = LatLng(location.latitude, location.longitude)
                    polylineOptions.add(LatLng(location.latitude, location.longitude))
                    googleMap?.addPolyline(polylineOptions)
                    binding.textViewDistance.text = distance.toString()
                    updateLocationUI()
                    updateBarUI()
                }
            }
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }
    }

    private fun updateBarUI() {
        binding.textViewBarDistance.text = distance.toString()
        val timeElapsed = SystemClock.elapsedRealtime() - binding.chronometer.base
        Log.d("log", timeElapsed.toString())
        val averageSpeed = distance / timeElapsed
        binding.textViewBarAverageSpeed.text = averageSpeed.toString()
    }

    private fun distanceInMeters(currentLocationLat: Double, currentLocationLong: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(
            previousLocation.latitude, previousLocation.longitude,
            currentLocationLat, currentLocationLong, results
        )
        return results[0]
    }

    private fun onClickStartPauseButton() {
        if (binding.buttonStartPause.text.toString() == "Start") {
            startLocationUpdates()
            binding.buttonStartPause.text = "Pause"
            binding.buttonEndRun.visibility = View.VISIBLE
            binding.chronometer.base = SystemClock.elapsedRealtime() - chronometerPauseOffset
            binding.chronometer.start()
            return
        }

        binding.buttonStartPause.text = "Start"
        binding.chronometer.stop()
        chronometerPauseOffset = SystemClock.elapsedRealtime() - binding.chronometer.base
        stopLocationUpdates()
    }

    private fun onClickEndRunButton() {
        alertDialogBuilder.setTitle("End Run?")
            .setMessage("Do you want to end this run?")
            .setCancelable(true)
            .setPositiveButton("Yes") { dialogInterface, i ->
                binding.chronometer.base = SystemClock.elapsedRealtime()
                chronometerPauseOffset = 0
                distance = 0.0
                binding.textViewBarAverageSpeed.text = "0 mph"


                val intent = Intent(activity, EditRecordActivity::class.java)
                intent.putExtra("Date", getCurrentDate())
                intent.putExtra("Distance", distance.toString())
                Log.d("distance", distance.toString())
                intent.putExtra("Time", (SystemClock.elapsedRealtime() - binding.chronometer.base).toString())
                Log.d("time", (SystemClock.elapsedRealtime() - binding.chronometer.base).toString())
                startActivity(intent)
            }
            .setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->

            })
            .show()
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        googleMap?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        updateLocationUI()
        getDeviceLocation()
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        updateLocationUI()
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        if (googleMap == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                googleMap?.isMyLocationEnabled = true
                googleMap?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                googleMap?.isMyLocationEnabled = false
                googleMap?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        lastKnownLocation = task.result

                        if (lastKnownLocation != null) {
                            googleMap?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnownLocation!!.latitude,
                                        lastKnownLocation!!.longitude
                                    ), DEFAULT_ZOOM.toFloat()
                                )
                            )
//                            googleMap?.addMarker(
//                                MarkerOptions().position(
//                                    LatLng(
//                                        lastKnownLocation!!.latitude,
//                                        lastKnownLocation!!.longitude
//                                    )
//                                ).title("Current Location")
//                            )
                        }
                    } else {
                        googleMap?.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                        )
                        googleMap?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun getCurrentDate(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-M-d")
        return formatter.format(time)
    }

    companion object {
        private const val DEFAULT_ZOOM = 20
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
    }
}