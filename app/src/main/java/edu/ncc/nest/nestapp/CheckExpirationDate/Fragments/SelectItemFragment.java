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

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.Objects;

import edu.ncc.nest.nestapp.CheckExpirationDate.Activities.CheckExpirationDateActivity;
import edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses.NestDBDataSource;
import edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses.NestUPC;
import edu.ncc.nest.nestapp.R;

/**
 * SelectItemFragment: Allows user to select item options that will be used to determine a proper
 * product id for the provided barcode. The barcode is then added into the local database and
 * the resulting item is sent to {@link SelectPrintedExpirationDateFragment}.
 *
 * See {@link edu.ncc.nest.nestapp.NewNestUPC} and {@link edu.ncc.nest.nestapp.ItemInformation}
 * for reference.
 */
public class SelectItemFragment extends Fragment {

    /////////////////////////////////////// Class Variables ////////////////////////////////////////

    public static final String LOG_TAG = SelectItemFragment.class.getSimpleName();

    /** CONSTANT DEFAULTS **/
    // FOR NON ESSENTIAL TEXT ENTRY VIEWS THAT WERE OPTIONAL AND INTENTIONALLY LEFT BLANK;
    private final String DEFAULT_STRING = "[LEFT BLANK]";
    NestDBDataSource dataSource;

    /** INSTANCE VARS **/
    private String upcBarcode; // UPC as passed from previous fragments (populated with saved bundle)

    // To access UI elements; both directly and indirectly
    Button categoryButton, productButton, subtitleButton;
    TextView categoryHint, productHint, subtitleHint;

    private String productName, productSubtitle;
    private int productCategoryId;

    /////////////////////////////////// Lifecycle Methods Start ////////////////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Initialize variables; placeholder values replaced later
        // FOR ESSENTIAL TEXT ENTRY VIEWS THAT SHOULD NEVER BE BLANK AND REPLACED IN CODE
        upcBarcode = "[NOT RECEIVED]";

        productCategoryId = -1;
        productName = null;
        productSubtitle = null;

        return inflater.inflate(R.layout.fragment_check_expiration_date_select_item,
                container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get a source object of the database to add the information;
        dataSource = CheckExpirationDateActivity.requireDataSource(this);

        // INITIALIZE UI ELEMENTS THAT ARE INSTANCE VARIABLES
        categoryHint = view.findViewById(R.id.select_item_category);
        categoryButton = view.findViewById(R.id.select_item_category_btn);
        categoryButton.setOnClickListener( v -> showCategories() );

        productHint = view.findViewById(R.id.select_item_product);
        productButton = view.findViewById(R.id.select_item_product_btn);
        productButton.setOnClickListener( v -> showProducts() );
        productButton.setEnabled(false);

        subtitleHint = view.findViewById(R.id.select_item_type);
        subtitleButton = view.findViewById(R.id.select_item_type_btn);
        subtitleButton.setOnClickListener( v -> showProductSubtitles(
                productCategoryId, productName));
        subtitleButton.setEnabled(false);

        // Retrieve the upc barcode from the fragment result
        getParentFragmentManager().setFragmentResultListener("FOOD ITEM",
                this, (key, result) -> {

            if (result.containsKey("upcBarcode"))

                upcBarcode = result.getString("upcBarcode");

            else

                upcBarcode = ((NestUPC) result.getSerializable("foodItem")).getUpc();

            assert upcBarcode != null : "Failed to retrieve required data";

            // Display the upc barcode to the user
            ((TextView) view.findViewById(R.id.upc)).setText(upcBarcode);

            // Clear the result listener since we successfully received the result
            getParentFragmentManager().clearFragmentResultListener(key);

        });

        // ACCEPT BUTTON CODE - PARSE VALUES FOR NEW UPC, PASS INFO TO PRINTED EXPIRATION DATE
        view.findViewById(R.id.select_item_accept_btn).setOnClickListener(view1 -> {

            // Retrieve the String information from each view, casting as appropriate;
            String brand = ((EditText) (view.findViewById(
                    R.id.select_item_brand_entry))).getText().toString();

            String description = ((EditText) (view.findViewById(
                    R.id.select_item_description_entry))).getText().toString();

            // Replace any fields from above with blank values;
            // TODO Make these next two fields required
            final String finalBrand = brand.isEmpty() ? DEFAULT_STRING : brand;

            final String finalDescription = description.isEmpty() ? DEFAULT_STRING : description;

            // First assert that the required values have been entered
            if ((productCategoryId == -1) || (productButton.isEnabled() && productName == null) ||
                    (subtitleButton.isEnabled() && productSubtitle == null)) {

                Toast.makeText(getContext(), "Don't forget to select a category, name, and " +
                        "subtitle-(if required)!", Toast.LENGTH_LONG).show();

            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

                // Allow the user one more chance to check the information they entered
                builder.setPositiveButton("Accept", (dialog, which) -> {

                    dialog.dismiss();

                    // NOTE: This will return -1 if the UPC has never been added before
                    if (dataSource.getProductIdFromUPC(upcBarcode) == -1) {

                        // No productId associated with this upc in the database, add it to the database
                        // with the appropriate product id

                        Log.d(LOG_TAG, "Selected Product: " + productCategoryId +
                                ", " + productName + ", " + productSubtitle);

                        int productId = dataSource.getProdIdfromProdInfo(
                                productCategoryId, productName, productSubtitle);

                        Log.d(LOG_TAG, "Product ID: " + productId);

                        if (dataSource.insertNewUPC(upcBarcode, finalBrand, finalDescription, productId) == -1)

                            throw new RuntimeException("Error inserting new UPC");

                    } else {

                        // A productId is already associated with this upc in the database. Update it
                        // with the new values.

                        int productId = dataSource.getProdIdfromProdInfo(
                                productCategoryId, productName, productSubtitle);

                        // TODO Update the UPC stored in the database with the new brand, description,
                        //  and productId

                        // Adding this exception for now to prevent hidden errors
                        throw new RuntimeException("NestUPC exists. Need to update upc in database.");

                    }

                    Bundle result = new Bundle();

                    NestUPC foodItem = dataSource.getNestUPC(upcBarcode);

                    result.putSerializable("foodItem", foodItem);

                    getParentFragmentManager().setFragmentResult("FOOD ITEM", result);

                    // Navigate over to SelectPrintedExpirationDateFragment;
                    NavHostFragment.findNavController(SelectItemFragment.this)
                            .navigate(R.id.action_CED_SelectItemFragment_to_SelectPrintedExpirationDateFragment);

                });

                builder.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());

                builder.setMessage("Please be sure the information you selected is accurate.");

                builder.setTitle("Are you sure?");

                builder.create().show();

            }

        });

