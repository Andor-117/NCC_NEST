package edu.ncc.nest.nestapp.CheckExpirationDate.Fragments;

/* Copyright (C) 2020-2021 The LibreFoodPantry Developers.
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
import android.util.AndroidRuntimeException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

import edu.ncc.nest.nestapp.CheckExpirationDate.DatabaseClasses.NestUPC;
import edu.ncc.nest.nestapp.R;

/**
 * SelectPrintedExpirationDateFragment: Allows the user to select or enter the item's printed
 * expiration date, then sends the result to {@link DisplayTrueExpirationFragment}.
 */
public class SelectPrintedExpirationDateFragment extends Fragment {

    /////////////////////////////////////// Class Variables ////////////////////////////////////////

    /** The tag to use when printing to the log from this class. */
    public static final String LOG_TAG = SelectPrintedExpirationDateFragment.class.getSimpleName();

    private final Calendar printedExpDate = Calendar.getInstance();

    private NumberPicker dayPicker;

    private NestUPC foodItem;

    /////////////////////////////////// Lifecycle Methods Start ////////////////////////////////////

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.fragment_check_expiration_date_select_printed_expiration_date,
                container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null)

            onBundleReceived(view, null, savedInstanceState);

        // Listen for the foodItem from the bundle sent from the previous fragment
        getParentFragmentManager().setFragmentResultListener(
                "FOOD ITEM", this,
                (requestKey, result) -> onBundleReceived(view, requestKey, result));

        //////////////////////////////// On Accept Button Pressed   ////////////////////////////////

        view.findViewById(R.id.selected_print_accept).setOnClickListener(clickedView -> {

            Bundle bundle = new Bundle();

            bundle.putSerializable("foodItem", foodItem);

            bundle.putSerializable("printedExpDate", printedExpDate.getTime());

            /* Need to clear the result with the same request key, before possibly using same
               request key again. */
            getParentFragmentManager().clearFragmentResult("FOOD ITEM");

            // Set the fragment result to the bundle
            getParentFragmentManager().setFragmentResult("FOOD ITEM", bundle);

            // Navigate to the proper fragment
            NavHostFragment.findNavController(SelectPrintedExpirationDateFragment.this)
                    .navigate(R.id.action_CED_SelectPrintedExpirationDateFragment_to_DisplayTrueExpirationFragment);

        });

        ///////////////////////////////// On Back Button Pressed   /////////////////////////////////

        view.setFocusableInTouchMode(true);

        view.requestFocus();

        view.setOnKeyListener((v, keyCode, event) -> {

            if (keyCode == KeyEvent.KEYCODE_BACK) {

                Bundle bundle = new Bundle();

                bundle.putSerializable("foodItem", foodItem);

                /* Clear any result that was previously set with the given {@code requestKey} and
                 * that hasn't yet been delivered to a FragmentResultListener. */
                getParentFragmentManager().clearFragmentResult("FOOD ITEM");

                getParentFragmentManager().setFragmentResult("FOOD ITEM", bundle);

            }

            // Always return false since we aren't handling the navigation here.
            return false;

        });

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putSerializable("printedExpDate", printedExpDate.getTime());

        outState.putSerializable("foodItem", foodItem);

        super.onSaveInstanceState(outState);

    }

    /**
     * Initializes this fragment based on the bundle received.
     * @param view The View of this fragment's UI
     * @param resultKey The {@code requestKey} of the fragment result if this bundle is a fragment
     *                  result
     * @param bundle The Bundle that was received
     */
    private void onBundleReceived(@NonNull View view, String resultKey, Bundle bundle) {

        if (!bundle.containsKey("foodItem"))

            throw new AndroidRuntimeException("Bundle is missing required requestKey.");

        foodItem = (NestUPC) bundle.getSerializable("foodItem");

        if (bundle.containsKey("printedExpDate"))

            printedExpDate.setTime((Date) bundle.getSerializable("printedExpDate"));

        else

            // Update the printed expiration date to reflect the current date
            setPrintedExpDate(printedExpDate.get(Calendar.YEAR), printedExpDate.get(Calendar.MONTH),
                    printedExpDate.get(Calendar.DAY_OF_MONTH));

        if (resultKey != null)

            // Make sure we clear the FragmentResultListener so we can use this requestKey again
            getParentFragmentManager().clearFragmentResultListener(resultKey);

        ((TextView) view.findViewById(R.id.display_upc)).setText(foodItem.getUpc());

        initializeMonthPicker(view);

        initializeDayPicker(view);

        initializeYearPicker(view);

    }

    //////////////////////////////////// Custom Methods Start  /////////////////////////////////////

    /**
     * Clamps a date so that the days can't be greater than the actual maximum number of days in
     * the month of the year then sets the printed expiration date to the clamped date.
     * @param year The desired year
     * @param month The desired month
     * @param day The desired day
     */
    private void setPrintedExpDate(int year, int month, int day) {

        // Clear all fields to make sure only the date fields get set
        printedExpDate.clear();

        // Make sure to set the year and month before calculating the actual maximum number of days
        printedExpDate.set(year, month, 1);

        // Set and clamp the date to the actual maximum number of days
        printedExpDate.set(year, month,
                Math.min(day, printedExpDate.getActualMaximum(Calendar.DAY_OF_MONTH)));

    }

    /**
     * Initializes a {@link NumberPicker} with the appropriate values for selecting a month, and
     * sets its {@link NumberPicker.OnValueChangeListener}.
     * @param view The {@link View} of this fragment's UI
     */
    private void initializeMonthPicker(@NonNull View view) {

        NumberPicker monthPicker = view.findViewById(R.id.number_picker_month);

        // Set the min and max values to be 0-based
        monthPicker.setMinValue(0);
        monthPicker.setMaxValue(11);

        // Get an array of the months as strings and set it as the displayed values
        monthPicker.setDisplayedValues(new DateFormatSymbols().getMonths());

        monthPicker.setValue(printedExpDate.get(Calendar.MONTH));

        monthPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {

            setPrintedExpDate(printedExpDate.get(Calendar.YEAR), newVal,
                    printedExpDate.get(Calendar.DAY_OF_MONTH));

            // Make sure we update the number of days in the selected month
            dayPicker.setMaxValue(printedExpDate.getActualMaximum(Calendar.DAY_OF_MONTH));

            dayPicker.setValue(printedExpDate.get(Calendar.DAY_OF_MONTH));

        });

    }

    /**
     * Initializes a {@link NumberPicker} with the appropriate values for selecting a day of the
     * selected month, and sets its {@link NumberPicker.OnValueChangeListener}.
     * @param view The {@link View} of this fragment's UI
     */
    private void initializeDayPicker(@NonNull View view) {

        dayPicker = view.findViewById(R.id.number_picker_day);

        dayPicker.setMinValue(1);

        // Set the max value to the actual number of days in the current month
        dayPicker.setMaxValue(printedExpDate.getActualMaximum(Calendar.DAY_OF_MONTH));

        dayPicker.setValue(printedExpDate.get(Calendar.DAY_OF_MONTH));

        dayPicker.setOnValueChangedListener((picker, oldVal, newVal) ->
                setPrintedExpDate(printedExpDate.get(Calendar.YEAR),
                printedExpDate.get(Calendar.MONTH), newVal));

    }

    /**
     * Initializes a {@link NumberPicker} with the appropriate values for selecting a year, and sets
     * its {@link NumberPicker.OnValueChangeListener}.
     * @param view The {@link View} of this fragment's UI
     */
    private void initializeYearPicker(@NonNull View view) {

        NumberPicker yearPicker = view.findViewById(R.id.number_picker_year);

        final int CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);

        yearPicker.setMinValue(CURRENT_YEAR - 10);
        yearPicker.setMaxValue(CURRENT_YEAR + 10);

        yearPicker.setValue(CURRENT_YEAR);

        yearPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {

            setPrintedExpDate(newVal, printedExpDate.get(Calendar.MONTH),
                    printedExpDate.get(Calendar.DAY_OF_MONTH));

            // Make sure we update the number of days in the month, in case of leap years
            dayPicker.setMaxValue(printedExpDate.getActualMaximum(Calendar.DAY_OF_MONTH));

            dayPicker.setValue(printedExpDate.get(Calendar.DAY_OF_MONTH));

        });

    }

}