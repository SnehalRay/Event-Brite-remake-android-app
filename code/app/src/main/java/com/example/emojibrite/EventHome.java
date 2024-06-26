package com.example.emojibrite;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * EventHome is an AppCompatActivity that serves as the main page for displaying a list of events.
 * It allows users to view event details and add new events.
 */

public class EventHome extends AppCompatActivity implements AddEventFragment.AddEventListener {

    ListView eventList;
    EventAdapter eventAdapter; // Custom adapter to bind event data to the ListView
    ArrayList<Event> dataList;
    private Users user;
    private Database database = new Database();

    FloatingActionButton fab;

    Button otherEvent;

//    Button otherEvent = findViewById(R.id.other_events_button);

    ImageView notifButton, logoButton;

    ImageView profileButton;

    private static final String TAG = "ProfileActivityTAG";

    private Map<Event, Boolean> eventMap = new HashMap<>();

    /**
     * Opens the EventDetailsActivity to show the details of the selected event.
     * @param event The event to show details for.
     * @param user The user object to pass to the EventDetailsActivity.
     */

    private void showEventDetails(Event event, Users user) {
        Intent intent = new Intent(this, EventDetailsActivity.class);
        intent.putExtra("eventId", event.getId());
        if (user!=null){
            Log.d("TAG","CHECKING CHECKING CHECKING  "+ user.getProfileUid());}
        intent.putExtra("userObject", user);
        intent.putExtra("privilege", "1");//You send the current user profile id into the details section
        startActivity(intent);
    }
    /**
     * Opens the NotificationsActivity to show the notifications of the selected event.
     * @param user The user object to pass to the NotificationsActivity.
     */

    private void showNotifications(Users user){
        Intent intent = new Intent(this, NotificationsActivity.class);
        if (user!=null){
            Log.d("TAG","CHECKING CHECKING CHECKING  "+ user.getProfileUid());}
        intent.putExtra("userObject", user);
        startActivity(intent);
    }

    /**
     * Shows the AddEventFragment to add a new event.
     */

    public void showAddEventDialog() {
        AddEventFragment dialog = AddEventFragment.newInstance(user);
        dialog.show(getSupportFragmentManager(), "AddEventFragment");
    }


    /**
     * Callback method triggered when an event is added.
     * It attempts to add the event to the Firestore database.
     *
     * @param event The event object to be added. This should not be null.
     */
    @Override
    public void onEventAdded(Event event) {
        if (event != null) {

            // Add the event to the database
            database.addEvent(event, task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Event added successfully into database");
                    eventMap.put(event, true);
                    updateLocalEventList(event);
                } else {
                    Log.e(TAG, "ERROR IN ADDING TO THE DATABASE", task.getException());
                }
            });
        }
    }

    /**
     * Updates the local list of events. This method is called after an event is successfully added
     * to the Firestore database to reflect the change in the local user interface.
     *
     * @param event The newly added or updated event.
     */
    private void updateLocalEventList(Event event) {
        int index = -1;
        for (int i = 0; i < dataList.size(); i++) {

            String existingEventId = dataList.get(i).getId();
            String newEventId = event.getId();
            // Check if both IDs are non-null and equal
            if (existingEventId != null && newEventId != null && existingEventId.equals(newEventId)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            // If the event is found, update it in the list.
            dataList.set(index, event);
        } else {
            // If the event is not found, add it to the list.
            dataList.add(event);
        }
        // Notify the adapter that the data has changed to update the UI.
        eventAdapter.notifyDataSetChanged();
    }

    /**
     * Method to check the user document
     * @param userUid
     */

    private void checkUserDoc(String userUid){
        database.getUserDocument(userUid, documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Log.d(TAG, "User document exists");
                user = documentSnapshot.toObject(Users.class);
                user.setEnableAdmin(false);
                settingUpPfp();
                buttonListeners();
            } else {
                Log.d(TAG, "User document does not exist");

                Toast.makeText(this, "User got deleted by admin", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EventHome.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }


    /**
     * In charge of all the button listeners
     */

    private void buttonListeners(){

        if(user!=null) {
            Log.d(TAG, "user name for EventHome: " + user.getName() + user.getProfileUid() + user.getUploadedImageUri() + user.getAutoGenImageUri() + user.getHomePage());
            Log.d(TAG, "user id for EventHome: " + user.getProfileUid());

            fab = findViewById(R.id.event_scan_btn);
            fab.setOnClickListener(view -> showAddEventDialog());
        }

        eventList.setOnItemClickListener(((parent, view, position, id) -> {
            Event selectedEvent = dataList.get(position);

            showEventDetails(selectedEvent, user);

        }));

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Go to the ProfileActivity page
                Log.d(TAG, "Enter button clicked"); // for debugging

                Intent intent = new Intent(EventHome.this, ProfileActivity.class);

                intent.putExtra("userObject", user);
                startActivity(intent);
            }
        });

        otherEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Other events button clicked");
                Intent intent2 = new Intent(EventHome.this, OtherEventHome.class);

                intent2.putExtra("userObject", user);
                startActivity(intent2);  // Use intent2 to start the activity
            }
        });
        notifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotifications(user);

            }
        });

        logoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventHome.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_home_page);

        eventList = findViewById(R.id.event_organizer_list);
        dataList = new ArrayList<>();

        eventAdapter = new EventAdapter(this, dataList, eventMap);
        eventList.setAdapter(eventAdapter);

        otherEvent = findViewById(R.id.other_events_button);

        notifButton = findViewById(R.id.notif_bell);

        logoButton = findViewById(R.id.imageView);

        Intent intent = getIntent();
