package edu.ncc.nest.nestapp.GuestVisit.Activities;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import edu.ncc.nest.nestapp.Choose;
import edu.ncc.nest.nestapp.R;

/**
 * GuestVisitActivity - Activity to use for navigation between fragments when a guest checks-in
 */
public class GuestVisitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_visit);

        // Set the support action bar to the respective toolbar from the layout file
        setSupportActionBar((Toolbar) findViewById(R.id.guest_visit_toolbar));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the toolbar with the menu menu_main
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // If the view clicked was the home action
        if (item.getItemId() == R.id.home_btn) {

            // Create an Intent that will bring the user back to the home page
            Intent intent = new Intent(this, Choose.class);

            // Go to the home page
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);

    }

}