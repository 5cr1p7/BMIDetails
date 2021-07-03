package com.ramilkapev.bmidetails.details

import android.app.Activity
import android.text.format.DateFormat.format
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import android.os.Environment
import java.io.File
import android.graphics.Bitmap
import java.io.FileOutputStream
import java.io.IOException
import androidx.core.content.FileProvider
import com.ramilkapev.bmidetails.BuildConfig
import android.content.Intent
import android.widget.Toast

import android.content.ActivityNotFoundException
import android.graphics.Canvas

class DetailsFragmentViewModel : ViewModel() {

    private val _viewState: MutableLiveData<DetailsViewState> = MutableLiveData()
    val viewState: LiveData<DetailsViewState> = _viewState

    fun processArgs(args: DetailsFragmentArgs) {
        _viewState.value = DetailsViewState(
            bmi = args.bmi,
            ponderalIndex = args.ponderalIndex,
            name = args.name
        )
    }

    fun takeScreenshot(view: View, activity: Activity) {
        val date = Date()
        val format: CharSequence = format("MM-dd-yyyy_hh:mm:ss", date)

        try {
            val mainDir = File(
                activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "FileShare"
            )
            if (!mainDir.exists()) {
                val mkdir: Boolean = mainDir.mkdir()
            }

            val path = "$mainDir/BMIDetails-$format.jpeg"
            val bitmap = createBitmap(view)

            val imageFile = File(path)
            val fileOutputStream = FileOutputStream(imageFile)
            bitmap?.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream)

            val canvas = bitmap?.let { Canvas(it) }
            view.draw(canvas)

            fileOutputStream.flush()
            fileOutputStream.close()

            shareScreenShot(imageFile, activity)
        } catch (e: IOException) {
            e.printStackTrace();
        }
    }

    private fun createBitmap(view: View): Bitmap? {
        val bitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun shareScreenShot(imageFile: File, activity: Activity) {
        val uri = FileProvider.getUriForFile(
            activity,
            BuildConfig.APPLICATION_ID + "." + activity.localClassName + ".provider",
            imageFile
        )

        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_TEXT, "Look at my Body Mass Index")
        intent.putExtra(Intent.EXTRA_STREAM, uri)

        try {
            activity.startActivity(Intent.createChooser(intent, "Share With"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "No App Available", Toast.LENGTH_SHORT).show()
        }
    }
}