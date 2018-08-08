package com.image.selector.adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cross.image_selector.R;

import java.util.List;

public class ImageSelectAdapter<T> extends RecyclerView.Adapter<ImageSelectAdapter.NewViewHolder> {

	private List<T> showItems;
	private LayoutInflater mInflater;
	public final static int TYPE_ADD = 1001;//添加的视图
	public final static int TYPE_NORMAL = 1002;//正常的视图
	private final BindViewDataListener<T> mBindViewData;   //回调接口,当bindView会被调用


	public ImageSelectAdapter(Fragment fragment, List<T> showItems, BindViewDataListener<T> bindViewData) {
		this.mInflater = LayoutInflater.from(fragment.getContext());
		mBindViewData = bindViewData;
		this.showItems = showItems;
	}

	@Override
	public NewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view;
		switch (viewType) {
			case TYPE_ADD:
				view = mInflater.inflate(R.layout.item_photo_select_add, parent, false);
				break;
			case TYPE_NORMAL:
				view = mInflater.inflate(R.layout.item_photo_select_img, parent, false);
				break;
			default:
				view = mInflater.inflate(R.layout.item_photo_select_img, parent, false);
				break;
		}

		return new NewViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final NewViewHolder holder, final int position) {

		int type = getItemViewType(position);
		T showItem;
		switch (type) {
			case TYPE_ADD:
				showItem = null;
				break;
			case TYPE_NORMAL:
				showItem = showItems.get(position);
				break;
			default:
				showItem = null;
				break;
		}
		mBindViewData.onBindView(holder,showItem,type);

	}

	@Override
	public int getItemCount() {
		if (showItems == null) {
			return 1;
		}
		int imageSize = showItems.size();
		return imageSize < 9 ? imageSize + 1 : imageSize;

	}

	@Override
	public int getItemViewType(int position) {

		return showItems == null || position >= showItems.size() ? TYPE_ADD : TYPE_NORMAL;

	}

	public void refresh(List<T> showItems) {
		this.showItems.clear();
		this.showItems.addAll(showItems);
		notifyDataSetChanged();
	}

	public static class NewViewHolder extends RecyclerView.ViewHolder {

		/**
		 * 针对android的高效map，key为int值
		 */
		private SparseArray<View> mSubViewSpArr = new SparseArray<>();

		private NewViewHolder(View itemView) {
			super(itemView);
		}

		/**
		 * 根据id获取view
		 * 1.未存储 -- 保存到map并返回（key值为int id）
		 * 2.已存储 -- 直接从map中拿
		 *
		 * @param Id 条目里的控件id
		 * @return view对象
		 */
		public View getSubViewById(int Id) {
			View view = mSubViewSpArr.get(Id);

			if (view == null) {
				view = itemView.findViewById(Id);
				mSubViewSpArr.put(Id, view);
			}
			return view;
		}
	}

	/**
	 * 用于绑定recycleView条目数据的回调接口
	 *
	 * @param <T> 数据bean的类型
	 */
	public interface BindViewDataListener<T> {
		void onBindView(NewViewHolder holder, T showItem,int type);
	}

}
