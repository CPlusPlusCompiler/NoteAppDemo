package com.andrius.notesappdemo


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.andrius.notesappdemo.ui.MainActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest2 {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest2() {
        val floatingActionButton = onView(
            allOf(
                withId(R.id.btn_add),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.fragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        floatingActionButton.perform(click())

        val textInputEditText = onView(
            allOf(
                childAtPosition(
                    childAtPosition(
                        withId(R.id.et_note_text),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("e"), closeSoftKeyboard())

        val materialButton = onView(
            allOf(
                withId(R.id.btn_add_note), withText("Save"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.fragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val linearLayout = onView(
            allOf(
                withId(R.id.cl_note),
                childAtPosition(
                    allOf(
                        withId(R.id.mcv_note),
                        childAtPosition(
                            withId(R.id.rv_notes),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        linearLayout.perform(click())

        val materialButton2 = onView(
            allOf(
                withId(R.id.btn_add_note), withText("Save"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.fragment),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
