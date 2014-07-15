package funfact.ninegag.activity;

import ydc.funny.funfact.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;

import funfact.ninegag.menu.SlideMenu;
import funfact.ninegag.model.Utils;

public class MainActivity extends SherlockFragmentActivity {

	FragmentTabHost mTabHost;
	SlidingMenu menuSlide;
	CanvasTransformer mTransformer;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.WHITE));

		setContentView(R.layout.main);
		setupTabHost();
		setupTab();
		setSlideMenu();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add("Refresh")
				.setIcon(R.drawable.ic_action_refresh)
				.setOnMenuItemClickListener(
						new MenuItem.OnMenuItemClickListener() {

							@Override
							public boolean onMenuItemClick(MenuItem item) {
								
								

								return true;
							}
						}).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		menu.add("Menu")
				.setIcon(R.drawable.ic_action_overflow)
				.setOnMenuItemClickListener(
						new MenuItem.OnMenuItemClickListener() {

							@Override
							public boolean onMenuItemClick(MenuItem item) {
								menuSlide.toggle();
								return true;
							}
						}).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	private void setupTabHost() {
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.tabcontent);

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

		menuSlide = new SlidingMenu(this);
		menuSlide.setShadowWidth(R.dimen.shadow_width);
	//	menuSlide.setShadowDrawable(R.drawable.slidingmenu_shadow);
		menuSlide.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menuSlide.setFadeDegree(0.35f);
		menuSlide.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menuSlide.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menuSlide.setBehindScrollScale(0.0f);
		menuSlide.setBehindCanvasTransformer(mTransformer);
		menuSlide.setMenu(R.layout.menu_frame);
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
		
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				Utils.showToast(getApplicationContext(), tabId+"");
				
			}
		});

	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context)
				.inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}
	
	
	private long lastPressedTime;
	private static final int PERIOD = 2000;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
	        switch (event.getAction()) {
	        case KeyEvent.ACTION_DOWN:
	            if (event.getDownTime() - lastPressedTime < PERIOD) {

	        		if (menuSlide.isMenuShowing()) {
	        			menuSlide.toggle();
	        		} else {
	        			finish();
	        			HotActivity.position = -1;
	        			TrendingActivity.position = -1;
	        			FreshActivity.position = -1;
	        		}
	        	
	            } else {
	                Toast.makeText(getApplicationContext(), "Press again to exit.",
	                        Toast.LENGTH_SHORT).show();
	                lastPressedTime = event.getEventTime();
	            }
	            return true;
	        }
	    }
	    return false;
	}

	@Override
	public void onBackPressed() {}
}
