package com.cloudclass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudclass.entity.ReturnInfo;
import com.cloudclass.utils.ApiUtils;
import com.cloudclass.view.Show;

public class AcSendVerfifyMessage extends Activity implements OnClickListener,
		TextWatcher {

	private EditText etIdentifyNum, etPhone;
	private TextView tvIdentifyNotify;
	private ImageView ivClear;
	private Button btnSubmit, btnGetVerify;

	private String verifyRet, phoneRet;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.input_identify_num_page);
		findViewById(R.id.ll_back).setOnClickListener(this);
		tvIdentifyNotify = (TextView) findViewById(R.id.tv_identify_notify);
		String text = getString(R.string.smssdk_send_mobile_detail);
		tvIdentifyNotify.setText(Html.fromHtml(text));
		etIdentifyNum = (EditText) findViewById(R.id.et_put_identify);
		etPhone = (EditText) findViewById(R.id.et_put_phone);
		// etIdentifyNum.addTextChangedListener(this);
		etPhone.addTextChangedListener(this);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(this);
	//	btnSubmit.setEnabled(false);
		ivClear = (ImageView) findViewById(R.id.iv_clear);
		ivClear.setOnClickListener(this);
		btnGetVerify = (Button) findViewById(R.id.btn_getVerify);
		btnGetVerify.setOnClickListener(this);
		btnGetVerify.setEnabled(false);
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// 如果输入框木有，就隐藏delbtn
		if (s.length() > 0) {
			btnGetVerify.setEnabled(true);
			// ivClear.setVisibility(View.VISIBLE);

			btnGetVerify.setBackgroundResource(R.drawable.smssdk_btn_enable);

		} else {
			btnGetVerify.setEnabled(false);
			// ivClear.setVisibility(View.GONE);

			btnGetVerify.setBackgroundResource(R.drawable.smssdk_btn_disenable);

		}
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	public void afterTextChanged(Editable s) {
		// btnSounds.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_getVerify:
			final String phone = etPhone.getText().toString().trim();
			if (!TextUtils.isEmpty(phone)) {
				new Thread() {

					@Override
					public void run() {
						final ReturnInfo ret = ApiUtils.sendVerify(phone, null);

						btnSubmit.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (ret == null) {
									Show.toast(AcSendVerfifyMessage.this,
											"连接服务器失败");
								} else if (ret.issuccess) {
									verifyRet = ret.vericode;
									phoneRet = ret.mobile;
									Show.toast(AcSendVerfifyMessage.this,
											"发送成功，请注意查收短信!");
								} else {
									Show.toast(AcSendVerfifyMessage.this,
											ret.errormsg + "");
								}
							}
						});

					}
				}.start();
			} else {
				Show.toast(this, R.string.smssdk_write_mobile_phone);

			}
			break;
		case R.id.forget:
			break;
		case R.id.iv_clear:
			etIdentifyNum.getText().clear();
			break;
		case R.id.btn_submit:
			String etverfiy = etIdentifyNum.getText().toString().trim();
			if (etverfiy.equals(verifyRet)) {
				Show.toast(AcSendVerfifyMessage.this, "验证成功");
				Intent intent = new Intent();
				intent.putExtra("phone", phoneRet);
				setResult(RESULT_OK, intent);
				finish();
			} else {
				Show.toast(AcSendVerfifyMessage.this, "验证失败,请填写正确的验证码!");
			}
			break;
		default:
			break;
		}
	}
}
