package com.example.raiseyourglass.dataclasses

import com.google.firebase.firestore.DocumentReference
import java.io.Serializable
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
    var participants: MutableList<String> = mutableListOf(),
    var invited: MutableList<String> = mutableListOf(),
    val documentID: DocumentReference? = null
){

    companion object {
        fun apply(
            date: LocalDate,
            place: String,
            isPrivate: Boolean,
            ownerID: String,
            participants: MutableList<String>,
            invited: MutableList<String>
        ): Event =
            Event(localDateToDate(date), place, isPrivate, ownerID, participants, invited)

        private fun localDateToDate(localDate: LocalDate): Date = java.util.Date.from(
            localDate.atStartOfDay().atZone(
                ZoneId.systemDefault()
            ).toInstant()
        )
    }

    fun dateToLocalDate(): LocalDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

    fun toMap():Map<String,Any> {
        val eventMap = HashMap<String, Any>()
        eventMap["date"] = date
        eventMap["place"] = place
        eventMap["private"] = isPrivate
        eventMap["ownerID"] = ownerID
        eventMap["invited"] = invited
        return eventMap
    }

}