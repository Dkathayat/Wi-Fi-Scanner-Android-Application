package com.kathayat.railscarmaapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kathayat.railscarmaapplication.repository.RoomRepository
import com.kathayat.railscarmaapplication.room.WifiData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(private val repository: RoomRepository) : ViewModel() {
    private val _items = MutableStateFlow<List<WifiData>>(emptyList())
    val items: StateFlow<List<WifiData>> = _items.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllWifi.collect { listOfItems ->
                _items.value = listOfItems
            }
        }
    }
    fun insert(item: WifiData) = viewModelScope.launch {
        try {
            repository.insert(item)
        } catch (e: Exception) {
            Log.e("RoomViewModel", "Error inserting WifiData", e)
        }
    }

    fun update(item: WifiData) = viewModelScope.launch {
        repository.update(item)
    }

    fun delete(item: WifiData) = viewModelScope.launch {
        repository.delete(item)
    }

    fun loadRoomData(): LiveData<List<WifiData>> {
        val liveData = MutableLiveData<List<WifiData>>()
        viewModelScope.launch {
            items.collect { wifiDataList ->
                liveData.postValue(wifiDataList) // Update LiveData with collected list
            }
        }
        return liveData
    }
}