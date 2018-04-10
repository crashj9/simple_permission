package kr.co.hj.hpermissionlib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

/**
 * Created by hwajun on 2017. 6. 23..
 *
 * Permission관련된 유틸들
 */

public class PermissionUtil {

    /**
     * permission을 체크하여 현재상태를 반환한다.
     * @param context
     * @param permissions 체크할 permissions
     * @return 입력받은 permissions의 현재 상태 값
     */
    public static int[] permissionCheck(@NonNull Context context, @NonNull String[] permissions) {
        int[] results = new int[permissions.length];
        for (int i = 0 ; i < permissions.length ; i++) {
            String permission = permissions[i];
            results[i] = ContextCompat.checkSelfPermission(context, permission);
        }

        return results;
    }

    /**
     * 해당앱의 설정페이지로 이동시킨다.
     * @param activity 설정페이지로 보낼 앱의 액티비티
     */
    public static void sendAppPermissionSetting(@NonNull Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", activity.getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /**
     * 권한 팝업이 필요한지 판단한다.
     * @param context
     * @param permissions 체크할 permissions
     * @return
     */
    public static boolean needPermissionPopup(@NonNull Context context, @NonNull String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }

        return false;
    }

    /**
     * permission 팝업을 띄운다.
     * @param activity
     * @param permissions permission 팝업을 띄운다.
     * @param listener 팝업 완료 후 응답을 받을 리스너
     */
    public static void showPermission(Activity activity, @NonNull String[] permissions, PermissionListener listener) {
        int key = -1;
        if (listener != null) {
            key = PermissionListenerManager.getInstance().addListener(listener);
        }
        Intent intent = new Intent(activity, PermissionActivity.class);
        intent.putExtra(PermissionActivity.NAME_EXTRA_INPUT_LISTENER_KEY, key);
        intent.putExtra(PermissionActivity.EXTRA_KEY, permissions);
        activity.startActivity(intent);
    }

    /**
     * permission 팝업을 띄운다.
     * @param activity
     * @param permissions permission 팝업을 띄운다.
     * @param requestCode resultActivity로 받은 코
     */
    public static void showPermission(Activity activity, @NonNull String[] permissions, int requestCode) {
        Intent intent = new Intent(activity, PermissionActivity.class);
        intent.putExtra(PermissionActivity.EXTRA_KEY, permissions);
        activity.startActivityForResult(intent, requestCode);
    }
}
