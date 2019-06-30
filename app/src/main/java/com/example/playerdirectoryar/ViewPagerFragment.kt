package com.example.playerdirectoryar

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import kotlinx.android.synthetic.main.fragment_view_pager.*


class ViewPagerFragment : Fragment(),
    HomeFragment.OnHeadlineSelectedListener, TableClubsFragment.OnHeadlineSelectedListener {

    private var callback: OnHeadlineSelectedListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.appbar_home, menu)
        val itemManager = menu!!.findItem(R.id.item_manager)
        itemManager.setOnMenuItemClickListener {
            val intent = Intent(context, ManagerActivity::class.java)
            startActivity(intent)

            return@setOnMenuItemClickListener true
        }
    }

    override fun onResume() {
        super.onResume()

        val pagerAdapterHome = PagerAdapterHome(childFragmentManager)
        viewPagerHome.adapter = pagerAdapterHome
        tabLayout.setupWithViewPager(viewPagerHome)
    }

    fun setOnHeadlineSelectedListener(callback: OnHeadlineSelectedListener) {
        this.callback = callback
    }

    override fun onArticleSelected(position: Int, clubName: String) {
        callback!!.onArticleSelected(position, clubName) // そのまま受け流す: Home/ TableClubs -> this fragment -> Main
    }

    override fun onSetTitle(title: String) {
        callback!!.onSetTitle(title) // そのまま受け流す
    }

    override fun onAttachFragment(fragment: Fragment) {
        when (fragment) {
            is HomeFragment -> {
                val homeFragment: HomeFragment = fragment
                homeFragment.setOnHeadlineSelectedListener(this)
            }
            is TableClubsFragment -> {
                val tableClubsFragment: TableClubsFragment = fragment
                tableClubsFragment.setOnHeadlineSelectedListener(this)
            }
        }
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnHeadlineSelectedListener {
        fun onArticleSelected(position: Int, clubName: String)
        fun onSetTitle(title: String)
    }
}