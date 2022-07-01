@file:Suppress("PackageName")
package gcu.production.guidespacex.Data

import android.widget.Toast

internal sealed class LoadingState
{
    object DefaultLoadingState : LoadingState()
    object LoadingProcessState : LoadingState()
    object LoadingSuccessLoadingState : LoadingState()

    class LoadingErrorLoadingState(
        private val messageError: Toast?
        ) : LoadingState()
    {
        fun showMessageError() =
            this.messageError?.show()
    }
}