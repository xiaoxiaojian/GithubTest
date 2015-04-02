package com.hdljz.nfc.application;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class InstalledApplicationListActivity extends ListActivity implements
		OnItemClickListener {
	//将所有的包放到一个list里面
	private List<String> mPackages = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PackageManager packageManager = getPackageManager();
		// 获取已经安装的程序包
		List<PackageInfo> packageInfos = packageManager
				.getInstalledPackages(PackageManager.GET_ACTIVITIES);
		for (PackageInfo packageInfo : packageInfos) {
			mPackages.add(packageInfo.applicationInfo.loadLabel(packageManager)
					+ "\n" + packageInfo.packageName);
		}
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				mPackages);
		setListAdapter(arrayAdapter);
		getListView().setOnItemClickListener(this);
	}

	/**
	 * 将点击的包通过窗口返回.
	 */
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		Intent intent = new Intent();
		intent.putExtra("package_name", mPackages.get(position));
		setResult(1, intent);
		finish();
	}

}
