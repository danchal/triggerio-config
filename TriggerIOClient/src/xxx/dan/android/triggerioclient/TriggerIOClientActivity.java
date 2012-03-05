package xxx.dan.android.triggerioclient;

import Client.DeviceClient;
import Client.DeviceClient.Parameter;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TriggerIOClientActivity extends Activity {
    private static final String TAG = TriggerIOClientActivity.class.getName();
    public static final String PREFS_NAME = "preferences";
    
    DeviceClient device;
    
    Button kitValue;
    Button programChangeValue;
    Button inputNumberValue;
    Button midiChannelValue;
    Button midiNoteValue;
    Button gainValue;
    Button velocityCurveValue;
    Button thresholdValue;
    Button xtalkValue;
    Button retriggerValue;
    Button triggerTypeValue;
    
    @Override
    public void onBackPressed() {
	super.onBackPressed();
	Log.v(TAG, "onBackPressed");
	savePreferences();
	System.exit(Activity.RESULT_CANCELED);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	init();
    }

    @Override
    protected void onStop() {
	super.onStop();
	savePreferences();
    }   

    public void savePreferences(){
	Log.v(TAG, "savePreferences");
	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	SharedPreferences.Editor editor = settings.edit();

	editor.putString(getResources().getString(R.string.serverAddress), device.getServerAddress());
	editor.putInt(getResources().getString(R.string.serverPort), device.getServerPort());
	editor.putInt(getResources().getString(R.string.currentKit), device.getCurrentKitNumber());
	editor.putInt(getResources().getString(R.string.currentInput), device.getCurrentInputNumber());

	// Commit the edits!
	editor.commit();
    }

    // ----------------------------------------
    public void loadPreferences(){
	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

	String address = settings.getString(getResources().getString(R.string.serverAddress), "192.168.1.100");
	int port = settings.getInt(getResources().getString(R.string.serverPort), 4444);
	int kitNumber = settings.getInt(getResources().getString(R.string.currentKit), 0);
	int inputNumber = settings.getInt(getResources().getString(R.string.currentInput), 0);

	device = new DeviceClient(kitNumber, inputNumber, address, port);
    }
    
    // --------------------------------------
    private void init() {
	Log.v(TAG, "init");
	loadPreferences();
	setupDisplay();
	
	Log.v(TAG, "before connectDialog");
	ConnectDialog alert = new ConnectDialog(this, device);
	alert.show();
	alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
	    
	    @Override
	    public void onDismiss(DialogInterface dialog) {
		loadValues();		
	    }
	});
    }

    // --------------------------------------------------
    private void addAction(View view, final Parameter parameter) {
	Log.v(TAG, "addShit");
	view.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Log.v(TAG, "onClick");
		PickerDialog p = new PickerDialog(v.getContext(), parameter, device);
		Log.v(TAG, "before PickerDialog.show");
		p.show();
		p.setOnDismissListener(new DialogInterface.OnDismissListener() {
		    
		    @Override
		    public void onDismiss(DialogInterface dialog) {
			loadValues();			
		    }
		});
	    }
	});
    }

    // -----------------------------------------
    private void setupDisplay() {
	Log.v(TAG, "setupDisplay");
	
	kitValue = (Button) findViewById(R.id.kit_value);
	programChangeValue = (Button) findViewById(R.id.program_change_value);
	inputNumberValue = (Button) findViewById(R.id.input_number_value);
	midiChannelValue = (Button) findViewById(R.id.midi_channel_value);
	midiNoteValue = (Button) findViewById(R.id.midi_note_value);
	gainValue = (Button) findViewById(R.id.gain_value);
	velocityCurveValue = (Button) findViewById(R.id.velocity_curve_value);
	thresholdValue = (Button) findViewById(R.id.threshold_value);
	xtalkValue = (Button) findViewById(R.id.xtalk_value);
	retriggerValue = (Button) findViewById(R.id.retrigger_value);
	triggerTypeValue = (Button) findViewById(R.id.trigger_type_value);
		
	addAction(kitValue, Parameter.KIT);
	addAction(programChangeValue, Parameter.PROG_CHANGE);
	addAction(inputNumberValue, Parameter.INPUT);
	addAction(midiChannelValue, Parameter.MIDI_CHANNEL);
	addAction(midiNoteValue, Parameter.MIDI_NOTE);
	addAction(gainValue, Parameter.GAIN);
	addAction(velocityCurveValue, Parameter.VEL_CURVE);
	addAction(thresholdValue, Parameter.THRESHOLD);
	addAction(xtalkValue, Parameter.XTALK);
	addAction(retriggerValue, Parameter.RETRIGGER);
	addAction(triggerTypeValue, Parameter.TRIGGER_TYPE);
    }

    // ------------------------------------
    private void loadValues() {
	Log.v(TAG, "loadValues");
	
	kitValue.setText(device.getValue(Parameter.KIT));
	inputNumberValue.setText(device.getValue(Parameter.INPUT));

	programChangeValue.setText(device.getValue(Parameter.PROG_CHANGE));
	midiChannelValue.setText(device.getValue(Parameter.MIDI_CHANNEL));
	midiNoteValue.setText(device.getValue(Parameter.MIDI_NOTE));
	gainValue.setText(device.getValue(Parameter.GAIN));
	velocityCurveValue.setText(device.getValue(Parameter.VEL_CURVE));
	thresholdValue.setText(device.getValue(Parameter.THRESHOLD));
	xtalkValue.setText(device.getValue(Parameter.XTALK));
	retriggerValue.setText(device.getValue(Parameter.RETRIGGER));
	triggerTypeValue.setText(device.getValue(Parameter.TRIGGER_TYPE));
    }
}