package xxx.dan.android.triggerioclient;

import xxx.dan.android.triggerioclient.R.string;
import Client.DeviceClient;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

//--------------------------------------------
public class ConnectDialog extends AlertDialog {
	String TAG = ConnectDialog.class.getName();
	DeviceClient device;
	EditText addressEdit;
	EditText portEdit;
	
	public ConnectDialog(Context context, DeviceClient device) {
	    super(context);
	    this.device = device;
	    
	    LayoutInflater factory = LayoutInflater.from(context);
	    final View textEntryView = factory.inflate(R.layout.connect_dialog, null);

	    setTitle(R.string.alert_dialog_text_entry);
	    setView(textEntryView);

	    addressEdit = (EditText) textEntryView.findViewById(R.id.server_address_edit);
	    portEdit = (EditText) textEntryView.findViewById(R.id.server_port_edit);

	    Log.v(TAG, "populate text input fields");
	    addressEdit.setText(device.getServerAddress());
	    portEdit.setText(String.valueOf(device.getServerPort()));

	    setButton(AlertDialog.BUTTON_POSITIVE, context.getResources().getString(R.string.alert_dialog_connect),
		    (new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			    // this will never be called
			}
		    }));

	    setButton(AlertDialog.BUTTON_NEGATIVE, context.getResources().getString(R.string.alert_dialog_cancel),
		    (new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			    // this will never be called
			}
		    }));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
		    new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			    setDeviceValues();
			    
			    if (device.connect()) {
				Log.v(TAG, "IS connected");
				dismiss();
			    } else {
				Log.v(TAG, "not connected");
				noConnectionDialog();
			    }
			}
		    });

	    getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(
		    new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			    System.exit(Activity.RESULT_CANCELED);
			}
		    });
	}
	
	private void setDeviceValues(){
	    device.setServerAddress(addressEdit.getText().toString());
	    device.setServerPort(Integer.parseInt(portEdit.getText().toString()));
	}
	
	// -----------------------------------------
	private void noConnectionDialog() {
	    AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
	    builder.setMessage("Unable to connect").setCancelable(true)
	    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int id) {
		    dialog.cancel();
		}
	    });
	    AlertDialog alert = builder.create();
	    alert.show();
	}	
}

