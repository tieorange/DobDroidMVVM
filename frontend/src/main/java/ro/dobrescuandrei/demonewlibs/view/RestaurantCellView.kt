package ro.dobrescuandrei.demonewlibs.view

import android.content.Context
import android.util.AttributeSet
import kotlinx.android.synthetic.main.cell_restaurant.view.*
import ro.andreidobrescu.declarativeadapterkt.view.CellView
import ro.dobrescuandrei.demonewlibs.R
import ro.dobrescuandrei.demonewlibs.activity.RestaurantListActivity
import ro.dobrescuandrei.demonewlibs.model.Restaurant
import ro.dobrescuandrei.demonewlibs.router.ActivityRouter

class RestaurantCellView : CellView<Restaurant>
{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun layout() : Int = R.layout.cell_restaurant

    override fun setData(restaurant : Restaurant)
    {
        nameTv.text=restaurant.name

        nameTv.setOnClickListener {
            if (it.context is RestaurantListActivity)
                ActivityRouter.startRestaurantDetailsActivity(from = it.context, restaurant = restaurant)
            else ActivityRouter.startEditRestaurantActivity(from = it.context, restaurant = restaurant)
        }
    }
}