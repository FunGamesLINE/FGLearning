package com.example.fglearning.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fglearning.database.MaterialsDatabase
import com.example.fglearning.database.entity.Package
import com.example.fglearning.database.entity.PackageItem
import com.example.fglearning.repository.PackageItemRepository
import com.example.fglearning.repository.PackageRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PackageItemsViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val packageItemsRepository: PackageItemRepository by lazy {
        PackageItemRepository(MaterialsDatabase.getInstance(getApplication()).packageItemDao())
    }

    private val _packageItems = MutableLiveData<List<PackageItem>>()
    val packageItems: LiveData<List<PackageItem>> = _packageItems

    fun loadPackages(exercise: Int) {
        viewModelScope.launch {
            //_packageItems.value = packageItemsRepository.getByExercise(exercise).first()
        }
    }
}