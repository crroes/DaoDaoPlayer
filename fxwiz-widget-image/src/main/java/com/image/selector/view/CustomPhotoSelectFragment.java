package com.image.selector.view;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cross.image_selector.R;
import com.image.selector.adapter.ImageSelectAdapter;
import com.image.selector.utils.CameraUtils;
import com.image.selector.utils.ImageSelectorUtils;
import com.image.selector.utils.PermissionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cross on 2018/1/22.
 * <p>描述:相册选择图片自定义View
 */

public abstract class CustomPhotoSelectFragment extends Fragment {

	public static final int REQUEST_SELECT_CODE = 0x00000011;
	private ImageSelectAdapter<String> mAdapter;
	private List<String> mDatas = new ArrayList<>();
	private View mView;
	private OnImageListChangeListener mOnImageListChangeListener;


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		if (mView == null) {
			mView = inflater.inflate(setLayout(), container, false);
			//获取配置数据 照片保存路径和provide
			initDirProvide();
			//初始化视图
			initView();
		} else {
			ViewParent parent = mView.getParent();
			if (parent != null) {
				((ViewGroup) parent).removeView(mView);
			}
		}
		return mView;
	}

	private void initDirProvide() {
		CameraUtils.setPathAndProvide(getPathDir(),getProvideString());
	}


	protected abstract File getPathDir();
	protected abstract String getProvideString();


	protected int setLayout() {
		return R.layout.fragment_photo_select;
	}

	protected void initView() {

		RecyclerView rvImage = (RecyclerView) mView.findViewById(R.id.rv_image);
		rvImage.setLayoutManager(new GridLayoutManager(getContext(), 4));
		mAdapter = new ImageSelectAdapter<>(this, mDatas, mBindViewDataListener);
		rvImage.setAdapter(mAdapter);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_SELECT_CODE && resultCode == Activity.RESULT_OK && data != null) {
			//图片选择回调
			ArrayList<String> images = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT);
			mDatas.clear();
			mDatas.addAll(images);
			mAdapter.notifyDataSetChanged();
			changeImageLists(mDatas);
		}
		if (requestCode == CameraUtils.REQUEST_CAMERA_CODE) {
			//拍照
			File file = new File(CameraUtils.getPhotoPath());
			if (resultCode == Activity.RESULT_OK) {
				//通知相册
				getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
				mDatas.add(CameraUtils.getPhotoPath());
				mAdapter.notifyDataSetChanged();
				changeImageLists(mDatas);
			} else {
				//取消时删除
				if (file.exists()) {
					file.delete();
				}
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == PermissionUtils.REQUEST_CAMERA_PERMISSION) {
			for (int grantResult : grantResults) {
				if (grantResult != PackageManager.PERMISSION_GRANTED) {
					//拒绝了权限
					Toast.makeText(getContext(), "没有拍照权限", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			//同意了权限
			CameraUtils.selectPicFromCamera(this);
		}
	}

	private ImageSelectAdapter.BindViewDataListener<String> mBindViewDataListener = new ImageSelectAdapter.BindViewDataListener<String>() {
		@Override
		public void onBindView(ImageSelectAdapter.NewViewHolder holder, final String showItem, int type) {
			switch (type) {
				case ImageSelectAdapter.TYPE_ADD:
					holder.itemView.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							//弹窗
							showSelectDialog();
						}
					});
					break;
				case ImageSelectAdapter.TYPE_NORMAL:

					ImageView iv_img = (ImageView) holder.getSubViewById(R.id.iv_image);
					Glide.with(getContext()).load(new File(showItem)).into(iv_img);
					holder.getSubViewById(R.id.iv_image_cut).setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							//删除图片
							mDatas.remove(showItem);
							mAdapter.notifyDataSetChanged();
							changeImageLists(mDatas);
						}
					});
					break;
				default:
					break;
			}
		}
	};

	//选择图片
	private void showSelectDialog() {

		CustomPhotoSelectDialog photoSelectDialog = new CustomPhotoSelectDialog(getContext());
		photoSelectDialog.setOnDialogClickListener(new CustomPhotoSelectDialog.OnDialogClickListener() {
			@Override
			public void onDialogClick(View view) {
				int id = view.getId();
				if (id == R.id.tv_dialog_album) {
					//相册选
					ImageSelectorUtils.openPhoto(CustomPhotoSelectFragment.this, REQUEST_SELECT_CODE, false, 9, (ArrayList<String>) mDatas); // 把已选的传入。
				} else if (id == R.id.tv_dialog_camera) {
					//拍照
					startCamera();
				}
			}
		});
		photoSelectDialog.show();

	}

	private void startCamera() {
		//权限判断
		if (new PermissionUtils(this).checkPermissions(PermissionUtils.REQUEST_CAMERA_PERMISSION, PermissionUtils.PHOTOS_PERMISSIONS)) {
			CameraUtils.selectPicFromCamera(this);
		}
	}

	private void changeImageLists(List<String> imageLists){

		if (mOnImageListChangeListener!=null){
			mOnImageListChangeListener.OnImageChange(imageLists);
		}
	}

	//设置图片选择监听
	public void setOnImageListChangeListener(OnImageListChangeListener listener) {
		this.mOnImageListChangeListener = listener;
	}


	public void setSelectedPhoto(List<String> imageList) {
		mDatas.clear();
		mDatas.addAll(imageList);
		if (mAdapter!=null){
			mAdapter.notifyDataSetChanged();
		}
	}


	//选择取消图片监听接口
	public interface OnImageListChangeListener {
		void OnImageChange(List<String> imageLists);
	}

}
