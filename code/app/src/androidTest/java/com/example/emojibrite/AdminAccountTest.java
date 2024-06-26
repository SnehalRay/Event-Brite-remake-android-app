package com.example.emojibrite;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;
import android.os.SystemClock;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;


@RunWith(AndroidJUnit4.class)
public class AdminAccountTest {
    public static Users mockUser;

    
    

    @Before
    public void setup() throws IOException {

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.executeShellCommand("settings put global window_animation_scale 0");
        device.executeShellCommand("settings put global transition_animation_scale 0");
        device.executeShellCommand("settings put global animator_duration_scale 0");

        // Mock user data
        mockUser = new Users();

        mockUser.setName("John Doe");
        mockUser.setEmail("johndoe@example.com");
        mockUser.setNumber("1234567890");
        mockUser.setRole("3");
        mockUser.setProfileUid("SroGuirZdhfbwcectKhBJkpJpdl2");
        mockUser.setEnableNotification(true);
        mockUser.setEnableGeolocation(true);
        mockUser.setAutoGenImageUri("https://ui-avatars.com/api/?name=John+Doe/");
        mockUser.setUploadedImageUri(null);
        mockUser.setHomePage("homepage");
        mockUser.setFcmToken("cXEdjvR2RRGFnwzJRgW0nj:APA91bGr5XYv7AhDGX6MqFKBwUxnKTwQUbIEg0u9IE1pitS2N9oi_jFPpPmz1VkYLpAF_HbS4W2YYlsOADlGc32fRVvCRgoMgiBxvQxCl4EoYVlqoI5yUn3qnDk_ZTLaOCG2mKaV_GqF");


        AdminAccountActivity.user = mockUser;


        // Create an ArrayList of Users
        ArrayList<Users> users = new ArrayList<>();
        users.add(mockUser);

        // Create the AttendeesArrayAdapter with the ArrayList of Users
        AttendeesArrayAdapter adapter = new AttendeesArrayAdapter(ApplicationProvider.getApplicationContext(), users, "3", null);

        // Create an intent that contains the Users object
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AdminAccountActivity.class);
        intent.putExtra("userObject", mockUser);

        // Manually launch the AdminAccountActivity with the intent
        ActivityScenario.launch(intent);
    }


    @Test
    public void testDeleteButton() {

        onView(withId(R.id.backButton)).check(matches(isDisplayed()));

    }

    @Test
    public void testAccountListDisplayed() {

        onView(withId(R.id.profile_list)).check(matches(isDisplayed()));
    }

    @Test
    public void testAdminToggle() {
        SystemClock.sleep(3000);
        onView(withId(R.id.backButton)).check(matches(isDisplayed()));
        onView(withId(R.id.backButton)).perform(click());
        onView(withId(R.id.accountAdminButton)).check(matches(isDisplayed()));
    }


}

