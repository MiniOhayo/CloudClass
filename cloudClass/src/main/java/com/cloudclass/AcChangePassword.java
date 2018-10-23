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
import android.widget.ImageView;

import com.cloudclass.entity.ReturnInfo;
import com.cloudclass.fragment.FgMainMyLesson;
import com.cloudclass.utils.ApiUtils;
import com.cloudclass.utils.CommenData;
import com.cloudclass.utils.NetUtils;
import com.cloudclass.utils.Utils;
import com.cloudclass.view.Show;

public class AcChangePassword extends Activity implements OnClickListener {
	protected static final int INITE_CONTENT = 6;
	Button btnSubmit;
	AppMain appMain;
	EditText etNewPass, etOldPass, etConfirmPass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_change_password);
		appMain = (AppMain) getApplication();
		etNewPass = (EditText) findViewById(R.id.et_new_pass);
		etOldPass = (EditText) findViewById(R.id.et_old_pass);
		etConfirmPass = (EditText) findViewById(R.id.et_pass_confirm);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(this);
		findViewById(R.id.back_btn).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_submit:
			String newPassword = etNewPass.getText().toString().trim();
			String oldpassword = etOldPass.getText().toString().trim();
			String confirmPassword = etConfirmPass.getText().toString().trim();
			if (!oldpassword.equals(appMain.loginInfo.pass)) {
				Show.toast(this, "密码错误");
				return;
			}
			if (checkInput(newPassword, confirmPassword)) {

				getData(appMain.loginInfo.id, confirmPassword);
			}
			break;
		case R.id.back_btn:
			finish();
			break;
		}
	}

	boolean checkInput(String newPass, String confirmPass) {
		if (TextUtils.isEmpty(newPass)) {
			Show.toast(this, "请输入新密码");
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
						appMain.loginInfo.pass = etNewPass.getText().toString()
								.trim();
						Utils.writeLoginInfo(appMain.loginInfo,
								CommenData.LOGIN_INFO_PATH);
						setResult(AcPersonInfo.RENAME);
						finish();
					} else {
						Show.toast(AcChangePassword.this, response.errormsg);

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
	void getData(final String code, final String pass) {
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ReturnInfo response = ApiUtils.uploadUserDetail(code, null,
						null, pass, null, hander);
				hander.sendMessage(Message.obtain(hander, INITE_CONTENT,
						response));

				super.run();
			}
		}.start();
	}
}
