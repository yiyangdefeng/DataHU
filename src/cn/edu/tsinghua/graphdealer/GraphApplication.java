package cn.edu.tsinghua.graphdealer;

import java.util.LinkedHashSet;
import java.util.Set;
import android.app.Application;
import android.util.Log;

public class GraphApplication extends Application {
	private static final String TAG = "GraphApplication";
	public void onCreate() {
   	 Log.d(TAG, "onCreate");
        super.onCreate();
        Set<String> tagSet = new LinkedHashSet<String>();
       tagSet.add("123"); 
   }

}
