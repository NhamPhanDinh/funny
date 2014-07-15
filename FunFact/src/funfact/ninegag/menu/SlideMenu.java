package funfact.ninegag.menu;

import java.util.ArrayList;
import java.util.List;

import funfact.ninegag.adapter.AdapterSlideMenu;
import funfact.ninegag.obj.ItemSlideMenu;
import ydc.funny.funfact.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class SlideMenu extends android.support.v4.app.Fragment {

	ListView listMenu;
	AdapterSlideMenu adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.slidemenu, container, false);

		listMenu = (ListView) v.findViewById(R.id.listMenu);
		List<ItemSlideMenu> list = new ArrayList<ItemSlideMenu>();
		list.add(new ItemSlideMenu(R.drawable.about, "About"));

		adapter = new AdapterSlideMenu(list, getActivity());
		listMenu.setAdapter(adapter);

		return v;
	}

}
