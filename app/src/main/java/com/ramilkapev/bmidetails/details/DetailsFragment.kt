package com.ramilkapev.bmidetails.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.gms.ads.AdRequest
import com.ramilkapev.bmidetails.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding get() = requireNotNull(_binding)

    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel: DetailsFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val adRequest = AdRequest.Builder().build()
        binding.bannerAdview.loadAd(adRequest)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "BMI Details"

        val viewStateObserver = Observer<DetailsViewState> { viewState ->
            when {
                (18.5..25.0).contains(viewState.bmi) -> {
                    binding.helloTv.text = "HELLO, ${viewState.name}, YOU ARE NORMAL"
                }
                viewState.bmi < 18.5 -> {
                    binding.helloTv.text = "HELLO, ${viewState.name}, YOU ARE UNDERWEIGHT"
                }
                else -> {
                    binding.helloTv.text = "HELLO, ${viewState.name}, YOU ARE OVERWEIGHT"
                }
            }

            binding.massIndexResultTv.text = String.format("%.2f", viewState.bmi)
            binding.ponderalIndexTv.text =
                "Ponderal index: ${String.format("%.2f", viewState.ponderalIndex)} kg/m3"
        }
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
        viewModel.processArgs(args)

        binding.shareBtn.setOnClickListener {
            viewModel.takeScreenshot(binding.bmiLayout, requireActivity())
        }

        binding.rateBtn.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.appovo.bmicalculator")
                )
            )
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}