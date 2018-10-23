package com.cloudclass.view;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.cloudclass.R;

public class Show {
	public static void toast(Context con, int id) {
		Toast.makeText(con, con.getResources().getString(id),
				Toast.LENGTH_SHORT).show();
	}

	public static void toast(Context con, String text) {
		Toast.makeText(con, text, Toast.LENGTH_SHORT).show();
	}

	public static void showShare(Context con, String title,String sharedText,
			String imgFilePath,String imageUrl,String playurl, String comentText) {
		ShareSDK.initSDK(con);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(title);
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(playurl);
		// text是分享文本，所有平台都需要这个字段
		oks.setText("恋爱公开课分享");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		if (!TextUtils.isEmpty(imgFilePath)) {
			oks.setImagePath(imgFilePath);// 确保SDcard下面存在此张图片
		}
		oks.setImageUrl(imageUrl);
		if (!TextUtils.isEmpty(playurl)) {
			// url仅在微信（包括好友和朋友圈）中使用
			oks.setUrl(playurl);
			// siteUrl是分享此内容的网站地址，仅在QQ空间使用
			oks.setSiteUrl(playurl);
		}
		//oks.setVenueName("来自恋爱公开课");
		oks.setVenueDescription("来自恋爱公开课");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		if (!TextUtils.isEmpty(comentText)) {
			oks.setComment(comentText);
		}
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(con.getString(R.string.app_name));

		// 启动分享GUI
		oks.show(con);
	}
}
