package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.deslomator.tagtimer.ui.theme.colorPickerColors
import com.deslomator.tagtimer.ui.theme.toHex
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
sealed class Label {
    abstract val name: String
    abstract val color: String
    abstract val inTrash: Boolean

    @Keep
    @Serializable
    @Entity(
        tableName = "tags",
        primaryKeys = ["name", "color"]
    )
    data class Tag(
        @SerialName("tag_name")
        override val name: String = "",

        override val color: String = colorPickerColors[7].toHex(),

        @SerialName("in_trash")
        @ColumnInfo(name ="in_trash")
        override val inTrash: Boolean = false
    ) : Label()

    @Keep
    @Serializable
    @Entity(
        tableName = "places",
        primaryKeys = ["name", "color"]
    )
    data class Place(
        @SerialName("place_name")
        override val name: String = "",

        override val color: String = colorPickerColors[7].toHex(),

        @SerialName("in_trash")
        @ColumnInfo(name ="in_trash")
        override val inTrash: Boolean = false
    ) : Label()

    @Keep
    @Serializable
    @Entity(
        tableName = "persons",
        primaryKeys = ["name", "color"]
    )
    data class Person(
        @SerialName("person_name")
        override val name: String = "",

        override val color: String = colorPickerColors[7].toHex(),

        @SerialName("in_trash")
        @ColumnInfo(name ="in_trash")
        override val inTrash: Boolean = false
    ) : Label()

    @delegate:Ignore
    val id: String by lazy { name + color }

    @delegate:Ignore
    val longColor: Long by lazy { color.toLong(16) }
}

