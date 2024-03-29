package com.pouyamiralayi.android.datatracker.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.pouyamiralayi.android.datatracker.R
import com.pouyamiralayi.android.datatracker.databinding.FragmentLoginBinding
import com.pouyamiralayi.android.datatracker.network.CredentialManager

class LoginFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login,container, false)
        binding.lifecycleOwner = this



        val viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        binding.viewModel = viewModel


        viewModel.navigateToCustomers.observe(this, Observer {
            it?.let{
                if(it){
                    findNavController()
                            .navigate(LoginFragmentDirections.actionLoginFragmentToSleepTrackerFragment(viewModel.customer_name.value ?: "", viewModel.username.value ?: "", viewModel.jwt.value ?: ""))
                    viewModel.onNavigateToCustomersCompleted()
                }
            }
        })

        viewModel.fetchError.observe(this, Observer {
            it?.let{
                Toast.makeText(context, it, Toast.LENGTH_SHORT)
                        .show()
            }
        })

        binding.login.setOnClickListener{
            viewModel.username.value = binding.username.text.toString()
            viewModel.password.value = binding.password.text.toString()
            viewModel.signIn()
        }

        val token = CredentialManager.getCredentials(context)
        Log.i("TOKEN", token ?: "")
        viewModel.testAuth(token)

        return binding.root
    }
}