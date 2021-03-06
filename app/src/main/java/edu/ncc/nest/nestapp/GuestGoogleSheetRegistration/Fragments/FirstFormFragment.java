package edu.ncc.nest.nestapp.GuestGoogleSheetRegistration.Fragments;

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
import android.widget.AdapterView;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import edu.ncc.nest.nestapp.GuestDatabaseRegistration.DatabaseClasses.GuestRegistryHelper;
import edu.ncc.nest.nestapp.GuestDatabaseRegistration.DatabaseClasses.GuestRegistrySource;
import edu.ncc.nest.nestapp.GuestDatabaseRegistration.Fragments.SecondFormFragment;
import edu.ncc.nest.nestapp.databinding.FragmentGuestGoogleSheetRegistrationFirstFormBinding;
import edu.ncc.nest.nestapp.R;

/**
 * FirstFormFragment: Represents a form that a guest can fill in with their personal information
 * such as, name, phone-number, email-address, ncc-id, postal-address, city, zip-code, birth-date,
 * and date-of-registration.
 */
public class FirstFormFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String TAG = FirstFormFragment.class.getSimpleName();

    private FragmentGuestGoogleSheetRegistrationFirstFormBinding binding;

    // Database where we will store user information
    private GuestRegistrySource db;

    // Variables to store user information
    private EditText lastName, firstName, ncc_affil, birth_date, gender, phone, ncc_id, address, city, state, zip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG,"In FirstFormFragment onCreateView()");

        binding = FragmentGuestGoogleSheetRegistrationFirstFormBinding.inflate(inflater, container, false);

        // Inflate the layout for this fragment
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG,"In FirstFormFragment onViewCreated()");

        // Creating the database and passing the correct context as the argument
        //db = new GuestRegistrySource(this);

        // Getting a handle on info from the UI
        //lastName = (EditText)(view.findViewById(R.id.editText));
        lastName = binding.editText;
        //firstName = (EditText)(view.findViewById(R.id.editText2));
        firstName = binding.editText2;
        ncc_affil = (EditText)(view.findViewById(R.id.editText3));
        birth_date = (EditText)(view.findViewById(R.id.editText4));
        gender = (EditText)(view.findViewById(R.id.editText5));
        phone = (EditText)(view.findViewById(R.id.editText6));
        ncc_id = (EditText)(view.findViewById(R.id.editText7));
        address = (EditText)(view.findViewById(R.id.editText8));
        city = (EditText)(view.findViewById(R.id.editText9));
        state = (EditText)(view.findViewById(R.id.editText10));
        zip = (EditText)(view.findViewById(R.id.editText11));

        lastName.setText("Potato");
        firstName.setText("Mr.");

        Log.d(TAG,"First name: " + lastName + "\nLast name: " + firstName);

        view.findViewById(R.id.next_button).setOnClickListener(clickedView -> {

            // Variable used for checks
            //boolean ins = false;

            // Adding the values into the database if submit button is pressed
            /*
            if (view.getId() == R.id.done_button) {

                // NOTE: The parameter 'barcode' was recently added to this method
                // TODO: Update parameter 'barcode' to the barcode representing this user
                //ins = db.insertData(name.getText().toString(), email.getText().toString(), phone.getText().toString(), date.getText().toString(), address.getText().toString(), city.getText().toString(), zip.getText().toString(), null);

            }
             */

            // Notifying the user if the add was successful
            /*
            if (ins) {
                Toast.makeText(getApplicationContext(), "User Added", Toast.LENGTH_LONG).show();
            }
            */

            // Navigate to SecondFormFragment
            NavHostFragment.findNavController(FirstFormFragment.this)
                    .navigate(R.id.action_GSR_FirstFormFragment_to_SecondFormFragment);

        });

    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG,"In FirstFormFragment onStart()");

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}