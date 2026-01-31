package com.example.presidentsusa

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class StateViewModel: ViewModel() {
    private val apiService = RetrofitInstance.api
    val presidents: MutableState<List<President>> =
        mutableStateOf(emptyList())
    val president: MutableState<President?> = mutableStateOf(null)
    fun getPresidents() {
        viewModelScope.launch {
            try {
                val response = apiService.getPresidents()
                if (response!=null) {
                    presidents.value=response
                    Log.d("testretrofit",
                        presidents.value?.toString()?:"")
                }
            } catch (e: Exception) {
                Log.d("testretrofit","Erreur Retrofit")
            }
        }
    }

    fun getOnePresident(no:String) {
        viewModelScope.launch {
            try {
                val response = apiService.getOnePresident(no)
                if (response!=null) {
                    president.value=response.body()
                    Log.d("testretrofit",president.value?.name?:"")
                }
            } catch (e: Exception) {
                Log.d("testretrofit",
                    "Problème de connexion Retrofit")
            }
        }
    }
}