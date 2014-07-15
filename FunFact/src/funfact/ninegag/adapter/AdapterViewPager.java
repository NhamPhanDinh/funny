package funfact.ninegag.adapter;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

import java.io.File;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import ydc.funny.funfact.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v4.view.PagerAdapter;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
/*import com.koushikdutta.ion.Ion;*/
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import funfact.ninegag.obj.ImageItem;

public class AdapterViewPager extends PagerAdapter {

	List<ImageItem> list;
	Context mContext;
	LayoutInflater inflater;
	Display display;
	ImageLoader loader;
	DisplayImageOptions option;

	public AdapterViewPager(List<ImageItem> list, Context mContext,
			Display display) {
		this.list = list;
		this.mContext = mContext;
		inflater = (LayoutInflater) this.mContext
				.getSystemService(this.mContext.LAYOUT_INFLATER_SERVICE);
		this.display = display;
		loader = ImageLoader.getInstance();

		File cacheDir = StorageUtils.getOwnCacheDirectory(mContext,
				"alrawda/Cache");

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				mContext)
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCache(new UsingFreqLimitedMemoryCache(2000000))
				.memoryCacheSize(1500000)
				// 1.5 Mb

				.denyCacheImageMultipleSizesInMemory()
				.discCache(new UnlimitedDiscCache(cacheDir))
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).build();
		loader.init(config);

		// loader.init(ImageLoaderConfiguration.createDefault(mContext));

		option = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.cacheInMemory(false).cacheOnDisc(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	public void AddList(List<ImageItem> newList) {
		for (int i = 0; i < newList.size(); i++) {
			list.add(newList.get(i));
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0.equals(arg1);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int vt) {
		ImageItem item = list.get(vt);
		View v = inflater.inflate(R.layout.item_adapter, null);

		TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
		/* ImageView imgImage = (ImageView) v.findViewById(R.id.imvImage); */
		PhotoView imgImage = (PhotoView) v
				.findViewById(R.id.imvImage);
		//imgImage.setFitsSystemWindows(true);
		final ProgressBar pro = (ProgressBar) v.findViewById(R.id.loading);
		final ScrollView scroll = (ScrollView) v.findViewById(R.id.scroll);

		/**
		 * nếu ảnh gif thì dùng ion load k thì vẫn dùng cái univer kia load
		 */

		if (item.getUrl().lastIndexOf("gif") > 0) {
			tvTitle.setText(item.getTitle() + " (GIF)");

			imgImage.setMinimumWidth(display.getWidth());
			imgImage.setMinimumHeight(display.getWidth());

			Ion.with(mContext).load(item.getUrl()).withBitmap()
					.resize(256, 256).centerInside().intoImageView(imgImage);

			pro.setVisibility(View.GONE);

		} else {
			tvTitle.setText(item.getTitle());
			// imgImage.setMinimumWidth(display.getWidth());
			// imgImage.setMinimumHeight(display.getWidth());
			loader.displayImage(item.getUrl(), imgImage, option,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							// TODO Auto-generated method stub
							pro.setVisibility(View.VISIBLE);

						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							pro.setVisibility(View.GONE);

						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {

							if (loadedImage != null) {
								pro.setVisibility(View.GONE);
								int widthImage = loadedImage.getWidth();
								int heightImage = loadedImage.getHeight();
								if (1.3 * widthImage > heightImage) {
									scroll.post(new Runnable() {
										@Override
										public void run() {
											scroll.fullScroll(ScrollView.FOCUS_DOWN);

										}
									});
								}
							}

						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							// TODO Auto-generated method stub

						}
					});

		}

		container.addView(v, 0);
		return v;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		super.destroyItem(container, position, object);
	}

}
