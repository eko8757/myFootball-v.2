package com.example.eko8757.footballclubmatchschedule.match

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.example.eko8757.footballclubmatchschedule.adapter.EventAdapter
import com.example.eko8757.footballclubmatchschedule.api.ApiRepository
import com.example.eko8757.footballclubmatchschedule.main.DetailActivity
import com.example.eko8757.footballclubmatchschedule.main.MatchView
import com.example.eko8757.footballclubmatchschedule.model.Events
import com.example.eko8757.footballclubmatchschedule.presenter.MatchPresenter

import com.example.eko8757.footballclubmatchschedule.R
import com.example.eko8757.footballclubmatchschedule.util.invisible
import com.example.eko8757.footballclubmatchschedule.util.visible
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_next_match.*
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.onRefresh


class NextMatch : Fragment(), MatchView {
    private var events: MutableList<Events> = mutableListOf()
    private lateinit var presenter: MatchPresenter
    private lateinit var eventList: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapter: EventAdapter
    private var listener: OnFragmentInteractionListener? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView: View = inflater.inflate(R.layout.fragment_next_match, container, false)
        eventList = rootView.findViewById(R.id.nextEventMatch) as RecyclerView
        eventList.layoutManager = LinearLayoutManager(activity)

        adapter = EventAdapter(events) {
            activity?.startActivity(intentFor<DetailActivity>("idEvent" to "${it.eventId}"))
        }

        eventList.adapter = adapter
        progressBar = rootView.findViewById(R.id.progressBar) as ProgressBar
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefresh) as SwipeRefreshLayout

        val request = ApiRepository()
        val gson = Gson()
        presenter = MatchPresenter(this, request, gson)

        presenter.getNextEventsList()

        swipeRefreshLayout.onRefresh {
            presenter.getNextEventsList()
        }

        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showPrevEvent(data: List<Events>) {
        swipeRefreshLayout.isRefreshing = false
        events.clear()
        events.addAll(data)
        adapter.notifyDataSetChanged()
    }

    override fun showNextEvent(data: List<Events>) {
        swipeRefresh.isRefreshing = false
        events.clear()
        events.addAll(data)
        adapter.notifyDataSetChanged()
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }


}
