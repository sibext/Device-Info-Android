/**
 * This file is part of DeviceInfo.
 * 
 * DeviceInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * DeviceInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with DeviceInfo.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sibext.deviceinfo.activity;

import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sibext.android.activity.CrashCatcherActivity;
import com.sibext.deviceinfo.R;
import com.sibext.deviceinfo.utils.SystemHelper;

public class MainActivity extends CrashCatcherActivity {

	private FrameLayout frameId;
	private FrameLayout frameMacAdress;
	private FrameLayout frameAndroidId;
	private FrameLayout frameGps;
	private FrameLayout frameCrypts;
	private FrameLayout frameAutoFocus;
	private FrameLayout frameSensors;
	private FrameLayout frameScreen;

	private static final String[] ALGORITHMS = new String[] { "AES", "DES",
			"RSA", "TripleDES", "PBE", "RC2", "RC4", "RC5", "CAST5", "3DES",
			"Twofish", "Blowfish", "Camellia", "IDEA", "SHA-1", "SHA-384",
			"SHA-256", "SHA-512", "TLSv1", "TLS", "SSL", "SSLv3", "MD5",
			"ARC4", "X509", "OAEP", };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		frameId = (FrameLayout) findViewById(R.id.activity_main_item_id);
		frameMacAdress = (FrameLayout) findViewById(R.id.activity_main_item_macadress);
		frameAndroidId = (FrameLayout) findViewById(R.id.activity_main_item_androidid);
		frameGps = (FrameLayout) findViewById(R.id.activity_main_item_gps);
		frameCrypts = (FrameLayout) findViewById(R.id.activity_main_item_crypts);
		frameAutoFocus = (FrameLayout) findViewById(R.id.activity_main_item_autofocus);
		frameSensors = (FrameLayout) findViewById(R.id.activity_main_item_sensors);
		frameScreen = (FrameLayout) findViewById(R.id.activity_main_item_screen_info);

		setDeviceID();
		setMacAdress();
		setAndroidId();
		setGps();
		setEncryptions();
		setAutofocus();
		setSensorsInfo();
		setScreenInfo();
	}

	private void setSensorsInfo() {
		List<Sensor> sensors = SystemHelper.getSensors(this);
		TextView titlev = (TextView) frameSensors.findViewById(R.id.item_title);
		titlev.setText(getString(R.string.sensors));
		TextView valuev = (TextView) frameSensors.findViewById(R.id.item_value);
		ImageButton button = (ImageButton) frameSensors
				.findViewById(R.id.item_button);

		StringBuilder sb = new StringBuilder();

		int i = 1;
		for (Sensor s : sensors) {
			sb.append(i).append(":").append(s.getName()).append("\n");
			i++;
		}
		valuev.setText(sb.toString());
	}

	private void setDeviceID() {

		TextView titlev = (TextView) frameId.findViewById(R.id.item_title);
		TextView valuev = (TextView) frameId.findViewById(R.id.item_value);
		ImageButton button = (ImageButton) frameId
				.findViewById(R.id.item_button);

		titlev.setText(R.string.id_title);

		final String value = SystemHelper.generateDeviceId(this);
		valuev.setText(value);

		button.setVisibility(View.VISIBLE);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent shareIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_SUBJECT,
						getString(R.string.id_subject));
				shareIntent.putExtra(Intent.EXTRA_TEXT, value);
				startActivity(Intent.createChooser(shareIntent,
						getString(R.string.app_chooser_title)));
			}
		});
	}

	private void setGps() {
		TextView titlev = (TextView) frameGps.findViewById(R.id.item_title);
		titlev.setText(R.string.gps_title);

		TextView valuev = (TextView) frameGps.findViewById(R.id.item_value);
		valuev.setText(SystemHelper.isGPSSupport(this) ? R.string.yes
				: R.string.no);
	}

	private void setMacAdress() {
		TextView titlev = (TextView) frameMacAdress
				.findViewById(R.id.item_title);
		TextView valuev = (TextView) frameMacAdress
				.findViewById(R.id.item_value);
		ImageButton button = (ImageButton) frameMacAdress
				.findViewById(R.id.item_button);

		titlev.setText(R.string.mac_adress_title);

		final String value = SystemHelper.getMacAdress(this);
		valuev.setText(value);

		button.setVisibility(View.VISIBLE);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent shareIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_SUBJECT,
						getString(R.string.mac_adress_subject));
				shareIntent.putExtra(Intent.EXTRA_TEXT, value);
				startActivity(Intent.createChooser(shareIntent,
						getString(R.string.app_chooser_title)));
			}
		});
	}

	private void setAndroidId() {
		TextView titlev = (TextView) frameAndroidId
				.findViewById(R.id.item_title);
		TextView valuev = (TextView) frameAndroidId
				.findViewById(R.id.item_value);
		ImageButton button = (ImageButton) frameAndroidId
				.findViewById(R.id.item_button);

		titlev.setText(R.string.android_id_title);

		final String value = SystemHelper.getAndroidID(this);
		valuev.setText(value);

		button.setVisibility(View.VISIBLE);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent shareIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_SUBJECT,
						getString(R.string.android_id_subject));
				shareIntent.putExtra(Intent.EXTRA_TEXT, value);
				startActivity(Intent.createChooser(shareIntent,
						getString(R.string.app_chooser_title)));
			}
		});
	}

	private void setEncryptions() {
		TextView title = (TextView) frameCrypts.findViewById(R.id.item_title);
		TextView value = (TextView) frameCrypts.findViewById(R.id.item_value);

		StringBuilder text = new StringBuilder();
		List<String> supportedList = new ArrayList<String>();
		for (Provider provider : Security.getProviders()) {
			supportedList.clear();
			text.append(provider.getName()).append(": ");
			for (String key : provider.stringPropertyNames()) {
				String p = provider.getProperty(key);
				String algorithm = existsEncryptions(key);
				if (null != algorithm && !supportedList.contains(algorithm)) {
					supportedList.add(algorithm);
				}
			}
			int i = 0;
			int size = supportedList.size();
			for (String key : supportedList) {
				text.append(key);
				if (size != ++i) {
					text.append(", ");
				} else {
					text.append(".");
				}
			}
			text.append("\n");
		}
		title.setText(R.string.encryptions);
		value.setText(text.toString());
	}

	private void setAutofocus() {
		TextView title = (TextView) frameAutoFocus.findViewById(R.id.item_title);
		TextView value = (TextView) frameAutoFocus.findViewById(R.id.item_value);
		title.setText(R.string.autofocus);
		value.setText(""
				+ getPackageManager().hasSystemFeature(
						PackageManager.FEATURE_CAMERA_AUTOFOCUS));
	}

	private String existsEncryptions(String key) {
		for (String algorithm : ALGORITHMS) {
			if (key.contains(algorithm)) {
				return algorithm;
			}
		}
		return null;
	}

	private void setScreenInfo() {
		StringBuilder sb = new StringBuilder();
		// Determine screen size
		int sizeOfScreen = (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK);
		switch (sizeOfScreen) {
        case Configuration.SCREENLAYOUT_SIZE_LARGE:
            sb.append("Large screen; ");
            break;
        case Configuration.SCREENLAYOUT_SIZE_NORMAL:
            sb.append("Normal screen; ");
            break;
        case Configuration.SCREENLAYOUT_SIZE_XLARGE:
            sb.append("Extra large screen; ");
            break;
        case Configuration.SCREENLAYOUT_SIZE_SMALL:
            sb.append("Small screen; ");
            break;
        default:
            sb.append("Screen size is neither large, normal or small; ");
        }
		// Determine density
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int density = metrics.densityDpi;

		if (density == DisplayMetrics.DENSITY_HIGH) {
			sb.append("HIGH: Density is " + String.valueOf(density));
		} else if (density == DisplayMetrics.DENSITY_MEDIUM) {
			sb.append("MEDIUM: Density is " + String.valueOf(density));
		} else if (density == DisplayMetrics.DENSITY_LOW) {
			sb.append("LOW: Density is " + String.valueOf(density));
		} else if (density == DisplayMetrics.DENSITY_XHIGH) {
		    sb.append("Extra HIGHT: Density is " + String.valueOf(density));
		} else if (density == DisplayMetrics.DENSITY_XXHIGH) {
		    sb.append("Double Extra HIGHT: Density is " + String.valueOf(density));
		} else {
			sb.append("Density is neither HIGH, MEDIUM OR LOW. Density is " + String.valueOf(density));
		}
		sb.append("\nResource directory = ").append(getString(R.string.resource_dirictory));
		TextView title = (TextView) frameScreen.findViewById(R.id.item_title);
		TextView value = (TextView) frameScreen.findViewById(R.id.item_value);
		title.setText(R.string.screen);
		value.setText(sb.toString());

	}

}
