package com.rose.animationpractices.di.fragment

import com.rose.animationpractices.ui.adapter.AdapterListener
import com.rose.animationpractices.ui.adapter.ListAdapter
import dagger.assisted.AssistedFactory

@AssistedFactory
interface ListAdapterAssistedFactory {
    fun create(adapterListener: AdapterListener): ListAdapter
}