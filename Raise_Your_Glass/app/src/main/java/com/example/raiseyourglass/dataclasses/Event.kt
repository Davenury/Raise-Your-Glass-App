package com.example.raiseyourglass.dataclasses

import java.time.LocalDate
import java.time.ZoneId
import java.util.*


data class Event(
    /**
     * date - date of event
     * place - place of event
     * isPrivate - determines if the event is private (only owner can...
     * ...invite people) or public (every logged user sees it)
     * ownerID - id of event owner
     * orders - list of objects that determines, which person ordered...
     * ...what
     * participants - list of userIDs that will participate in event
     * invited - list of userIDs that are invited to the event*/

    var date: Date = Date(),
    var place: String = "",
    var isPrivate: Boolean = false,
    var ownerID: String = "",
    var orders: List<Order> = mutableListOf(),
    var participants: List<String> = mutableListOf(),
    var invited: List<String> = mutableListOf()
) {

    companion object {
        fun apply(
            date: LocalDate,
            place: String,
            isPrivate: Boolean,
            ownerID: String,
            orders: List<Order>,
            participants: List<String>,
            invited: List<String>
        ): Event =
            Event(localDateToDate(date), place, isPrivate, ownerID, orders, participants, invited)

        private fun localDateToDate(localDate: LocalDate): Date = java.util.Date.from(
            localDate.atStartOfDay().atZone(
                ZoneId.systemDefault()
            ).toInstant()
        )

    }

    fun dateToLocalDate(): LocalDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

}