package com.android.pizzaapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.android.pizzaapp.R
import com.android.pizzaapp.data.remote.NetworkHandler
import com.android.pizzaapp.data.remote.model.SelectedItem
import com.android.pizzaapp.databinding.ActivityMainBinding
import com.android.pizzaapp.ui.viewModel.AppNavigator
import com.android.pizzaapp.ui.viewModel.AppViewModel
import com.android.pizzaapp.util.RemoveItemFromCart
import com.android.pizzaapp.util.RemovePizzaDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AppNavigator {
    val viewModel: AppViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.setNavigator(this)
        viewModel.invoke()
        changeFragment(PizzaFragment())


    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().addToBackStack(null)
            .replace(R.id.container, fragment).commit()
    }

    override fun switchFragment() {
        changeFragment(PizzaCartFragment())
        Log.d("fragment", "PizzaCartFragment")
    }

    override fun selectedItemList(item: SelectedItem?) {

        var commonPizzaItem =
            viewModel.selectedItemList?.find { it?.sizeId == item?.sizeId && it?.crustId == item?.crustId }

        if (commonPizzaItem != null) {
            // Increase quantity of existing item
            commonPizzaItem.quantity = commonPizzaItem.quantity.plus(item?.quantity ?: 1)
            Log.e("selectedList", viewModel.selectedItemList.toString())

        } else {
            // Add new item to list
            item?.let {
                viewModel.selectedItemList.add(item)
            }

            Log.e("selectedList", viewModel.selectedItemList.toString())
        }

        viewModel.totalQuantity = viewModel.selectedItemList.sumOf {
            it.quantity
        }
        viewModel.selectedItemList.forEach {
           viewModel.totalPrice += it.price
        }

    }

    override fun invokeRemove() {
        RemovePizzaDialog(this, viewModel.selectedItemList, object : RemoveItemFromCart {
            override fun removeItem(item: SelectedItem?) {
                Log.d("removedItem", item.toString())
                viewModel.selectedItemList?.remove(item)
            }

        }).show()

    }


}