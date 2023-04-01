package com.rose.animationpractices.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rose.animationpractices.R
import com.rose.animationpractices.domain.data_source.CanNotUpdateException
import com.rose.animationpractices.domain.data_source.DataSource
import com.rose.animationpractices.domain.data_source.IdNotMatchedException
import com.rose.animationpractices.domain.entity.Food
import com.rose.animationpractices.domain.use_case.GetFoods
import com.rose.animationpractices.view_model.state.ListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val getFoods: GetFoods) : ViewModel() {

    companion object {
        private const val TAG = "ListViewModel"
    }

    private val dataSource: Flow<DataSource<List<Food>>> = getFoods()

    private val _listState: StateFlow<ListState<Food>> = dataSource.map {
        when (it) {
            is DataSource.Loading -> ListState(loading = true)
            is DataSource.Failure -> {
                when (it.exception) {
                    is CanNotUpdateException -> ListState(messageRes = R.string.can_not_update_error)
                    is IdNotMatchedException -> ListState(messageRes = R.string.can_not_find_data)
                    else -> ListState(messageRes = R.string.loading_error)
                }
            }
            is DataSource.Success -> {
                if (it.data.isNullOrEmpty()) {
                    ListState(messageRes = R.string.empty_food_text)
                } else {
                    ListState(items = it.data)
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ListState(loading = true)
    )
    val listState: StateFlow<ListState<Food>> get() = _listState

    init {
        Log.i(TAG, "init: ")
    }
}