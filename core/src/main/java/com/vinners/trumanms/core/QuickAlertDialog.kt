package com.vinners.trumanms.core

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import androidx.annotation.IntDef
import androidx.appcompat.app.AlertDialog
import com.himanshu.trumanms.core.R

/**
 * Created by Himanshu on 10/6/2017.
 */

object QuickAlertDialog {

    /**
     * Constant for error message
     */
    const val ERROR_MESSAGE = 0

    /**
     * Constant for information message
     */
    const val INFORMATION_MESSAGE = 1

    /**
     * Constant for success message
     */
    const val SUCCESS_MESSAGE = 2

    @Retention(AnnotationRetention.RUNTIME)
    @IntDef(
        ERROR_MESSAGE,
        INFORMATION_MESSAGE,
        SUCCESS_MESSAGE
    )
    annotation class MessageType

    /**
     * Shows A Cancellable Error Dialog
     */
    fun showErrorDialog(context: Context, errorMessage: String) {
        showAlertBox(
            context,
            errorMessage,
            ERROR_MESSAGE,
            true,
            null,
            null
        )
    }

    fun showNonCancellableErrorDialog(context: Context, errorMessage: String) {
        showAlertBox(
            context,
            errorMessage,
            ERROR_MESSAGE,
            false,
            null,
            null
        )
    }

    fun showSuccessDialog(context: Context, message: String) {
        showAlertBox(
            context,
            message,
            SUCCESS_MESSAGE,
            true,
            null,
            null
        )
    }

    fun showNonCancellableSuccessDialog(context: Context, message: String) {
        showAlertBox(
            context,
            message,
            SUCCESS_MESSAGE,
            false,
            null,
            null
        )
    }

    fun showInformationDialog(context: Context, message: String) {
        showAlertBox(
            context,
            message,
            INFORMATION_MESSAGE,
            true,
            null,
            null
        )
    }

    fun showMessageDialogWithButton(context: Context, message: String, listener: OnClickListener) {
        showAlertBox(
            context,
            message,
            INFORMATION_MESSAGE,
            true,
            listener,
            null
        )
    }

    fun showMessageDialogWithBothButton(
        context: Context,
        message: String,
        okListener: OnClickListener,
        cancelListener: OnClickListener
    ) {
        showAlertBox(
            context,
            message,
            INFORMATION_MESSAGE,
            true,
            okListener,
            cancelListener
        )
    }

    fun showNonCancellableMessageDialog(context: Context, message: String, listener: OnClickListener) {
        showAlertBox(
            context,
            message,
            INFORMATION_MESSAGE,
            false,
            listener,
            null
        )
    }

    fun showAlertBox(
        activity: Context,
        message: String,
        @MessageType messageType: Int,
        cancellable: Boolean,
        okButtonClickListener: OnClickListener?,
        onCancelButtonListener: OnClickListener?
    ) {

        // If activity is not shown or Died else continue
        if (!isActive(activity as Activity)) {
            return
        }

        val alertDialog = AlertDialog.Builder(activity).create()
        alertDialog.setMessage(message)
        if (messageType == ERROR_MESSAGE) {
            alertDialog.setIcon(R.drawable.ic_error)
            alertDialog.setTitle("Error")
        } else if (messageType == INFORMATION_MESSAGE) {
            alertDialog.setIcon(R.drawable.ic_information)
            alertDialog.setTitle("Information")
        } else if (messageType == SUCCESS_MESSAGE) {
            alertDialog.setIcon(R.drawable.ic_success)
            alertDialog.setTitle("Success")
        }

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _,_ ->
            alertDialog.cancel()
            okButtonClickListener?.okButtonClicked()
        }
        alertDialog.setCancelable(cancellable)
        alertDialog.show()
    }

    private fun isActive(activity: Activity?): Boolean {
        if (activity != null) {
            try {
                return activity.window.decorView.isShown
            } catch (e: Exception) {
                return false
            }
        }
        return false
    }

    interface OnClickListener {
        fun okButtonClicked()

        fun onCancelButtonClick()
    }
}
