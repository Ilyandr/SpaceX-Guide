@file:Suppress("PackageName")
package gcu.production.guidespacex.Service

import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

internal class NavigationApp (
    @field:IdRes private val navigateID : Int? = null
    , private val fragment: Fragment? = null
    , private val activity: FragmentActivity
)
{
    fun <T> navigate(arguments: Pair<String, T?>? = null) =
        this.fragment?.let { navigateTo ->
            navigateID?.let { res ->
                if (arguments?.second is Parcelable)
                {
                    val bundle = Bundle()
                    bundle.putParcelable(
                        arguments.first
                        , arguments.second as Parcelable
                    )
                    navigateTo.arguments = bundle
                }
                this.activity
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(res, navigateTo)
                    .commitNow()
            }
        } ?: this.activity.onBackPressed()

    companion object {
        internal const val NAVIGATE_KEY = "key0"
    }
}

internal interface ActivityOnBackPressed {
    fun callOnBackPressed(activity: FragmentActivity): Any
}
