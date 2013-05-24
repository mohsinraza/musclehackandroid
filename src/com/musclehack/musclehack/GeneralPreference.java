package com.musclehack.musclehack;


import android.os.Bundle;
import android.preference.PreferenceActivity;
 
public class GeneralPreference extends PreferenceActivity {
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}