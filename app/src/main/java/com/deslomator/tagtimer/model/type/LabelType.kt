package com.deslomator.tagtimer.model.type

import androidx.annotation.Keep
import com.deslomator.tagtimer.R

/**
 * Types of label
 */
@Keep
enum class LabelType(
    val typeId: Int,
    val iconId: Int,
    val messageId: Int,
    val editTitleId: Int,
    val newTitleId: Int,
    val addIconId: Int,
    val addStringId: Int,
    val titleBarId: Int,
    val checkedStringId: Int,
    val unCheckedStringId: Int,
) {
    TAG(
        0,
        R.drawable.tag,
        R.string.tag_sent_to_trash,
        R.string.edit_tag,
        R.string.new_tag,
        R.drawable.add_tag,
        R.string.add_tag,
        R.string.tags,
        R.string.tag_checked,
        R.string.tag_unchecked,
    ),
    PERSON(
        1,
        R.drawable.person,
        R.string.person_sent_to_trash,
        R.string.edit_person,
        R.string.new_person,
        R.drawable.add_person,
        R.string.add_person,
        R.string.persons,
        R.string.person_checked,
        R.string.person_unchecked,
    ),
    PLACE(
        2,
        R.drawable.place,
        R.string.place_sent_to_trash,
        R.string.edit_place,
        R.string.new_place,
        R.drawable.add_place,
        R.string.add_place,
        R.string.places,
        R.string.place_checked,
        R.string.place_unchecked,
    )
}