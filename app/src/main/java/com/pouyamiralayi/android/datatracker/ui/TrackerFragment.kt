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

package com.pouyamiralayi.android.datatracker.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pouyamiralayi.android.datatracker.R
import com.pouyamiralayi.android.datatracker.databinding.FragmentSleepTrackerBinding
import com.pouyamiralayi.android.datatracker.datatracker.DataViewModel
import com.pouyamiralayi.android.datatracker.datatracker.DataViewModelFactory
import com.pouyamiralayi.android.datatracker.hideKeyboard
import com.pouyamiralayi.android.datatracker.network.ApiState
import com.pouyamiralayi.android.datatracker.network.CredentialManager

class TrackerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_tracker, container, false)
        binding.lifecycleOwner = this


        val application = requireNotNull(this.activity).application

        val args = arguments?.let {
            TrackerFragmentArgs.fromBundle(it)
        }

        CredentialManager.saveCredentials(context, args?.jwt)
//        Log.i("TOKEN", args?.jwt ?: "No JWT!")

        val viewModelFactory = DataViewModelFactory(args?.customerName ?: "", args?.customerNo
                ?: "", args?.jwt ?: "")
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(DataViewModel::class.java)

        binding.viewModel = viewModel



        binding.search.setOnClickListener {
            hideKeyboard(activity!!)
            binding.query.text?.let {
                viewModel.query(it.toString())
            }
        }

        binding.up.setOnClickListener {
            viewModel.customersScreen.value?.let {
                when (it) {
                    true -> binding.customersList.scrollToPosition(0)
                    else -> binding.sellersList.scrollToPosition(0)
                }
            }
        }

        binding.down.setOnClickListener {
            viewModel.customersScreen.value?.let {
                when (it) {
                    true -> binding.customersList.scrollToPosition(viewModel.customers.value?.size?.minus(1)
                            ?: 0)
                    else -> binding.sellersList.scrollToPosition(viewModel.sellers.value?.size?.minus(1)
                            ?: 0)
                }
            }
        }

        viewModel.customersScreen.observe(this, Observer {
            when (it) {
                true -> {
                    binding.customersList.visibility = View.VISIBLE
                    binding.sellersList.visibility = View.GONE
                }
                false -> {
                    binding.customersList.visibility = View.GONE
                    binding.sellersList.visibility = View.VISIBLE
                }
            }

        })


        val adapter = CustomerAdapter(CustomerListener { customerId ->
        }, viewModel.customerNo, viewModel.customerName)
        val adapter2 = SellerAdapter(SellerListener { sellerId ->
        }, viewModel.customerNo, viewModel.customerName)


        binding.customersList.adapter = adapter
        binding.sellersList.adapter = adapter2

        viewModel.customers.observe(this, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.state.observe(this, Observer { state ->
            if (!viewModel.listIsEmpty()) {
                viewModel.customersScreen.value?.let {
                    if (it) {
                        adapter.state = state ?: ApiState.DONE
                    } else {
                        adapter2.state = state ?: ApiState.DONE
                    }
                }
            }
        })

        viewModel.owed.observe(this, Observer {
            it?.let {
                adapter.owed = it
            }
        })

        viewModel.owned.observe(this, Observer {
            it?.let {
                adapter.owned = it
            }
        })

        viewModel.rem.observe(this, Observer {
            it?.let {
                adapter.rem = it
            }
        })

        viewModel.plus.observe(this, Observer {
            it?.let {
                adapter.plus = it
            }
        })


        viewModel.sellers.observe(this, Observer {
            it?.let {
                adapter2.submitList(it)
            }
        })

        return binding.root
    }
}
