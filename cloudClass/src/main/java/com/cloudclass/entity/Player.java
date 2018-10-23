package com.cloudclass.entity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import com.cloudclass.R;
import com.cloudclass.view.NumberCircleProgressBar;
import com.cloudclass.view.Show;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Player implements OnBufferingUpdateListener, OnCompletionListener,
		MediaPlayer.OnPreparedListener, OnSeekBarChangeListener,
		OnErrorListener {
	public MediaPlayer mediaPlayer;
	private SeekBar skbProgress;
	private Timer mTimer = new Timer();
	SurfaceHolder hold;
	private ImageView btn_play, btnPlay;
	private TextView tvTime;
	String videoUrl;
	int duration = 0;
	private TextView tvsVideoTime;
	private ProgressBar progressBar;
	private Context context;
	NumberCircleProgressBar bnp;
	public boolean isMp3;

	public Player(Context context, SeekBar skbProgress, SurfaceHolder hold) {
		this.skbProgress = skbProgress;
		this.hold = hold;
		this.context = context;
		this.skbProgress.setOnSeekBarChangeListener(this);
		mTimer.schedule(mTimerTask, 0, 100);
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
		showCacheProgress(true);
	}

	public void showCacheProgress(boolean isShow) {
		if (progressBar != null) {
			if (isShow) {
				progressBar.setVisibility(View.VISIBLE);
				if (btn_play != null) {
					btn_play.setVisibility(View.GONE);
				}

			} else
				progressBar.setVisibility(View.GONE);
		}
	}

	public NumberCircleProgressBar getBnp() {
		return bnp;
	}

	public void setBnp(NumberCircleProgressBar bnp) {
		this.bnp = bnp;
	}

	public void setBtn_play(ImageView btn_play) {
		this.btn_play = btn_play;
	}

	public void setBtnPlay(ImageView btnPlay) {
		this.btnPlay = btnPlay;
	}

	public TextView getTvsVideoTime() {
		return tvsVideoTime;
	}

	public void setTvsVideoTime(TextView tvsVideoTime) {
		this.tvsVideoTime = tvsVideoTime;
	}

	public void setTvTime(TextView tvTime) {
		this.tvTime = tvTime;
	}

	/*******************************************************
	 * 通过定时器和Handler来更新进度条
	 ******************************************************/
	TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			if (mediaPlayer == null)
				return;
			if (mediaPlayer.isPlaying() && !skbProgress.isPressed()) {
				handleProgress.sendEmptyMessage(0);
			}
		}
	};
	private int currentpos;
	Handler handleProgress = new Handler() {

		String strDuration = "";
		SimpleDateFormat myFmt2 = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();

		public void handleMessage(Message msg) {
			if (mediaPlayer == null)
				return;
			int position = mediaPlayer.getCurrentPosition();

			if (duration == 0) {
				myFmt2.setTimeZone(TimeZone.getTimeZone("Asia/Beijing"));
				if (mediaPlayer != null && mediaPlayer.isPlaying()) {
					duration = mediaPlayer.getDuration();
				}
				date.setTime(duration);
				strDuration = myFmt2.format(date);
				if (tvsVideoTime != null) {
					tvsVideoTime.setText(strDuration);
				}
				// skbProgress.setMax(duration);
			}

			if (duration > 0) {
				currentpos = skbProgress.getMax() * position / duration;
				date.setTime(position);
				String strPosition = myFmt2.format(date);
				tvTime.setText(strPosition + "/" + strDuration);
				skbProgress.setProgress(currentpos);
			}

		};
	};
	private int progress;
	private int videoWidth;
	private int videoHeight;

	// *****************************************************
	public boolean isPlaying() {
		if (mediaPlayer != null) {
			return mediaPlayer.isPlaying();
		}
		return false;

	}

	public void play() {
		if (TextUtils.isEmpty(videoUrl)) {
			return;
		}

		if (mediaPlayer != null) {
			btn_play.setVisibility(View.GONE);
			btnPlay.setImageResource(R.drawable.pause);
			mediaPlayer.start();
		}

	}

	public void playUrl(String videoUrl) {
		this.videoUrl = videoUrl;
		try {

			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDisplay(hold);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.setOnCompletionListener(this);
			mediaPlayer.setOnErrorListener(this);
		} catch (Exception e) {
			Log.e("mediaPlayer", "error", e);
		}

		try {
			mediaPlayer.reset();
			Uri uri = Uri.parse(videoUrl);
			mediaPlayer.setDataSource(context, uri);
			mediaPlayer.prepareAsync();// prepare之后自动播放
			// mediaPlayer.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void pause() {
		btn_play.setVisibility(View.VISIBLE);
		btnPlay.setImageResource(R.drawable.play);
		if (mediaPlayer == null) {
			return;
		}
		mediaPlayer.pause();
	}

	public void stop() {
		mTimer.cancel();
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}

	}

	@Override
	/**
	 * 通过onPrepared播放
	 */
	public void onPrepared(MediaPlayer arg0) {
		// try {
		// arg0.prepare();
		// } catch (IllegalStateException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		Log.e("mediaPlayer", "onPrepared");
		videoWidth = mediaPlayer.getVideoWidth();
		videoHeight = mediaPlayer.getVideoHeight();

		if (!isMp3) {
			if (videoHeight != 0 && videoWidth != 0) {
				arg0.start();
				btn_play.setVisibility(View.GONE);
				btnPlay.setImageResource(R.drawable.pause);
				showCacheProgress(false);
			} else
				Show.toast(context, "该视频无法正常播放");
		} else {
			arg0.start();
			btn_play.setVisibility(View.GONE);
			btnPlay.setImageResource(R.drawable.pause);
			showCacheProgress(false);
		}

	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// if (!arg0.isPlaying()) {
		//
		// }
		btn_play.setVisibility(View.VISIBLE);
		btnPlay.setImageResource(R.drawable.play);

	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
		// skbProgress.setSecondaryProgress(bufferingProgress);
		if (bnp != null) {
			bnp.setProgress(bufferingProgress);
		}
		// int currentProgress = skbProgress.getMax()
		// * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		// if (fromUser) {
		// if (mediaPlayer == null)
		// return;
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			this.progress = progress * mediaPlayer.getDuration()
					/ seekBar.getMax();
		}

		// Log.e("onProgressChanged", "onCompletion"+progress);
		// }

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}

	public String getProgress() {
		if (mediaPlayer == null) {
			return null;
		}
		int sec = mediaPlayer.getCurrentPosition() / 1000;
		Log.w("onProgressChanged", "onCompletion:" + sec);
		if (sec == 0) {
			return null;
		}
		if (sec > 60) {
			return String.valueOf(sec / 60) + "分钟" + (sec % 60) + "秒";
		} else
			return String.valueOf(sec) + "秒";
	}

	public int getPro() {
		if (mediaPlayer != null) {

			return mediaPlayer.getCurrentPosition();
		}
		return 0;
	}

	public void setProgress(int progress) {
		this.progress = progress;
		mediaPlayer.seekTo(progress);
		Log.e("seekPosition", "setProgress%" + ",,,position:" + progress);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		if (mediaPlayer == null)
			return;
		int seekPosition = seekBar.getProgress();
		double ss = ((seekPosition * 1.00) / seekBar.getMax());
		int seekTime = (int) (ss * duration);
		mediaPlayer.seekTo(progress);
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		Log.e("MediaPlayer onError ", "MediaPlayer onError  " + what
				+ ",,extra:" + extra);

		return false;
	}

}
