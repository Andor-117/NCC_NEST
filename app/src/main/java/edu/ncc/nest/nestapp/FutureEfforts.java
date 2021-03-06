package edu.ncc.nest.nestapp;
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

import android.content.DialogInterface;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import edu.ncc.nest.nestapp.GuestGoogleSheetRegistration.Activities.GuestGoogleSheetRegistrationActivity;

public class FutureEfforts extends AppCompatActivity implements OnClickListener {
    private static final String TAG = "testing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future_efforts);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    //implements the menu options for the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }

    /**
     * onOptionsSelected method --
     * description: this method makes you comeback to the launch UI when the home button is clicked
     * or launch the Login UI when the login button is clicked
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home_btn) {
            home();
        }
        if(item.getItemId() == R.id.login_btn){
            launchLoginActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * home method --
     * description: this method goes to the nest home screen
     */
    public void home() {
        Intent intent = new Intent(this, Choose.class);
        startActivity(intent);
    }

    /**
     * launchLoginActivity - starts the Login Activity
     */
    public void launchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     *
     * Title : onClick Method -- Whenever a certain button is clicked it would
     * call the method and inside that method would launch an activity and display to the user
     * and then would break afterwords..
     *
     * @param v - The activity that was clicked by the user.
     */
    public void onClick(View v) {
          switch (v.getId()) {
            case R.id.addToInventoryBtn:
                launchAddToInventory();
                break;
            case R.id.scheduleBtn:
                launchSchedule();
                break;
            //code for the two buttons that were added
            case R.id.guestRegGoogle:
               launchGuestRegGoogle();
                break;
            case R.id.guesVisitGoogle:
                /*
                intent = new Intent(this, );
                startActivity(intent);
                 */
                break;
            case R.id.volunteerFormBtn:
                launchVolForm();
                break;
            case R.id.signUpBtn:
                createAccountDialog();
                break;
        }
    }

    /**
     * createAccountDialog method - this method creates an alert dialog when the 'Create Nest Account' button is clicked. An alert dialog
     * will be displayed telling the user what creating an account entails, and will ask them if they want to create an account or not.
     * If they select 'Yes' they will be directed to the SignUp activity. If they select 'No' the dialog will close.
     */
    public void createAccountDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(FutureEfforts.this);
        alertBuilder.setCancelable(false);
        alertBuilder.setMessage("Creating a NEST account will allow you to receive notifications relating to guest, donator, or volunteer " +
                "opportunities. You will be allowed to manage your notification preferences once your account is created. Your name, email " +
                "address, and phone number will be required." + "\n" + "Create an account?");
        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //launch signUp activity
                launchSignUp();
            }
        });

        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = alertBuilder.create();

        alert.show();
    }



    /**
     * launchSchedule - starts the Schedule activity
     */
    public void launchSchedule() {
        Intent intent = new Intent(this, Schedule.class);
        startActivity(intent);
    }

    /**
     * launchSignUp - starts the SignUp activity
     */
    public void launchSignUp() {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    /**
     * launchGuestRegGoogle --
     * description: this method starts the
     * Guest GoogleSheet Registration Activity
     */
    public void launchGuestRegGoogle() {
        Intent intent = new Intent(this, GuestGoogleSheetRegistrationActivity.class);
        startActivity(intent);
            }

    /**
     * launchVolForm - starts the Volunteer activity
     */
    public void launchVolForm() {
        Intent intent = new Intent(this, VolunteerForm.class);
        startActivity(intent);
    }

    /**
     * launchVolForm - starts the Volunteer activity
     */
    public void launchAddToInventory()
    {
        Intent intent = new Intent(this, AddToInventory.class);
        startActivity(intent);
    }

}


