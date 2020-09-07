package com.example.raiseyourglass.firebase

import org.junit.Test
import com.google.common.truth.Truth.assertThat

class ValidatorLoginValidTest{
    @Test
    fun `empty email returns false`(){
        //given
        val email = ""
        val password = "1234567"
        //when
        val result = Validator.areValid(email, password)
        //then
        assertThat(result).isFalse()
    }
    
    @Test
    fun `email without at returns false`(){
        //given
        val email = "abcd"
        val password = "1234567"
        //when
        val result = Validator.areValid(email, password)
        //then
        assertThat(result).isFalse()
    }

    @Test
    fun `password with less than six characters returns false`(){
        //given
        val email = "dorotka@wp.pl"
        val password = "123"
        //when
        val result = Validator.areValid(email, password)
        //then
        assertThat(result).isFalse()
    }

    @Test
    fun `empty password returns false`(){
        //given
        val email = "dorotka@wp.pl"
        val password = ""
        //when
        val result = Validator.areValid(email, password)
        //then
        assertThat(result).isFalse()
    }

    @Test
    fun `valid email and password return true`(){
        //given
        val email = "dorotka@wp.pl"
        val password = "abcdef2312"
        //when
        val result = Validator.areValid(email, password)
        //then
        assertThat(result).isTrue()
    }
}