@file:Suppress("PackageName")

package gcu.production.guidespacex.UI.CustomInterface

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog
import gcu.production.guidespacex.R

// Класс олицетворения работы мозга человека, когда он впервые решает 3-ые интегралы по выш.мату.
internal class CustomLoadingDialog(val context: Context)
{
    private var loadingDialog: AlertDialog? = null

    @SuppressLint("InflateParams")
    internal fun startLoadingDialog() =
        this.loadingDialog?.show() ?: run {
            val dialogBuilder =
                AlertDialog
                    .Builder(this.context)
                    .setView(
                        (context as Activity).layoutInflater
                            .inflate(
                                R.layout.custom_loading_dialog, null
                            )
                    ).setCancelable(false)

            this.loadingDialog = dialogBuilder.create()
            this.loadingDialog!!.show()
        }

    internal fun stopLoadingDialog() =
        this.loadingDialog?.dismiss()
}