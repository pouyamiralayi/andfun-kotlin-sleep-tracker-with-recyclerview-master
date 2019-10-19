package com.example.android.trackmysleepquality.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.trackmysleepquality.datatracker.ApiState
import com.example.android.trackmysleepquality.network.StrapiApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val apiJob = Job()
    private val coroutineScope = CoroutineScope(apiJob + Dispatchers.Main)

    val navigateToCustomers = MutableLiveData<Boolean>()

    val fetchError = MutableLiveData<String>()

    val state = MutableLiveData<ApiState>()

    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val customer_name = MutableLiveData<String>()


    fun onNavigateToCustomersCompleted() {
        navigateToCustomers.value = false
    }

    fun signIn() {
        if (username.value.equals("")) {
            fetchError.postValue("نام کاربری را وارد نمایید")
            return
        } else if (password.value.equals("")) {
            fetchError.postValue("رمز عبور را وارد نمایید")
            return
        } else {
            coroutineScope.launch {
                val loginDeferred = StrapiApi.retrofitService.login(username.value ?: "", password.value ?: "")
                try {
                    state.value = ApiState.LOADING
                    val res = loginDeferred.await()
                    state.value = ApiState.DONE
                    customer_name.value = res.user.email
                    navigateToCustomers.value = true
                } catch (t: Throwable) {
                    Log.i("login", t.message)
                    state.value = ApiState.ERROR
                    fetchError.value = "خطا!"
                }
            }
        }
    }

    init {
        navigateToCustomers.value = false
        state.value = ApiState.DONE
        customer_name.value = ""
        username.value = ""
        password.value = ""
    }

    override fun onCleared() {
        super.onCleared()
        apiJob.cancel()
    }
}