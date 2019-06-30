package com.example.playerdirectoryar

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),
    ViewPagerFragment.OnHeadlineSelectedListener,
    ClubDetailFragment.OnHeadlineSelectedListener, SearchFragment.OnHeadlineSelectedListener {

    private lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionBar = supportActionBar!!
        onReplaceFragment(ViewPagerFragment())
    }

    override fun onResume() {
        super.onResume()

        actionBar.title = getString(R.string.title_app)

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                onReplaceFragment(ViewPagerFragment())
                actionBar.title = getString(R.string.title_app)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_ar -> {
                val intent = Intent(this, ArActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {
                onReplaceFragment(SearchFragment())
                actionBar.title = getString(R.string.title_search)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_news -> {
                onReplaceFragment(NewsFragment())
                actionBar.title = getString(R.string.title_news)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onAttachFragment(fragment: Fragment) {
        when (fragment) {
            is ViewPagerFragment -> {
                val viewPagerFragment: ViewPagerFragment = fragment
                viewPagerFragment.setOnHeadlineSelectedListener(this)
            }
            is ClubDetailFragment -> {
                val clubDetailFragment: ClubDetailFragment = fragment
                clubDetailFragment.setOnHeadlineSelectedListener(this)
            }
            is SearchFragment -> {
                val  searchFragment: SearchFragment = fragment
                searchFragment.setOnHeadlineSelectedListener(this)
            }
        }
    }

    override fun onArticleSelected(position: Int, clubName: String) { // position is NOT needed, so you should delete later
        val clubFrag = supportFragmentManager.findFragmentById(R.id.fragmentClubDetail) as ClubDetailFragment?

        if (clubFrag != null) {
            // If article frag is available, we're in two-pane layout...

            // Call a method in the ClubDetailFragment to update its content
            clubFrag.updateClubView()
        } else {
            // Otherwise, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            val newFragment = ClubDetailFragment()
            val args = Bundle()
            args.putString(INTENT_KEY_CLUB_NAME, clubName)
            newFragment.arguments = args

            val transaction = supportFragmentManager.beginTransaction()

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.add(R.id.frameLayout, newFragment)
            transaction.hide(supportFragmentManager.fragments.first())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    // TODO: "onReplace" -> add/ hide/ remove. onReplace should be used minimally.
    override fun onReplaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }

    override fun onSetTitle(title: String) {
        actionBar.title = title
    }
}