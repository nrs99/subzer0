package edu.lehigh.cse216.slj222;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EspressoTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void espressoTest() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.textView),
                        childAtPosition(
                                allOf(withId(R.id.add_post),
                                        childAtPosition(
                                                withClassName(is("androidx.appcompat.widget.LinearLayoutCompat")),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("performing ui test"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.post_button), withText("post"),
                        childAtPosition(
                                allOf(withId(R.id.add_post),
                                        childAtPosition(
                                                withClassName(is("androidx.appcompat.widget.LinearLayoutCompat")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatButton.perform(click());

        pressBack();

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.like_button),
                        childAtPosition(
                                allOf(withId(R.id.LinearLayout02),
                                        childAtPosition(
                                                withId(R.id.llCardBack),
                                                1)),
                                0),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.dislike_button),
                        childAtPosition(
                                allOf(withId(R.id.LinearLayout02),
                                        childAtPosition(
                                                withId(R.id.llCardBack),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.textView), withText("performing ui test"),
                        childAtPosition(
                                allOf(withId(R.id.add_post),
                                        childAtPosition(
                                                withClassName(is("androidx.appcompat.widget.LinearLayoutCompat")),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(longClick());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.textView), withText("performing ui test"),
                        childAtPosition(
                                allOf(withId(R.id.add_post),
                                        childAtPosition(
                                                withClassName(is("androidx.appcompat.widget.LinearLayoutCompat")),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText(""));

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.textView),
                        childAtPosition(
                                allOf(withId(R.id.add_post),
                                        childAtPosition(
                                                withClassName(is("androidx.appcompat.widget.LinearLayoutCompat")),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatEditText4.perform(closeSoftKeyboard());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
