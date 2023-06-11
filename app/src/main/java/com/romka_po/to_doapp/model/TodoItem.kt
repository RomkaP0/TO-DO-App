package com.romka_po.to_doapp.model

import com.romka_po.to_doapp.utils.Importance

data class TodoItem(
    val id:String,
    var text:String,
    var importance: Importance,
    var isComplete:Boolean,
    val dateCreate: Long,
    var dateComplete: Long? = null,
    var dateEdit:Long? = null,
)
