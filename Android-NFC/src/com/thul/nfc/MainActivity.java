package com.thul.nfc;

import com.thul.nfc.R;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {
	String phone = "1234567890"; // Replace with your mobile number
	String yellow_tag = "04D658B2003E80"; // Replace with your NFC Tag ID
	String white_tag = "044C54B2003E80"; // Replace with your NFC Tag ID
	String red_tag = "04E25FB2003E80"; // Replace with your NFC Tag ID
	String blue_tag = "04385BB2003E80"; // Replace with your NFC Tag ID
	String green_tag = "04FC4EB2003E80"; // Replace with your NFC Tag ID
	
	NfcAdapter mAdapter;
	IntentFilter[] mFilters;
	PendingIntent mPendingIntent;
	
	void resolveIntent(Intent intent) {
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_LONG;
		
        String action = intent.getAction();
        
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            byte[] extraID = tagFromIntent.getId();
            
            StringBuilder sb = new StringBuilder();
            for (byte b : extraID) {
                sb.append(String.format("%02X", b));
            };

			String tagID = sb.toString();
			Log.e("nfc ID", tagID);

            String name;
            
            if (tagID.equals(yellow_tag)){
            	
            	Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+phone));
                startActivity(callIntent);
            	name = "yellow";
            }
            else if (tagID.equals(white_tag)){
            	
            	 SmsManager sms = SmsManager.getDefault();
            	    sms.sendTextMessage(phone, null, "Hi this is a Test message", null, null);
            	name = "White";
        		
            }
            else if (tagID.equals(red_tag)){
            	
        		name = "Red";
        		
            } 
            else if (tagID.equals(blue_tag)){
            	
        		name = "Blue";
        		
            } 
          else if (tagID.equals(green_tag)){
            	
        		name = "Green";
        		
            }
            else {
            	
            	name ="Unknown Tag";
            }
            
            
            Toast toast = Toast.makeText(context, name, duration);
            toast.show();  
            
         };
    }
	
	
	   
    String[][] mTechLists = new String[][] { new String[] { NfcA.class.getName() } };
    Intent intent = getIntent();
  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mPendingIntent = PendingIntent.getActivity(this, 0,
	            new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		
		IntentFilter ndef1 = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
		mFilters = new IntentFilter[] {
	            ndef1,
	    };
		
        try {
            ndef1.addDataType("*/*");
           
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        
        if (getIntent() != null){
        	resolveIntent(getIntent());
        }
         
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public void onResume() {
        super.onResume();
        mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists);
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.i("Foreground dispatch", "Discovered tag with intent: " + intent);
        resolveIntent(intent);            
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.disableForegroundDispatch(this);
    }
    

}
