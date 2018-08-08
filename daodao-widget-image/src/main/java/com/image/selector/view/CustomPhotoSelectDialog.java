package com.image.selector.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.cross.image_selector.R;

/**
 * Created by cross on 2018/1/23.
 * <p>描述:
 */

public class CustomPhotoSelectDialog extends Dialog {

	private Context context;
	private OnDialogClickListener mOnDialogClickListener;

	public CustomPhotoSelectDialog(Context context) {
		this(context, R.style.PhotoSelectDialog);
	}

	private CustomPhotoSelectDialog(Context context, int themeResId) {
		//设置主题样式
		super(context, themeResId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//设置xml文件
		setContentView(R.layout.dialog_photo_select);
		//设置动画
		windowDeploy();
		//设置触摸对话框意外的地方取消对话框
		setCanceledOnTouchOutside(true);
		initView();
	}

	//初始化布局
	private void initView() {

		findViewById(R.id.tv_dialog_camera).setOnClickListener(mOnClickListener);
		findViewById(R.id.tv_dialog_album).setOnClickListener(mOnClickListener);
		findViewById(R.id.tv_dialog_cancel).setOnClickListener(mOnClickListener);
	}

	/**
	 * 设置窗口显示
	 */
	private void windowDeploy() {

		//得到对话框
		Window window = getWindow();
		if (window == null) {
			return;
		}
		window.setWindowAnimations(R.style.dialog_photo_select_anim); //设置窗口弹出动画
		WindowManager.LayoutParams params = window.getAttributes();//获取对话框当前的参数值
		params.width = ViewGroup.LayoutParams.MATCH_PARENT; //宽度设置为屏幕的百分比
		params.height = ViewGroup.LayoutParams.WRAP_CONTENT;//高度设置为自适应
		params.gravity = Gravity.BOTTOM;//设置位置
		window.setAttributes(params);

	}

	private View.OnClickListener mOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.tv_dialog_cancel) {//关闭

			} else {
				if (mOnDialogClickListener != null){
					mOnDialogClickListener.onDialogClick(v);
				}
			}
			dismiss();
		}
	};

	public void setOnDialogClickListener(OnDialogClickListener listener) {
		mOnDialogClickListener = listener;
	}

	/**
	 * 事件监听
	 */
	public interface OnDialogClickListener {
		void onDialogClick(View view);
	}
}
