@file:Suppress("PackageName")
package gcu.production.guidespacex.GeneralImpl

internal interface NetworkActions
{
    fun networkFaultConnection()
    fun launchWithCheckNetworkConnection()
}