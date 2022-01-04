package com.yernazar.pidapplication.presentation.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yernazar.pidapplication.R
import com.yernazar.pidapplication.data.repository.model.Route
import com.yernazar.pidapplication.databinding.FragmentRouteBinding
import com.yernazar.pidapplication.domain.MapsSharedViewModel
import com.yernazar.pidapplication.domain.usecases.DeleteFavouriteRouteUseCase
import com.yernazar.pidapplication.domain.usecases.GetFavouriteRouteByUid
import com.yernazar.pidapplication.domain.usecases.SaveFavouriteRouteUseCase
import com.yernazar.pidapplication.utils.config.Config
import com.yernazar.pidapplication.utils.config.Config.SP_TOKEN
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RouteFragment : BaseFragment() {

    private lateinit var binding: FragmentRouteBinding
    override val name = Config.ROUTE_FRAGMENT_NAME
    override val bottomSheetState = BottomSheetBehavior.STATE_HALF_EXPANDED

    private val getFavouriteRouteByUid: GetFavouriteRouteByUid by inject()
    private val deleteFavouriteRouteUseCase: DeleteFavouriteRouteUseCase by inject()
    private val saveFavouriteRoute: SaveFavouriteRouteUseCase by inject()

    private var selectedRoute: Route? = null
    private lateinit var sharedPreferences: SharedPreferences

    private val mViewModelMaps: MapsSharedViewModel by sharedViewModel()

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

        sharedPreferences = requireActivity().getSharedPreferences(Config.SHARED_PREFERENCES,Context.MODE_PRIVATE)

        val token = sharedPreferences.getString(SP_TOKEN, null)
        if (token != null) {

            binding.likeIb.visibility = View.VISIBLE

            binding.likeIb.setOnClickListener{
                selectedRoute?.let {

                    CoroutineScope(Dispatchers.Default).launch {
                        val route = getFavouriteRouteByUid.execute(it.uid)

                        if (route != null) {
                            deleteFavouriteRouteUseCase.execute(selectedRoute!!, token)
                            setUnLike()
                        } else {
                            saveFavouriteRoute.execute(selectedRoute!!, token)
                            setLike()
                        }
                    }

                }
            }
        }

        mViewModelMaps.liveDataRoute.observe(viewLifecycleOwner, {
            binding.routeShortNameTv.text = it.shortName
            binding.routeLongNameTv.text = it.longName

            if (token != null){
                CoroutineScope(Dispatchers.Default).launch {
                    val favouriteRoute = getFavouriteRouteByUid.execute(routeUid = it.uid)

                    favouriteRoute?.let {
                        setLike()
                    }
                }
            }

            selectedRoute = it
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