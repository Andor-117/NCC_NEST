package edu.ncc.nest.nestapp.CheckExpirationDate.Fragments;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.ncc.nest.nestapp.CheckExpirationDate.Activities.CheckExpirationDateActivity;
import edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses.NestDBDataSource;
import edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses.NestUPC;
import edu.ncc.nest.nestapp.R;
import edu.ncc.nest.nestapp.ShelfLife;

/**
 * DisplayTrueExpirationFragment:  Calculates the true expiration date based upon the printed
 * expiration date. Then displays the true expiration date for the item along with a synopsis of
 * the item including the UPC, category, item, and printed expiration date.
 */
public class DisplayTrueExpirationFragment extends Fragment {

    /////////////////////////////////////// Class Variables ////////////////////////////////////////

    private static final String LOG_TAG = DisplayTrueExpirationFragment.class.getSimpleName();

    private final Calendar printedExpDate = Calendar.getInstance();

    private NestDBDataSource dataSource;

    private ShelfLife shortestShelfLife;

    private NestUPC foodItem;

    /////////////////////////////////// Lifecycle Methods Start ////////////////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_expiration_date_display_true_expiration,
                container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the FragmentResultListener
        getParentFragmentManager().setFragmentResultListener("FOOD ITEM",
                this, (requestKey, data) -> {

            //printedExpDate.setTime((Date) data.getSerializable("PRINTED_EXPIRATION_DATE"));

            // Retrieve the NestUPC from the bundle
            foodItem = (NestUPC) data.getSerializable("foodItem");

            Log.e(LOG_TAG, data.containsKey("printedExpDate") + "");

            // Retrieve the printed expiration date from the bundle
            printedExpDate.setTime((Date) data.getSerializable("printedExpDate"));

            if (foodItem != null) {

                // Display item name, upc, category name on fragment_check_expiration_date_display_true_expiration.xml
                ((TextView) view.findViewById(R.id.item_display)).setText(foodItem.getProductName());
                ((TextView) view.findViewById(R.id.upc_display)).setText(foodItem.getUpc());
                ((TextView) view.findViewById(R.id.category_display)).setText(foodItem.getCatDesc());

                // Retrieve a reference to the database from this fragment's activity
                dataSource = CheckExpirationDateActivity.requireDataSource(this);

                // Get the product's shelf lives from the database
                List<ShelfLife> shelfLives = dataSource.getShelfLivesForProduct(
                        foodItem.getProductId());

                // Get the shortest shelf life from the list of shelf lives
                shortestShelfLife = getShortestShelfLife(shelfLives);

            } else

                Log.e(LOG_TAG, "'foodItem' is null.");

            // Clear the FragmentResultListener so we can use this requestKey again.
            getParentFragmentManager().clearFragmentResultListener("FOOD ITEM");

        });

        // Set the OnClickListener for button_display_date
        view.findViewById(R.id.button_display_date).setOnClickListener(view1 -> {

            if (shortestShelfLife != null)

                // Calculate and display the food item's true expiration date to the user
                ((TextView) view.findViewById(R.id.exp_date_display))
                        .setText(calculateTrueExpDate(shortestShelfLife));

        });

    }

    //////////////////////////////////// Custom Methods Start  /////////////////////////////////////

    /**
     * Finds and returns the shortest shelf life from a List of {@link ShelfLife} objects.
     *
     * @param shelfLives A list of {@link ShelfLife} objects.
     * @return The shortest shelf life from the list.
     */
    public ShelfLife getShortestShelfLife(List<ShelfLife> shelfLives) {

        int index = -1;

        String metric = "";

        ShelfLife shelfLife;

        // loop through the list of shelf lives and compare its metric
        for (int i = 0; i < shelfLives.size(); i++) {

            shelfLife = shelfLives.get(i);

            switch (shelfLife.getMetric()) {

                case "Years":
                    if (metric.isEmpty()) {
                        metric = "Years";
                        index = i;
                    }
                    break;

                case "Months":
                    if (metric.isEmpty() || metric.equals("Years")) {
                        metric = "Months";
                        index = i;
                    }
                    break;

                case "Weeks":
                    if (metric.isEmpty() || metric.equals("Years") || metric.equals("Months")) {
                        metric = "Weeks";
                        index = i;
                    }
                    break;

                case "Days":
                    if (metric.isEmpty() || metric.equals("Years") || metric.equals("Months") || metric.equals("Weeks")) {
                        metric = "Days";
                        index = i;
                    }
                    break;

                default:
                    Log.d(LOG_TAG, "getShortestShelfLife: Error invalid option");
                    break;

            }

        }

        return shelfLives.get(index);

    }

    /**
     * Calculates the true expiration date of an item based on it's shelf life and printed expiration
     * date.
     * @param shelfLife The shelf life of the item.
     */
    public String calculateTrueExpDate(ShelfLife shelfLife) {

        // metric dop_pantryLife
        String metric = shelfLife.getMetric();

        metric = metric.toLowerCase();

        // max dop_pantryLife
        int max = shelfLife.getMax();

        // Get the month, day, and year from the printed expiration date
        int month = printedExpDate.get(Calendar.MONTH) + 1;

        int day = printedExpDate.get(Calendar.DAY_OF_MONTH);

        int year = printedExpDate.get(Calendar.YEAR);

        int finalExMonth = 0;
        int finalExDate = 0;
        int finalExYear = 0;

        // if metric is weeks
        if (metric.equals("weeks")) {
            metric = "days";
            max = 7 * max;
        }

        // if metric is months
        if (metric.equals("months")) {

            finalExDate = day;
            finalExYear = year;
            finalExMonth = month + max;

            while (finalExMonth > 12) {
                finalExMonth = finalExMonth - 12;
                finalExYear = year + 1;
                year = finalExYear;
            }

        }

        // if metric is days
        if (metric.equals("days")) {
            finalExYear = year;
            finalExMonth = month;

            finalExDate = day + max;

            // months that have 31 days
            if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                if (finalExDate > 31) {
                    finalExDate = finalExDate - 31;
                    finalExMonth = finalExMonth + 1;
                    if (finalExMonth > 12) {
                        finalExMonth = finalExMonth - 12;
                        finalExYear = year + 1;

                    }
                }

            }

            // months that have 30 days
            if (month == 2 || month == 4 || month == 6 || month == 9 || month == 11) {
                if (finalExDate > 30) {
                    finalExDate = finalExDate - 30;
                    finalExMonth = finalExMonth + 1;
                    if (finalExMonth > 12) {
                        finalExMonth = finalExMonth - 12;
                        finalExYear = year + 1;

                    }

                }

            }

            // if the final date is greater than 31
            while (finalExDate > 31) {
                finalExDate = finalExDate - 31;
                finalExMonth = finalExMonth + 1;

                // if the final month is greater than 12
                if (finalExMonth > 12) {
                    finalExMonth = finalExMonth - 12;
                    finalExYear = finalExYear + 1;

                }
            }

        }

        // if metric is years
        if (metric.equals("years")) {
            finalExMonth = month;
            finalExDate = day;
            finalExYear = year + max;
        }


        return finalExMonth + "/" + finalExDate + "/" + finalExYear;

    }

}