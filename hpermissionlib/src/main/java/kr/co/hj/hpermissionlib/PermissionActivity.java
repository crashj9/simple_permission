package kr.co.hj.hpermissionlib;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * permission 팝업을 띄우기 위한 activity
 */
public class PermissionActivity extends AppCompatActivity {

    public static final String EXTRA_KEY = "permissions";
    public static final String NAME_EXTRA_OUTPUT_PERMISSION = "NAME_EXTRA_OUTPUT_PERMISSION";
    public static final String NAME_EXTRA_OUTPUT_GRANT_RESULTS = "NAME_EXTRA_OUTPUT_GRANT_RESULTS";

    protected static final String NAME_EXTRA_INPUT_LISTENER_KEY = "NAME_EXTRA_INPUT_LISTENER_KEY";

    private ArrayList<String> requestPermissions = new ArrayList<>();
    private int listenerKey = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.util.Log.e("eeeeee", "eeeeeeeee10");

        listenerKey = getIntent().getIntExtra(NAME_EXTRA_INPUT_LISTENER_KEY, -1);
        String[] permissions = getIntent().getExtras().getStringArray(EXTRA_KEY);

        if (permissions != null) {
            android.util.Log.e("eeeeee", "eeeeeeeee11");
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions.add(permission);
                }
            }
        } else {
            android.util.Log.e("eeeeee", "eeeeeeeee13");
            throw new IllegalArgumentException("Not input any permissions!!");
        }

        if (requestPermissions.size() <= 0) {
            android.util.Log.e("eeeeee", "eeeeeeeee12");
            PermissionListener listener = null;
            if (listenerKey > -1) {
                listener = PermissionListenerManager.getInstance().getListener(listenerKey);
            }

            if (listener != null) {
                listener.onResultReceive(true, null);
                PermissionListenerManager.getInstance().removeListener(listenerKey);
            } else {
                resultCall(Activity.RESULT_OK, null);
            }

            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            ActivityCompat.requestPermissions(this, requestPermissions.toArray(new String[requestPermissions.size()]), 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArrayList<Integer> refuseIndexs = new ArrayList<>();
        if (requestCode == 1) {
            for (int i = 0 ; i < grantResults.length ; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    refuseIndexs.add(i);
                }
            }
        }

        String[] rtnPermissions = new String[refuseIndexs.size()];
        int[] rtnGrantResults = new int[refuseIndexs.size()];
        int count = 0;
        for (int i : refuseIndexs) {
            rtnPermissions[count] = permissions[i];
            rtnGrantResults[count] = grantResults[i];
        }

        PermissionListener listener = null;
        if (listenerKey > -1) {
            listener = PermissionListenerManager.getInstance().getListener(listenerKey);
        }

        if (listener != null) {
            listener.onResultReceive(rtnPermissions.length <= 0, rtnPermissions);
            PermissionListenerManager.getInstance().removeListener(listenerKey);
            finish();
        } else {
            Intent intent = new Intent();
            intent.putExtra(NAME_EXTRA_OUTPUT_PERMISSION, rtnPermissions);
            intent.putExtra(NAME_EXTRA_OUTPUT_GRANT_RESULTS, rtnGrantResults);
            resultCall(Activity.RESULT_OK, intent);
        }
    }

    private void resultCall(int resultCode, Intent intent) {
        setResult(resultCode, intent);
        finish();
    }
}
