package com.deslomator.tagtimer.model.type

import com.deslomator.tagtimer.R

enum class FileItemButton(
    val iconId: Int
) {
    RESTORE(R.drawable.restore_backup),
    SAVE_TO_STORAGE(R.drawable.save_to_storage),
    SHARE(R.drawable.share),
    DELETE(R.drawable.delete_forever),
}