package com.hdljz.nfc.application;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.hdljz.nfc.R;

public class RunApplicationActivity extends Activity {
	private Button btn;
	private String mPackageName;
	// 下面两个向NFC写数据
	private NfcAdapter mNfcAdapter;
	private PendingIntent mPendingIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent in = new Intent(RunApplicationActivity.this,
						InstalledApplicationListActivity.class);
				startActivityForResult(in, 0);

			}
		});
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()), 0);
	}

	/**
	 * 当 android:launchMode="singleTop"设置为singleTop的时候，调用onNewIntent
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (mPackageName == null)
			return;
		// 第一步：获得tag
		Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		writeNFCTag(detectedTag);
	}

	// 获得焦点
	@Override
	protected void onResume() {
		super.onResume();
		// 优先级优于所有可以识别nfc的窗口
		if (mNfcAdapter != null) {
			mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null,
					null);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 当应用程序退出时，回到默认
		if (mNfcAdapter != null) {
			mNfcAdapter.disableForegroundDispatch(this);
		}
	}

	public void writeNFCTag(Tag tag) {
		if (tag == null) {
			return;
		}
		NdefMessage ndefMessage = new NdefMessage(
				new NdefRecord[] { NdefRecord
						.createApplicationRecord(mPackageName) });
		// 获得写入数据大小
		int size = ndefMessage.toByteArray().length;
		try {
			// 判断是否是nfc标签
			Ndef ndef = Ndef.get(tag);
			if (ndef != null) {
				// 连接
				ndef.connect();
				// 判断是否可写入
				if (!ndef.isWritable()) {
					return;
				}
				// 判断大小
				if (ndef.getMaxSize() < size) {
					return;
				}
				ndef.writeNdefMessage(ndefMessage);
				Toast.makeText(this, "oK", 1).show();
			}
		} catch (Exception e) {

		}
	}
}