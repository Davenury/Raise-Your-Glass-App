package com.example.raiseyourglass.dataclasses

import java.time.LocalDateTime


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

    var date: LocalDateTime = LocalDateTime.now(),
    var place: String = "",
    var isPrivate: Boolean = false,
    var ownerID: String = "",
    var orders: List<Order> = mutableListOf(),
    var participants: List<String> = mutableListOf(),
    var invited: List<String> = mutableListOf()
)