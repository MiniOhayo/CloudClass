package com.cloudclass;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.cloudclass.entity.ReturnInfo;
import com.cloudclass.entity.UploadFileRet;
import com.cloudclass.fragment.FgMainMyLesson;
import com.cloudclass.utils.ApiUtils;
import com.cloudclass.utils.CommenData;
import com.cloudclass.utils.NetUtils;
import com.cloudclass.utils.Utils;
import com.cloudclass.utils.ViewUtils;
import com.cloudclass.view.SelectDialog;
import com.cloudclass.view.Show;

public class AcPersonInfo extends Activity implements OnClickListener {

	public static final int RENAME = 0x222;
	public static final int MODIFY_Phone = 0x333;
	Button btnLogout;
	AppMain appMain;
	private ImageView imgFace;
	private int requestCode = 0x111;

	TextView tvName, tvPhone;
	private SelectDialog selectDialog;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case ViewUtils.UPLOAD_FAILD:
				Show.toast(AcPersonInfo.this, "上传失败");
				break;
			case ViewUtils.UPLOAD_S:

				String retmsg = (String) msg.obj;
				Log.i("retmsg", "retmsg:" + retmsg);
				final UploadFileRet uploadFileRet = JSON.parseObject(retmsg,
						UploadFileRet.class);
				if (uploadFileRet.issuccess) {
					Show.toast(AcPersonInfo.this, "上传成功");
					new Thread() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							ReturnInfo returnInfo = ApiUtils.uploadUserDetail(
									appMain.loginInfo.id,
									uploadFileRet.imageurl, null, null, null,
									handler);
							// hander.sendMessage(Message.obtain(hander,
							// INITE_CONTENT,
							// response));
							if (returnInfo != null && returnInfo.issuccess) {
								FgMainMyLesson.freshInfo = true;
							}

							super.run();
						}
					}.start();
				}

				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_person_info);

		tvName = (TextView) findViewById(R.id.tv_name);
		tvPhone = (TextView) findViewById(R.id.tv_Phone);
		appMain = (AppMain) getApplication();
		btnLogout = (Button) findViewById(R.id.btn_logout);
		btnLogout.setOnClickListener(this);
		findViewById(R.id.back_btn).setOnClickListener(this);

		if (!TextUtils.isEmpty(appMain.loginInfo.name)) {
			tvName.setText(appMain.loginInfo.name + "");
		}
		if (appMain.loginInfo.isThirdLogin) {
			findViewById(R.id.ll_bind_phone).setVisibility(View.GONE);
			findViewById(R.id.ll_modify_pass).setVisibility(View.GONE);
			findViewById(R.id.ll_rename).setVisibility(View.GONE);
		} else
			findViewById(R.id.ll_face).setOnClickListener(this);
		findViewById(R.id.ll_rename).setOnClickListener(this);
		findViewById(R.id.ll_modify_pass).setOnClickListener(this);
		findViewById(R.id.ll_bind_phone).setOnClickListener(this);
		tvPhone.setText(appMain.loginInfo.mobile + "");
		imgFace = (ImageView) findViewById(R.id.img_face);
		imgFace.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				NetUtils.loadImage(appMain.loginInfo.headimage,
						NetUtils.TiNE_IMAGE, imgFace);
			}
		}, 50);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_logout:
			appMain.setLoginInfo(null);
			Utils.writeLoginInfo(null, CommenData.LOGIN_INFO_PATH);
			setResult(FgMainMyLesson.LOGOUT);
			finish();
			break;
		case R.id.back_btn:
			finish();
			break;
		case R.id.ll_face:
			updateIcon();
			break;
		case R.id.ll_rename:
			startActivityForResult(new Intent(this, AcReName.class),
					requestCode);
			break;
		case R.id.ll_bind_phone:
			startActivityForResult(new Intent(this, AcBindPhone.class),
					requestCode);
			break;
		case R.id.ll_modify_pass:
			startActivityForResult(new Intent(this, AcChangePassword.class),
					requestCode);
			break;
		}
	}

	void updateIcon() {
		if (selectDialog == null) {
			View view = LayoutInflater.from(this).inflate(
					R.layout.dialog_layout_select_photo, null);

			selectDialog = new SelectDialog(this, R.style.MMTheme_DataSheet,
					view);
			view.findViewById(R.id.btn_camera).setOnClickListener(
					new SelectDialogListener());
			view.findViewById(R.id.btn_photos).setOnClickListener(
					new SelectDialogListener());
			view.findViewById(R.id.btn_cancle).setOnClickListener(
					new SelectDialogListener());
		}

		selectDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 完成照相后回调用此方法
		super.onActivityResult(requestCode, resultCode, data);
		if (this.requestCode == requestCode) {
			if (resultCode == MODIFY_Phone) {
				tvPhone.setText(appMain.loginInfo.mobile + "");
			} else if (resultCode == RENAME) {

				tvName.setText(appMain.loginInfo.name + "");
			}

		} else if (requestCode == 1) {

			switch (resultCode) {
			case Activity.RESULT_OK:// 照相完成点击确定
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
					Log.v("TestFile",
							"SD card is not avaiable/writeable right now.");
					return;
				}

				Bundle bundle = data.getExtras();
				Bitmap bitmap = (Bitmap) bundle.get("data");//
				Bitmap tempTmap = NetUtils.transImage(bitmap, 200, 200);
				if (tempTmap != null) {
					try {
						File file = new File(NetUtils.THUMB_PATH + "image.jpg");
						if (!file.exists()) {
							file.createNewFile();
						}
						BufferedOutputStream bos = new BufferedOutputStream(
								new FileOutputStream(file));
						tempTmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
						bos.flush();
						bos.close();
						getData(NetUtils.THUMB_PATH + "image.jpg");
						imgFace.setImageBitmap(tempTmap);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// 获取相机返回的数据，并转换为Bitmap图片格式
				// drawable = BitmapDrawable.createFromPath(fileName);
				// LocalFile.SaveImage(bitmap, Config.FACE_IMAGE,
				// Config.UserImageDir);

				// setDrawable(fileName);

				break;
			case Activity.RESULT_CANCELED:// 取消
				break;
			}
		} else if (requestCode == 2) {

			switch (resultCode) {
			case Activity.RESULT_OK: {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
				Bitmap tempTmap = NetUtils.transImage(bitmap, 200, 200);
				if (tempTmap != null) {
					getData(picturePath);

					// byte[] bit = Bitmap2Bytes(tempTmap);
					// Bitmap bitm = Bytes2Bimap(bit);

					imgFace.setImageBitmap(tempTmap);
				}
				// File srcfile = new File(picturePath);
				// File desFile = Utility.getFilePath(Config.UserImageDir,
				// Config.FACE_IMAGE);
				// LocalFile.fileChannelCopy(srcfile, desFile);
				// setDrawable(fileName);

			}
				break;
			case Activity.RESULT_CANCELED:// 取消
				break;
			}
		}
	}

	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * 获取数据
	 */
	void getData(final String path) {
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				ApiUtils.uploadImage(path, handler);
				// hander.sendMessage(Message.obtain(hander, INITE_CONTENT,
				// response));

				super.run();
			}
		}.start();
	}

	public String getFilePathByUri(Context con, Uri uri) {

		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = con.getContentResolver().query(uri, filePathColumn,
				null, null, null);
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);
		Log.w("cursor", picturePath);
		cursor.close();
		return picturePath;
	}

	/**
	 * 
	 * 图片选择监听
	 * 
	 */
	class SelectDialogListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_camera:
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//
				// // 调用android自带的照相机
				// intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new
				// File(
				// NetUtils.THUMB_PATH + "image.jpg")));
				startActivityForResult(intent, 1);
				break;
			case R.id.btn_photos:
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, 2);
				break;
			case R.id.btn_cancle:

				break;

			default:
				break;
			}
			selectDialog.dismiss();

		}

	}
}
