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
import com.cloudclass.view.Show;

public class AcRegist extends Activity implements OnClickListener {
	protected static final int INITE_CONTENT = 6;
	Button btnSubmit;
	AppMain appMain;
	EditText etUser, etConfirmPass, etPass, etPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_regist);
		appMain = (AppMain) getApplication();
		etUser = (EditText) findViewById(R.id.et_user);
		etPass = (EditText) findViewById(R.id.et_pass);
		String phone = getIntent().getStringExtra("phone");
		etConfirmPass = (EditText) findViewById(R.id.et_pass_confirm);
		etPhone = (EditText) findViewById(R.id.et_phone);
		etPhone.setText("" + phone);
		etPhone.setVisibility(View.GONE);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(this);
		findViewById(R.id.back_btn).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_submit:
			String user = etUser.getText().toString().trim();
			String confirmPassword = etConfirmPass.getText().toString().trim();
			String password = etPass.getText().toString().trim();
			String phone = etPhone.getText().toString().trim();
			if (checkInput(password, confirmPassword, user, phone)) {

				getData(user, confirmPassword, phone);
			}
			break;
		case R.id.back_btn:
			finish();
			break;
		}
	}

	boolean checkInput(String newPass, String confirmPass, String user,
			String phone) {
		if (TextUtils.isEmpty(phone)) {
			Show.toast(this, "请输入手机号码");
			return false;
		}
		if (TextUtils.isEmpty(user)) {
			Show.toast(this, "请输入用户名");
			return false;
		}
		if (TextUtils.isEmpty(newPass)) {
			Show.toast(this, "请输入密码");
			return false;
		}
		if (TextUtils.isEmpty(confirmPass)) {
			Show.toast(this, "请输入确认密码");
			return false;
		}
		if (!newPass.equals(confirmPass)) {
			Show.toast(this, "新密码与确认密码不一致");
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
						finish();
					} else {
						Show.toast(AcRegist.this, response.errormsg);

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
	void getData(final String code, final String password, final String mobile) {
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ReturnInfo response = ApiUtils.userRegis(code, mobile,
						password, hander);
				hander.sendMessage(Message.obtain(hander, INITE_CONTENT,
						response));

				super.run();
			}
		}.start();
	}
}
