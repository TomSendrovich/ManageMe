package com.e.manageme2.ui

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.e.manageme2.R
import com.google.gson.Gson
import com.yalantis.beamazingtoday.interfaces.BatModel
import www.sanju.motiontoast.MotionToast

fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
fun Context.motionToastSuccess(message: String, activity: Activity) = MotionToast.createToast(
    activity, message,
    MotionToast.TOAST_SUCCESS,
    MotionToast.GRAVITY_BOTTOM,
    MotionToast.LONG_DURATION,
    ResourcesCompat.getFont(this, R.font.helvetica_regular)
)

fun Context.motionToastError(message: String, activity: Activity) = MotionToast.createToast(
    activity, message,
    MotionToast.TOAST_ERROR,
    MotionToast.GRAVITY_BOTTOM,
    MotionToast.LONG_DURATION,
    ResourcesCompat.getFont(this, R.font.helvetica_regular)
)

fun Context.motionToastInfo(message: String, activity: Activity) = MotionToast.createToast(
    activity, message,
    MotionToast.TOAST_INFO,
    MotionToast.GRAVITY_BOTTOM,
    MotionToast.LONG_DURATION,
    ResourcesCompat.getFont(this, R.font.helvetica_regular)
)

fun Context.motionToastDelete(message: String, activity: Activity) = MotionToast.createToast(
    activity, message,
    MotionToast.TOAST_DELETE,
    MotionToast.GRAVITY_BOTTOM,
    MotionToast.LONG_DURATION,
    ResourcesCompat.getFont(this, R.font.helvetica_regular)
)


