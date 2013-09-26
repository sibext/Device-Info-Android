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

package com.sibext.deviceinfo.utils;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Debug;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Retrieve overall information about current application.
 * 
 * @author Nikolay Moskvin <moskvin@sibext.com>
 **/
public class SystemHelper {

	public static final String NULL_IMEI = "000000000000000";

	private static final String DEFAULT_TIME_ZONE = "Europe/Moscow";

	/**
	 * Get version for current application
	 * 
	 * @param context
	 *            is current Activity
	 * @param cls
	 *            is class
	 * @return string version
	 */
	public static String getVersionName(Context context, Class<?> cls) {
		try {
			ComponentName componentName = new ComponentName(context, cls);
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(componentName.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (android.content.pm.PackageManager.NameNotFoundException e) {
			return null;
		}
	}

	/**
	 * Hide always Soft Keyboard
	 * 
	 * @param context
	 *            is current Activity
	 */
	public static void hideKeyboard(Context context, EditText editText) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (editText != null) {
			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
			editText.clearFocus();
		}
	}

	public static void hideKeyboard(Activity context) {
		InputMethodManager imm = (InputMethodManager) context
				.getApplicationContext().getSystemService(
						Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(context.getWindow().getDecorView()
				.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * Show always Soft Keyboard
	 * 
	 * @param context
	 *            is current Activity
	 */
	public static void showKeyboard(Context context, EditText editText) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (editText != null) {
			imm.showSoftInput(editText, 0);
		}
	}

	/**
	 * Gets Date with UTC time zone
	 * 
	 * @param date
	 *            is concrete date
	 * @return new instance calendar
	 */
	public static Calendar getCalendarUTC(Date date) {
		GregorianCalendar calendar = new GregorianCalendar(
				TimeZone.getTimeZone(DEFAULT_TIME_ZONE));
		calendar.setTimeInMillis(date.getTime());
		return calendar;
	}

	public static boolean isCameraSupport(Context context) {
		PackageManager pm = context.getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}

	public static boolean isCameraSupportLight(Context context) {
		PackageManager pm = context.getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
	}

	public static long getMemmoryAvailable() {
		return Debug.getNativeHeapAllocatedSize() / 1024L;
	}

	/**
	 * Gets IMEI only for devices with telephony
	 * 
	 * @param Context
	 * @return String imei
	 */
	public static String getNativeIMEI(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	/**
	 * Since Android 2.3 (“Gingerbread”) this is available via
	 * android.os.Build.SERIAL. Devices without telephony are required to report
	 * a unique device ID here; some phones may do so also.
	 */
	public static String getSerial() {
		return android.os.Build.SERIAL;
	}

	/**
	 * Gets UUID for devices from hash's of MacAdress and ANDROID_ID
	 * 
	 * @param Context
	 * @return String uuid
	 */
	public static String generateDeviceId(Context context) {

		if (getSdkVersion() >= 10) {
			// Works since 10 SDK (2.3).
			return getSerial();
		} else {
			final String macAdr, androidId;

			// MAC_ADRESS
			macAdr = getMacAdress(context);
			// ANDROID_ID
			androidId = getAndroidID(context);

			// Generating of 128-bit universal unique key
			UUID deviceUuid = new UUID(androidId.hashCode(), macAdr.hashCode());

			return deviceUuid.toString();
		}
	}

	public static String getMacAdress(Context context) {
		WifiManager wifiMan = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInf = wifiMan.getConnectionInfo();
		return wifiInf.getMacAddress();
	}

	/**
	 * Gets a 64-bit quantity that is generated and stored when the device first
	 * boots. It is reset when the device is wiped.
	 * 
	 * @param Context
	 * @return ANDROID_ID
	 */
	public static String getAndroidID(Context context) {
		return android.provider.Settings.Secure.getString(
				context.getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);
	}

	public static String getMD5Checksum(byte[] b) throws Exception {
		String result = "";
		BigInteger bigInt = new BigInteger(1, b);
		result = bigInt.toString(16);
		return result;
	}

	/**
	 * Gets Padding left from edge of main window
	 * 
	 * @param view
	 *            for getting padding left from edge of main window
	 * @return left padding
	 */
	public static int getRelativeLeft(View myView) {
		if (myView.getParent() == myView.getRootView())
			return myView.getLeft();
		else
			return myView.getLeft()
					+ getRelativeLeft((View) myView.getParent());
	}

	/**
	 * Gets Padding top from top of main window
	 * 
	 * @param view
	 *            for getting padding top from top of main window
	 * @return top padding
	 */
	public static int getRelativeTop(View myView) {
		if (myView.getParent() == myView.getRootView())
			return myView.getTop();
		else
			return myView.getTop() + getRelativeTop((View) myView.getParent());
	}

	/**
	 * Make vibration during few time
	 * 
	 * @param Context
	 *            , Milliseconds for duration
	 */
	public static void makeVibration(Context context, int milisec) {
		if (null != context && milisec > 0) {
			// Get instance of Vibrator from current Context
			Vibrator v = (Vibrator) context
					.getSystemService(Context.VIBRATOR_SERVICE);
			if (null != v) {
				v.vibrate(milisec);
			}
		}
	}

	/**
	 * Get sensors by device
	 * @param context
	 * @return List of sensors
	 */
	public static List<Sensor> getSensors(Context context) {
		SensorManager sensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> Sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
		return Sensors;
	}

	/**
	 * Check network connection exists or not
	 * 
	 * @param Context
	 * @return true if exists otherwise false
	 */
	public static boolean checkNetworkConnection(Context context) {
		if (null != context) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();

			if (mNetworkInfo != null && mNetworkInfo.isConnectedOrConnecting()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Kill the app either safely or quickly. The app is killed safely by
	 * killing the virtual machine that the app runs in after finalizing all
	 * {@link Object}s created by the app. The app is killed quickly by abruptly
	 * killing the process that the virtual machine that runs the app runs in
	 * without finalizing all {@link Object}s created by the app. Whether the
	 * app is killed safely or quickly the app will be completely created as a
	 * new app in a new virtual machine running in a new process if the user
	 * starts the app again.
	 * 
	 * <P>
	 * <B>NOTE:</B> The app will not be killed until all of its threads have
	 * closed if it is killed safely.
	 * </P>
	 * 
	 * <P>
	 * <B>NOTE:</B> All threads running under the process will be abruptly
	 * killed when the app is killed quickly. This can lead to various issues
	 * related to threading. For example, if one of those threads was making
	 * multiple related changes to the database, then it may have committed some
	 * of those changes but not all of those changes when it was abruptly
	 * killed.
	 * </P>
	 * 
	 * @param killSafely
	 *            Primitive boolean which indicates whether the app should be
	 *            killed safely or quickly. If true then the app will be killed
	 *            safely. Otherwise it will be killed quickly.
	 */
	public static void killApp(boolean killSafely) {
		if (killSafely) {
			/*
			 * Notify the system to finalize and collect all objects of the app
			 * on exit so that the virtual machine running the app can be killed
			 * by the system without causing issues. NOTE: If this is set to
			 * true then the virtual machine will not be killed until all of its
			 * threads have closed.
			 */
			System.runFinalizersOnExit(true);

			/*
			 * Force the system to close the app down completely instead of
			 * retaining it in the background. The virtual machine that runs the
			 * app will be killed. The app will be completely created as a new
			 * app in a new virtual machine running in a new process if the user
			 * starts the app again.
			 */
			System.exit(0);
		} else {
			/*
			 * Alternatively the process that runs the virtual machine could be
			 * abruptly killed. This is the quickest way to remove the app from
			 * the device but it could cause problems since resources will not
			 * be finalized first. For example, all threads running under the
			 * process will be abruptly killed when the process is abruptly
			 * killed. If one of those threads was making multiple related
			 * changes to the database, then it may have committed some of those
			 * changes but not all of those changes when it was abruptly killed.
			 */
			android.os.Process.killProcess(android.os.Process.myPid());
		}

	}

	public static boolean isGPSSupport(Context context) {

		LocationManager manager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		if (manager != null) {
			LocationProvider gpsProvider = manager
					.getProvider(LocationManager.GPS_PROVIDER);
			if (gpsProvider != null) {
				return true;
			}
		}
		return false;
	}

	public static int getSdkVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

}