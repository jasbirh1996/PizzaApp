package com.android.pizzaapp.ui.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.pizzaapp.data.remote.NetworkHandler
import com.android.pizzaapp.data.remote.model.PizzaAppResponse
import com.android.pizzaapp.data.remote.model.SelectedItem
import com.android.pizzaapp.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(private val repo: AppRepository) : ViewModel() {
    lateinit var mNavigator: WeakReference<AppNavigator>
    var pizzaLiveData = MutableLiveData<NetworkHandler<PizzaAppResponse>>()
    var pizzaData : PizzaAppResponse ? = null
    var selectedItemList = mutableListOf<SelectedItem>()
    var totalQuantity : Int =0
    var totalPrice : Double = 0.0


    fun getNavigator(): AppNavigator? {
        return mNavigator.get()
    }

    fun setNavigator(navigator: AppNavigator) {
        this.mNavigator = WeakReference(navigator)
    }


    fun invoke() {
        viewModelScope.launch {
            pizzaLiveData.postValue(repo.getPizzaData())
        }

    }




}