        // CANCEL BUTTON CODE - NAVIGATE BACK TO START FRAGMENT
        view.findViewById(R.id.select_item_cancel_btn).setOnClickListener(clickedView -> {

            Bundle result = new Bundle();

            result.putString("upcBarcode", upcBarcode);

            getParentFragmentManager().setFragmentResult("FOOD ITEM", result);

            requireActivity().onBackPressed();

        });

        //////////////////////////////// On Back Button Pressed   //////////////////////////////////

        view.setFocusableInTouchMode(true);

        view.requestFocus();

        view.setOnKeyListener((v, keyCode, event) -> {

            if (keyCode == KeyEvent.KEYCODE_BACK) {

                Bundle result = new Bundle();

                result.putString("upcBarcode", upcBarcode);

                getParentFragmentManager().setFragmentResult("FOOD ITEM", result);

            }

            // Always return false since we aren't handling the navigation here.
            return false;

        });

    }

    //////////////////////////////////// Custom Methods Start //////////////////////////////////////

    /**
     * Creates a PopupMenu to display a list of categories for the user to choose from.
     **/
    private void showCategories() {

        PopupMenu menuPop = new PopupMenu(getContext(), categoryButton);

        Menu menu = menuPop.getMenu();

        ArrayList<String> categories = dataSource.getCategories();

        for (int i = categories.size() - 1; i >= 0; i--)

            menu.add(i + 1, i, i, categories.get(i));

        // THE ACTUAL ON CLICK CODE TO SET THE SUB CATEGORY INDEX AND POPULATE A TEXT VIEW WITH THE INFORMATION
        menuPop.setOnMenuItemClickListener(item -> {

            productCategoryId = item.getItemId() + 1;

            // Set a text view with the category for the user to see;
            categoryHint.setText(item.toString());

            clearSubtitleSelection();

            subtitleButton.setEnabled(false);

            productButton.setEnabled(true);

            productHint.setText("");

            productName = null;

            return true;

        });

        menuPop.show();

    }



    /**
     * Creates a PopupMenu to display a list of products for the user to choose from, as long as a
     * category has been selected.
     **/
    private void showProducts() {

        if (productCategoryId != -1) {

            PopupMenu menuPop = new PopupMenu(getContext(), productButton);

            Menu menu = menuPop.getMenu();

            final ArrayList<String> products = dataSource.getProductNames(productCategoryId);

            for (int i = products.size() - 1; i >= 0; i--)

                menu.add(productCategoryId, i, i, products.get(i));


            // THE ACTUAL ON CLICK CODE TO SET THE SUBCATEGORY AND POPULATE A TEXT VIEW WITH THE INFORMATION
            menuPop.setOnMenuItemClickListener(item -> {

                productName = item.toString();

                productHint.setText(productName);

                clearSubtitleSelection();

                subtitleButton.setEnabled(!dataSource.getProductSubtitles(
                        productCategoryId, productName).isEmpty());

                return true;

            });

            menuPop.show();

        } else

            Toast.makeText(getContext(), "Please narrow the choices with a main category first!", Toast.LENGTH_LONG).show();

    }

    /**
     * Creates a PopupMenu to display a list of subtitles for the user to choose from, as long as a
     * product has been selected.
     *
     * @param categoryId
     * @param productName
     */
    private void showProductSubtitles(int categoryId, @NonNull String productName) {

        PopupMenu menuPop = new PopupMenu(getContext(), subtitleButton);

        Menu menu = menuPop.getMenu();

        ArrayList<String> subtitles = dataSource.getProductSubtitles(categoryId,
                Objects.requireNonNull(productName));

        for (int i = subtitles.size() - 1; i >= 0; i--)

            menu.add(i, i, i, subtitles.get(i));

        // THE ACTUAL ON CLICK CODE TO SET THE SUB CATEGORY INDEX AND POPULATE A TEXT VIEW WITH THE INFORMATION
        menuPop.setOnMenuItemClickListener(item -> {

            productSubtitle = item.toString();

            // Set a text view with the subtitle for the user to see;
            subtitleHint.setText(productSubtitle);

            return true;

        });

        menuPop.show();

    }

    /**
     * Clears the subtitle selection
     */
    private void clearSubtitleSelection() {

        subtitleHint.setText("");

        productSubtitle = null;

    }

}
