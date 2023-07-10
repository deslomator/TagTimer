package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.deslomator.tagtimer.ui.theme.colorPickerColors
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
sealed class Label (
    @SerialName("super_name") open val name: String = "",
    @SerialName("super_color") open val color: Int = 0,
    @SerialName("super_inTrash") open val inTrash: Boolean = false,
    @SerialName("super_id") open val id: Long = 0
) {
    @Keep
    @Serializable
    @Entity(tableName = "tags")
    data class Tag(
        override val name: String = "",
        override val color: Int = colorPickerColors[7].toArgb(),
        @SerialName("in_trash") override val inTrash: Boolean = false,
        @PrimaryKey(autoGenerate = true) override val id: Long = 0
    ) : Label(name, color, inTrash, id)

    @Keep
    @Serializable
    @Entity(tableName = "places")
    data class Place(
        override val name: String = "",
        override val color: Int = colorPickerColors[7].toArgb(),
        @SerialName("in_trash") override val inTrash: Boolean = false,
        @PrimaryKey(autoGenerate = true) override val id: Long = 0
    ) : Label(name, color, inTrash, id)

    @Keep
    @Serializable
    @Entity(tableName = "persons")
    data class Person(
        override val name: String = "",
        override val color: Int = colorPickerColors[7].toArgb(),
        @SerialName("in_trash") override val inTrash: Boolean = false,
        @PrimaryKey(autoGenerate = true) override val id: Long = 0
    ) : Label(name, color, inTrash, id)
}

