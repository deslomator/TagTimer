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
    ) : Label() {
        override fun copyMe(
            name: String,
            color: String,
            inTrash: Boolean
        ): Tag {
            return this.copy(
                name = name,
                color = color,
                inTrash = inTrash
            )
        }
    }

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
    ) : Label() {
        override fun copyMe(
            name: String,
            color: String,
            inTrash: Boolean
        ): Place {
            return this.copy(
                name = name,
                color = color,
                inTrash = inTrash
            )
        }
    }

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
    ) : Label() {
        override fun copyMe(
            name: String,
            color: String,
            inTrash: Boolean
        ): Person {
            return this.copy(
                name = name,
                color = color,
                inTrash = inTrash
            )
        }
    }

    @delegate:Ignore
    val id: String by lazy { name + color }

    @delegate:Ignore
    val longColor: Long by lazy { color.toLong(16) }

    abstract fun copyMe(
        name: String = this.name,
        color: String = this.color,
        inTrash: Boolean = this.inTrash
    ): Label
}

