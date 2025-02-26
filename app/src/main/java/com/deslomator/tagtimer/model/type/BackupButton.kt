package com.deslomator.tagtimer.model.type

import com.deslomator.tagtimer.R

enum class BackupButton(
    val textId: Int,
    val iconIds: List<Int>,
    val showSnackBar: Boolean = true
) {
    FULL(R.string.full_backup, listOf(R.drawable.document_and_ray)),
    LABELS(R.string.backup_labels, listOf(R.drawable.tag, R.drawable.person, R.drawable.place )),
    RESTORE(R.string.from_storage, listOf(R.drawable.storage), false)
}