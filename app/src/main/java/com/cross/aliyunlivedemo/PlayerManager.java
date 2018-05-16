//package com.cross.aliyunlivedemo;
//
//import android.content.Context;
//import android.graphics.SurfaceTexture;
//import android.util.Log;
//import android.view.Surface;
//import android.view.TextureView;
//import android.widget.Toast;
//
//import com.aliyun.vodplayer.media.AliyunLocalSource;
//import com.aliyun.vodplayer.media.AliyunVodPlayer;
//import com.aliyun.vodplayer.media.IAliyunVodPlayer;
//
///**
// * Created by cross on 2018/5/14.
// * <p>描述:
// */
//
//private static class PlayerManager {
//
//	private Context context;
//	private AliyunVodPlayer vodPlayer;
//	private int position = -1;
//
//
//	private SurfaceTexture surfaceTexture;
//	private MyHolder holder;
//
//	public PlayerManager(final Context context, AliyunVodPlayer player) {
//		this.context = context;
//		vodPlayer = player;
//		vodPlayer.enableNativeLog();
//		vodPlayer.setCirclePlay(true);
//		vodPlayer.setAutoPlay(true);
//		vodPlayer.setReferer("http://aliyun.com");
//		vodPlayer.setOnErrorListener(new IAliyunVodPlayer.OnErrorListener() {
//			@Override
//			public void onError(int arg0, int arg1, String msg) {
//				Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//				stop();
//			}
//		});
//	}
//
//	public void start(MyHolder holder) {
//		if (holder.getTag() == this.position) {
//			return;
//		}
//
//		stop();
//
//		this.holder = holder;
//		this.position = holder.getTag();
//
//		Log.d(TAG, " start pos = " + position);
//		AliyunLocalSource localSource = urlList.get(position % 5);
//
//
//		vodPlayer.reset();
//
//		Log.d(TAG, " prepareAsync pos = " + position);
//		vodPlayer.prepareAsync(localSource);
//	}
//
//	public void resetTextureView() {
//		if (holder != null) {
//			holder.removeTextureView();
//
//			TextureView textureView = new TextureView(context);
//			textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
//				@Override
//				public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
//					Log.d(DemoMultiPlayerActivity.TAG, " onSurfaceTextureAvailable ");
//					setSurfaceTexture(surface);
//				}
//
//				@Override
//				public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
//
//				}
//
//				@Override
//				public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
//					Log.d(DemoMultiPlayerActivity.TAG, " onSurfaceTextureDestroyed ");
//					return true;
//				}
//
//				@Override
//				public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//
//				}
//			});
//
//			holder.addTextureView(textureView);
//		}
//
//	}
//
//	public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
//		this.surfaceTexture = surfaceTexture;
//		Surface surface = new Surface(surfaceTexture);
//		vodPlayer.setSurface(surface);
//	}
//
//	public void stop() {
//
//		Log.d(TAG, " stop pos = " + position);
//		if (position < 0) {
//			return;
//		}
//		this.position = -1;
//		vodPlayer.stop();
//
//		if (holder != null) {
//			//                clearSurface(surfaceTexture);
//			holder.stop();
//		}
//	}
//
//	public int getPosition() {
//		return position;
//	}
//
//	public void release() {
//		stop();
//		vodPlayer.release();
//	}
//
//}