//        user = intent.getParcelableExtra("userObject");
//        //this allows the user to not be stuck on admin activity all the time
//        user.setEnableAdmin(false);
        if (intent.hasExtra("userObject"))
        {
            user = intent.getParcelableExtra("userObject");
            if (user != null) {
                user.setEnableAdmin(false);
            } else {
                Log.e("EVENTHOME: PROFILE", "User object is null");
            }
        }
        else {
            Log.e(TAG, "No userObject passed in intent");



        }

        profileButton = findViewById(R.id.profile_pic);
        checkUserDoc(user.getProfileUid());

        Log.d(TAG, "PROFILE PIC EVENT HOME "+user.getUploadedImageUri());

//        notifButton.setOnClickListener();







        // When profile is clicked, go to profile activity
    }

    /**
     * In charge of setting up the profile picture on top right
     */
    private void settingUpPfp(){
        if (user.getUploadedImageUri() != null) {
            // User uploaded a picture, use that as the ImageView
            //Uri uploadedImageUri = Uri.parse(user.getUploadedImageUri());
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(EventHome.this).load(user.getUploadedImageUri()).into(profileButton);
                }
            });

        } else if (user.getUploadedImageUri() == null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(EventHome.this).load(user.getAutoGenImageUri()).into(profileButton);
                }
            });

//            fetchEventsForCurrentUser();
//            fetchSignedUpEvents();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchMyEventsPage(); // Refresh the events list every time the activity resumes
    }

    /**
     * Fetches events from the Firestore database that are organized by the current user.
     * This method queries the database for events where the current user is the organizer and updates
     * the local list to reflect these events. This is typically used to populate the UI with relevant data.
     */
    private void fetchEventsForCurrentUser() {

        if (user != null) {
            // Retrieve the unique ID of the current user.
            String currentUserId = user.getProfileUid();
            // Call the method in the Database class to get events organized by this user.
            database.getEventsByOrganizer(currentUserId, events -> {
                // Clear the current list of events to prepare for updated data.
                dataList.clear();
                dataList.addAll(events); // Add all the retrieved events to the local list.
                eventAdapter.notifyDataSetChanged();
            });
        }
    }

    /**
     * Fetches events from the Firestore database that are signed up by the current user.
     * This method queries the database for events where the current user is the signee and updates
     * the local list to reflect these events. This is typically used to populate the UI with relevant data.
     */
    private void fetchSignedUpEvents(){
        if (user!=null){
            String currentUserId = user.getProfileUid();
            database.getSignedUpEvents(currentUserId, events -> {
                dataList.clear();
                dataList.addAll(events);
                eventAdapter.notifyDataSetChanged();
            });
        }
    }

    /**
     * Fetches events from the Firestore database that are organized by the current user as well as the events signed up by user.
     * This method queries the database for events where the current user is the organizer and updates
     * the local list to reflect these events. This is typically used to populate the UI with relevant data.
     */
    private void fetchMyEventsPage() {
        if (user != null) {
            String currentUserId = user.getProfileUid();
            AtomicInteger pendingQueries = new AtomicInteger(2);

            // Clear previous data
            eventMap.clear();
            dataList.clear();

            database.getSignedUpEvents(currentUserId, events -> {
                for (Event event : events) {
                    eventMap.put(event, false); // False for events signed up for
                }
                if (pendingQueries.decrementAndGet() == 0) {
                    updateAdapter();
                }
            });

            database.getEventsByOrganizer(currentUserId, events -> {
                for (Event event : events) {
                    eventMap.put(event, true); // True for events organized by the user
                }
                if (pendingQueries.decrementAndGet() == 0) {
                    updateAdapter();
                }
            });
        }
    }

    /**
     * In charge of updating the adapter
     */
    private void updateAdapter() {
        dataList.addAll(eventMap.keySet()); // Add all the events from the map
        eventAdapter.notifyDataSetChanged();
    }





}
