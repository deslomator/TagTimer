package com.deslomator.tagtimer.model.type

import androidx.annotation.Keep
import com.deslomator.tagtimer.R

/**
 * Types of Preference
 */
@Keep
enum class DialogArchiveState(val iconId: Int) {
    HIDDEN(R.drawable.document_and_ray),
    ARCHIVE(R.drawable.archive),
    UNARCHIVE(R.drawable.unarchive)
}

