package edu.ncc.nest.nestapp.FragmentsUpc;

/**
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
 *
 * Copyright (C) 2012-2018 ZXing authors, Journey Mobile
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;


import java.util.Collections;
import java.util.List;


import edu.ncc.nest.nestapp.NestDBDataSource;
import edu.ncc.nest.nestapp.NestUPC;
import edu.ncc.nest.nestapp.R;
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission;

public class ScanFragment extends Fragment implements BarcodeCallback, View.OnClickListener {

    public static final String TAG = ScanFragment.class.getSimpleName();

    // Changed this to scan only for UPC_A codes (most common with food items in the US)
    private static final List<BarcodeFormat> BARCODE_FORMATS = Collections.singletonList(BarcodeFormat.UPC_A);
    // To support multiple formats change this to Arrays.asList() and fill it with the required
    // formats. For example, Arrays.asList(BarcodeFormat.CODE_39, BarcodeFormat.UPC_A, ...);

    // Used to ask for camera permission. Calls onCameraPermissionResult method with the result
    private final ActivityResultLauncher<String> REQUEST_CAMERA_PERMISSION_LAUNCHER = registerForActivityResult(
            new RequestPermission(), this::onCameraPermissionResult);

    private static final long SCAN_DELAY = 1500L;   // 1.5 Seconds in milliseconds

    private DecoratedBarcodeView decBarcodeView;
    private BeepManager beepManager;
    private TextView resultTextView;
    private Button confirmButton;
    private Button rescanButton;

    private boolean scannerPaused = true;

    // Stores the bar code that has been scanned
    private String barcodeResult = null;


    ////////////// Lifecycle Methods Start //////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_scan, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get respective views from layout
        decBarcodeView = (DecoratedBarcodeView) view.findViewById(R.id.zxing_barcode_scanner);

        confirmButton = (Button) view.findViewById(R.id.guest_scan_confirm_button);

        rescanButton = (Button) view.findViewById(R.id.guest_scan_rescan_button);

        resultTextView = (TextView) view.findViewById(R.id.guest_scan_result_view);


        // Specifies which barcode formats to decode. (Removing this line, defaults scanner to use all formats)
        decBarcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(BARCODE_FORMATS));


        // Make this class the OnClickListener for both feedback buttons
        confirmButton.setOnClickListener(this);

        rescanButton.setOnClickListener(this);

        // Disable the feedback buttons until we scan a barcode
        setFeedbackButtonsEnabled(false);


        // Create new BeepManager object to handle beeps and vibration
        beepManager = new BeepManager(requireActivity());

        beepManager.setVibrateEnabled(true);

        beepManager.setBeepEnabled(true);

    }

    @Override
    public void onResume() {
        super.onResume();

        // If the camera permission is granted
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Update the status text to inform the guest that camera permission is required
            decBarcodeView.setStatusText(getString(R.string.guest_scan_camera_permission_required));

            // Empty the result text view
            resultTextView.setText(null);

            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {

                // TODO Create a dialog window describing why we need the permission for this feature

                // Display a reason of why we need the permission
                Toast.makeText(requireContext(), "Camera permission is needed in order to scan.",
                        Toast.LENGTH_LONG).show();

            } else

                // Request the camera permission
                REQUEST_CAMERA_PERMISSION_LAUNCHER.launch(Manifest.permission.CAMERA);

        } else

            resumeScanning();

    }

    @Override
    public void onPause() {
        super.onPause();

        // Since we have paused the fragment, pause and wait for the camera to close
        decBarcodeView.pauseAndWait();

        scannerPaused = true;

    }

    @Override
    public void onDestroy() {

        // Make sure we have the view in-case the view isn't initialized before destruction
        if (decBarcodeView != null) {

            // Since we are destroying the fragment, pause and wait for the camera to close
            decBarcodeView.pauseAndWait();

            scannerPaused = true;

        }

        super.onDestroy();

    }


    ////////////// Other Event Methods Start  //////////////

    @Override
    public void barcodeResult(BarcodeResult result) {

        // Gets the barcode from the result
        String resultText = result.getText();

        // Make sure we actually have a barcode scanned
        if (resultText != null) {

            // Play a sound and vibrate when a scan has been processed
            beepManager.playBeepSoundAndVibrate();

            // Tell the user to confirm that the barcode is correct
            decBarcodeView.setStatusText(getString(R.string.guest_scan_confirm_msg));

            // Display the barcode back to the user
            resultTextView.setText(resultText);

            // Store the barcode
            barcodeResult = resultText;

            // Pause the scanner
            decBarcodeView.pause();

            scannerPaused = true;

            // Enable the feedback buttons after we have stored the bar-code and stopped scanner
            setFeedbackButtonsEnabled(true);

            Log.d(TAG, "Barcode Result: " + resultText + ", Barcode Format: " + result.getBarcodeFormat());

        } else

            // Scan for another bar-code
            decBarcodeView.decodeSingle(ScanFragment.this);

    }

    @Override
    public void onClick(View view) {

        // NOTE: Removed permission check here since buttons will be disabled until a scan is performed

        int id = view.getId();

        if (id == R.id.guest_scan_rescan_button)

            resumeScanning();

        else if (id == R.id.guest_scan_confirm_button && barcodeResult != null) {

            Log.d(TAG, "Scan Confirmed: " + barcodeResult);

            // Check database
            NestDBDataSource dataSource = new NestDBDataSource(getContext());
            NestUPC result = dataSource.getNestUPC(barcodeResult);

            // Used this to test if there was a non-null result given (successful)
//            result = new NestUPC("123456789123", "Hershey's", "Chocolate Bar", 123, "Hershey's Chocolate Bar", null, 1234,"Some category description");

            // If there is a result from the database
            if(result != null) {

                Log.d(TAG, "Result returned: " + result.getUpc() + " " + result.getProductName());

                // Put the item in a bundle and pass it to ConfirmItemFragment
                Bundle bundle = new Bundle();
                bundle.putSerializable("foodItem", result);
                getParentFragmentManager().setFragmentResult("FOOD ITEM", bundle);
                NavHostFragment.findNavController(ScanFragment.this).navigate((R.id.confirmItemFragment));

                // If there was no result from the database
            }else {

                // Put UPC into a bundle and pass it to SelectItemFragment (may not be necessary)
                Bundle bundle = new Bundle();
                bundle.putString("barcode", barcodeResult);
                getParentFragmentManager().setFragmentResult("BARCODE", bundle);
                NavHostFragment.findNavController(ScanFragment.this).navigate((R.id.selectItemFragment));
            }

        }

    }


    ////////////// Custom Methods Start  //////////////

    /**
     * Takes 1 parameter. This method gets called by the REQUEST_CAMERA_PERMISSION_LAUNCHER, after
     * asking for camera permission. Determines what happens when the permission gets granted or
     * denied.
     * @param isGranted - true if permission was granted false otherwise
     */
    private void onCameraPermissionResult(boolean isGranted) {

        if (!isGranted) {

            // Display a reason of why we need the permission
            Toast.makeText(requireContext(), "Camera permission is needed in order to scan.",
                    Toast.LENGTH_LONG).show();

        } else

            // Camera permission is granted, so resume scanning
            resumeScanning();

    }

    /**
     * Takes 0 parameters. Resumes the scanner if it is not paused, resets resultTextView text,
     * resets the barcodeResult to be null so we can scan a new bar-code, and starts the decoder
     * after a delay of SCAN_DELAY.
     */
    private void resumeScanning() {

        if (scannerPaused) {

            // Update the status text to explain how to use the scanner
            decBarcodeView.setStatusText(getString(R.string.zxing_msg_default_status));

            // Update the display text so the user knows we are waiting for them to scan a barcode
            resultTextView.setText(getString(R.string.guest_scan_waiting_for_scan));

            // Disable the feedback buttons until we scan another barcode
            setFeedbackButtonsEnabled(false);

            // Reset our barcodeResult
            barcodeResult = null;

            // Resume the scanner but not the decoder
            decBarcodeView.resume();

            scannerPaused = false;

            // Create a handler that resumes the decoder after a delay
            // Gives the user time to move their camera before scanning
            Handler handler = new Handler();
            handler.postDelayed(() -> {

                // As long as the scanner hasn't been paused, start the decoder
                if (!scannerPaused)

                    // Tells the decoder to stop after a single scan
                    decBarcodeView.decodeSingle(ScanFragment.this);

            }, SCAN_DELAY);

        }

    }

    /**
     * Takes 1 parameter. Toggles whether both rescanButton and confirmScan button are enabled or
     * disabled, based on the value of the parameter.
     *
     * @param enabled true to enable or false to disable
     */
    private void setFeedbackButtonsEnabled(boolean enabled) {

        confirmButton.setEnabled(enabled);

        rescanButton.setEnabled(enabled);

    }




}
