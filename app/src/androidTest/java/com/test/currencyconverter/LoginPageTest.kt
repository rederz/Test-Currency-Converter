package com.test.currencyconverter

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule

import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@RunWith(AndroidJUnit4::class)
class LoginPageTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LoginPage::class.java)
    private var mActivity:LoginPage? = null

    @org.junit.jupiter.api.Test
    fun loginTest(){
        mActivity = mActivityTestRule.activity

        val viewUserId = onView(withId(R.id.userID)).perform(typeText("aaa@aaa.com"))
        assertNotNull(viewUserId)

        val viewPwd = onView(withId(R.id.password)).perform(typeText("Test123"), closeSoftKeyboard())
        assertNotNull(viewPwd)

        onView(withId(R.id.login_button)).check(matches(isClickable())).perform(click())
        mActivity = null
    }
}