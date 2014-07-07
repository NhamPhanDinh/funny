package funfact.ninegag.activity;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;

import funfact.ninegag.menu.SlideMenu;

import ydc.funny.funfact.R;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainActivity extends SherlockFragmentActivity {

	FragmentTabHost mTabHost;
	SlidingMenu menu;
	CanvasTransformer mTransformer;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.WHITE));

		setContentView(R.layout.main);
		setupTabHost();
		setupTab();
		setSlideMenu();

	}

	private void setupTabHost() {
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.tabcontent);
		// mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);

	}

	private static Interpolator interp = new Interpolator() {
		@Override
		public float getInterpolation(float t) {
			t -= 1.0f;
			return t * t * t + 1.0f;
		}
	};

	private void setSlideMenu() {

		mTransformer = new CanvasTransformer() {

			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				canvas.translate(
						0,
						canvas.getHeight()
								* (1 - interp.getInterpolation(percentOpen)));
			}
		};

		menu = new SlidingMenu(this);
		menu.setShadowWidth(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.slidingmenu_shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setBehindScrollScale(0.0f);
		menu.setBehindCanvasTransformer(mTransformer);
		menu.setMenu(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new SlideMenu()).commit();
	}

	private void setupTab() {

		View v1 = createTabView(getApplicationContext(), "Hot");
		View v2 = createTabView(getApplicationContext(), "Trending");
		View v3 = createTabView(getBaseContext(), "Fresh");
		mTabHost.addTab(mTabHost.newTabSpec("hot").setIndicator(v1),
				HotActivity.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("trending").setIndicator(v2),
				TrendingActivity.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("fresh").setIndicator(v3),
				FreshActivity.class, null);

	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context)
				.inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}

	@Override
	public void onBackPressed() {
		if (menu.isMenuShowing()) {
			menu.toggle();
		} else {
			finish();
			HotActivity.position = -1;
			TrendingActivity.position = -1;
			FreshActivity.position = -1;
		}
	}
}
