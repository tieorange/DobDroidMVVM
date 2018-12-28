package ro.dobrescuandrei.demonewlibs.view.sticky_headers

import android.content.Context
import android.util.AttributeSet
import ro.andreidobrescu.declarativeadapterkt.view.HeaderView
import ro.dobrescuandrei.demonewlibs.R
import ro.dobrescuandrei.demonewlibs.model.SecondPageHeader

class SecondPageHeaderView : HeaderView<SecondPageHeader>
{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun layout(): Int = R.layout.header_second_page

    override fun setData(header : SecondPageHeader) {}
}