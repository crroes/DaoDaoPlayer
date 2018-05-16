package com.cross.fxwiz_widget_player.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cross on 2018/5/14.
 * <p>描述:权限管理
 */
public class PermissionUtils {

	private Context mContext;
	private boolean isActivity;
	private Fragment mFragment;

	//存储权限
	public static int REQUEST_STORAGE_PERMISSION = 101;
	public final static String[] STORAGE_PERMISSION = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

	private List<String> PermissionsVessel = new ArrayList<>();


	public PermissionUtils(Context context, boolean isActivity) {
		mContext = context;
		this.isActivity = isActivity;
	}

	public PermissionUtils(Fragment fragment) {
		mContext = fragment.getContext();
		this.mFragment = fragment;
	}

	// 判断权限集合
	public boolean checkPermissions(int requestCode, String... permissions) {
		PermissionsVessel.clear();

		for (String permission : permissions) {
			if (!checkPermission(permission)) {
				PermissionsVessel.add(permission);
			}
		}

		if (PermissionsVessel.size() != 0) {
			if (isActivity) {
				openPermission((Activity) mContext, PermissionsVessel.toArray(new String[PermissionsVessel.size()]), requestCode);
			} else {
				openPermission(mFragment, PermissionsVessel.toArray(new String[PermissionsVessel.size()]), requestCode);
			}
			return false;
		} else {
			return true;
		}

	}

	// 判断是否缺少权限
	private boolean checkPermission(String permission) {
		int checkSelfPermission = ContextCompat.checkSelfPermission(mContext, permission);
		return checkSelfPermission == PackageManager.PERMISSION_GRANTED;
	}

	//询问是否允许开启权限 activity
	private void openPermission(final Activity activity, final String[] permission, final int requestCode) {

		if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission[0])) {
			new AlertDialog.Builder(activity).setMessage("你需要开启权限才能使用该功能").setNegativeButton("拒绝", null).setPositiveButton("允许", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ActivityCompat.requestPermissions(activity, permission, requestCode);
				}
			}).create().show();
		} else {
			ActivityCompat.requestPermissions(activity, permission, requestCode);
		}
	}

	//询问是否允许开启权限 fragment
	private void openPermission(final Fragment fragment, final String[] permission, final int requestCode) {

		if (fragment.shouldShowRequestPermissionRationale(permission[0])) {
			new AlertDialog.Builder(fragment.getContext()).setMessage("你需要开启权限才能使用该功能").setPositiveButton("允许", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					fragment.requestPermissions(permission, requestCode);
				}
			}).setNegativeButton("拒绝", null).create().show();
		} else {
			fragment.requestPermissions(permission, requestCode);
		}
	}


}
