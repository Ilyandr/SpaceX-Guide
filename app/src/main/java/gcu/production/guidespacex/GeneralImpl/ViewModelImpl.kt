@file:Suppress("PackageName")
package gcu.production.guidespacex.GeneralImpl

import gcu.production.guidespacex.CustomInterface.ViewInflator

internal interface ViewModelImpl
{
    fun actionStateFaultLoading()
    fun actionStateDefaultLoading()
    fun actionStateSuccessLoading(viewInflator: ViewInflator? = null)
}