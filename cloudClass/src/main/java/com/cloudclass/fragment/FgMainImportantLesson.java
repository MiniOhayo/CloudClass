package com.cloudclass.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.cloudclass.AcHistory;
import com.cloudclass.AcSearch;
import com.cloudclass.AppMain;
import com.cloudclass.R;

public class FgMainImportantLesson extends Fragment implements OnClickListener,
		android.widget.CompoundButton.OnCheckedChangeListener,
		OnCheckedChangeListener {
	public static final int SAVE_OK = 2;
	public static final int SAVE_ERROE = 3;
	private Fragment currentFragment;
	AppMain appMain;
	TextView titleName;
	FgMainImportantSuggestion fgMainImportantSuggestion;
	FgMainImportantFree fgMainImportantFree;
	FgMainImportantNew fgMainImportantNew;
	FgMainImportantTutor fgMainImportantTutor;
	RadioGroup rgImportant;
	private FragmentActivity con;
	private ProgressDialog progressDialog;
	private View view;
	// public ArrayList<Fragment> fragments = new ArrayList<Fragment>();;
	public Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			}
			progressDialog.dismiss();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		con = this.getActivity();

		appMain = (AppMain) con.getApplication();
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		if (view == null) {
			initeView(inflater, container);
		}

		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);

		}
		return view;

	}

	private void initeView(LayoutInflater inflater, ViewGroup container) {
		view = inflater.inflate(R.layout.fg_main_important_lesson, container,
				false);
		// view.findViewById(R.id.menu_btn).setOnClickListener(this);
		// view.findViewById(R.id.menu_btn).setVisibility(View.GONE);
		titleName = (TextView) view.findViewById(R.id.title_name);

		fgMainImportantSuggestion = new FgMainImportantSuggestion();
		fgMainImportantFree = new FgMainImportantFree();
		fgMainImportantNew = new FgMainImportantNew();
		fgMainImportantTutor = new FgMainImportantTutor();
		rgImportant = (RadioGroup) view.findViewById(R.id.rgImportant);
		view.findViewById(R.id.btn_search).setOnClickListener(this);
		view.findViewById(R.id.btn_history).setOnClickListener(this);
		rgImportant.setOnCheckedChangeListener(this);
		fragmentTransaction(fgMainImportantSuggestion);

	}

	public void fragmentTransaction(Fragment fragment) {

		currentFragment = fragment;
		FragmentTransaction transaction = ((FragmentActivity) con)
				.getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.rgImportant_content, fragment);
		transaction.commit();
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.rb_important_suggestion) {

		} else {

		}

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub

		fgMainImportantSuggestion.onPause();

		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if ((currentFragment instanceof FgMainImportantLesson)) {
			fgMainImportantSuggestion.onResume();
		}

		super.onResume();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		if (checkedId == R.id.rb_important_suggestion) {
			fragmentTransaction(fgMainImportantSuggestion);
		} else if (checkedId == R.id.rb_important_free) {
			fragmentTransaction(fgMainImportantFree);
		} else if (checkedId == R.id.rb_important_new) {
			fragmentTransaction(fgMainImportantNew);
		} else if (checkedId == R.id.rb_important_tutor) {
			fragmentTransaction(fgMainImportantTutor);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_search:
			startActivity(new Intent(con, AcSearch.class));
			break;
		case R.id.btn_history:
			startActivity(new Intent(con, AcHistory.class));
			break;

		default:
			break;
		}
	}

}
