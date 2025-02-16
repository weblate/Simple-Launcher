package com.simplemobiletools.launcher.models

import android.graphics.drawable.Drawable
import androidx.room.*
import com.simplemobiletools.commons.extensions.normalizeString
import com.simplemobiletools.commons.helpers.SORT_BY_TITLE
import com.simplemobiletools.commons.helpers.SORT_DESCENDING

@Entity(tableName = "apps", indices = [(Index(value = ["package_name"], unique = true))])
data class AppLauncher(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "package_name") var packageName: String,
    @ColumnInfo(name = "order") var order: Int,
    @ColumnInfo(name = "thumbnail_color") var thumbnailColor: Int,

    @Ignore var drawable: Drawable?
) : Comparable<AppLauncher> {

    constructor() : this(null, "", "", 0, 0, null)

    companion object {
        var sorting = 0
    }

    override fun equals(other: Any?) = packageName.equals((other as AppLauncher).packageName, true)

    override fun hashCode() = super.hashCode()

    fun getBubbleText() = title

    override fun compareTo(other: AppLauncher): Int {
        var result = when {
            sorting and SORT_BY_TITLE != 0 -> title.normalizeString().lowercase().compareTo(other.title.normalizeString().lowercase())
            else -> {
                if (order > 0 && other.order == 0) {
                    -1
                } else if (order == 0 && other.order > 0) {
                    1
                } else if (order > 0 && other.order > 0) {
                    order.compareTo(other.order)
                } else {
                    title.lowercase().compareTo(other.title.lowercase())
                }
            }
        }

        if (sorting and SORT_DESCENDING != 0) {
            result *= -1
        }

        return result
    }
}
