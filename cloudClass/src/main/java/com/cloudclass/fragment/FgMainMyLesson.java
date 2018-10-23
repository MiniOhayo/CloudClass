package com.cloudclass.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudclass.AcDownLoad;
import com.cloudclass.AcFavorite;
import com.cloudclass.AcFeedback;
import com.cloudclass.AcHistory;
import com.cloudclass.AcLogin;
import com.cloudclass.AcMyLesson;
import com.cloudclass.AcPersonInfo;
import com.cloudclass.AppMain;
import com.cloudclass.R;
import com.cloudclass.entity.LoginInfo;
import com.cloudclass.utils.ApiUtils;
import com.cloudclass.utils.CommenData;
import com.cloudclass.utils.NetUtils;
import com.cloudclass.utils.Utils;

public class FgMainMyLesson extends Fragment implements OnClickListener {
	public static final int SAVE_OK = 2;
	public static final int SAVE_ERROE = 3;
	private Fragment currentFragment;
	AppMain appMain;
	TextView titleName;
	boolean isModify;

	private FragmentActivity con;
	private ProgressDialog progressDialog;
	private View view, llLogin, llUnLogin;
	private ImageView imgFace;
	private TextView tvName;
	public static boolean freshInfo = false;
	// public ArrayList<Fragment> fragments = new ArrayList<Fragment>();;
	public Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			}
			progressDialog.dismiss();
		}
	};
	private int requestCode = 0x111;
	public static int LOGOUT = 0x222;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		con = this.getActivity();
		appMain = (AppMain) con.getApplication();
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		if (view == null) {
			initeView(inflater, container);
		}

		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);

		}
		return view;

	}

	private void initeView(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.fg_main_my_lesson, container, false);
		// view.findViewById(R.id.menu_btn).setOnClickListener(this);
		llLogin = view.findViewById(R.id.ll_login);
		llUnLogin = view.findViewById(R.id.ll_unlogin);
		imgFace = (ImageView) view.findViewById(R.id.img_face);
		tvName = (TextView) view.findViewById(R.id.tv_name);

		view.findViewById(R.id.ll_comments).setOnClickListener(this);

		titleName = (TextView) view.findViewById(R.id.title_name);
		view.findViewById(R.id.btnLogin).setOnClickListener(this);

		view.findViewById(R.id.btn_down).setOnClickListener(this);
		view.findViewById(R.id.btn_history).setOnClickListener(this);
		view.findViewById(R.id.btn_my_lesson).setOnClickListener(this);
		view.findViewById(R.id.btn_favorite).setOnClickListener(this);

		view.findViewById(R.id.ll_feed_back).setOnClickListener(this);
		// fragmentTransaction(alarmEvent);

	}

	public void fragmentTransaction(Fragment fragment) {

		currentFragment = fragment;
		FragmentTransaction transaction = ((FragmentActivity) con)
				.getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.rgImportant_content, fragment);
		transaction.commit();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (appMain.loginInfo != null) {
			llLogin.setVisibility(View.VISIBLE);
			llUnLogin.setVisibility(View.GONE);
			NetUtils.loadImage(appMain.loginInfo.headimage,
					NetUtils.TiNE_IMAGE, imgFace);
			tvName.setText(appMain.loginInfo.name);
		} else {
			llLogin.setVisibility(View.GONE);
			llUnLogin.setVisibility(View.VISIBLE);
		}
		Log.w("FgMainMyLesson", "FgMainMyLesson onResume");
		if (freshInfo) {
			new Thread() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					LoginInfo tInfo = ApiUtils.getUserDetail(
							appMain.loginInfo.id, null);
					if (tInfo != null) {
						appMain.loginInfo = tInfo;
					}

					Utils.writeLoginInfo(appMain.loginInfo,
							CommenData.LOGIN_INFO_PATH);
					titleName.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (!TextUtils.isEmpty(appMain.loginInfo.headimage)) {
								NetUtils.loadImage(appMain.loginInfo.headimage,
										NetUtils.TiNE_IMAGE, imgFace);
							} else
								imgFace.setBackgroundResource(R.drawable.comments);

						}
					});
					super.run();
				}
			}.start();
		}

		super.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (this.requestCode == requestCode) {
			if (resultCode == Activity.RESULT_OK) {
				LoginInfo loginInfo = (LoginInfo) data
						.getSerializableExtra("loginInfo");
				if (loginInfo != null) {

					llLogin.setVisibility(View.VISIBLE);
					llUnLogin.setVisibility(View.GONE);
					if (!TextUtils.isEmpty(appMain.loginInfo.headimage)) {
						NetUtils.loadImage(appMain.loginInfo.headimage,
								NetUtils.TiNE_IMAGE, imgFace);
					} else
						imgFace.setBackgroundResource(R.drawable.comments);
					// NetUtils.loadImage(appMain.loginInfo.headimage,
					// NetUtils.TiNE_IMAGE, imgFace);
					tvName.setText(loginInfo.name);
				}
			} else if (resultCode == LOGOUT) {
				llLogin.setVisibility(View.GONE);
				llUnLogin.setVisibility(View.VISIBLE);
			}

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnLogin:
			startActivityForResult(new Intent(con, AcLogin.class), requestCode);
			break;
		case R.id.menu_btn:

			break;
		case R.id.ll_comments:
			// startActivity(new Intent(con, AcPersonInfo.class));
			startActivityForResult(new Intent(con, AcPersonInfo.class),
					requestCode);
			break;
		case R.id.btn_down:
			startActivity(new Intent(con, AcDownLoad.class));
			break;
		case R.id.btn_history:
			startActivity(new Intent(con, AcHistory.class));
			break;
		case R.id.btn_my_lesson:
			startActivity(new Intent(con, AcMyLesson.class));
			break;
		case R.id.btn_favorite:
			startActivity(new Intent(con, AcFavorite.class));
			break;
		case R.id.ll_feed_back:
			startActivity(new Intent(con, AcFeedback.class));
			break;
		}
	}

}
