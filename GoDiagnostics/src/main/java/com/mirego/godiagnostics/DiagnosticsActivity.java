// Copyright (c) 2017, Mirego
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// - Redistributions of source code must retain the above copyright notice,
//   this list of conditions and the following disclaimer.
// - Redistributions in binary form must reproduce the above copyright notice,
//   this list of conditions and the following disclaimer in the documentation
//   and/or other materials provided with the distribution.
// - Neither the name of the Mirego nor the names of its contributors may
//   be used to endorse or promote products derived from this software without
//   specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.
package com.mirego.godiagnostics;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.mirego.godiagnostics.adapter.DiagnosticsInfoAdapter;
import com.mirego.godiagnostics.utils.DiskUtils;
import com.mirego.godiagnostics.view.SeparatorDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DiagnosticsActivity extends AppCompatActivity {

    private ConnectivityManager connectivityManager;

    private RecyclerView diagnosticRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_diagnostics);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
        } else {
            toolbar.setVisibility(View.GONE);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        diagnosticRecyclerView = (RecyclerView) findViewById(R.id.diagnostic_items);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setupDiagnosticRecyclerView();
    }

    private void setupDiagnosticRecyclerView() {
        diagnosticRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        diagnosticRecyclerView.setLayoutManager(linearLayoutManager);
        DiagnosticsInfoAdapter adapter = new DiagnosticsInfoAdapter();
        adapter.setData(createDiagnosticInfo());
        diagnosticRecyclerView.setAdapter(adapter);

        diagnosticRecyclerView.addItemDecoration(new SeparatorDividerItemDecoration(diagnosticRecyclerView.getContext(),
                linearLayoutManager.getOrientation()));
    }

    private List<DiagnosticInfoViewData> createDiagnosticInfo() {
        List<DiagnosticInfoViewData> diagnosticInfoViewDataList = new ArrayList<>();

        DiagnosticInfoViewData osTypeInfo = new DiagnosticInfoViewData(getString(R.string.diagnostic_os_name_title), getSystemName());
        diagnosticInfoViewDataList.add(osTypeInfo);

        DiagnosticInfoViewData osVersionInfo = new DiagnosticInfoViewData(getString(R.string.diagnostic_os_version_title), getSystemVersion());
        diagnosticInfoViewDataList.add(osVersionInfo);

        DiagnosticInfoViewData deviceModelInfo = new DiagnosticInfoViewData(getString(R.string.diagnostic_device_model_title), getDeviceModel());
        diagnosticInfoViewDataList.add(deviceModelInfo);

        DiagnosticInfoViewData appVersionInfo = new DiagnosticInfoViewData(getString(R.string.diagnostic_app_version_title), getAppVersion());
        diagnosticInfoViewDataList.add(appVersionInfo);

        DiagnosticInfoViewData batteryLevelInfo = new DiagnosticInfoViewData(getString(R.string.diagnostic_battery_level_title), getBatteryLevel());
        diagnosticInfoViewDataList.add(batteryLevelInfo);

        DiagnosticInfoViewData totalDiskSpaceInfo = new DiagnosticInfoViewData(getString(R.string.diagnostic_total_disk_title), getTotalDiskSpace());
        diagnosticInfoViewDataList.add(totalDiskSpaceInfo);

        DiagnosticInfoViewData availableDiskSpaceInfo = new DiagnosticInfoViewData(getString(R.string.diagnostic_available_disk_title), getFreeDiskSpace());
        diagnosticInfoViewDataList.add(availableDiskSpaceInfo);

        DiagnosticInfoViewData memoryUsageInfo = new DiagnosticInfoViewData(getString(R.string.diagnostic_memory_usage_title), getMemoryUsage());
        diagnosticInfoViewDataList.add(memoryUsageInfo);

        DiagnosticInfoViewData connectivityInfo = new DiagnosticInfoViewData(getString(R.string.diagnostic_connectivity_title), getConnectivity());
        diagnosticInfoViewDataList.add(connectivityInfo);

        return diagnosticInfoViewDataList;
    }

    private String getSystemName() {
        return getString(R.string.diagnostic_os_name);
    }

    private String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    private String getDeviceModel() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model != null && model.startsWith(manufacturer)) {
            return model.toUpperCase();
        }
        return manufacturer + " " + model;
    }

    private String getAppVersion() {
        String version = "?";
        try {
            PackageInfo pInfo  = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    private String getBatteryLevel() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, intentFilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float)scale;

        return String.format(Locale.CANADA, "%.0f %%", batteryPct * 100);
    }

    private String getTotalDiskSpace() {
        return String.format(Locale.CANADA, "%d %s", DiskUtils.totalSpace(false), getString(R.string.diagnostic_memory_mb_unit));
    }

    private String getFreeDiskSpace() {
        return String.format(Locale.CANADA, "%d %s", DiskUtils.freeSpace(false), getString(R.string.diagnostic_memory_mb_unit));
    }

    private String getMemoryUsage() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableMegs = mi.availMem / 0x100000L;
        double totalMegs = mi.totalMem / 0x100000L;
        double usedMegs = totalMegs - availableMegs;

        return String.format(Locale.CANADA, "%.0f %s / %.0f %s",
                usedMegs,
                getString(R.string.diagnostic_memory_mb_unit),
                totalMegs,
                getString(R.string.diagnostic_memory_mb_unit));
    }

    private String getConnectivity() {
        NetworkInfo networkInfo = this.connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            switch(networkInfo.getType()) {
                case 0:
                case 1:
                    return getString(R.string.diagnostic_connectivity_online);
                default:
                    return getString(R.string.diagnostic_connectivity_unknown);
            }
        }
        return getString(R.string.diagnostic_connectivity_offline);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
