package com.deslomator.tagtimer.model.type

import androidx.annotation.Keep
import com.deslomator.tagtimer.R

/**
 * Types of label
 */
@Keep
enum class LabelType(
    val iconId: Int,

    val archiveMessageId: Int,
    val unArchiveMessageId: Int,
    val trashMessageId: Int,
    val unTrashMessageId: Int,
    val purgeMessageId: Int,

    val editTitleId: Int,
    val newTitleId: Int,
    val addIconId: Int,
    val addStringId: Int,
    val titleBarId: Int,
    val checkedStringId: Int,
    val unCheckedStringId: Int,
) {
    TAG(
        R.drawable.tag,

        R.string.tag_archived,
        R.string.tag_unarchived,
        R.string.tag_trashed,
        R.string.tag_untrashed,
        R.string.tag_purged,

        R.string.edit_tag,
        R.string.new_tag,
        R.drawable.add_tag,
        R.string.add_tag,
        R.string.tags,
        R.string.tag_checked,
        R.string.tag_unchecked,
    ),
    PERSON(
        R.drawable.person,

        R.string.person_archived,
        R.string.person_unarchived,
        R.string.person_trashed,
        R.string.person_untrashed,
        R.string.person_purged,

        R.string.edit_person,
        R.string.new_person,
        R.drawable.add_person,
        R.string.add_person,
        R.string.persons,
        R.string.person_checked,
        R.string.person_unchecked,
    ),
    PLACE(
        R.drawable.place,

        R.string.place_archived,
        R.string.place_unarchived,
        R.string.place_trashed,
        R.string.place_untrashed,
        R.string.place_purged,

        R.string.edit_place,
        R.string.new_place,
        R.drawable.add_place,
        R.string.add_place,
        R.string.places,
        R.string.place_checked,
        R.string.place_unchecked,
    )
}