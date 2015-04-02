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
	// ����������NFCд����
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
	 * �� android:launchMode="singleTop"����ΪsingleTop��ʱ�򣬵���onNewIntent
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (mPackageName == null)
			return;
		// ��һ�������tag
		Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		writeNFCTag(detectedTag);
	}

	// ��ý���
	@Override
	protected void onResume() {
		super.onResume();
		// ���ȼ��������п���ʶ��nfc�Ĵ���
		if (mNfcAdapter != null) {
			mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null,
					null);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// ��Ӧ�ó����˳�ʱ���ص�Ĭ��
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
		// ���д�����ݴ�С
		int size = ndefMessage.toByteArray().length;
		try {
			// �ж��Ƿ���nfc��ǩ
			Ndef ndef = Ndef.get(tag);
			if (ndef != null) {
				// ����
				ndef.connect();
				// �ж��Ƿ��д��
				if (!ndef.isWritable()) {
					return;
				}
				// �жϴ�С
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