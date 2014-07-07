package funfact.ninegag.model;

import android.content.Context;
import android.widget.Toast;

public class Utils {
	
	public static void showToast(Context mContext,String content){
		Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
	}

}
