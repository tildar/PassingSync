/*
* Copyright 2013 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


package edu.cmu.mastersofflyingobjects.passingsync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


/**
 * A simple launcher activity containing a summary sample description, sample log and a custom
 * {@link android.support.v4.app.Fragment} which can display a view.
 * <p/>
 * For devices with displays with a width of 720dp or greater, the sample log is always visible,
 * on other devices it's visibility is controlled by an item on the Action Bar.
 */
public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_SITESWAP = "edu.cmu.passingsync.SITESWAP";
    public final static String EXTRA_TITLE = "edu.cmu.passingsync.TITLE";
    public final static String EXTRA_SITESWAP_KIND = "edu.cmu.passingsync.SITESWAP_KIND";


    // Whether the Log Fragment is currently shown
    private boolean mLogShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BluetoothChatFragment fragment = new BluetoothChatFragment();
            fragment.setApp((PassingSyncApplication) getApplicationContext());
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }

    }


    public static void startSiteswap(BluetoothService bluetooth, Activity activity, Character kind, String config, String title) {
        // Check that we're actually connected before trying anything
        if (bluetooth.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(activity, "Warning, starting siteswap without connection!", Toast.LENGTH_LONG).show();
        }

        // Check that there's actually something to send
        if (config.length() > 0) {
            Intent intent = new Intent(activity, RunSiteswapMasterActivity.class);
            intent.putExtra(MainActivity.EXTRA_SITESWAP, config);
            intent.putExtra(MainActivity.EXTRA_TITLE, title);
            intent.putExtra(MainActivity.EXTRA_SITESWAP_KIND, kind);
            activity.startActivity(intent);

            bluetooth.startSiteswap(config);
        }
    }

}
