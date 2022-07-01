@file:Suppress("PackageName")
package gcu.production.guidespacex.Data

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

// С такой реактивностью недалеко до событий 1986 года..
internal class ObservableList<T>(
    val wrapped: MutableList<T>
    , private var actionChange: ((newListData: MutableList<T>) -> Unit)? = null
) : MutableList<T> by wrapped, Observable()
{
    @OptIn(DelicateCoroutinesApi::class)
    override fun add(element: T) =
        if (wrapped.add(element))
        {
            setChanged()
            notifyObservers()

            GlobalScope.launch(Dispatchers.Main)
            {
                this@ObservableList
                    .actionChange
                    ?.invoke(wrapped)
            }
            true
        }
        else false

    internal fun setActionChange(
        newActionChange: (newListData: MutableList<T>) -> Unit)
    {
        if (actionChange != null)
        {
            val actionChangeSave = actionChange
            actionChange =
                { list ->
                    actionChangeSave?.invoke(list)
                    newActionChange.invoke(list)
                }
        }
        else
            this.actionChange = newActionChange
    }
}