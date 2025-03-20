package com.redflag.newsmobile.ui.model

class NavigationItem (
    val label: String? = null,
    val iconDrawable: Int,
    val iconDescription: String,
    val onClick: () -> Unit
)
    