package com.rose.animationpractices.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.doOnPreDraw
import androidx.databinding.Observable
import androidx.databinding.Observable.OnPropertyChangedCallback
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.ChangeBounds
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialFadeThrough
import com.rose.animationpractices.R
import com.rose.animationpractices.databinding.FragmentAddBinding
import com.rose.animationpractices.di.view_model_factory.AddFoodViewModelAssistedFactory
import com.rose.animationpractices.domain.entity.Food
import com.rose.animationpractices.ui.util.collectWhenActive
import com.rose.animationpractices.ui.util.load
import com.rose.animationpractices.view_model.AddFoodViewModel
import com.rose.animationpractices.view_model.factory.AddFoodViewModelFactory
import com.rose.animationpractices.view_model.state.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class AddFragment : Fragment() {

    companion object {
        private const val TAG = "AddFragment"
    }

    private lateinit var binding: FragmentAddBinding

    @Inject
    lateinit var assistedFactory: AddFoodViewModelAssistedFactory
    private val viewModel: AddFoodViewModel by viewModels {
        val originalFood = Food(
            id = arguments?.getLong("foodId", 0) ?: 0,
            imageUri = arguments?.getString("imageUri") ?: "",
            title = arguments?.getString("title") ?: "",
            description = arguments?.getString("description") ?: ""
        )
        AddFoodViewModelFactory(assistedFactory, originalFood)
    }

    private val selectImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { handleImageSelectedIntent(it) }
        }
    }

    private val title: ObservableField<String> = ObservableField("")
    private val description: ObservableField<String> = ObservableField("")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)

        binding.titleText = title
        binding.descriptionText = description

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.save.setOnClickListener {
            viewModel.save()
        }

        binding.cancel.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.foodImage.setOnClickListener {
            selectImageLauncher.launch(Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            })
        }

        val id = arguments?.getLong("foodId", 0) ?: 0

        if (id != 0L) {
            enterTransition = MaterialFadeThrough().setDuration(400.toLong())

            sharedElementEnterTransition = MaterialContainerTransform().apply {
                duration = 400.toLong()
                scrimColor = Color.TRANSPARENT
                drawingViewId = R.id.nav_host_fragment
            }

            arguments?.getString("imageUri")?.let { imageUri ->
                binding.foodImage.transitionName = imageUri
                binding.foodImage.load(imageUri)
            } ?: run {
                binding.foodImage.transitionName = "empty_image"
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.food.collectWhenActive(viewLifecycleOwner) { food ->
            if (food.title != title.get()) {
                title.set(food.title)
            }
            if (food.description != description.get()) {
                description.set(food.description)
            }
        }

        title.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                viewModel.updateFood(title = title.get())
            }
        })

        description.addOnPropertyChangedCallback(object : OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                viewModel.updateFood(description = description.get())
            }
        })

        viewModel.addState.collectWhenActive(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Failure -> {
                    Snackbar.make(binding.root, state.messageResId, Snackbar.LENGTH_LONG).show()
                    viewModel.resetFailureState()
                }
                is UiState.Success -> {
                    Snackbar.make(binding.root, R.string.saved, Snackbar.LENGTH_SHORT).show()
                    delay(500)
                    findNavController().navigateUp()
                }
                else -> Unit
            }
        }
    }

    private fun handleImageSelectedIntent(intent: Intent) {
        viewModel.updateFood(imageUri = intent.data.toString())
    }
}