package edu.ncc.nest.nestapp;

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

public class GuestFormFirstFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    protected static final String TAG = "TESTING";
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"In GuestFormFirstFragment onStart()");
    }
    //database where we will store user information
    GuestFormHelper db;

    //variables to store user information
    EditText lastName, firstName, ncc_affil, age, gender, phone, ncc_id, address, city, state, zip;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Log.d(TAG,"In GuestFormFirstFragment onCreateView()");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_guest_form_first_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG,"In GuestFormFirstFragment onViewCreated()");

        //creating the database and passing the correct context as the argument
        //db = new GuestFormHelper(this);

        //getting a handle on info from the UI
        lastName = (EditText)(getView().findViewById(R.id.editText));
        firstName = (EditText)(getView().findViewById(R.id.editText2));
        ncc_affil = (EditText)(getView().findViewById(R.id.editText3));
        age = (EditText)(getView().findViewById(R.id.editText4));
        gender = (EditText)(getView().findViewById(R.id.editText5));
        phone = (EditText)(getView().findViewById(R.id.editText6));
        ncc_id = (EditText)(getView().findViewById(R.id.editText7));
        address = (EditText)(getView().findViewById(R.id.editText8));
        city = (EditText)(getView().findViewById(R.id.editText9));
        state = (EditText)(getView().findViewById(R.id.editText10));
        zip = (EditText)(getView().findViewById(R.id.editText11));

        view.findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //variable used for checks
                //boolean ins = false;

                //adding the values into the database if submit button is pressed
                /*
                if (view.getId() == R.id.done_button) {

                    // NOTE: The parameter 'barcode' was recently added to this method
                    // TODO: Update parameter 'barcode' to the barcode representing this user
                    //ins = db.insertData(name.getText().toString(), email.getText().toString(), phone.getText().toString(), date.getText().toString(), address.getText().toString(), city.getText().toString(), zip.getText().toString(), null);

                }
                 */

                //notifying the user if the add was successful
                /*
                if (ins) {
                    Toast.makeText(getApplicationContext(), "User Added", Toast.LENGTH_LONG).show();
                }
                */

                NavHostFragment.findNavController(GuestFormFirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    /**
     * home method - goes to the home screen
     */
    /*
    public void home() {
        Intent intent = new Intent(this, Choose.class);
        startActivity(intent);
    }
     */


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // save state
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