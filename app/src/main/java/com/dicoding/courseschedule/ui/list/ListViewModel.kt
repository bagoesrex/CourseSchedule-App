package com.dicoding.courseschedule.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.util.SortType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class ListViewModel(private val repository: DataRepository) : ViewModel() {

    private val _sortParams = MutableStateFlow(SortType.TIME)

    @OptIn(ExperimentalCoroutinesApi::class)
    val courses: Flow<PagingData<Course>> = _sortParams
        .flatMapLatest { sortType ->
            repository.getAllCourse(sortType)
        }
        .cachedIn(viewModelScope)

    fun sort(newValue: SortType) {
        _sortParams.value = newValue
    }

    fun delete(course: Course) {
        repository.delete(course)
    }
}
