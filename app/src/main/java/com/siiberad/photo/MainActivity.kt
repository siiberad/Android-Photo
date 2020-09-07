package com.siiberad.photo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File


class MainActivity : AppCompatActivity() {

    companion object {
        const val CAMERA_PIC_REQUEST = 98
        const val LOCAL_PIC_REQUEST = 99
    }

    lateinit var vm: MainViewModel
    private var isFABOpen = false
    var number = 0
    var listData: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        vm = ViewModelProvider(this).get(MainViewModel::class.java)
        initClick()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_PIC_REQUEST -> {
                    val file = File(this.externalCacheDir!!.absolutePath, "MyPhoto$number.jpg")
                    val uri = FileProvider.getUriForFile(
                        this,
                        this.applicationContext.packageName + ".provider",
                        file
                    )
                    if (uri != null) {
                        listData.add(uri.toString())
                        vm.setdata(listData)
                    }
                }
                LOCAL_PIC_REQUEST -> {
                    val selectedImageUri = data?.data
                    val picturePath = getPath(applicationContext, selectedImageUri)
                    if (picturePath != null) {
                        listData.add(selectedImageUri.toString())
                        vm.setdata(listData)
                    }
                }
            }
        }
    }

    private fun getPath(context: Context, uri: Uri?): String? {
        var result: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = context.contentResolver.query(uri!!, proj, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val column_index: Int = cursor.getColumnIndexOrThrow(proj[0])
                result = cursor.getString(column_index)
            }
            cursor.close()
        }
        if (result == null) {
            result = "Not found"
        }
        return result
    }

    private fun initClick() {
        fab.setOnClickListener {
            if (!isFABOpen) {
                showFABMenu();
            } else {
                closeFABMenu();
            }
        }

        fab1.setOnClickListener {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            number++
            val file = File(this.externalCacheDir!!.absolutePath, "MyPhoto$number.jpg")
            val uri = FileProvider.getUriForFile(
                this,
                this.applicationContext.packageName + ".provider",
                file
            )
            i.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            startActivityForResult(i, CAMERA_PIC_REQUEST)
        }

        fab2.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, LOCAL_PIC_REQUEST)
        }
    }


    private fun showFABMenu() {
        isFABOpen = true
        fab.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.close))
        fab1.animate().translationY(-resources.getDimension(R.dimen.standard_55))
        fab2.animate().translationY(-resources.getDimension(R.dimen.standard_105))
    }

    private fun closeFABMenu() {
        isFABOpen = false
        fab.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.add))
        fab1.animate().translationY(0F)
        fab2.animate().translationY(0F)
    }

    override fun onBackPressed() {
        when(NavHostFragment.findNavController(nav_host_fragment).currentDestination?.id) {
            R.id.SecondFragment-> {
                super.onBackPressed()
            }
            else -> {
                if (!isFABOpen) {
                    super.onBackPressed()
                } else {
                    closeFABMenu()
                }
            }
        }
    }
    fun hideFAB(){
        fab?.visibility = View.GONE
        fab1?.visibility = View.GONE
        fab2?.visibility = View.GONE
    }

    fun showFAB(){
        fab?.visibility = View.VISIBLE
        fab1?.visibility = View.VISIBLE
        fab2?.visibility = View.VISIBLE
    }
}