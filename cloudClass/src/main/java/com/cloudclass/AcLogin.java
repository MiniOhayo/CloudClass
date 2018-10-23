package com.cloudclass;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tpl.UserInfo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.CommonDialog;
import cn.smssdk.gui.RegisterPage;

import com.cloudclass.entity.LoginInfo;
import com.cloudclass.entity.ReturnInfo;
import com.cloudclass.utils.ApiUtils;
import com.cloudclass.utils.CommenData;
import com.cloudclass.utils.Utils;
import com.cloudclass.view.Show;

public class AcLogin extends Activity implements OnClickListener, Callback,
		PlatformActionListener {
	// 填写从短信SDK应用后台注册得到的APPKEY
	private static String APPKEY = "8829c044182c";// 81d10c704832 8829c044182c

	// 填写从短信SDK应用后台注册得到的APPSECRET
	private static String APPSECRET = "3e6e0c2d6e15bababee1c2046ffd42c1";// d8b6f13906c9f4cec1006adec5453755
																			// 62021fa0ce12f35bdad379e3ef77a13b
	private static final int MSG_AUTH_CANCEL = 2;
	private static final int MSG_AUTH_ERROR = 3;
	private static final int MSG_AUTH_COMPLETE = 4;
	protected static final int INITE_CONTENT = 6;
	AppMain appMain;
	EditText etUser, etPass;
	private Dialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_login);
		appMain = (AppMain) getApplication();
		etUser = (EditText) findViewById(R.id.et_user);
		etPass = (EditText) findViewById(R.id.et_pass);
		findViewById(R.id.btn_login).setOnClickListener(this);
		findViewById(R.id.btnRegist).setOnClickListener(this);
		findViewById(R.id.forget).setOnClickListener(this);
		findViewById(R.id.back_btn).setOnClickListener(this);
		findViewById(R.id.tvQq).setOnClickListener(this);
		findViewById(R.id.tvWeibo).setOnClickListener(this);
		findViewById(R.id.tvWeixin).setOnClickListener(this);
		ShareSDK.initSDK(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.forget:
			startActivityForResult(
					new Intent(this, AcSendVerfifyMessage.class), 3);
//			initSDK();
//			// 打开注册页面
//			RegisterPage registerPage1 = new RegisterPage();
//			registerPage1.setRegisterCallback(new EventHandler() {
//				public void afterEvent(int event, int result, Object data) {
//					// 解析注册结果
//					if (result == SMSSDK.RESULT_COMPLETE) {
//						@SuppressWarnings("unchecked")
//						HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
//						String country = (String) phoneMap.get("country");
//
//						// Toast.makeText(AcLogin.this,
//						// R.string.smssdk_user_info_submited,
//						// Toast.LENGTH_SHORT).show();
//					}
//				}
//			});
//			registerPage1.show(this);
			break;
		case R.id.btnRegist:
			// initSDK();
			// // 打开注册页面
			// RegisterPage registerPage = new RegisterPage();
			// registerPage.setRegisterCallback(new EventHandler() {
			// public void afterEvent(int event, int result, Object data) {
			// // 解析注册结果
			// if (result == SMSSDK.RESULT_COMPLETE) {
			// @SuppressWarnings("unchecked")
			// HashMap<String, Object> phoneMap = (HashMap<String, Object>)
			// data;
			// String country = (String) phoneMap.get("country");
			// String phone = (String) phoneMap.get("phone");
			// startActivity(new Intent(AcLogin.this, AcRegist.class)
			// .putExtra("phone", phone));
			// // Toast.makeText(AcLogin.this,
			// // R.string.smssdk_user_info_submited,
			// // Toast.LENGTH_SHORT).show();
			// }
			// }
			// });
			// registerPage.show(this);

			startActivityForResult(
					new Intent(this, AcSendVerfifyMessage.class), 4);
			break;
		case R.id.back_btn:
			finish();
			break;
		case R.id.btn_login:
			String code = etUser.getText().toString().trim();
			String password = etPass.getText().toString().trim();
			if (checkInput(code, password)) {
				getData(code, password);
			}

			break;
		case R.id.tvQq:
			Platform qq = ShareSDK.getPlatform(QQ.NAME);
			authorize(qq);
			// AcLogin.showDemo();
			break;
		case R.id.tvWeibo:
			Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
			authorize(sina);
			// AcLogin.showDemo();
			break;
		case R.id.tvWeixin:
			Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
			authorize(wechat);
			// AcLogin.showDemo();
			break;
		default:
			break;
		}
	}

	private void authorize(Platform plat) {
		if (plat == null) {
			return;
		}

		plat.setPlatformActionListener(this);
		// 关闭SSO授权
		plat.SSOSetting(true);
		plat.showUser(null);
	}

	@Override
	public void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if (arg1 == Activity.RESULT_OK) {
			if (arg0 == 4) {

				String phone = arg2.getStringExtra("phone");
				startActivity(new Intent(AcLogin.this, AcRegist.class)
						.putExtra("phone", phone));
			} else if (arg0 == 3) {
				final String phone = arg2.getStringExtra("phone");
				final EditText TextView = new EditText(AcLogin.this);
				new AlertDialog.Builder(AcLogin.this)
						.setTitle("请输入新密码")
						.setView(TextView)
						.setPositiveButton("重置密码",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										new Thread() {

											@Override
											public void run() {
												// TODO Auto-generated
												// method stub
												final ReturnInfo returnInfo = ApiUtils
														.forgetpwd(
																phone,
																TextView.getText()
																		.toString()
																		.trim(),
																null);
												etPass.post(new Runnable() {

													@Override
													public void run() {
														// TODO
														// Auto-generated
														// method stub
														if (returnInfo == null) {
															Show.toast(
																	AcLogin.this,
																	"重置密码失败，请检查网络！");
														} else {
															if (returnInfo.issuccess) {
																Show.toast(
																		AcLogin.this,
																		"重置密码成功");
															} else
																Show.toast(
																		AcLogin.this,
																		""
																				+ returnInfo.errormsg);
														}
													}
												});

											}

										}.start();
										Log.d("TextView", "TextView");
									}
								}).setNegativeButton("取消", null).show();
			}
		}
		super.onActivityResult(arg0, arg1, arg2);
	}

	class LoginRunable implements Runnable {
		UserInfo userInfo;

		public LoginRunable(UserInfo userInfo) {
			this.userInfo = userInfo;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.w("UserInfo", "UserInfo:" + userInfo.toString());
			// userInfo.setUserName("yin");
			LoginInfo response = null;
			try {
				response = ApiUtils.otherUserLogin(userInfo.getPlatName(),
						userInfo.getUserNote(), userInfo.getUserName(), null);
				if (response.issuccess) {
					response.headimage = userInfo.getUserIcon();
					// ApiUtils.uploadUserDetail(response.id,
					// response.headimage,
					// userInfo.getUserName(), "", "", null);
					AppMain appMain = (AppMain) getApplication();
					response.isThirdLogin = true;
					appMain.setLoginInfo(response);
					Utils.writeLoginInfo(response, CommenData.LOGIN_INFO_PATH);
					Intent Intent = new Intent();
					Intent.putExtra("loginInfo", response);
					setResult(Activity.RESULT_OK, Intent);
					finish();
					hander.sendMessage(Message.obtain(hander, 12));
				} else {
					hander.sendMessage(Message.obtain(hander, 13,
							response.errormsg));

				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	boolean checkInput(String code, String password) {
		if (TextUtils.isEmpty(code)) {
			Show.toast(this, "请输入用户名");
			return false;
		}
		if (TextUtils.isEmpty(password)) {
			Show.toast(this, "请输入密码");
			return false;
		}
		return true;

	}

	public Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case MSG_AUTH_CANCEL: {
				// 取消授权
				Toast.makeText(AcLogin.this, R.string.auth_cancel,
						Toast.LENGTH_SHORT).show();
			}
				break;
			case MSG_AUTH_ERROR: {
				// 授权失败
				Toast.makeText(AcLogin.this, R.string.auth_error,
						Toast.LENGTH_SHORT).show();
			}
				break;
			case MSG_AUTH_COMPLETE: {
				// 授权成功

				Object[] objs = (Object[]) msg.obj;
				String plat = (String) objs[0];
				Platform platform = ShareSDK.getPlatform(plat);
				UserInfo userInfo = new UserInfo();
				if (platform != null) {
					String gender = platform.getDb().getUserGender();
					if (gender.equals("m")) {
						userInfo.setUserGender(UserInfo.Gender.BOY);
						gender = getString(R.string.tpl_boy);
					} else {
						userInfo.setUserGender(UserInfo.Gender.GIRL);
						gender = getString(R.string.tpl_girl);
					}
					userInfo.setPlatName(platform.getDb().getPlatformNname());
					userInfo.setUserIcon(platform.getDb().getUserIcon());
					userInfo.setUserName(platform.getDb().getUserName());
					userInfo.setUserNote(platform.getDb().getUserId());
					userInfo.setUserIcon(platform.getDb().getUserIcon());
					Log.d("userInfo.toString()",
							"userInfo:" + userInfo.toString());
					new Thread(new LoginRunable(userInfo)).start();
				}

			}
				break;
			case 13:
				Show.toast(AcLogin.this, "" + msg.obj.toString());
				break;
			case 12:

				break;
			case INITE_CONTENT:
				LoginInfo response = (LoginInfo) msg.obj;
				if (response != null) {
					if (response.issuccess) {

						response.pass = etPass.getText().toString().trim();
						appMain.setLoginInfo(response);
						Utils.writeLoginInfo(response,
								CommenData.LOGIN_INFO_PATH);
						Intent Intent = new Intent();
						Intent.putExtra("loginInfo", response);
						setResult(Activity.RESULT_OK, Intent);
						finish();
					} else {
						Show.toast(AcLogin.this, response.errormsg);

					}

				}

				break;
			}
			// progressDialog.dismiss();
		}
	};

	private boolean ready;

	/**
	 * 获取数据
	 */
	void getData(final String code, final String password) {
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				LoginInfo response = ApiUtils.userLogin(code, password, hander);
				hander.sendMessage(Message.obtain(hander, INITE_CONTENT,
						response));

				super.run();
			}
		}.start();
	}

	private void initSDK() {
		// 初始化短信SDK
		SMSSDK.initSDK(this, APPKEY, APPSECRET);
		final Handler handler = new Handler(this);
		EventHandler eventHandler = new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};
		// 注册回调监听接口
		SMSSDK.registerEventHandler(eventHandler);
		ready = true;
		// 获取新好友个数
		// showDialog();
		// SMSSDK.getNewFriendsCount();

	}

	// 弹出加载框
	private void showDialog() {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
		pd = CommonDialog.ProgressDialog(this);
		pd.show();
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}

		int event = msg.arg1;
		int result = msg.arg2;
		Object data = msg.obj;
		if (event == SMSSDK.EVENT_SUBMIT_USER_INFO) {
			// 短信注册成功后，返回MainActivity,然后提示新好友
			if (result == SMSSDK.RESULT_COMPLETE) {
				Toast.makeText(this, R.string.smssdk_user_info_submited,
						Toast.LENGTH_SHORT).show();
			} else {
				((Throwable) data).printStackTrace();
			}
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		if (ready) {
			// 销毁回调监听接口
			SMSSDK.unregisterAllEventHandler();
		}
		super.onDestroy();
	}

	@Override
	public void onCancel(Platform platform, int action) {
		if (action == Platform.ACTION_USER_INFOR) {
			hander.sendEmptyMessage(MSG_AUTH_CANCEL);
		}
	}

	@Override
	public void onComplete(Platform platform, int action,
			HashMap<String, Object> res) {
		if (action == Platform.ACTION_USER_INFOR) {
			Message msg = new Message();
			msg.what = MSG_AUTH_COMPLETE;
			msg.obj = new Object[] { platform.getName(), res };
			hander.sendMessage(msg);
		}
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(Platform platform, int action, Throwable t) {
		if (action == Platform.ACTION_USER_INFOR) {
			hander.sendEmptyMessage(MSG_AUTH_ERROR);
		}
		t.printStackTrace();
	}
}
