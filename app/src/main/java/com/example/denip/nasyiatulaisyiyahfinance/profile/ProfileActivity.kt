package com.example.denip.nasyiatulaisyiyahfinance.profile

import android.Manifest
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.denip.nasyiatulaisyiyahfinance.R
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import gun0912.tedbottompicker.TedBottomPicker
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.ArrayList

class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        var selectedUri: Uri? = null
        lateinit var glideRequestManager: RequestManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        glideRequestManager = Glide.with(this)

        initLayout()
    }

    private fun initLayout() {
        initToolbar()
        initLocations()
        profile_photo.setOnClickListener(this)
        profile_photo.isClickable = false
    }

    private fun initLocations() {
        val locationAdapter: ArrayAdapter<String> = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, resources.getStringArray(R.array.locations))
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        location_spinner.adapter = locationAdapter
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_profile_layout)

        edit_profile_button.setOnClickListener(this)
        save_profile_button.setOnClickListener(this)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.edit_profile_button -> enableFields()
            R.id.save_profile_button -> {
                disableFields()
                Toast.makeText(this, "Make your save profile action", Toast.LENGTH_SHORT).show()
            }
            R.id.profile_photo -> pickImage()
        }
    }

    private fun pickImage() {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                val bottomSheetDialogFragment: TedBottomPicker = TedBottomPicker.Builder(this@ProfileActivity)
                    .setOnImageSelectedListener { uri ->
                        Log.d("ted", "uri:" + uri)
                        Log.d("ted", "uri.path:" + uri.path)
                        selectedUri = uri

                        profile_photo.visibility = View.VISIBLE
                        profile_photo.post {
                            glideRequestManager
                                .load(uri)
                                .fitCenter()
                                .into(profile_photo)
                        }

                        profile_photo.layoutParams = LinearLayout.LayoutParams(400, 400, 1F)
                    }

                    .setSelectedUri(selectedUri)
                    .setPeekHeight(1200)
                    .create()
                bottomSheetDialogFragment.show(supportFragmentManager)
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                showPermissionDenied(deniedPermissions)
            }
        }

        TedPermission(this@ProfileActivity)
            .setPermissionListener(permissionListener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .check()
    }

    private fun showPermissionDenied(deniedPermissions: ArrayList<String>?) {
        Toast.makeText(this@ProfileActivity, "Permission denied\n" +
            deniedPermissions.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun enableFields() {
        profile_name_field.isEnabled = true
        phone_number_profile_field.isEnabled = true
        location_spinner.isClickable = true
        profile_photo.isClickable = true
        edit_profile_button.visibility = View.INVISIBLE
        save_profile_button.visibility = View.VISIBLE
    }

    private fun disableFields() {
        profile_name_field.isEnabled = false
        phone_number_profile_field.isEnabled = false
        location_spinner.isClickable = false
        profile_photo.isClickable = false
        edit_profile_button.visibility = View.VISIBLE
        save_profile_button.visibility = View.INVISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }
}
