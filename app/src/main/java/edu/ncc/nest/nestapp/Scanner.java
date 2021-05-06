package edu.ncc.nest.nestapp;

/*
 * Copyright (C) 2019-2021 The LibreFoodPantry Developers.
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

import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.zxing.BarcodeFormat;

import edu.ncc.nest.nestapp.AbstractScanner.AbstractScannerActivity;
import edu.ncc.nest.nestapp.CheckExpirationDate.Fragments.ScannerFragment;

/**
 * @deprecated This Activity is being replaced by a Fragment. ({@link ScannerFragment})
 */
@Deprecated
public class Scanner extends AbstractScannerActivity {

    @Override
    protected void onBarcodeConfirmed(@NonNull String barcode, @NonNull BarcodeFormat format) {

        Intent intent = new Intent(this, FoodItem.class);

        intent.putExtra("barcode", barcode);

        startActivity(intent);

        finish();

    }

}