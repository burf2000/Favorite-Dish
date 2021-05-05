package com.burf.favdish.utils

import android.os.Build
import android.text.Html

object Helper {

    fun stripHtml(html : String) : String {
        var text = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            text = Html.fromHtml(
                html,
                Html.FROM_HTML_MODE_COMPACT
            ).toString()
        } else {
            @Suppress("DEPRECATION")
            text = Html.fromHtml(html).toString()
        }

        return text
    }
}