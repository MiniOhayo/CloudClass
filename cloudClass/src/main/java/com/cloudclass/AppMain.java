package com.cloudclass;

import android.app.Application;
import android.os.Handler;
import android.os.Message;

import com.cloudclass.entity.LoginInfo;
import com.cloudclass.utils.ApiUtils;
import com.cloudclass.utils.ApplicationUtils;
import com.cloudclass.utils.CommenData;
import com.cloudclass.utils.Utils;
import com.cloudclass.utils.ViewUtils;
import com.cloudclass.view.Show;

public class AppMain extends Application {
	public LoginInfo loginInfo;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case ViewUtils.NET_ERROR:
				Show.toast(getApplicationContext(), "网络错误,请检查网络是否畅通!");
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
//		CrashHandler crashHandler = CrashHandler.getInstance();
//		crashHandler.init(getApplicationContext());
		CommenData.LOGIN_INFO_PATH = "//data//data//" + this.getPackageName()
				+ "//" + "userInfo.xml";
		loginInfo = Utils.readLoginInfo(CommenData.LOGIN_INFO_PATH);
		if (loginInfo != null&&!loginInfo.isThirdLogin) {
			if (ApplicationUtils.netState(this)) {
				new Thread() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						LoginInfo tInfo = ApiUtils.getUserDetail(loginInfo.id,
								handler);
						if (tInfo != null) {
							loginInfo.headimage = tInfo.headimage;
							loginInfo.mobile = tInfo.mobile;
							loginInfo.name = tInfo.name;
							Utils.writeLoginInfo(loginInfo,
									CommenData.LOGIN_INFO_PATH);

						}

						super.run();
					}
				}.start();
			} else
				handler.sendEmptyMessage(ViewUtils.NET_ERROR);
		}

		super.onCreate();
	}

	public LoginInfo getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}

}
