package kr.co.hj.hpermission

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.hj.hpermissionlib.PermissionListener
import kr.co.hj.hpermissionlib.PermissionUtil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var requestPermissions = kotlin.arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
        textview.setOnClickListener(View.OnClickListener {
            if (PermissionUtil.needPermissionPopup(this, requestPermissions)) {
                PermissionUtil.showPermission(this, requestPermissions, object : PermissionListener {
                    override fun onResultReceive(isSuccess: Boolean, failPermissions: Array<out String>?) {
                        if (isSuccess) {
                            Toast.makeText(this@MainActivity, "권한 획득 성공", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this@MainActivity, "권한 획득 실패", Toast.LENGTH_LONG).show()
                        }
                    }
                })
            } else {
                Toast.makeText(this, "이미 권한을 획득하였습니다", Toast.LENGTH_LONG).show()
            }
        })
    }
}
