package com.romka_po.to_doapp.model

import android.os.Parcelable
import com.romka_po.to_doapp.utils.Importance
import kotlinx.parcelize.Parcelize

@Parcelize
data class TodoItem(
    var id: String,
    var text: String,
    var importance: Importance,
    var dateCreate: Long,
    var dateComplete: Long? = null,
    var dateEdit: Long? = null,
    var isComplete: Boolean = false,
) : Parcelable
