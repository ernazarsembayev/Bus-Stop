package com.yernazar.pidapplication.presentation.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yernazar.pidapplication.R
import com.yernazar.pidapplication.data.repository.model.Trip
import com.yernazar.pidapplication.databinding.FragmentTripBinding
import com.yernazar.pidapplication.domain.SharedViewModel
import com.yernazar.pidapplication.domain.usecases.DeleteFavouriteTripUseCase
import com.yernazar.pidapplication.domain.usecases.GetFavouriteTripByUid
import com.yernazar.pidapplication.domain.usecases.SaveFavouriteTripUseCase
import com.yernazar.pidapplication.utils.config.Config
import com.yernazar.pidapplication.utils.mapper.TripMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat

class TripFragment : BaseFragment() {

    private lateinit var binding: FragmentTripBinding
    override val bottomSheetState = BottomSheetBehavior.STATE_HALF_EXPANDED
    override val name = Config.TRIP_FRAGMENT_NAME

    private val getFavouriteTripByUid: GetFavouriteTripByUid by inject()
    private val saveFavouriteTripUseCase: SaveFavouriteTripUseCase by inject()
    private val deleteFavouriteTripUseCase: DeleteFavouriteTripUseCase by inject()

    private val mViewModel: SharedViewModel by sharedViewModel()

    private var selectedTrip: Trip? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTripBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences(Config.SHARED_PREFERENCES,
            Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(Config.SP_TOKEN, null)
        if (token != null) {

            binding.likeIb.visibility = View.VISIBLE

            binding.likeIb.setOnClickListener{
                selectedTrip?.let {

                    CoroutineScope(Dispatchers.Default).launch {
                        val trip = getFavouriteTripByUid.execute(it.uid)

                        if (trip != null) {
                            deleteFavouriteTripUseCase.execute(it, token)
                            setUnLike()
                        } else {
                            saveFavouriteTripUseCase.execute(it, token)
                            setLike()
                        }
                    }
                }
            }
        }

        val pattern = SimpleDateFormat("hh:mm")

        mViewModel.liveDataVehicle.observe(viewLifecycleOwner, {
            binding.routeNameTv.text = it.originRouteName
            binding.directionNameTv.text = it.lastStop.name
            binding.nextBusArrivalTv.text = pattern.format(it.nextStopArrival)
            binding.delayVariableTv.text = it.delay.toString()

            selectedTrip = TripMapper.toTrip(it.trip)

            if (token != null) {
                CoroutineScope(Dispatchers.Default).launch {
                    val favouriteRoute = getFavouriteTripByUid.execute(tripUid = it.trip.uid)

                    favouriteRoute?.let {
                        setLike()
                    }
                }
            }
        })
    }

    private fun setLike() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.likeIb.setImageResource(R.drawable.ic_baseline_favorite_24)
        }
    }

    private fun setUnLike() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.likeIb.setImageResource(R.drawable.ic_favorite_border)
        }
    }
}