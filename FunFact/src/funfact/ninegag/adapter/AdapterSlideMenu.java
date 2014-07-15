package funfact.ninegag.adapter;

import java.util.List;

import ydc.funny.funfact.R;

import funfact.ninegag.obj.ItemSlideMenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterSlideMenu extends BaseAdapter {

	List<ItemSlideMenu> list;
	Context mContext;
	LayoutInflater inflater;

	public AdapterSlideMenu(List<ItemSlideMenu> list, Context mContext) {
		super();
		this.list = list;
		this.mContext = mContext;
		this.inflater = (LayoutInflater) this.mContext
				.getSystemService(this.mContext.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		// TODO Auto-generated method stub

		ItemSlideMenu item = list.get(position);

		if (v == null) {
			v = inflater.inflate(R.layout.item_menu, null);
		}

		ImageView img = (ImageView) v.findViewById(R.id.imgMenu);
		TextView tv = (TextView) v.findViewById(R.id.tvMenu);

		img.setImageResource(item.getIcon());
		tv.setText(item.getTitle());

		return v;
	}

}
