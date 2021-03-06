package edu.ncc.nest.nestapp.CheckExpirationDate.Fragments;

/* Copyright (C) 2020 The LibreFoodPantry Developers.
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import edu.ncc.nest.nestapp.CheckExpirationDate.Activities.CheckExpirationDateActivity;
import edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses.NestDBDataSource;
import edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses.NestUPC;
import edu.ncc.nest.nestapp.R;

/**
 * EnterUpcFragment: Allows user to enter a upc barcode manually. When the user presses a "Lookup"
 * button, it should then validate the upc and check whether or not the upc exists in the local
 * database.
 *
 * Navigates to {@link ConfirmItemFragment} with the item pulled from database, if the upc exists in
 * the local database.
 *
 * Navigates to {@link SelectItemFragment} with the barcode, if the upc does not exist in the local
 * database.
 *
 *  @deprecated This Fragment is being combined with {@link StartFragment}.
 */
@Deprecated
public class EnterUpcFragment extends Fragment {

    /////////////////////////////////// Lifecycle Methods Start ////////////////////////////////////

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Clear any set fragment results since they are not needed in or prior to this fragment
        getParentFragmentManager().clearFragmentResult("FOOD ITEM");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_expiration_date_enter_upc, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Call retrieveUPC method when button_enter_next is pressed
        view.findViewById(R.id.button_enter_next).setOnClickListener(v -> retrieveUPC());

    }

    //////////////////////////////////// Custom Methods Start //////////////////////////////////////

    /**
     * Gets the UPC from the EditText object that the user entered the UPC into and navigates to the
     * appropriate fragment with the entered UPC.
     */
    public void retrieveUPC() {

        // Look in the EditText widget and retrieve the String the user passed in
        EditText editText = requireView().findViewById(R.id.edittext_enter_upc);

        // Get the upc string from the EditText object
        String upcBarcode = editText.getText().toString();

        // Check validity of the UPC
        if(upcBarcode.length() != 12) {

            Toast.makeText(this.getContext(),"UPC length is 12 numbers, please enter a 12-digit number", Toast.LENGTH_SHORT).show();

        } else {

            NestDBDataSource dataSource = CheckExpirationDateActivity.requireDataSource(this);

            FragmentManager fragmentManager = getParentFragmentManager();

            NestUPC foodItem = dataSource.getNestUPC(upcBarcode);

            Bundle result = new Bundle();

            if (foodItem != null) {

                // NOTE: If we get here, then the upc is already in the database.

                // Put the item in a bundle and pass it to ConfirmItemFragment
                result.putSerializable("foodItem", foodItem);

                fragmentManager.setFragmentResult("FOOD ITEM", result);

                NavHostFragment.findNavController(EnterUpcFragment.this)
                        .navigate((R.id.CED_ConfirmItemFragment));

            } else {

                // NOTE: If we get here, then the upc is does not exist in the database.

                // Put UPC into a bundle and pass it to SelectItemFragment (may not be necessary)
                result.putString("upcBarcode", upcBarcode);

                fragmentManager.setFragmentResult("FOOD ITEM", result);

                NavHostFragment.findNavController(EnterUpcFragment.this)
                        .navigate((R.id.CED_SelectItemFragment));

            }

        }

    }

}