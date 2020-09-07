package com.example.raiseyourglass.firebase

import com.example.raiseyourglass.dataclasses.Drink
import org.junit.Test
import com.google.common.truth.Truth.assertThat

class ValidatorDrinkTests {
    @Test
    fun `empty name returns false`(){
        //given
        val drink = Drink("", "Long", "2edasdsd", mutableListOf(), mutableListOf())
        //when
        val result = Validator.addDrinkValidator(drink)
        //then
        assertThat(result).isFalse()
    }

    @Test
    fun `empty type returns false`(){
        //given
        val drink = Drink("Cokolwiek", "", "2edasdsd", mutableListOf(), mutableListOf())
        //when
        val result = Validator.addDrinkValidator(drink)
        //then
        assertThat(result).isFalse()
    }

    @Test
    fun `good drink returns true`(){
        //given
        val drink = Drink("Cokolwiek", "Long", "2edasdsd", mutableListOf(), mutableListOf())
        //when
        val result = Validator.addDrinkValidator(drink)
        //then
        assertThat(result).isTrue()
    }
}