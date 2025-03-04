package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.type.LabelType
import com.deslomator.tagtimer.ui.theme.colorPickerColors
import com.deslomator.tagtimer.ui.theme.toHex
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
@Entity(tableName = "labels")
data class Label(
    @SerialName(COLUMN_LABEL_NAME)
    @ColumnInfo(name = COLUMN_LABEL_NAME)
    val name: String = "",

    @SerialName(COLUMN_LABEL_COLOR)
    @ColumnInfo(name = COLUMN_LABEL_COLOR)
    val color: String = colorPickerColors[7].toHex(),

    @SerialName("in_trash")
    @ColumnInfo(name = "in_trash")
    val inTrash: Boolean = false,

    val archived: Boolean = false,

    val type: Int = LabelType.TAG.typeId,

    @PrimaryKey(autoGenerate = true) val id: Long? = null,
) {
    @delegate:Ignore
    val longColor: Long by lazy { color.toLong(16) }

    fun isPerson(): Boolean = type == LabelType.PERSON.typeId

    fun getIcon() = when (type) {
        LabelType.TAG.typeId -> LabelType.TAG.iconId
        LabelType.PERSON.typeId -> LabelType.PERSON.iconId
        else -> LabelType.PLACE.iconId
    }

    fun getLabelType() = when (type) {
        LabelType.TAG.typeId -> LabelType.TAG
        LabelType.PERSON.typeId -> LabelType.PERSON
        else -> LabelType.PLACE
    }

    suspend fun canBeDeleted(appDao: AppDao) =
        appDao.getSEventsForTag(this.id!!) == 0
}

const val COLUMN_LABEL_NAME = "name"
const val COLUMN_LABEL_COLOR = "color"