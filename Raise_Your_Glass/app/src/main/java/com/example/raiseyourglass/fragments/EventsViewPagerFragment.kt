package com.example.raiseyourglass.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.raiseyourglass.R
import com.example.raiseyourglass.activities.StartActivity
import kotlinx.android.synthetic.main.fragment_events_view_pager.*

class EventsViewPagerFragment : Fragment(R.layout.fragment_events_view_pager) {

    private var setCurrentFragment: (fragment: Fragment, id: Int) -> Unit = { _, _ -> null }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as StartActivity
        setCurrentFragment = activity.lambdaSetCurrentFragmentForViewPagers

        val eventListFragment = EventsListFragment()
        val myEventsFragment = MyEventsFragment()
        val id = R.id.flEventsViewPagerFragment

        btnEventFragmentI.setOnClickListener {
            setCurrentFragment(eventListFragment, id)
        }

        btnEventFragmentII.setOnClickListener {
            setCurrentFragment(myEventsFragment, id)
        }
    }

}