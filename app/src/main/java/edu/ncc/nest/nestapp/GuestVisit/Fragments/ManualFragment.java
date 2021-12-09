package edu.ncc.nest.nestapp.GuestVisit.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import edu.ncc.nest.nestapp.GuestDatabaseRegistration.DatabaseClasses.GuestRegistrySource;
import edu.ncc.nest.nestapp.R;

public class ManualFragment extends Fragment {


    String barcode, field1, field2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_visit_manual_entry, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) { //Recovering after a screen rotation
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            ((EditText) view.findViewById(R.id.guest_visit_barcode_entry)).setText(savedInstanceState.getString("barE"));
            ((EditText) view.findViewById(R.id.guest_visit_msie_pt1)).setText(savedInstanceState.getString("msiE1"));
            ((EditText) view.findViewById(R.id.guest_visit_msie_pt2)).setText(savedInstanceState.getString("msiE2"));

        }

        ( view.findViewById(R.id.manual_submit_btn)).setOnClickListener(view1 -> {

            // Create an instance of the database helper
            GuestRegistrySource db = new GuestRegistrySource(requireContext());


            //Saving the information in the EditText views
            barcode = String.valueOf(((EditText) view.findViewById(R.id.guest_visit_barcode_entry)).getText());
            Log.d("Saving", "Barcode : " + barcode);

            //Will most likely make field 1 the guest's name
            field1 = String.valueOf(((EditText) view.findViewById(R.id.guest_visit_msie_pt1)).getText());
            Log.d("Saving", "Field1 : " + field1);

            //Up for debate
            field2 = String.valueOf(((EditText) view.findViewById(R.id.guest_visit_msie_pt2)).getText());
            Log.d("Saving", "Field2 : " + field2);

            Bundle entryResults = new Bundle();

            //If barcode is entered other information is irrelevant
            if (barcode != null) {
                //Packaging the barcode in a neat little bundle
                if (db.isRegistered(barcode) == null) {

//            db.open();

                    // NOTE: This method may change over time, make sure it is up to date with GuestRegistrySource.
                    if (!db.insertData("John Doe", "John.Doe@example.com", "555-555-5555", "01/23/45",
                            "123 Test St", "Test", "12345", "11111", barcode))

                    db.close();

                entryResults.putString("BARCODE", barcode);
                final String GUEST_NAME = db.isRegistered(barcode);

                if (GUEST_NAME != null)


                    // If the guest is registered, include the guest's name in the result
                    entryResults.putString("GUEST_NAME", GUEST_NAME);


            }
        //TODO Create other sign in method
        else {


            }


            //Allowing the bundle to be accessed from other fragments
            //Using Scan_confirmed to stop conflicts
            getParentFragmentManager().setFragmentResult("SCAN_CONFIRMED", entryResults);


            NavHostFragment.findNavController(ManualFragment.this)
                    .navigate(R.id.action_GV_ManualFragment_to_ConfirmationFragment);


        });
    }
}
