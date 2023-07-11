package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import androidx.compose.ui.graphics.toArgb
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.deslomator.tagtimer.ui.theme.colorPickerColors
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
sealed class Label {
    abstract val name: String
    abstract val color: Int
    abstract val inTrash: Boolean
    abstract val id: Long?

    @Keep
    @Serializable
    @Entity(tableName = "tags")
    data class Tag(
        @SerialName("tag_name")
        override val name: String = "",

        override val color: Int = colorPickerColors[7].toArgb(),

        @SerialName("in_trash")
        @ColumnInfo(name ="in_trash")
        override val inTrash: Boolean = false,

        @PrimaryKey(autoGenerate = true)
        override val id: Long? = null
    ) : Label()

    @Keep
    @Serializable
    @Entity(tableName = "places")
    data class Place(
        @SerialName("place_name")
        override val name: String = "",

        override val color: Int = colorPickerColors[7].toArgb(),

        @SerialName("in_trash")
        @ColumnInfo(name ="in_trash")
        override val inTrash: Boolean = false,

        @PrimaryKey(autoGenerate = true)
        override val id: Long? = null
    ) : Label()

    @Keep
    @Serializable
    @Entity(tableName = "persons")
    data class Person(
        @SerialName("person_name")
        override val name: String = "",

        override val color: Int = colorPickerColors[7].toArgb(),

        @SerialName("in_trash")
        @ColumnInfo(name ="in_trash")
        override val inTrash: Boolean = false,

        @PrimaryKey(autoGenerate = true)
        override val id: Long? = null
    ) : Label()
}

