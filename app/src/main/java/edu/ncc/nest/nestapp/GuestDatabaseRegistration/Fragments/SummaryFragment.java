package edu.ncc.nest.nestapp.GuestDatabaseRegistration.Fragments;

/**
 *
 * Copyright (C) 2020 The LibreFoodPantry Developers.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.fragment.NavHostFragment;


import edu.ncc.nest.nestapp.R;
import edu.ncc.nest.nestapp.databinding.FragmentGuestDatabaseRegistrationFourthFormBinding;
import edu.ncc.nest.nestapp.databinding.FragmentGuestDatabaseRegistrationSummaryBinding;

/**
 * SummaryFragment: This fragment represent a summary of the registration process. Displays messages
 * to the guest depending on whether or not the registration process was successful or not. Should
 * also generate a barcode for the guest if needed.
 *
 * TODO: ///////////////////////////////////////////////////////////////////////////////////////////
 *  - This fragment should retrieve all the guest's information passed in a FragmentResult from
 *    previous fragments.
 *  - Check to make sure that the guest isn't already registered in the database (using name).
 *  - Generate a unique barcode not currently in use for the guest (if the user did not scan one).
 *  - Register the guest with all their information and barcode into the local registry database.
 *  - If the guest is already registered, this fragment should show a message saying that the guest
 *    is already registered.
 *  - If the registration is successful this fragment should display a message saying that
 *    registration was successful and display the users newly generated barcode back to the guest.
 * TODO: ///////////////////////////////////////////////////////////////////////////////////////////
 */
public class SummaryFragment extends Fragment  {

    private FragmentGuestDatabaseRegistrationSummaryBinding binding;

    private String fname;
    private String lname;
    private String phoneNum;
    private String nccId;

    private String streetAddress1;
    private String streetAddress2;
    private String city;
    private String state;
    private String zip;
    private String affiliation;
    private String age;
    private String gender;

    private String dietary;
    private String snap;
    private String otherProg;
    private String employment;
    private String health;
    private String housing;
    private String income;

    private String numPeople;
    private String childcare;
    private String children1;
    private String children5;
    private String children12;
    private String children18;

    public static final String TAG = SummaryFragment.class.getSimpleName();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "In SummaryFragment onCreateView()");

        binding = FragmentGuestDatabaseRegistrationSummaryBinding.inflate(inflater, container, false);
        return binding.getRoot();

        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_guest_database_registration_summary, container, false);

    }
    /*
        Test work done from obtaining first and second form information.
            * Tested the First Name(obtained from FirstFormFragment)
            * Tested the City(obtained from SecondFormFragment)
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toast.makeText(getContext(), "WARNING: Pressing back will clear data. Please double check before continuing.", Toast.LENGTH_LONG).show();

        //retrieving first name, last name, phone number and NCC ID from FirstFormFragment bundle.
        getParentFragmentManager().setFragmentResultListener("sending_first_form_fragment_info",
                this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        fname = result.getString("First Name");
                        lname = result.getString("Last Name");
                        phoneNum = result.getString("Phone Number");
                        nccId = result.getString("NCC ID");
                        Log.d(TAG, "The first name obtained is: " + fname);
                        Log.d(TAG, "The NCC ID is: " + nccId);

                        binding.grf1FName.setText(fname);
                        binding.grf1LName.setText(lname);
                        binding.grf1Phone.setText("(" + phoneNum.substring(0, 3) + ") " + phoneNum.substring(3, 6) + "-" + phoneNum.substring(6));
                        binding.grf1NccId.setText("N" + nccId);
                    }
                });

        //retrieving streetAddress1&2, city, state zip, affiliation, age, and gender from SecondFormFragment bundle.
        getParentFragmentManager().setFragmentResultListener("sending_second_form_fragment_info",
                this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        streetAddress1 = result.getString("Street Address 1");
                        streetAddress2 = result.getString("Street Address 2");  //optional
                        city = result.getString("City");
                        state = result.getString("State");
                        zip = result.getString("Zip");
                        affiliation = result.getString("Affiliation");
                        age = result.getString("Age");
                        gender = result.getString("Gender");
                        Log.d(TAG, "The city obtained is: " + city);
                        Log.d(TAG, "The age obtained is: " + age);

                        binding.grf2Address1.setText(streetAddress1);

                        if(streetAddress2 != null){
                            binding.grf2Address2.setText(streetAddress2);
                        }

                        binding.grf2City.setText(city);
                        binding.grf2State.setText(state);
                        binding.grf2Zip.setText(zip);
                        binding.grf2Affiliation.setText(affiliation);
                        binding.grf2Age.setText(age);
                        binding.grf2Gender.setText(gender);
                    }
                });

        getParentFragmentManager().setFragmentResultListener("sending_third_form_fragment_info",
                this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        dietary = result.getString("Dietary");
                        snap = result.getString("SNAP");
                        otherProg = result.getString("Other Programs");
                        employment = result.getString("Employment Status");
                        health = result.getString("Health Status");
                        housing = result.getString("Housing Status");
                        income = result.getString("Income");

                        binding.grf3Dietary.setText(dietary);
                        binding.grf3Snap.setText(snap);
                        binding.grf3OtherProgs.setText(otherProg);
                        binding.grf3StatusEmployment.setText(employment);
                        binding.grf3StatusHealth.setText(health);
                        binding.grf3StatusHousing.setText(housing);
                        binding.grf3Income.setText(income);
                    }
                });


                // OnClickListener for the "Done" button
                //TODO store in database when done button is clicked
//        view.findViewById(R.id.button).setOnClickListener(clickedView -> {
//
//            // Navigate back to splash screen.
//            // later, make if/else to go to scanner or login if scanner already in db
//            NavHostFragment.findNavController(SummaryFragment.this)
//                    .navigate(R.id.action_DBR_SummaryFragment_to_DBR_StartFragment);
//
//        });

    }

}