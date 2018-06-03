/*
* Copyright (C) 2016 The OmniROM Project
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*
*/
package org.omnirom.device;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.PreferenceFragment;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceClickListener;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.TwoStatePreference;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.util.Log;

public class DeviceSettings extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    public static final String KEY_VIBSTRENGTH = "vib_strength";
    public static final String KEY_GLOVE_MODE = "glove_mode";
    public static final String KEY_PROX_WAKE = "prox_wake";
    public static final String KEY_DISABLE_KEYS = "keypad_mode";

    private VibratorStrengthPreference mVibratorStrength;
    private TwoStatePreference mGloveMode;
    private TwoStatePreference mProxWake;
    private TwoStatePreference mDisableKeys;

    private static final String GLOVE_MODE_FILE = "/sys/devices/soc/78b7000.i2c/i2c-3/3-0038/glove_mode";
    private static final String PROX_WAKE_FILE = "/sys/devices/soc/78b7000.i2c/i2c-3/3-0038/Enable_Proximity_Check";
    private static final String DISABLE_KEYS_FILE = "/sys/bus/i2c/devices/i2c-3/3-0038/keypad_mode";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.main, rootKey);

        PreferenceScreen mKcalPref = (PreferenceScreen) findPreference("kcal");
        mKcalPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
             @Override
             public boolean onPreferenceClick(Preference preference) {
                 Intent intent = new Intent(getActivity().getApplicationContext(), DisplayCalibration.class);
                 startActivity(intent);
                 return true;
             }
        });

        mVibratorStrength = (VibratorStrengthPreference) findPreference(KEY_VIBSTRENGTH);
        if (mVibratorStrength != null) {
            mVibratorStrength.setEnabled(VibratorStrengthPreference.isSupported());
        }

        mGloveMode = (TwoStatePreference) findPreference(KEY_GLOVE_MODE);
        mGloveMode.setChecked(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(DeviceSettings.KEY_GLOVE_MODE, false));
        mGloveMode.setOnPreferenceChangeListener(this);

        mProxWake = (TwoStatePreference) findPreference(KEY_PROX_WAKE);
        mProxWake.setChecked(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(DeviceSettings.KEY_PROX_WAKE, false));
        mProxWake.setOnPreferenceChangeListener(this);

        mDisableKeys = (TwoStatePreference) findPreference(KEY_DISABLE_KEYS);
        mDisableKeys.setChecked(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(DeviceSettings.KEY_DISABLE_KEYS, false));
        mDisableKeys.setOnPreferenceChangeListener(this);
    }

    public static void restore(Context context) {
        boolean gloveModeData = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(DeviceSettings.KEY_GLOVE_MODE, false);
        Utils.writeValue(GLOVE_MODE_FILE, gloveModeData ? "1" : "0");
        boolean proxWakeData =  PreferenceManager.getDefaultSharedPreferences(context).getBoolean(DeviceSettings.KEY_PROX_WAKE, false);
        Utils.writeValue(PROX_WAKE_FILE, proxWakeData ? "1" : "0");
        boolean disableKeysData =  PreferenceManager.getDefaultSharedPreferences(context).getBoolean(DeviceSettings.KEY_DISABLE_KEYS, false);
        Utils.writeValue(DISABLE_KEYS_FILE, disableKeysData ? "0" : "1");
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        return super.onPreferenceTreeClick(preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Boolean enabled = (Boolean) newValue;
        SharedPreferences.Editor prefChange = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        if (preference == mGloveMode) {
            prefChange.putBoolean(KEY_GLOVE_MODE, enabled).commit();
            Utils.writeValue(GLOVE_MODE_FILE, enabled ? "1" : "0");
        } else if (preference == mProxWake) {
            prefChange.putBoolean(KEY_PROX_WAKE, enabled).commit();
            Utils.writeValue(PROX_WAKE_FILE, enabled ? "1" : "0");
        } else if (preference == mDisableKeys) {
            prefChange.putBoolean(KEY_DISABLE_KEYS, enabled).commit();
            Utils.writeValue(DISABLE_KEYS_FILE, enabled ? "0" : "1");
        }
        return true;
    }
}
