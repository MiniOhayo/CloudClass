package com.cloudclass;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.cloudclass.entity.ReturnInfo;
import com.cloudclass.utils.ApiUtils;
import com.cloudclass.utils.CommenData;
import com.cloudclass.utils.Utils;
import com.cloudclass.view.Show;

public class AcReName extends Activity implements OnClickListener {
	protected static final int INITE_CONTENT = 6;
	Button btnSubmit;
	AppMain appMain;
	EditText etPass, etName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_rename);
		appMain = (AppMain) getApplication();
		etPass = (EditText) findViewById(R.id.et_pass);
		etName = (EditText) findViewById(R.id.et_user);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(this);
		findViewById(R.id.back_btn).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_submit:
			String password = etPass.getText().toString().trim();
			String name = etName.getText().toString().trim();

			if (checkInput(name)) {
				if (!password.equals(appMain.loginInfo.pass)) {
					Show.toast(this, "密码错误");
					return;
				}
				getData(appMain.loginInfo.id, name);
			}
			break;
		case R.id.back_btn:
			finish();
			break;
		}
	}

	boolean checkInput(String name) {
		if (TextUtils.isEmpty(name)) {
			Show.toast(this, "请输入新昵称");
			return false;
		}
		return true;

	}

	public Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case INITE_CONTENT:
				ReturnInfo response = (ReturnInfo) msg.obj;
				if (response != null) {
					if (response.issuccess) {
						appMain.loginInfo.name = etName.getText().toString()
								.trim();
						Utils.writeLoginInfo(appMain.loginInfo,
								CommenData.LOGIN_INFO_PATH);
						setResult(AcPersonInfo.RENAME);
						finish();
					} else {
						Show.toast(AcReName.this, response.errormsg);

					}

				}

				break;
			}
			// progressDialog.dismiss();
		}
	};

	/**
	 * 获取数据
	 */
	void getData(final String code, final String name) {
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ReturnInfo response = ApiUtils.uploadUserDetail(code, null,
						name, null, null, hander);
				hander.sendMessage(Message.obtain(hander, INITE_CONTENT,
						response));

				super.run();
			}
		}.start();
	}
}
