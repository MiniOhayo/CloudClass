package com.cloudclass;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cloudclass.entity.LessonDetailInfo;
import com.cloudclass.entity.LessonInfo;
import com.cloudclass.entity.Player;
import com.cloudclass.entity.ReturnInfo;
import com.cloudclass.entity.TagsInfo;
import com.cloudclass.entity.UpLoadInfo;
import com.cloudclass.entity.UpLoadRetInfo;
import com.cloudclass.utils.ApiUtils;
import com.cloudclass.utils.DownLoadFile;
import com.cloudclass.utils.MD5Util;
import com.cloudclass.utils.MyAnimations;
import com.cloudclass.utils.NetUtils;
import com.cloudclass.utils.ViewUtils;
import com.cloudclass.view.NumberCircleProgressBar;
import com.cloudclass.view.Show;
import com.example.provinceselector.SortModel;

@SuppressLint("HandlerLeak")
public class AcLessonDetail extends Activity implements OnClickListener,
		Callback {
	protected static final int INITE_CONTENT = 6;
	protected static final int FAVORITE_S = 7;
	TextView tvTitle, tvShowName, tvProgress;
	AppMain appMain;
	private TextView btnBack, btnDown, btnFavorite, btnShared, btnComments;

	private ImageView btnFull;
	View llContent, rlBottom, llCommets, llTeacher;
	FrameLayout flPlay;
	private boolean isLand;
	NumberCircleProgressBar bnp;
	float height = 0;
	private SurfaceView mPreview;
	private SurfaceHolder holder;

	private Player player;
	private SeekBar seekBar;
	private ImageView btn_play;
	private ImageView btnPlay;
	private TextView tvTime;
	String id;
	LessonDetailInfo response;

	DownLoadFile downLoadFile;
	LessonInfo lessonInfo;
	View llTitle, rlMenu;
	private ImageView imgFace;
	private TextView tvsShowTutor;
	private TextView tvTutorJob;
	private TextView tvCommentsNum;
	private TextView tvPlayTimes;
	private TextView tv_dedails;
	private boolean isFinish = false; //
	private boolean isClickDown = false; // 点击了下载
	boolean isMp3;
	EditText etComment;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case FAVORITE_S:
				Show.toast(AcLessonDetail.this, "收藏成功");
				break;
			case 0:
				// 下载完成 播放

				if (msg.arg1 == 100) {
					isFinish = true;
					bnp.setProgress(msg.arg1);
					player.isMp3 = isMp3;
					player.playUrl(msg.obj.toString());
					handler.postDelayed(new HideViewRun(), 5000);
				}
				break;
			case 1:
				// url 播放
				// bnp.setProgress(0);
				player.isMp3 = isMp3;
				player.playUrl(response.downloadurl);
				isFinish = true;
				handler.postDelayed(new HideViewRun(), 5000);
				break;
			case 2:
				// 下载进度
				// bnp.setProgress(msg.arg1);
				break;
			case INITE_CONTENT:
				response = (LessonDetailInfo) msg.obj;
				if (response != null) {
					tv_dedails.setText(response.detail + "");

					if (!TextUtils.isEmpty(response.downloadurl)) {
						String url = response.downloadurl;
						isMp3 = ".mp3".equals(url.substring(
								url.lastIndexOf('.'), url.length())
								.toLowerCase());
						if (isMp3) {
							mPreview.setBackgroundResource(R.drawable.player_bg);
						}
						downLoadFile = new DownLoadFile(response.id,
								response.downloadurl, handler);
						downLoadFile.start();

					} else {
						Show.toast(AcLessonDetail.this, "音视频链接失效");
					}

					NetUtils.loadImage(response.teacher.headimage,
							NetUtils.TiNE_IMAGE, imgFace);
					tvsShowTutor.setText("" + response.teacher.name);
					tvTutorJob.setText("" + response.teacher.job);
					tvCommentsNum.setText("(" + response.commentscount + ")");
				} else {
					Show.toast(AcLessonDetail.this, "获取音视频信息失败");
				}
				break;
			}
		}
	};
	private TextView tvsVideoTime;
	private int progress;
	private com.cloudclass.AcLessonDetail.UpdateReceiver upReceiver;
	private ProgressBar progressBar;
	private boolean isMyLesson = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.ac_lesson_detail);
		appMain = (AppMain) getApplication();
		flPlay = (FrameLayout) findViewById(R.id.fl_play);
		llContent = findViewById(R.id.ll_content);
		rlBottom = findViewById(R.id.rl_bottom);
		llCommets = findViewById(R.id.ll_comments);
		llTeacher = findViewById(R.id.ll_teacher);
		llTitle = findViewById(R.id.ll_title);
		rlMenu = findViewById(R.id.rl_menu);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvProgress = (TextView) findViewById(R.id.detail_hide);
		tvShowName = (TextView) findViewById(R.id.show_lesson_name);

		imgFace = (ImageView) findViewById(R.id.img_face);
		tvsVideoTime = (TextView) findViewById(R.id.show_lesson_time);
		tvsShowTutor = (TextView) findViewById(R.id.show_tutor_title);
		tvTutorJob = (TextView) findViewById(R.id.show_tutor_job);
		tvCommentsNum = (TextView) findViewById(R.id.comments_num);
		tvPlayTimes = (TextView) findViewById(R.id.tvplayTimes);
		tv_dedails = (TextView) findViewById(R.id.tv_dedails);
		btnBack = (TextView) findViewById(R.id.btn_back);
		btnDown = (TextView) findViewById(R.id.btn_down_load);
		btnFavorite = (TextView) findViewById(R.id.btn_favorite);
		btnShared = (TextView) findViewById(R.id.btn_shared);
		btnComments = (TextView) findViewById(R.id.btn_comment);
		btnFull = (ImageView) findViewById(R.id.full_screen);
		etComment = (EditText) findViewById(R.id.et_comment);
		findViewById(R.id.btn_cancle).setOnClickListener(this);
		findViewById(R.id.btn_finish).setOnClickListener(this);
		seekBar = (SeekBar) findViewById(R.id.sbProgress);
		llCommets.setOnClickListener(this);
		llTeacher.setOnClickListener(this);
		btnFull.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnDown.setOnClickListener(this);
		btnFavorite.setOnClickListener(this);
		btnShared.setOnClickListener(this);
		btnComments.setOnClickListener(this);
		// setTheNumberProgressBar();
		bnp = (NumberCircleProgressBar) findViewById(R.id.numbercircleprogress_bar);
		height = getResources().getDimension(R.dimen.play_height);
		initePlayView();
		initeData();
		reCulatePlayLayout(
				flPlay,
				getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
		upReceiver = new UpdateReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.MY_RECEIVER");
		registerReceiver(upReceiver, filter);

	}

	void initeData() {
		lessonInfo = (LessonInfo) getIntent().getSerializableExtra("info");
		// lessonInfo.
		tvTitle.setText("" + lessonInfo.name);
		tvShowName.setText("" + lessonInfo.name);

		tvsShowTutor.setText("" + lessonInfo.teacher.name);
		tvTutorJob.setText("" + lessonInfo.teacher.job);

		tvPlayTimes.append("" + lessonInfo.playtimes);
		getData(String.valueOf(lessonInfo.id));
	}

	@SuppressWarnings("deprecation")
	void initePlayView() {
		progressBar = (ProgressBar) findViewById(R.id.progress);
		mPreview = (SurfaceView) findViewById(R.id.surface);
		btn_play = (ImageView) findViewById(R.id.btn_play);
		btnPlay = (ImageView) findViewById(R.id.play);
		tvTime = (TextView) findViewById(R.id.tvProgress);
		btn_play.setOnClickListener(this);
		btnPlay.setOnClickListener(this);
		mPreview.setOnClickListener(this);
		holder = mPreview.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// Surface类型
		holder.addCallback(this);
		player = new Player(this, seekBar, holder);
		player.setBtn_play(btn_play);
		player.setBtnPlay(btnPlay);
		player.setTvTime(tvTime);
		player.setBnp(bnp);
		player.setTvsVideoTime(tvsVideoTime);
		player.setProgressBar(progressBar);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.full_screen:
			if (!isLand) {
				btnBack.postDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
					}
				}, 2000);
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			} else {
				btnBack.postDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
					}
				}, 2000);
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
			break;
		case R.id.ll_teacher:
			if (response != null) {
				if (response.teacher != null) {
					SortModel sortModel = new SortModel(response.teacher.id,
							response.teacher.name, "", response.teacher.job,
							response.teacher.headimage);
					startActivity(new Intent(this, AcTutorDetail.class)
							.putExtra("info", sortModel));
				}
			}
			break;
		case R.id.ll_comments:
			if (response != null) {
				startActivity(new Intent(this, AcComments.class).putExtra("id",
						String.valueOf(response.id)));
			}

			break;

		case R.id.btn_down_load:
			if (appMain.loginInfo != null) {
				isClickDown = true;
				new Thread() {

					@Override
					public void run() {
						UpLoadRetInfo upLoadRetInfo = new UpLoadRetInfo();
						upLoadRetInfo.data.add(new UpLoadInfo(
								appMain.loginInfo.id, String
										.valueOf(response.id), false));
						ApiUtils.uploadDownloadRecordsByUser(upLoadRetInfo,
								handler);
						super.run();
					}
				}.start();
				if (isClickDown && response != null) { // 如果没下载完成
					// 而且点击了下载按钮
					if (!TextUtils.isEmpty(response.downloadurl)) {
						Show.toast(AcLessonDetail.this, "后台下载中...");
						startService(new Intent(this, DownLoadService.class)
								.putExtra("info", response)); // 启动下载服务
					} else {
						Show.toast(AcLessonDetail.this, "音视频链接失效");
					}

					// 后台下载
					// 后台下载
				} else {
					Show.toast(AcLessonDetail.this, "已经下载过了");
				}
			} else
				startActivity(new Intent(this, AcLogin.class));

			break;
		case R.id.btn_favorite:
			if (appMain.loginInfo != null) {
				new Thread() {

					@Override
					public void run() {
						UpLoadRetInfo upLoadRetInfo = new UpLoadRetInfo();
						upLoadRetInfo.data.add(new UpLoadInfo(
								appMain.loginInfo.id, String
										.valueOf(response.id), false));
						ReturnInfo returnInfo = ApiUtils
								.uploadFavoriteRecordsByUser(upLoadRetInfo,
										handler);
						if (returnInfo != null && returnInfo.issuccess) {
							handler.sendEmptyMessage(FAVORITE_S);

						}
						super.run();
					}
				}.start();
			} else
				startActivity(new Intent(this, AcLogin.class));
			break;
		case R.id.btn_shared:
			if (response != null) {
				String url="http://139.129.14.75:8080/mediaplayer/"+lessonInfo.id;
				Show.showShare(this, lessonInfo.name, "恋爱秘籍,就看恋爱公开课！", " ",
						lessonInfo.image, url, " ");
			}
			break;
		case R.id.btn_cancle:
			InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(etComment.getWindowToken(), 0);
			findViewById(R.id.rl_comment).setVisibility(View.GONE);
			break;
		case R.id.btn_finish:
			InputMethodManager imm1 = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
			imm1.hideSoftInputFromWindow(etComment.getWindowToken(), 0);
			findViewById(R.id.rl_comment).setVisibility(View.GONE);
			final String text = etComment.getText().toString().trim();
			if (TextUtils.isEmpty(text)) {
				Show.toast(AcLessonDetail.this, "说点什么吧！");
				return;
			}
			new Thread() {

				@Override
				public void run() {
					UpLoadRetInfo upLoadRetInfo = new UpLoadRetInfo();
					upLoadRetInfo.data.add(new UpLoadInfo(appMain.loginInfo.id,
							String.valueOf(response.id), text));
					ApiUtils.uploadUserComments(upLoadRetInfo, handler);
					super.run();
				}
			}.start();
			break;
		case R.id.btn_comment:
			if (appMain.loginInfo != null) {
				findViewById(R.id.rl_comment).setVisibility(View.VISIBLE);

			} else
				startActivity(new Intent(this, AcLogin.class));
			break;
		case R.id.btn_play:
			player.play();
			break;
		case R.id.play:
			if (!player.isPlaying()) {
				player.play();
			}else{
				player.pause();
			}

			break;
		case R.id.surface:
			if (llTitle.getVisibility() == View.GONE) {
				showView();
			} else
				player.pause();
			break;
		default:
			break;
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			isLand = true;
			reCulatePlayLayout(flPlay, true);
		} else {
			isLand = false;
			reCulatePlayLayout(flPlay, false);
		}
		super.onConfigurationChanged(newConfig);
	}

	void reCulatePlayLayout(FrameLayout view, boolean isLand) {
		LayoutParams p = (LayoutParams) view.getLayoutParams();
		if (isLand) {
			llContent.setVisibility(View.GONE);
			rlBottom.setVisibility(View.GONE);
			p.height = ViewUtils.getScreenHeight(this);
		} else {
			llContent.setVisibility(View.VISIBLE);
			rlBottom.setVisibility(View.VISIBLE);
			int width = ViewUtils.getScreenWidth(this);
			p.height = (int) 3.0 * width / 4;
			Log.d("reCulatePlayLayout", "reCulatePlayLayout:" + height);
		}
		view.setLayoutParams(p);
	}

	/**
	 * 获取数据
	 */
	void getData(final String id) {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LessonDetailInfo response = ApiUtils.getDetailByCourse(id,
						handler);
				handler.sendMessage(Message.obtain(handler, INITE_CONTENT,
						response));
				super.run();
			}
		}.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (isLand) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.w("surfaceDestroyed", "surfaceCreated");
		if (isFinish) {

			player.playUrl(response.downloadurl);
		}
		// holder.lockCanvas();

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		Log.w("surfaceDestroyed", "surfaceChanged");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.w("surfaceDestroyed", "surfaceDestroyed");

		if (downLoadFile != null) {
			if (!downLoadFile.isStops()) {
				downLoadFile.stops(true);
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		// player.play();

		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		progress = player.getPro();
		player.pause();

		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		unregisterReceiver(upReceiver);
		if (response != null && appMain.loginInfo != null) {// 上传播放记录
			final String progress = player.getProgress();
			if (!TextUtils.isEmpty(progress)) {
				new Thread() {
					@Override
					public void run() {
						UpLoadRetInfo upLoadRetInfo = new UpLoadRetInfo();
						List<UpLoadInfo> data = new ArrayList<UpLoadInfo>();
						data.add(new UpLoadInfo(appMain.loginInfo.id, String
								.valueOf(response.id), progress, false));
						upLoadRetInfo.setData(data);
						ApiUtils.uploadPlayRecordsByUser(upLoadRetInfo, handler);
						super.run();
					}
				}.start();
			}
		}

		if (lessonInfo.tags != null) {
			for (int i = 0; i < lessonInfo.tags.size(); i++) {
				TagsInfo info = lessonInfo.tags.get(i);
				if ("mf".equals(info.code) || "xm".equals(info.code)) {
					isMyLesson = true;
				}
			}
		}

		if (response != null && isMyLesson && appMain.loginInfo != null) {// 上传我的课程记录
			new Thread() {
				@Override
				public void run() {
					UpLoadRetInfo upLoadRetInfo = new UpLoadRetInfo();
					List<UpLoadInfo> data = new ArrayList<UpLoadInfo>();
					data.add(new UpLoadInfo(appMain.loginInfo.id, String
							.valueOf(response.id), false));
					upLoadRetInfo.setData(data);
					ApiUtils.uploadMyCoursesByUser(upLoadRetInfo, handler);
					super.run();
				}
			}.start();
		}
		player.stop();
		super.onDestroy();
	}

	/**
	 * 隐藏播放信息
	 */
	class HideViewRun implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			Animation topToHideAnimation = MyAnimations.topToHideAnimation();
			topToHideAnimation.setAnimationListener(new OnAnimation(llTitle,
					false));
			llTitle.startAnimation(topToHideAnimation);
			Animation bottomToHideAnimation = MyAnimations
					.bottomToHideAnimation();
			bottomToHideAnimation.setAnimationListener(new OnAnimation(rlMenu,
					false));
			rlMenu.startAnimation(bottomToHideAnimation);
		}

	}

	/**
	 * 显示播放信息
	 */
	void showView() {
		Animation topShowAnimation = MyAnimations.topShowAnimation();
		topShowAnimation.setAnimationListener(new OnAnimation(llTitle, true));
		llTitle.startAnimation(topShowAnimation);
		Animation bottomToShowAnimation = MyAnimations.bottomToShowAnimation();
		bottomToShowAnimation
				.setAnimationListener(new OnAnimation(rlMenu, true));
		rlMenu.startAnimation(bottomToShowAnimation);
		handler.postDelayed(new HideViewRun(), 5000);
	}

	class OnAnimation implements AnimationListener {
		View view;
		boolean isShow;

		public OnAnimation(View view, boolean isShow) {
			this.view = view;
			this.isShow = isShow;
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			view.setVisibility(isShow ? View.VISIBLE : View.GONE);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationStart(Animation animation) {

		}

	}

	/**
	 * 列表更新广播接收器
	 * 
	 * @author Administrator
	 * 
	 */
	public class UpdateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			int id = intent.getIntExtra("id", 0);
			int progress = intent.getIntExtra("progress", 0);
			if (response.id == id) {
				Log.w("UpdateReceiver", "进度条刷新~~~~~~~~~~~~・~~~~~id:" + id
						+ ",progress" + progress);
				// bnp.setProgress(progress);
			}

		}
	}
}
