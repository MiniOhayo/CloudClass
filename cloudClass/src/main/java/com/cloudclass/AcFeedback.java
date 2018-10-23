package com.cloudclass;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudclass.entity.ReturnInfo;
import com.cloudclass.entity.TutorInfo;
import com.cloudclass.utils.ApiUtils;
import com.cloudclass.utils.NetUtils;
import com.cloudclass.view.Show;

public class AcFeedback extends Activity implements OnClickListener {
	protected static final int INITE_CONTENT = 6;
	protected static final int SUBMIT = 2;
	AppMain appMain;
	EditText etAdvice, etContactInfo;
	private ImageView img;

	TextView name, info;
	public Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case SUBMIT:
				ReturnInfo responses = (ReturnInfo) msg.obj;
				if (responses != null) {
					if (responses.issuccess) {
						Show.toast(AcFeedback.this, "提交成功！感谢您的支持");
						finish();
					}
				}

				break;
			case INITE_CONTENT:
				TutorInfo response = (TutorInfo) msg.obj;
				if (response != null) {
					NetUtils.loadImage(response.headimage, NetUtils.TiNE_IMAGE,
							img);
					name.setText("我是产品部："+response.name);
					info.setText(response.detail);

				} else
					Show.toast(AcFeedback.this, "");

				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_feedback);
		appMain = (AppMain) getApplication();
		etAdvice = (EditText) findViewById(R.id.et_advice);
		etContactInfo = (EditText) findViewById(R.id.etContactInfo);
		findViewById(R.id.btn_submit).setOnClickListener(this);
		findViewById(R.id.back_btn).setOnClickListener(this);
		img = (ImageView) findViewById(R.id.img_face);
		name = (TextView) findViewById(R.id.show_admin);
		info = (TextView) findViewById(R.id.show_admin_job);
		getData();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_submit:
			String contactinfo = etContactInfo.getText().toString().trim();
			String content = etAdvice.getText().toString().trim();
			if (checkInput(contactinfo, content)) {
				upData(appMain.loginInfo.id, contactinfo, content);
			}

			break;
		case R.id.back_btn:
			finish();
			break;
		}
	}

	boolean checkInput(String contactinfo, String content) {
		if (TextUtils.isEmpty(contactinfo)) {
			Show.toast(this, "请输入您的联系方式");
			return false;
		}
		if (TextUtils.isEmpty(content)) {
			Show.toast(this, "把你使用过程中的感受，意见告诉我们把！");
			return false;
		}
		return true;

	}

	/**
	 * 获取数据
	 */
	void getData() {
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				TutorInfo response = ApiUtils.getFeedbackContent(hander);
				hander.sendMessage(Message.obtain(hander, INITE_CONTENT,
						response));

				super.run();
			}
		}.start();
	}

	/**
	 * 获取数据
	 */
	void upData(final String id, final String contactinfo, final String content) {
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ReturnInfo response = ApiUtils.uploadFeedbackByUser(id,
						contactinfo, content, hander);
				hander.sendMessage(Message.obtain(hander, SUBMIT, response));

				super.run();
			}
		}.start();
	}
}
