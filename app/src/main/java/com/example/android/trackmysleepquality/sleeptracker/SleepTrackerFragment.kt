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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.example.android.trackmysleepquality.datatracker.DataViewModel

class SleepTrackerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_tracker, container, false)
        binding.lifecycleOwner = this


        val application = requireNotNull(this.activity).application


        val dataViewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)

        dataViewModel.navigateToCustomerDetail.observe(this, Observer { customerId ->
            customerId?.let {
                /*TODO show dialog*/
                dataViewModel.onCustomerNavigated()
            }

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



        val manager = GridLayoutManager(activity, 3)
        val manager2 = GridLayoutManager(activity, 3)
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
        val adapter2 = SellerAdapter()


        binding.customersList.adapter = adapter
        binding.sellersList.adapter = adapter2

        dataViewModel.customers.observe(this, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        dataViewModel.sellers.observe(this, Observer {
            it?.let {
                adapter2.data = it
            }
        })

        return binding.root
    }
}
