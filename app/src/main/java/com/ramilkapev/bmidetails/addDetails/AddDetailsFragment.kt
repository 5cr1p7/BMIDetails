package com.ramilkapev.bmidetails.addDetails

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.ramilkapev.bmidetails.Constants.INTERSTITIAL_AD_UNIT_ID
import com.ramilkapev.bmidetails.Constants.MAX_HEIGHT
import com.ramilkapev.bmidetails.Constants.MAX_WEIGHT
import com.ramilkapev.bmidetails.Constants.MIN_HEIGHT
import com.ramilkapev.bmidetails.Constants.MIN_WEIGHT
import com.ramilkapev.bmidetails.R
import com.ramilkapev.bmidetails.extensions.callError
import com.ramilkapev.bmidetails.extensions.clearError
import com.ramilkapev.bmidetails.databinding.FragmentAddDetailsBinding

class AddDetailsFragment : Fragment() {

    private val viewModel: AddDetailsFragmentViewModel by viewModels()

    private var _binding: FragmentAddDetailsBinding? = null
    private val binding: FragmentAddDetailsBinding get() = requireNotNull(_binding)

    private var mInterstitialAd: InterstitialAd? = null
    private var navController: NavController? = null
    private val genders: Array<String> = arrayOf("Female", "Male")

    private var bmi = 0f
    private var ponderalIndex = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddDetailsBinding.inflate(inflater, container, false)
        requestAd()

        binding.weightNp.minValue = MIN_WEIGHT
        binding.weightNp.maxValue = MAX_WEIGHT
        binding.weightNp.setSelectedTypeface(
            Typeface.createFromAsset(
                requireActivity().assets,
                "fonts/poppins_bold.ttf"
            )
        )
        binding.weightNp.typeface =
            Typeface.createFromAsset(requireActivity().assets, "fonts/poppins.ttf")

        binding.heightNp.minValue = MIN_HEIGHT
        binding.heightNp.maxValue = MAX_HEIGHT
        binding.heightNp.setSelectedTypeface(
            Typeface.createFromAsset(
                requireActivity().assets,
                "fonts/poppins_bold.ttf"
            )
        )
        binding.heightNp.typeface =
            Typeface.createFromAsset(requireActivity().assets, "fonts/poppins.ttf")

        binding.genderNp.minValue = 0
        binding.genderNp.maxValue = 1
        binding.genderNp.displayedValues = genders
        binding.genderNp.setSelectedTypeface(
            Typeface.createFromAsset(
                requireActivity().assets,
                "fonts/poppins_bold.ttf"
            )
        )
        binding.genderNp.typeface =
            Typeface.createFromAsset(requireActivity().assets, "fonts/poppins.ttf")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Add BMI Details"

        navController = Navigation.findNavController(view)

        binding.calculateBtn.setOnClickListener {
            when {
                binding.nameEt.text?.isEmpty() == true -> {
                    binding.nameEtLayout.callError(R.string.error_name_is_empty)
                }
                binding.weightNp.value == 0 -> {
                    Toast.makeText(
                        requireContext(),
                        R.string.error_weight_is_zero,
                        Toast.LENGTH_LONG
                    ).show()
                }
                binding.heightNp.value == 0 -> {
                    Toast.makeText(
                        requireContext(),
                        R.string.error_height_is_zero,
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                    if (mInterstitialAd != null) {
                        mInterstitialAd?.show(requireActivity())
                    }

                    binding.nameEtLayout.clearError()
                    viewModel.calculateIndexes(
                        binding.weightNp.value.toFloat(),
                        binding.heightNp.value.toFloat()
                    )

                    val viewStateObserver = Observer<AddDetailsViewState> { viewState ->
                        bmi = viewState.bmi
                        ponderalIndex = viewState.ponderalIndex
                    }

                    viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)

                    openDetailsFragment(
                        bmi,
                        ponderalIndex,
                        binding.nameEt.text.toString()
                    )
                }
            }
        }
    }

    private fun openDetailsFragment(
        bmi: Float,
        ponderalIndex: Float,
        name: String,
    ) {
        val args = bundleOf(
            "bmi" to bmi,
            "ponderalIndex" to ponderalIndex,
            "name" to name
        )
        navController?.navigate(R.id.action_addDetailsFragment_to_detailsFragment, args)
    }

    private fun requestAd() {
        val adRequest: AdRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            requireContext(),
            INTERSTITIAL_AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                    Log.d("TAG", adError.message)
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    Log.d("TAG", "success")
                }
            })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}