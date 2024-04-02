package com.example.emojibrite;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CheckedInFragment extends Fragment {
    // put your uninitialized attributes here
    ListView attendeesListView;
    AttendeesArrayAdapter attendeesArrayAdapter;

    ArrayList<Users> attendeesList;

    HashMap<String, Integer> checkInCounts = new HashMap<>();

    private String eventId;
    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View checkedInLayout = inflater.inflate(R.layout.fragment_checked_in, container, false);
        Log.d("Aivan", "CheckInFragmentonCreateView: called");

        // init your attributes here
        attendeesListView = checkedInLayout.findViewById(R.id.attendee_view_list);
        // if the data is passed via a bundle, do the following
        // Bundle bundle = getArguments();

        Bundle bundle = getArguments();
        if (bundle != null) {
            eventId = bundle.getString("eventId");
            Log.d("SignedUpFragment", "Received Event ID: " + eventId);
        } else {
            Log.d("SignedUpFragment", "No bundle passed");
        }

        attendeesList = new ArrayList<>();
        ArrayList<Users> uniqueUidArray = new ArrayList<>();
        attendeesArrayAdapter = new AttendeesArrayAdapter(getContext(), uniqueUidArray, "1", checkInCounts);
        attendeesListView.setAdapter(attendeesArrayAdapter);
        loadAttendees(eventId);




        return checkedInLayout;
    }


    private void loadAttendees(String eventId) {
        Database db = new Database();
        ArrayList<Users> uniqueUidArray = new ArrayList<>();

        db.getEventById(eventId, new Database.EventCallBack() {
            @Override
            public void onEventFetched(Event event) {
                ArrayList<String> listAttendees = event.getAttendeesList();
                for (String userId : listAttendees) {
                    // Update check-in count
                    checkInCounts.put(userId, checkInCounts.getOrDefault(userId, 0) + 1);

                    // Load user details only if not already in the unique list
                    if (!uniqueUidArray.stream().anyMatch(u -> u.getProfileUid().equals(userId))) {
                        db.getUserDocument(userId, new Database.OnUserDocumentRetrievedListener() {
                            @Override
                            public void onUserDocumentRetrieved(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    Users user = documentSnapshot.toObject(Users.class);
                                    if (user != null) {
                                        uniqueUidArray.add(user);
                                        if (getActivity() != null) {
                                            getActivity().runOnUiThread(() -> attendeesArrayAdapter.notifyDataSetChanged());
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }


    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored in to the view. This gives
     * subclasses a chance to initialize themselves once they know their view hierarchy has been completely created.
     * The fragment's view hierarchy is not however attached to its parent at this point.
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // button logics
    }
}