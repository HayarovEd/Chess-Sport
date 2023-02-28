package bk.stavki.na.sport.app.ui.home

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import bk.stavki.na.sport.app.R
import bk.stavki.na.sport.app.databinding.FragmentHomeBinding
import bk.stavki.na.sport.app.ui.home.HomeState.Error
import bk.stavki.na.sport.app.ui.home.HomeState.Loading
import bk.stavki.na.sport.app.ui.home.HomeState.NoInternet
import bk.stavki.na.sport.app.ui.home.HomeState.SuccessConnect
import bk.stavki.na.sport.app.utils.SAVED_SETTINGS
import bk.stavki.na.sport.app.utils.URL

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var currentState: HomeState
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentState = Loading
        val sharedPref =
            requireContext().getSharedPreferences(SAVED_SETTINGS, Context.MODE_PRIVATE)
        val sharedUrl = sharedPref.getString(URL, "")
        viewModel.getFromLocal(
            checkedInternetConnection = checkedInternetConnection(),
            pathUrl = sharedUrl ?: ""
        )
        viewModel.showData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Loading -> {
                    binding.startBt.isVisible = false
                    currentState =  state
                }
                is NoInternet -> {
                    binding.startBt.isVisible = true
                    currentState =  state
                }
                is SuccessConnect -> {
                    binding.startBt.isVisible = true
                    val editor = sharedPref.edit()
                    editor.putString(URL, state.remoteData.urlPath)
                    editor.apply()
                    currentState =  state
                }
                is Error -> {
                    binding.startBt.isVisible = true
                    currentState =  state
                }
            }
        }
        binding.startBt.setOnClickListener {
            when (currentState) {
                is SuccessConnect -> {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse((currentState as SuccessConnect).remoteData.urlPath))
                    startActivity(browserIntent)
                    Handler(Looper.getMainLooper()).postDelayed({
                        view.findNavController().navigate(R.id.action_navigation_home_to_navigation_dashboard)
                    }, 500)
                }
                else -> {
                    view.findNavController().navigate(R.id.action_navigation_home_to_navigation_dashboard)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkedInternetConnection() : Boolean {
        var result = false
        val connectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }
}