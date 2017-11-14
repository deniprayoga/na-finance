package com.example.denip.nasyiatulaisyiyahfinance.profile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.example.denip.nasyiatulaisyiyahfinance.user.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.layout_position_list.view.*

class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private val dbProfile = FirebaseDatabase.getInstance().getReference("users")
        private val auth = FirebaseAuth.getInstance()
        lateinit var glideRequestManager: RequestManager
        private val HUNTR = "huntr_profileAct"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        glideRequestManager = Glide.with(this)

        initLayout()
    }

    private fun initLayout() {
        initToolbar()
        location_profile_field.setOnClickListener(this)
        user_position_profile_field.setOnClickListener(this)
        phone_number_profile_field.movementMethod = null
        phone_number_profile_field.isLongClickable = false
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_profile_layout)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.location_profile_field -> showLocationDialog()
            R.id.user_position_profile_field -> {

                val dialogBuilder = AlertDialog.Builder(this)
                val positionInflater = layoutInflater
                val view = positionInflater.inflate(R.layout.layout_position_list, null)
                dialogBuilder
                    .setView(view)
                    .setTitle(getString(R.string.user_position))
                val dialog = dialogBuilder.create()
                dialog.show()

                val positions = arrayOf("Bendahara Umum", "Bendahara I", "Bendahara II",
                    "Bendahara III")
                val positionAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    positions)
                view?.list_view_position?.adapter = positionAdapter
                view?.list_view_position?.setOnItemClickListener { parent, view_, position, id ->
                    val itemValue = view.list_view_position?.getItemAtPosition(position)
                    user_position_profile_field?.setText(itemValue.toString())
                    dialog.dismiss()
                }
            }
        }
    }

    private fun showLocationDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder
            .setTitle(getString(R.string.location))
            .setNeutralButton(getString(R.string.jakarta), { dialog, which ->
                location_profile_field?.setText(getString(R.string.jakarta))
            })
            .setPositiveButton(getString(R.string.yogyakarta), { dialog, which ->
                location_profile_field?.setText(getString(R.string.yogyakarta))
            })
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> onBackPressed()
            R.id.action_bar_save -> {
                var fullName = profile_name_field?.text.toString()
                var phoneNumber = phone_number_profile_field?.text.toString()

                when {
                    fullName.isEmpty() -> showWarningAnimation(profile_name_field)
                    phoneNumber.isEmpty() -> showWarningAnimation(phone_number_profile_field)

                    else -> {
                        val location = location_profile_field?.text.toString()
                        val userId = auth.currentUser?.uid
                        val userEmail = auth.currentUser?.email
                        val position = user_position_profile_field?.text.toString()
                        fullName = profile_name_field?.text.toString()

                        val initial = StringBuilder()
                        for (s: String in fullName.split(" ")) {
                            initial.append(s[0])
                        }

                        Log.d(HUNTR, "initials : " + initial.toString())

                        phoneNumber = phone_number_profile_field?.text.toString()

                        val profileData = UserModel(userId, fullName, phoneNumber, location,
                            userEmail, initial.toString(), position)
                        updateProfile(profileData)
                    }
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showWarningAnimation(view: View) {
        val shake = AnimationUtils.loadAnimation(this@ProfileActivity, R.anim.shake)
        view.startAnimation(shake)
    }

    private fun updateProfile(profileData: UserModel?) {
        val dbUpdateProfileRef = dbProfile.child(profileData?.userId)
        dbUpdateProfileRef.setValue(profileData)
        Toast.makeText(this, getString(R.string.profile_updated), Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        Log.d("taggg", "In the onStart() event")
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        dbProfile.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError?) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                val fullName = dataSnapshot?.child(auth.currentUser?.uid)?.child("fullName")?.value.toString()
                val location = dataSnapshot?.child(auth.currentUser?.uid)?.child("location")?.value.toString()
                val phoneNumber = dataSnapshot?.child(auth.currentUser?.uid)?.child("phoneNumber")?.value.toString()
                val email = dataSnapshot?.child(auth.currentUser?.uid)?.child("email")?.value.toString()
                val position = dataSnapshot?.child(auth.currentUser?.uid)?.child("position")?.value.toString()
                val initial = dataSnapshot?.child(auth.currentUser?.uid)?.child("initial")?.value.toString()
                profile_name_field.setText(fullName)
                phone_number_profile_field.setText(phoneNumber)
                location_profile_field.setText(location)
                email_profile_field.setText(email)
                user_position_profile_field.setText(position)
                initial_field.text = initial
            }
        })


    }

    override fun onRestart() {
        super.onRestart()
        Log.d("taggg", "In the onRestart() event")
    }

    override fun onResume() {
        super.onResume()
        Log.d("taggg", "In the onResume() event")
    }

    override fun onPause() {
        super.onPause()
        Log.d("taggg", "In the onPause() event")
    }

    override fun onStop() {
        super.onStop()
        Log.d("taggg", "In the onStop() event")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("taggg", "In the onDestroy() event")
    }
}
