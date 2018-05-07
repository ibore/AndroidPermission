package me.ibore.permission.demo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.util.List;

import me.ibore.permission.PermissionManager;
import me.ibore.permission.annotation.PermissionCanceled;
import me.ibore.permission.annotation.PermissionDenied;
import me.ibore.permission.annotation.PermissionGranted;
import me.ibore.permission.util.SettingUtil;

public class PermissionService extends Service {

    public PermissionService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestCamera();
            }
        }, 500);
        return super.onStartCommand(intent, flags, startId);
    }

    private void requestCamera(){
        PermissionManager.PermissionRequest(this, new String[]{Manifest.permission.CAMERA},10);
    }

    private void callMap(){
        PermissionManager.PermissionRequest(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
    }

    @PermissionGranted
    public void PermissionGranted(int requestCode) {
        switch (requestCode){
            case 10:
                Toast.makeText(getBaseContext(), "相机权限申请通过", Toast.LENGTH_SHORT).show();
                break;
            case 0:
                Toast.makeText(getBaseContext(), "定位权限申请通过", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @PermissionDenied
    public void PermissionDenied(int requestCode, List<String> denyList) {
        switch (requestCode) {
            case 10:
                //多个权限申请返回结果
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < denyList.size(); i++) {
                    if (Manifest.permission.CALL_PHONE.equals(denyList.get(i))) {
                        builder.append("电话");
                    } else if (Manifest.permission.CAMERA.equals(denyList.get(i))) {
                        builder.append("相机");
                    }
                }
                builder.append("权限被禁止，需要手动打开");
                new AlertDialog.Builder(getBaseContext())
                        .setTitle("提示")
                        .setMessage(builder)
                        .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SettingUtil.go2Setting(getBaseContext());
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();

                break;
            case 0:
                //单个权限申请返回结果
                new AlertDialog.Builder(getBaseContext())
                        .setTitle("提示")
                        .setMessage("定位权限被禁止，需要手动打开")
                        .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SettingUtil.go2Setting(getBaseContext());
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
                break;
            default:
                break;
        }
    }

    @PermissionCanceled
    public void PermissionCanceled(int requestCode) {
        Toast.makeText(getBaseContext(), "requestCode:" + requestCode, Toast.LENGTH_SHORT).show();
    }
}
