package com.example.gridrecyclerviewapp.screens.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gridrecyclerviewapp.model.Human
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel : ViewModel() {
    private val _humansFlow = MutableStateFlow<List<Human>>(emptyList())
    var humansFlow: SharedFlow<List<Human>> = _humansFlow.asStateFlow()

    fun addNewUser(name: String, job: String, gender: Boolean, logoId: Int) {
        viewModelScope.launch {
            _humansFlow.value = _humansFlow.value.toMutableList().apply {
                add(
                    Human(
                        id = Random.nextInt(),
                        name = name,
                        job = job,
                        gender = gender,
                        logoId = logoId
                    )
                )
            }
        }
    }

    fun deleteUser(user: Human) {
        viewModelScope.launch {
            _humansFlow.value = _humansFlow.value.toMutableList().apply {
                remove(user)
            }
        }
    }

    fun updateUser(updatedUser: Human) {
        viewModelScope.launch {
            _humansFlow.value = _humansFlow.value.toMutableList().apply {
                val index = indexOfFirst { it.id == updatedUser.id }

                if (index != -1) {
                    set(index, updatedUser)
                }
            }
        }
    }
}
