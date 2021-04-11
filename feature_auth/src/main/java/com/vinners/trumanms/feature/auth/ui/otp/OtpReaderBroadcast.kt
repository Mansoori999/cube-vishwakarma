package com.vinners.trumanms.feature.auth.ui.otp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class OtpReaderBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val smsExtras = extras!!.get(SmsRetriever.EXTRA_STATUS) ?: return

            val status = smsExtras as Status

            when (status.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                    extractOtp(context, message)
                }
            }
        }
    }

    /**
     * Extracts Otp From Message
     */
    private fun extractOtp(context: Context, message: String?) {
        if (message == null)
            return

        if (message.contains("\n")) {

            val lastIndex = message.lastIndexOf("\n")
            if (lastIndex > 4) {
                val startingIndex = lastIndex - 4
                val otpReceived = message.substring(startingIndex, lastIndex)

                val smsInt = Intent(ACTION_OTP_DELIVER)
                smsInt.putExtra(INTENT_EXTRA_OTP_RECEIVED, otpReceived)
                LocalBroadcastManager.getInstance(context).sendBroadcast(smsInt)
            }
        }
    }

    companion object {
        const val ACTION_OTP_DELIVER = "ACTION_OTP_DELIVER"
        const val INTENT_EXTRA_OTP_RECEIVED = "INTENT_EXTRA_OTP_RECEIVED"
    }
}




