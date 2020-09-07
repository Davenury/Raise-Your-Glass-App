package com.example.raiseyourglass.firebase

import com.example.raiseyourglass.dataclasses.Event
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import java.util.*

class ValidatorEventTests {
    @Test
    fun `empty place returns false`(){
        //given
        val event = Event(Date(), "", true, "344123", mutableListOf(), mutableListOf(), mutableListOf())
        //when
        val result = Validator.addEventValidator(event)
        //then
        assertThat(result).isFalse()
    }

    @Test
    fun `not adding date returns false`(){
        //given
        val date = Date(1000, 0, 1)
        val event = Event(date, "place", true, "231343243", mutableListOf(), mutableListOf(), mutableListOf())
        //when
        val result = Validator.addEventValidator(event)
        //then
        assertThat(result).isFalse()
    }

    @Test
    fun `good event returns true`(){
        //given
        val event = Event(Date(2020, 10, 1), "Place", true, "344123", mutableListOf(), mutableListOf(), mutableListOf())
        //when
        val result = Validator.addEventValidator(event)
        //then
        assertThat(result).isTrue()
    }
}