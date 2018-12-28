package ro.dobrescuandrei.mvvm.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.greenrobot.eventbus.Subscribe
import ro.andreidobrescu.declarativeadapterkt.BaseDeclarativeAdapter
import ro.andreidobrescu.declarativeadapterkt.view.HeaderView
import ro.dobrescuandrei.mvvm.BaseFragment
import ro.dobrescuandrei.mvvm.R
import ro.dobrescuandrei.mvvm.list.item_decoration.FABDividerItemDecoration
import ro.dobrescuandrei.mvvm.list.item_decoration.StickyHeadersItemDecoration
import ro.dobrescuandrei.mvvm.utils.OnKeyboardClosedEvent
import ro.dobrescuandrei.mvvm.utils.OnKeyboardOpenedEvent

abstract class BaseListFragment<VIEW_MODEL : BaseListViewModel<*>, ADAPTER : BaseDeclarativeAdapter> : BaseFragment<VIEW_MODEL>()
{
    lateinit var recyclerView : RecyclerViewMod
    lateinit var emptyView : TextView
    var stickyHeadersItemDecoration : StickyHeadersItemDecoration? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view=super.onCreateView(inflater, container, savedInstanceState)!!

        recyclerView=view.findViewById(R.id.recyclerView)
        emptyView=view.findViewById(R.id.emptyView)

        view.findViewById<View>(R.id.addButton)?.setOnClickListener { onAddButtonClicked() }

        recyclerView.layoutManager=provideLayoutManager()

        val adapter=provideAdapter()
        recyclerView.adapter=adapter

        val decoration=provideItemDecoration()
        if (decoration!=null)
        {
            recyclerView.addItemDecoration(decoration)
        }

        if (hasStickyHeaders())
        {
            stickyHeadersItemDecoration=StickyHeadersItemDecoration(
                /*headerViewInstantiator*/  { provideStickyHeaderView(it) },
                /*headerModelClassProvider*/{ provideStickyHeaderModelClass(it) })
            recyclerView.addItemDecoration(stickyHeadersItemDecoration!!)
        }

        if (shouldLoadMoreOnScroll())
        {
            recyclerView.loadMoreDataAction={
                viewModel().loadMoreData()
            }
        }

        emptyView.text = provideEmptyViewText()

        viewModel().run {
            firstPageItems.value=null
            nextPageItems.value=null
            isEmpty.value=false

            firstPageItems.observe(this@BaseListFragment) { items ->
                recyclerView.adapter?.setItems(items)
                recyclerView.scrollToPosition(0)
            }

            nextPageItems.observe(this@BaseListFragment) { items ->
                recyclerView.adapter?.addItems(items)
            }

            isEmpty.observe(this@BaseListFragment) { isEmpty ->
                if (isEmpty)
                {
                    emptyView.visibility=View.VISIBLE
                    recyclerView.visibility=View.GONE
                }
                else
                {
                    emptyView.visibility=View.GONE
                    recyclerView.visibility=View.VISIBLE
                }
            }
        }

        viewModel().loadData()

        return view
    }

    abstract fun provideAdapter() : ADAPTER
    open fun provideLayoutManager() : RecyclerView.LayoutManager = LinearLayoutManager(context)
    open fun provideItemDecoration() : RecyclerView.ItemDecoration? = FABDividerItemDecoration(context)
    open fun shouldLoadMoreOnScroll() : Boolean = true
    open fun provideEmptyViewText(): String = getString(R.string.no_items)
    open fun hasStickyHeaders() : Boolean = false
    open fun provideStickyHeaderModelClass(position : Int) : Class<*>? = null
    open fun provideStickyHeaderView(position : Int) : HeaderView<*>? = null

    override fun onDestroy()
    {
        stickyHeadersItemDecoration?.onDestroy()
        stickyHeadersItemDecoration=null

        super.onDestroy()
    }

    @Subscribe
    fun onKeyboardOpened(event : OnKeyboardOpenedEvent)
    {
        try { view?.findViewById<View>(R.id.addButton)?.visibility=View.GONE }
        catch (e : Exception) {}
    }

    @Subscribe
    fun onKeyboardClosed(event : OnKeyboardClosedEvent)
    {
        try { view?.findViewById<View>(R.id.addButton)?.visibility=View.VISIBLE }
        catch (e : Exception) {}
    }

    open fun onAddButtonClicked()
    {
    }
}