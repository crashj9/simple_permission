package kr.co.hj.hpermissionlib;

/**
 * Created by hwajun on 2017. 6. 23..
 *
 * permission 팝업의 선택 결과를 받기 위한 리스너
 */

public interface PermissionListener {
    void onResultReceive(boolean isSuccess, String[] failPermissions);
}
