package com.cloudclass.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.cloudclass.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

public class NetUtils {
	public final static String THUMB_PATH = "/sdcard/CloudClass/Image/";
	public final static int LARGE_IMAGE = 480 * 320;
	public final static int MIDDLE_IMAGE = 320 * 320;
	public final static int TiNE_IMAGE = 240 * 240;

	/**
	 * 内存卡是否可以使用
	 * 
	 * @return 可以用为true,不可以用为false
	 */
	public static boolean isSDCardAvaible() {
		String state = Environment.getExternalStorageState();
		if (!state.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		}
		return true;
	}

	public static void loadImage(final Context activity, final String imageUrl,
			final int maxNumOfPixel, final ImageView img) {
		if (TextUtils.isEmpty(imageUrl)) {
			img.setBackgroundResource(R.drawable.defult);
			return;
		}
		synchronized (imageUrl) {
			new Thread() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					Bitmap bmp = loadImageFromNetwork(imageUrl, maxNumOfPixel);
					if (bmp == null) {
						Log.e("decodeStream", "loadImageFromNetwork  读取图片失败");
						bmp = loadImageFromNetwork(imageUrl, maxNumOfPixel);
					}
					img.post(new SetImageRunnble(activity, img, bmp));

					super.run();
				}
			}.start();
		}

	}

	public static void loadImage(final String imageUrl,
			final int maxNumOfPixel, final ImageView img) {
		if (TextUtils.isEmpty(imageUrl)) {
			return;
		}
		synchronized (imageUrl) {
			new Thread() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					Bitmap bmp = loadImageFromNetwork(imageUrl, maxNumOfPixel);
					if (bmp == null) {
						Log.e("decodeStream", "loadImageFromNetwork  读取图片失败");
						bmp = loadImageFromNetwork(imageUrl, maxNumOfPixel);
					}
					img.post(new SetImageRunnble(img, bmp));

					super.run();
				}
			}.start();
		}

	}

	public static void loadImage(final Context context,
			final List<String> urls, final int maxNumOfPixel,
			final ImageCallBack callback) {
		if (urls == null) {
			return;
		}
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// List<Bitmap> maps = new ArrayList<Bitmap>(urls.size());
				for (int i = 0; i < urls.size(); i++) {
					Bitmap bmp = null;
					if (TextUtils.isEmpty(urls.get(i))) {
						bmp = BitmapFactory.decodeResource(
								context.getResources(), R.drawable.defult);
					} else {
						bmp = loadImageFromNetwork(urls.get(i), maxNumOfPixel);
						if (bmp == null) {
							// maps.add(bmp);
							bmp = loadImageFromNetwork(urls.get(i),
									maxNumOfPixel);
						}

					}
					callback.callBack(i, urls.get(i), bmp);
				}

				// img.post(new SetImageRunnble(img, bmp));

				super.run();
			}
		}.start();
	}

	public interface ImageCallBack {
		public abstract void callBack(int index, String urls, Bitmap maps);
	}

	/**
	 * 同过网络读取图片
	 * 
	 * @param imageUrl
	 * @return
	 */
	public static Bitmap loadImageFromNetwork(String imageUrl, int maxNumOfPixel) {

		Bitmap tmpBitmap = null;
		try {
			// 可以在这里通过文件名来判断，是否本地有此图片
			if (!isSDCardAvaible()) {

				return null;
			}
			File dirFile = new File(THUMB_PATH);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			File[] files = dirFile.listFiles();
			if (files == null) {
				return null;
			}
			boolean isExsit = false;
			String picPath = "";
			String fileName = MD5Util.string2MD5(imageUrl);

			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().equals(fileName)) {
					isExsit = true;
					picPath = files[i].getPath();
					break;
				}
			}
			if (isExsit) {
				tmpBitmap = GetThumbImage(picPath, maxNumOfPixel);
				if (tmpBitmap == null) {
					// tmpBitmap = GetThumbImage(picPath, maxNumOfPixel);
					File tempFile = new File(picPath);
					if (tempFile.exists()) {
						tempFile.delete();
						loadImageFromNetwork(imageUrl, maxNumOfPixel);
					}
				}

			} else {
				InputStream is = new java.net.URL(imageUrl).openStream();
				tmpBitmap = BitmapFactory.decodeStream(is);

				if (tmpBitmap != null) {
					File myCaptureFile = getFilePath(THUMB_PATH, fileName);
					FileOutputStream out = new FileOutputStream(myCaptureFile);
					tmpBitmap.compress(Bitmap.CompressFormat.PNG, 80, out);
					out.flush();
					out.close();
				} else
					Log.e("decodeStream", "decodeStream  读取图片失败");

				is.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return tmpBitmap;
	}

	public static File getFilePath(String filePath, String fileName) {
		File file = null;

		makeRootDirectory(filePath.trim());
		try {
			file = new File(filePath, fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	public static Bitmap transImage(Bitmap bitmap, int width, int height) {
		int bitmapWidth = bitmap.getWidth();
		int bitmapHeight = bitmap.getHeight();
		// 缩放图片的尺寸
		float scaleWidth = (float) width / bitmapWidth;
		float scaleHeight = (float) height / bitmapHeight;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 产生缩放后的Bitmap对象
		Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth,
				bitmapHeight, matrix, false);
		if (!bitmap.isRecycled()) {
			bitmap.recycle();
		}
		return resizeBitmap;
	}

	public static Bitmap GetThumbImage(String fileName, int maxNumOfPixel) {
		Bitmap result = null;
		try {
			BitmapFactory.Options options = new Options();
			options.inJustDecodeBounds = true;
			Bitmap temp = BitmapFactory.decodeFile(fileName, options);
			options.inSampleSize = computeSampleSize(options, -1, maxNumOfPixel);
			options.inJustDecodeBounds = false;

			result = BitmapFactory.decodeFile(fileName, options);
			// System.out.println("scale:"+scale);
			// if(!temp.isRecycled()){
			// temp.recycle();
			// }
		} catch (RuntimeException e) {
			System.out.println("RuntimeException获取缩略图出错：" + e.getMessage());
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			System.out.println("获取缩略图出错：" + e.getMessage());
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static void makeRootDirectory(String filePath) {
		// File file = null;
		try {
			// file = new File(filePath);
			// if (!file.exists()) {
			// boolean ss=file.mkdir();

			// }
			File file = null;
			String newPath = null;
			String[] path = filePath.split("/");
			for (int i = 0; i < path.length; i++) {
				if (TextUtils.isEmpty(path[i])) {
					break;
				}
				if (newPath == null) {
					newPath = path[i];
				} else {
					newPath = newPath + "/" + path[i];
				}
				file = new File(newPath);
				if (!file.exists()) {
					boolean ss = file.mkdir();
					Log.e("makeRootDirectory",
							"filePath:" + file.getAbsolutePath()
									+ "makeRootDirectory:     --------       "
									+ ss);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
