package kr.co.hj.hpermissionlib;

import java.util.Hashtable;

/**
 * Created by hwajun on 2017. 6. 23..
 *
 * PermissionListener를 관리하기 위한 클래스.
 * 등록된 PermissionListener을 가지고 있다가 Permission팝업 결과를 되돌려줄 때 사용
 */

public class PermissionListenerManager {
    private volatile  static PermissionListenerManager instance = null;
    public static PermissionListenerManager getInstance() {
        if (instance == null) {
            synchronized (PermissionListenerManager.class) {
                if (instance == null) {
                    instance = new PermissionListenerManager();
                }
            }
        }

        return instance;
    }

    private int keyIndex = 0;
    private Hashtable<Integer, PermissionListener> hashtable = new Hashtable<>();

    private PermissionListenerManager() {
    }

    private int getKey() {
        synchronized (PermissionListenerManager.class) {
            if (keyIndex == Integer.MAX_VALUE) {
                keyIndex = 0;
                return 0;
            }
            return keyIndex++;
        }
    }
    public int addListener(PermissionListener listener) {
        int key = getKey();
        hashtable.put(key, listener);
        return key;
    }

    public PermissionListener getListener(int key) {
        return hashtable.get(key);
    }

    public void removeListener(int key) {
        hashtable.remove(key);
    }
}
