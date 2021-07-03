package com.ramilkapev.bmidetails.addDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.pow

class AddDetailsFragmentViewModel : ViewModel() {

    private val _viewState: MutableLiveData<AddDetailsViewState> = MutableLiveData()
    val viewState: LiveData<AddDetailsViewState> = _viewState

    fun calculateIndexes(weight: Float, height: Float) {
        _viewState.value = AddDetailsViewState(
            bmi = weight / (height / 100).pow(2),
            ponderalIndex = weight / (height / 100).pow(3)
        )
    }

}