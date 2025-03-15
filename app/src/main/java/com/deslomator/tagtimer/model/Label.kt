package com.deslomator.tagtimer.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.deslomator.tagtimer.dao.AppDao
import com.deslomator.tagtimer.model.type.ItemState
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

    val state: ItemState = ItemState.ENABLED,

    val type: LabelType = LabelType.TAG,

    @PrimaryKey(autoGenerate = true) val id: Long? = null,
) {
    @delegate:Ignore
    val longColor: Long by lazy { color.toLong(16) }

    fun isPerson(): Boolean = type == LabelType.PERSON

    fun getIcon() = when (type) {
        LabelType.TAG -> LabelType.TAG.iconId
        LabelType.PERSON -> LabelType.PERSON.iconId
        else -> LabelType.PLACE.iconId
    }

    suspend fun canBeDeleted(appDao: AppDao) =
        appDao.getEventCountForLabel(this.id!!) == 0
}

const val COLUMN_LABEL_NAME = "name"
const val COLUMN_LABEL_COLOR = "color"
