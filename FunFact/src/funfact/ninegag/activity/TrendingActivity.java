package funfact.ninegag.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import funfact.ninegag.activity.FreshActivity.TaskLoad;
import funfact.ninegag.model.GetData;
import funfact.ninegag.model.Utils;
import funfact.ninegag.model.Variable;
import funfact.ninegag.obj.ImageItem;
import funfact.ningag.adapter.AdapterViewPager;
import ydc.funny.funfact.R;
import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ext.SatelliteMenu;
import android.view.ext.SatelliteMenuItem;
import android.view.ext.SatelliteMenu.SateliteClickedListener;
import android.widget.Toast;

public class TrendingActivity extends SherlockFragment {
	SatelliteMenu menu;// menu
	List<ImageItem> listItem;
	AdapterViewPager adapter;
	GetData data;
	String link_next;
	ViewPager pager;
	SmoothProgressBar progress;
	ImageLoader loader;
	static int position = -1;// vị trí hiện tại của pager
	static List<ImageItem> saveList;// danh sách lưu trạng thái
	Display display;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Utils.showToast(getActivity(), "Oncreate");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		display = getActivity().getWindowManager().getDefaultDisplay();
		// Utils.showToast(getActivity(), "OncreateVIew");
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.hot_activity, container, false);
		progress = (SmoothProgressBar) v.findViewById(R.id.progress);
		loader = ImageLoader.getInstance();
		loader.init(ImageLoaderConfiguration.createDefault(getActivity()));
		pager = (ViewPager) v.findViewById(R.id.view_pager);
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int vt) {

				position = vt;

				if (vt == listItem.size() - 1) {
					progress.setVisibility(View.VISIBLE);
				}

				if (vt == listItem.size() - 3) {
					loadData(link_next);
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		createSateMenu(v);

		data = new GetData();

		if (position != -1) {

			listItem = saveList;
			adapter = new AdapterViewPager(listItem, getActivity(), display);
			pager.setAdapter(adapter);
			pager.setCurrentItem(position);
			progress.setVisibility(View.GONE);

		} else {

			reload();
		}

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	private void reload() {
		listItem = new ArrayList<ImageItem>();
		adapter = new AdapterViewPager(listItem, getActivity(), display);
		pager.setAdapter(adapter);
		loadData(Variable.LINK_TRENDING);
	}

	/**
	 * Call class taskload with url
	 * 
	 * @param url
	 */
	private void loadData(String url) {

		new TaskLoad().execute(url);
	}

	/**
	 * Create SateMenu with setwallpaper and download button
	 * 
	 * @param v
	 */
	private void createSateMenu(View v) {
		menu = (SatelliteMenu) v.findViewById(R.id.menu);
		menu.setSatelliteDistance(450);
		menu.setTotalSpacingDegree(100);

		List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
		items.add(new SatelliteMenuItem(3, R.drawable.reload));
		items.add(new SatelliteMenuItem(2, R.drawable.download));
		items.add(new SatelliteMenuItem(1, R.drawable.setwallpaper));

		menu.addItems(items);

		menu.setOnItemClickedListener(new SateliteClickedListener() {

			public void eventOccured(int id) {
				if (id == 1) {
					setWallPaper();
				} else if (id == 2) {
					downLoadImage();
				} else if (id == 3) {
					reload();
				}

			}
		});

	}

	/**
	 * Store Image to SDcard in folder funfact
	 * 
	 * @param imageData
	 * @param filename
	 * @return
	 */

	private String storeImage(Bitmap imageData, String filename) {
		// get path to external storage (SD card)
		String iconsStoragePath = Environment.getExternalStorageDirectory()
				+ "/funfact";
		File sdIconStorageDir = new File(iconsStoragePath);

		// create storage directories, if they don't exist
		sdIconStorageDir.mkdirs();

		try {
			String filePath = sdIconStorageDir.toString() + filename;
			FileOutputStream fileOutputStream = new FileOutputStream(filePath);

			BufferedOutputStream bos = new BufferedOutputStream(
					fileOutputStream);

			// choose another format if PNG doesn't suit you
			imageData.compress(CompressFormat.JPEG, 100, bos);

			bos.flush();
			bos.close();

		} catch (FileNotFoundException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());
			return "Can't save photo";
		} catch (IOException e) {
			Log.w("TAG", "Error saving image file: " + e.getMessage());
			return "Can't save photo";
		}

		return "Download successfully, photo's saved in "
				+ sdIconStorageDir.toString();
	}

	/**
	 * Get name of Image by url Image
	 * 
	 * @param url
	 * @return
	 */
	String getName(String url) {
		int index = url.lastIndexOf("/");
		int index2 = url.lastIndexOf(".");
		String st = url.substring(index, index2);
		return st + ".jpg";
	}

	/**
	 * Set wallpaper
	 */
	private void setWallPaper() {
		int chido = pager.getCurrentItem();
		if (loader.loadImageSync(listItem.get(chido).getUrl()) != null) {
			WallpaperManager wpm = WallpaperManager.getInstance(getActivity());
			try {
				wpm.setBitmap(loader
						.loadImageSync(listItem.get(chido).getUrl()));
				Toast.makeText(getActivity(), "Set wallpaper successfully !",
						Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				Toast.makeText(getActivity(), "Can't set wallpaper !",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getActivity(), "Can't set wallpaper !",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Download Image from URL
	 */
	private void downLoadImage() {
		int chido = pager.getCurrentItem();
		if (loader.loadImageSync(listItem.get(chido).getUrl()) != null) {

			Toast.makeText(
					getActivity(),
					storeImage(
							loader.loadImageSync(listItem.get(chido).getUrl()),
							getName(listItem.get(chido).getUrl())),
					Toast.LENGTH_SHORT).show();

		} else {
			Toast.makeText(getActivity(), "Can't download photo",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Class load Data from URL
	 * 
	 * @author Eo
	 * 
	 */
	class TaskLoad extends AsyncTask<String, Void, List<ImageItem>> {
		String link;

		@Override
		protected void onPreExecute() {
			// progress.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected List<ImageItem> doInBackground(String... arr) {
			link = arr[0];
			List<ImageItem> newList = data.parsing(link);
			return newList;
		}

		@Override
		protected void onPostExecute(List<ImageItem> result) {

			progress.setVisibility(View.GONE);

			if (result.size() == 0) {
				new TaskLoad().execute(link);
			} else {

				link_next = data.getLink_next();
				adapter.AddList(result);
				saveList = listItem;
				adapter.notifyDataSetChanged();
			}
			super.onPostExecute(result);
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Utils.showToast(getActivity(), "save state");
		outState.putInt("position", pager.getCurrentItem());

		// outState.putSerializable("list", (Serializable) listItem);
	}

}
