package io.thedatapirates.cashplan.domains.helpcenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.thedatapirates.cashplan.R

/**
 * A simple [Fragment] subclass.
 * Use the [HelpCenterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HelpCenterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help_center, container, false)
    }

}