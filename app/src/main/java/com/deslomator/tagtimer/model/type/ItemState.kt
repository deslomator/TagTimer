package com.deslomator.tagtimer.model.type

import androidx.annotation.Keep
import com.deslomator.tagtimer.R

@Keep
enum class ItemState(val iconId: Int) {
    ENABLED(0),
    ARCHIVED(R.drawable.folder),
    TRASHED(R.drawable.trash)
}