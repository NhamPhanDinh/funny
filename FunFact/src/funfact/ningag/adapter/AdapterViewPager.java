package funfact.ningag.adapter;

import java.util.List;

import ydc.funny.funfact.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
/*import com.koushikdutta.ion.Ion;*/
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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
		loader.init(ImageLoaderConfiguration.createDefault(mContext));

		option = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
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
		ImageView imgImage = (ImageView) v.findViewById(R.id.imvImage);
		final ProgressBar pro = (ProgressBar) v.findViewById(R.id.loading);

		if (item.getUrl().lastIndexOf("gif") > 0) {
			tvTitle.setText(item.getTitle()+" (GIF)");
			Ion.with(mContext).load(item.getUrl()).withBitmap()
					.resize(display.getWidth(), display.getWidth()).centerInside().intoImageView(imgImage);

			pro.setVisibility(View.GONE);
			
		} else {
			tvTitle.setText(item.getTitle());
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
							pro.setVisibility(View.GONE);

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

}
