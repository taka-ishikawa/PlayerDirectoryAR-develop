package com.example.playerdirectoryar

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.fragment_news.*


class NewsFragment : Fragment() {

//    private var callback: OnHeadlineSelectedListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://www.jleague.jp/sp/news/")
    }

//    fun setOnHeadlineSelectedListener(callback: OnHeadlineSelectedListener) {
//        this.callback = callback
//    }
//
//    // This interface can be implemented by the Activity, parent Fragment,
//    // or a separate test implementation.
//    interface OnHeadlineSelectedListener {
//        fun onArticleSelected(position: Int, clubName: String)
//    }
}