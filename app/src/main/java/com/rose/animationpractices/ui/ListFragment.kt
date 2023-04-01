package com.rose.animationpractices.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialFade
import com.rose.animationpractices.R
import com.rose.animationpractices.databinding.FragmentListBinding
import com.rose.animationpractices.di.fragment.ListAdapterAssistedFactory
import com.rose.animationpractices.domain.entity.Food
import com.rose.animationpractices.ui.adapter.AdapterListener
import com.rose.animationpractices.ui.adapter.ListAdapter
import com.rose.animationpractices.ui.util.collectWhenActive
import com.rose.animationpractices.view_model.ListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment(), AdapterListener {

    companion object {
        private const val TAG = "ListFragment"
    }

    private lateinit var binding: FragmentListBinding

    @Inject
    lateinit var adapterAssistedFactory: ListAdapterAssistedFactory
    private lateinit var adapter: ListAdapter

    private val viewModel: ListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = adapterAssistedFactory.create(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)

        binding.list.adapter = adapter

        binding.addButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_listFragment_to_addFragment,
                null,
                NavOptions.Builder()
                    .setEnterAnim(android.R.anim.slide_in_left)
                    .setExitAnim(android.R.anim.slide_out_right)
                    .build()
            )
        }

        postponeEnterTransition()
        binding.list.doOnPreDraw {
            startPostponedEnterTransition()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.listState.collectWhenActive(viewLifecycleOwner) { state ->
            Log.i(TAG, "onCreateView: list stated $state")

            val previousState = binding.list.layoutManager?.onSaveInstanceState()
            adapter.setFoods(state.items)
            binding.list.layoutManager?.onRestoreInstanceState(previousState)

            TransitionManager.beginDelayedTransition(binding.root as ViewGroup, MaterialFade())

            if (state.messageRes == 0) {
                binding.messageTextView.text = ""
                binding.messageTextView.visibility = View.GONE
            } else {
                binding.messageTextView.setText(state.messageRes)
                binding.messageTextView.visibility = View.VISIBLE
            }
            if (state.loading) {
                binding.animationView.visibility = View.VISIBLE
                binding.animationView.playAnimation()
            } else {
                binding.animationView.visibility = View.GONE
                binding.animationView.pauseAnimation()
            }
        }
    }

    override fun navigateFoodDetails(food: Food, imageView: View) {
        Log.i(TAG, "navigateFoodDetails: ${food.id}")
        findNavController().navigate(
            R.id.action_listFragment_to_addFragment,
            bundleOf(
                "foodId" to food.id,
                "imageUri" to food.imageUri,
                "title" to food.title,
                "description" to food.description
            ),
            null,
            FragmentNavigatorExtras(imageView to imageView.transitionName)
        )
    }
}