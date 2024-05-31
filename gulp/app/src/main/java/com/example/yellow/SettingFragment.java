package com.example.yellow;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class SettingFragment extends Fragment {

    private Switch switchDarkMode, switchNotifications, switchLocation;
    private Spinner spinnerLanguage;
    private Button btnChangePassword, btnFeedback;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_setting_fragment, container, false);

        // Initialize views
        switchDarkMode = view.findViewById(R.id.switch_dark_mode);
        switchNotifications = view.findViewById(R.id.switch_notifications);
        switchLocation = view.findViewById(R.id.switch_location);
        spinnerLanguage = view.findViewById(R.id.spinner_language);
        btnChangePassword = view.findViewById(R.id.btn_change_password);
        btnFeedback = view.findViewById(R.id.btn_feedback);

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

        switchDarkMode.setChecked(sharedPreferences.getBoolean("dark_mode", false));
        switchNotifications.setChecked(sharedPreferences.getBoolean("notifications", true));
        switchLocation.setChecked(sharedPreferences.getBoolean("location_access", true));
        setSpinnerSelection(sharedPreferences.getString("language", "English"));

        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                savePreference("dark_mode", isChecked);
                applyDarkMode(isChecked);
            }
        });

        switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                savePreference("notifications", isChecked);
            }
        });

        switchLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                savePreference("location_access", isChecked);
            }
        });

        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = (String) parent.getItemAtPosition(position);
                savePreference("language", selectedLanguage);
                updateLocale(selectedLanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });

        return view;
    }

    private void savePreference(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private void savePreference(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void applyDarkMode(boolean isDarkMode) {
        int nightMode = isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(nightMode);
        getActivity().recreate();
    }

    private void setSpinnerSelection(String language) {
        if (language.equals("French")) {
            spinnerLanguage.setSelection(1);
        } else {
            spinnerLanguage.setSelection(0);
        }
    }

    private void updateLocale(String language) {
        Locale locale;
        if (language.equals("French")) {
            locale = new Locale("fr");
        } else {
            locale = new Locale("en");
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        getActivity().recreate();
    }

    private void changePassword() {
    }

    private void sendFeedback() {
    }
}
