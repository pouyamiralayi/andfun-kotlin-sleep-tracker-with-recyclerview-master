/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.example.android.trackmysleepquality.datatracker.DataViewModel
import com.example.android.trackmysleepquality.hideKeyboard

class SleepTrackerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_tracker, container, false)
        binding.lifecycleOwner = this


        val application = requireNotNull(this.activity).application


        val viewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)

        binding.viewModel = viewModel

        binding.search.setOnClickListener{
            hideKeyboard(activity!!)
            binding.query.text?.let{
                viewModel.query(it.toString())
            }
        }

        binding.up.setOnClickListener{
            viewModel.customersScreen.value?.let {
                when(it){
                    true -> binding.customersList.scrollToPosition(0)
                    else -> binding.sellersList.scrollToPosition(0)
                }
            }
        }

        binding.down.setOnClickListener{
            viewModel.customersScreen.value?.let {
                when(it){
                    true -> binding.customersList.scrollToPosition(viewModel.customers.value?.size?.minus(1) ?: 0)
                    else -> binding.sellersList.scrollToPosition(viewModel.sellers.value?.size?.minus(1) ?: 0)
                }
            }
        }

        viewModel.customersScreen.observe(this, Observer {
            when(it){
                true ->  {
                    binding.customersList.visibility = View.VISIBLE
                    binding.sellersList.visibility = View.GONE
                }
                false -> {
                    binding.customersList.visibility = View.GONE
                    binding.sellersList.visibility = View.VISIBLE
                }
            }

        })

        viewModel.queryNotFound.observe(this, Observer {
            when(it){
                true -> {
                    Toast.makeText(context, "داده ای یافت نشد.", Toast.LENGTH_SHORT)
                        .show()
                    viewModel.onQueryNotFoundCompleted()
                }
                else -> null
            }

        })



        viewModel.fetchError.observe(this, Observer { error ->
            Toast.makeText(context, "$error", Toast.LENGTH_SHORT)
                    .show()
        })


        // Add an Observer on the state variable for showing a Snackbar message
        // when the CLEAR button is pressed.
//        sleepTrackerViewModel.showSnackBarEvent.observe(this, Observer {
//            if (it == true) { // Observed state is true.
//                Snackbar.make(
//                        activity!!.findViewById(android.R.id.content),
//                        getString(R.string.cleared_message),
//                        Snackbar.LENGTH_SHORT // How long to display the message.
//                ).show()
//                // Reset state to make sure the snackbar is only shown once, even if the device
//                // has a configuration change.
//                sleepTrackerViewModel.doneShowingSnackbar()
//            }
//        })


//        val manager = GridLayoutManager(activity, 3)
//        val manager2 = GridLayoutManager(activity, 3)
//        binding.customersList.layoutManager = manager
//        binding.sellersList.layoutManager = manager2

//        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int) = when (position) {
//                0 -> 3
//                else -> 1
//            }
//        }
//        manager2.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int) = when (position) {
//                0 -> 3
//                else -> 1
//            }
//        }

        val adapter = CustomerAdapter(CustomerListener { customerId ->
            /*TODO show a dialog of the description*/
//            Toast.makeText(context, "$customerId", Toast.LENGTH_SHORT)
//                    .show()
        })
        val adapter2 = SellerAdapter(SellerListener { sellerId ->
            /*TODO show a dialog of the description*/
        })


        binding.customersList.adapter = adapter
        binding.sellersList.adapter = adapter2

        viewModel.customers.observe(this, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        viewModel.sellers.observe(this, Observer {
            it?.let {
                adapter2.addHeaderAndSubmitList(it)
            }
        })

        return binding.root
    }
}
