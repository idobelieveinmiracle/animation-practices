package com.rose.animationpractices.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rose.animationpractices.R
import com.rose.animationpractices.domain.data_source.DataSource
import com.rose.animationpractices.domain.entity.Food
import com.rose.animationpractices.domain.use_case.AddFood
import com.rose.animationpractices.domain.use_case.GetFood
import com.rose.animationpractices.view_model.state.AddMode
import com.rose.animationpractices.view_model.state.UiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddFoodViewModel @AssistedInject constructor(
    private val addFood: AddFood,
    private val getFood: GetFood,
    @Assisted
    private val originalFood: Food
) : ViewModel() {

    companion object {
        private const val TAG = "AddFoodViewModel"
    }

    private val _addState: MutableStateFlow<UiState<Food>> = MutableStateFlow(UiState.Idle())
    val addState: StateFlow<UiState<Food>> get() = _addState

    private val _addMode: MutableStateFlow<AddMode>
    val addMode: StateFlow<AddMode> get() = _addMode

    private val _food: MutableStateFlow<Food> = MutableStateFlow(originalFood)
    val food: StateFlow<Food> get() = _food

    init {
        Log.i(TAG, "init: foodId=${originalFood.id}")
        if (originalFood.id == 0L) {
            _addMode = MutableStateFlow(AddMode.EDIT)
        } else {
            _addMode = MutableStateFlow(AddMode.VIEW)
            viewModelScope.launch {
                delay(500)
                when (val foodDataSource = getFood(originalFood.id)) {
                    is DataSource.Success -> {
                        if (foodDataSource.data == null) {
                            _addState.value = UiState.Failure(R.string.data_error)
                        } else {
                            _food.value = foodDataSource.data
                        }
                    }
                    is DataSource.Failure -> _addState.value = UiState.Failure(R.string.data_error)
                    else -> {
                        Log.e(TAG, "add: invalid data return $foodDataSource")
                        _addState.value = UiState.Failure(R.string.data_error)
                    }
                }
            }
        }
    }

    fun save() {
        if (food.value.title.isEmpty() || food.value.description.isEmpty()) {
            _addState.value = UiState.Failure(R.string.missing_fill_field)
            return
        }

        viewModelScope.launch {
            _addState.value = UiState.Loading()

            val addResult = addFood(food.value.copy(createdTime = System.currentTimeMillis()))

            _addState.value = when (addResult) {
                is DataSource.Success -> {
                    if (addResult.data == null) {
                        UiState.Failure(R.string.data_error)
                    } else {
                        UiState.Success(addResult.data)
                    }
                }
                is DataSource.Failure -> UiState.Failure(R.string.data_error)
                else -> {
                    Log.e(TAG, "add: invalid data return $addResult")
                    UiState.Failure(R.string.data_error)
                }
            }
        }
    }

    fun resetFailureState() {
        _addState.value = UiState.Idle()
    }

    fun updateFood(
        title: String? = null,
        description: String? = null,
        imageUri: String? = null
    ) {
        if (title != null && title == food.value.title) {
            return
        }

        if (description != null && description == food.value.description) {
            return
        }

        _food.update {
            it.copy(
                title = title ?: it.title,
                description = description ?: it.description,
                imageUri = imageUri ?: it.imageUri
            )
        }
    }
}