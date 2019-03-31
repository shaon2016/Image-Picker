package com.lastblade.imagepickertest

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.esafirm.imagepicker.features.ImagePicker
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_GALLERY_IMAGE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPick.setOnClickListener {
            showImagePickerDialog()
        }
    }

    private fun showImagePickerDialog() {
        val myItems = listOf("Camera", "Gallery")

        MaterialDialog(this).show {
            listItems(items = myItems) { dialog, index, text ->
                when (index) {
                    0 -> {
                        dispatchTakePictureIntent()
                    }
                    1 -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            chooseFromGallery()
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun chooseFromGallery() {
        ImagePicker.create(this)
            .toolbarFolderTitle(getString(R.string.folder)) // folder selection title
            .toolbarImageTitle(getString(R.string.tap_to_select)) // image selection title
            .toolbarArrowColor(Color.BLACK)
            .limit(5)
            .showCamera(false)
            .toolbarArrowColor(ContextCompat.getColor(this, R.color.ef_black_alpha_50))
            .start(REQUEST_GALLERY_IMAGE)
    }

    private fun dispatchTakePictureIntent() {
        /*Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }*/

        ImagePicker.cameraOnly().start(this, REQUEST_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            /* val imageBitmap = data?.extras?.get("data") as Bitmap
             iv.setImageBitmap(imageBitmap)*/

            val i = ImagePicker.getFirstImageOrNull(data)
            val myBitmap = BitmapFactory.decodeFile(i.path)
            iv.setImageBitmap(myBitmap)
        } else if (requestCode == REQUEST_GALLERY_IMAGE) {
            val images = ImagePicker.getImages(data)

            val myBitmap = BitmapFactory.decodeFile(images[0].path)
            iv.setImageBitmap(myBitmap)
        }
    }

}
