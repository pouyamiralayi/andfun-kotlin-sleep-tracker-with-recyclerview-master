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
import androidx.navigation.fragment.findNavController
import com.pouyamiralayi.android.datatracker.R
import com.pouyamiralayi.android.datatracker.databinding.FragmentSleepTrackerBinding
import com.pouyamiralayi.android.datatracker.datatracker.DataViewModel
import com.pouyamiralayi.android.datatracker.datatracker.DataViewModelFactory
import com.pouyamiralayi.android.datatracker.hideKeyboard
import com.pouyamiralayi.android.datatracker.network.ApiState
import com.pouyamiralayi.android.datatracker.network.CredentialManager
import android.widget.Toast
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import android.R.attr.typeface
import android.graphics.Color
import android.graphics.Typeface
import ir.hamsaa.persiandatepicker.Listener
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat


class TrackerFragment : Fragment() {
    lateinit var pickerFrom: PersianDatePickerDialog
    lateinit var pickerTo: PersianDatePickerDialog
    lateinit var viewModel: DataViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        pickerInit()

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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DataViewModel::class.java)

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
                    true -> binding.customersList.scrollToPosition(viewModel.customers.value?.size
                            ?: 0)
                    else -> binding.sellersList.scrollToPosition(viewModel.sellers.value?.size
                            ?: 0)
                }
            }
        }

        binding.fromDateBtn.setOnClickListener {
            pickerFrom.show()
        }

        binding.toDateBtn.setOnClickListener {
            pickerTo.show()
        }

        binding.logout.setOnClickListener {
            CredentialManager.saveCredentials(context, "")
            findNavController().navigate(TrackerFragmentDirections.actionSleepTrackerFragmentToLoginFragment())
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
            viewModel.dateFrom.value = null
            viewModel.dateFromPersian.value = null
            viewModel.dateTo.value = null
            viewModel.dateToPersian.value = null
            if(binding.query.text.isNotEmpty()){
                binding.query.setText("")
                viewModel.reload()
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

    private fun pickerInit() {
        pickerFrom = PersianDatePickerDialog(context)
                .setPositiveButtonString("باشه")
                .setNegativeButton("بیخیال")
                .setTodayButton("امروز")
                .setTodayButtonVisible(true)
                .setMinYear(1300)
                .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
                .setActionTextColor(Color.GRAY)
                .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                .setListener(object : Listener {
                    override fun onDateSelected(persianCalendar: PersianCalendar) {
                        try {
                            val persianDate = persianCalendar.persianYear.toString() + "/" + persianCalendar.persianMonth + "/" + persianCalendar.persianDay
                            Log.i("picker", persianDate)
                            val pd = PersianDate()
                            pd.shDay = persianCalendar.persianDay
                            pd.shMonth = persianCalendar.persianMonth
                            pd.shYear = persianCalendar.persianYear
                            var grgDate = pd.grgYear.toString() + "/"
                            if (pd.grgMonth < 10) {
                                grgDate += "0" + pd.grgMonth.toString() + "/"
                            } else {
                                grgDate += pd.grgMonth.toString() + "/"
                            }
                            if (pd.grgDay < 10) {
                                grgDate += "0" + pd.grgDay.toString()
                            } else {
                                grgDate += pd.grgDay.toString()
                            }
                            Log.i("picker", grgDate)
                            viewModel.dateFrom.value = grgDate
                            viewModel.dateFromPersian.value = pd.shYear.toString() + "/" + pd.shMonth + "/" + pd.shDay

                            //                            Toast.makeText(context, PersianDateFormat.format(pd, "l j F Y"), Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Log.e("picker", e.toString())
                        }
                    }

                    override fun onDismissed() {

                    }
                })
        pickerTo = PersianDatePickerDialog(context)
                .setPositiveButtonString("باشه")
                .setNegativeButton("بیخیال")
                .setTodayButton("امروز")
                .setTodayButtonVisible(true)
                .setMinYear(1300)
                .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
                .setActionTextColor(Color.GRAY)
                .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                .setListener(object : Listener {
                    override fun onDateSelected(persianCalendar: PersianCalendar) {
                        try {
                            val persianDate = persianCalendar.persianYear.toString() + "/" + persianCalendar.persianMonth + "/" + persianCalendar.persianDay
                            Log.i("picker", persianDate)
                            val pd = PersianDate()
                            pd.shDay = persianCalendar.persianDay
                            pd.shMonth = persianCalendar.persianMonth
                            pd.shYear = persianCalendar.persianYear
                            var grgDate = pd.grgYear.toString() + "/"
                            if (pd.grgMonth < 10) {
                                grgDate += "0" + pd.grgMonth.toString() + "/"
                            } else {
                                grgDate += pd.grgMonth.toString() + "/"
                            }
                            if (pd.grgDay < 10) {
                                grgDate += "0" + pd.grgDay.toString()
                            } else {
                                grgDate += pd.grgDay.toString()
                            }
                            Log.i("picker", grgDate)
                            viewModel.dateTo.value = grgDate
                            viewModel.dateToPersian.value = pd.shYear.toString() + "/" + pd.shMonth + "/" + pd.shDay
                        } catch (e: Exception) {
                            Log.e("picker", e.toString())
                        }
                    }

                    override fun onDismissed() {

                    }
                })
    }
}
