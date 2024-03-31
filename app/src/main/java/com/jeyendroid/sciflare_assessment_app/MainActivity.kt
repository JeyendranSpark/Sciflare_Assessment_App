package com.jeyendroid.sciflare_assessment_app

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jeyendroid.sciflare_assessment_app.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: UserViewModel by viewModels()
    private var userList: ArrayList<UserDataModel> = arrayListOf()
    private var adapter: UserListAdapter? = null
    private var existingModel: UserDataModel? = null

    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel.deleteAllUsers()

        observers()
        main()

        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(this@MainActivity)

        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_map_view_layout)
        fetchLocation()
        bottomSheetDialog.setCancelable(true)
        bottomSheetDialog.setCanceledOnTouchOutside(true)

        binding.openBottomSheetButton.setOnClickListener {
            fetchLocation()
            bottomSheetDialog.show()
        }

    }

    fun main() {
        // You need to launch a coroutine to make the suspending call
        runBlocking {
            val unicornApi = RetrofitClient.instance

            // Dispatchers.IO is usually used for network calls
            val job = CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userData = unicornApi.getAllUserData()
                    userData.forEach {
                        println("Unicorn ID: ${it._id}, Name: ${it.Name}")

//                        Toast.makeText(applicationContext,userData.toString(),Toast.LENGTH_SHORT).show()

                        if (userData.isNotEmpty()) {

                            for (i in 0 until userData.size) {
                                viewModel.addUser(userData[i])
                            }
                            binding.tvNoRecordFound.isVisible = false
                            binding.rvUserList.isVisible = true

                        }else {
                            binding.tvNoRecordFound.isVisible = true
                            binding.rvUserList.isVisible = false
                        }
                    }
                } catch (e: Exception) {
                    println("Exception: ${e.message}")
                }
            }

            // You can wait for the job to complete if needed
            job.join()
        }
    }

    private fun observers() {
//
        viewModel.userList.observe(this@MainActivity) {
            if (it != null && it.isNotEmpty()) {
                userList = it as ArrayList<UserDataModel>
                setUserList(userList)
            } else {
                setUserList(arrayListOf())
            }
        }


    }

    private fun setUserList(list: ArrayList<UserDataModel>) {

        adapter = UserListAdapter(list, listener = object : UserListAdapter.OnPerformAction {

            override fun onEdit(userDataModel: UserDataModel) {
                existingModel = userDataModel
                editWindlow(userDataModel)
            }

            override fun onDelete(userDataModel: UserDataModel) {
                viewModel.deleteUser(userDataModel)
            }
        })
        binding.rvUserList.adapter = adapter
    }

    @SuppressLint("MissingInflatedId")
    private fun editWindlow(userDataModel: UserDataModel) {


        // Inflate the custom layout
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.edit_list_item, null)

        val button = view.findViewById<Button>(R.id.btnUpdate)
        val etName = view.findViewById<EditText>(R.id.etName)
        val etNumber = view.findViewById<EditText>(R.id.etNumber)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etGender = view.findViewById<EditText>(R.id.etGender)

        etName.setText(userDataModel.Name)
        etNumber.setText(userDataModel.Mobile.toString())
        etEmail.setText(userDataModel.Email)
        etGender.setText(userDataModel.Gender)

        // Build the AlertDialog
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(view)

        // Create and show the dialog
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
        // Example of accessing elements inside the custom layout
        button.setOnClickListener {
            // Handle button click
            if (existingModel != null) {
                viewModel.updateUserDetail(
                    UserDataModel(
                        existingModel!!._id, etName.text.toString(),
                        etNumber.text.toString(), etEmail.text.toString(),etGender.text.toString()
                    )
                )
                existingModel = null
            }
            alertDialog.dismiss()
        }

    }

    private fun fetchLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionCode)
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                Toast.makeText(applicationContext, "${currentLocation.latitude}, ${currentLocation.longitude}", Toast.LENGTH_SHORT).show()
                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.myMap) as
                        SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this@MainActivity)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("I am here!")
        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
        googleMap?.addMarker(markerOptions)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                recreate()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


}


