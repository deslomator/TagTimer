package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.deslomator.tagtimer.model.type.LabelType
import com.deslomator.tagtimer.ui.theme.colorPickerColors
import com.deslomator.tagtimer.ui.theme.toHex
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
@Entity(tableName = "labels")
data class Label(
    @SerialName(LABEL_NAME)
    @ColumnInfo(name = LABEL_NAME)
    val name: String = "",

    @SerialName(LABEL_COLOR)
    @ColumnInfo(name = LABEL_COLOR)
    val color: String = colorPickerColors[7].toHex(),

    @SerialName("in_trash")
    @ColumnInfo(name = "in_trash")
    val inTrash: Boolean = false,

    val type: Int = LabelType.TAG.typeId,

    @PrimaryKey(autoGenerate = true) val id: Long? = null,
) {
    @delegate:Ignore
    val longColor: Long by lazy { color.toLong(16) }

    fun isTag(): Boolean = type == LabelType.TAG.typeId
    fun isPerson(): Boolean = type == LabelType.PERSON.typeId
    fun isPlace(): Boolean = type == LabelType.PLACE.typeId

    fun getIcon() = when (type) {
        LabelType.TAG.typeId -> LabelType.TAG.iconId
        LabelType.PERSON.typeId -> LabelType.PERSON.iconId
        else -> LabelType.PLACE.iconId
    }
}

const val LABEL_NAME = "name"
const val LABEL_COLOR = "color"