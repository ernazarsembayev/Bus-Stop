package com.yernazar.pidapplication.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yernazar.pidapplication.databinding.FragmentRouteBinding
import com.yernazar.pidapplication.domain.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RouteFragment : Fragment() {

    private lateinit var binding: FragmentRouteBinding
    val bottomSheetState = BottomSheetBehavior.STATE_HALF_EXPANDED

    private val mViewModel: SharedViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRouteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel.liveDataRoute.observe(viewLifecycleOwner, {
            binding.routeShortNameTv.text = it.shortName
            binding.routeLongNameTv.text = it.longName
        })

    }

}