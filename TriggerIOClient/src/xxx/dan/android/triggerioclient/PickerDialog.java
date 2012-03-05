package xxx.dan.android.triggerioclient;

import Client.DeviceClient;
import Client.DeviceClient.Parameter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

// --------------------------------------------
public class PickerDialog extends AlertDialog {
	final String TAG = PickerDialog.class.getName();

	DeviceClient device;
	Parameter parameter;
	TextView valueText;
	Context context;
	final int originalKey;
	
	// --------------------------------------------
	public PickerDialog(Context context, Parameter parameter, DeviceClient device) {
	    super(context);
	    this.context = context;
	    this.device = device;
	    this.parameter = parameter;
	    originalKey = device.getkey(parameter);
	    setup(); 
	}

	// ---------------------------------------
	private void setup() {
	    
	    Log.v(TAG, "setup parameter=<"+parameter+">");
	    
	    LayoutInflater factory = LayoutInflater.from(context);
	    final View pickerDialogView = factory.inflate(
		    R.layout.picker_dialog, null);

	    setTitle(getTitle());
	    setView(pickerDialogView);
	    
	    setButton(AlertDialog.BUTTON_NEGATIVE,
		    context.getResources().getString(R.string.reset),
		    new DialogInterface.OnClickListener() {
		        
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		            updateKey(originalKey);		    	
		        }
		    });	
	    
	    Log.v(TAG, "before setButton");
	    setButton(AlertDialog.BUTTON_POSITIVE,
		    context.getResources().getString(R.string.ok),
		    new DialogInterface.OnClickListener() {
		        
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		            dismiss();
		    	
		        }
		    });
  	    
	    Log.v(TAG, "before minusButton");
	    Button minusButton = (Button) pickerDialogView.findViewById(R.id.minusButton);
	    minusButton.setText(context.getResources().getString(R.string.minus_button));
	    minusButton.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
		    updateKey(false);
		}
	    });

	    Log.v(TAG, "before plusButton");
	    Button plusButton = (Button) pickerDialogView.findViewById(R.id.plusButton);
	    plusButton.setText(context.getResources().getString(R.string.plus_button));
	    plusButton.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
		    updateKey(true);
		}
	    });
	    
	    valueText = (TextView) pickerDialogView.findViewById(R.id.keyText);
	    valueText.setText(device.getValue(parameter));
	}
	
	// ---------------------------------------------
	public void updateKey(int newKey){
	    boolean updated = device.sendKey(parameter, newKey);

	    Log.v(TAG, "updated=<"+String.valueOf(updated)+">");
	    
	    if (updated) {
		valueText.setText(device.getValue(parameter));
	    } else {
		Toast.makeText(context, context.getResources().getString(R.string.message_failed), Toast.LENGTH_SHORT).show();
	    }
	}
	
	// ---------------------------------------------
	public void updateKey(boolean isPlus) {
	    int delta = (isPlus ? 1 : -1);
	    int oldKey = device.getkey(parameter);
	    int newKey = oldKey + delta;

	    Log.v(TAG, "newKey=<"+newKey+">, delta=<"+delta+">, oldKey=<"+oldKey+">");
	    updateKey(newKey);
	}
	
	// --------------------------------------------
	public String getTitle() {
	    Log.v(TAG, "getTitle");
	    String title = null;

	    switch (parameter) {
	    case KIT:
		title = context.getResources().getString(R.string.kit_title);
		break;
	    case INPUT:
		title = context.getResources().getString(R.string.input_title);
		break;
	    case PROG_CHANGE:
		title = context.getResources().getString(R.string.program_change_title);
		break;
	    case MIDI_CHANNEL:
		title = context.getResources().getString(R.string.midi_channel_title);
		break;
	    case MIDI_NOTE:
		title = context.getResources().getString(R.string.midi_note_title);
		break;
	    case GAIN:
		title = context.getResources().getString(R.string.gain_title);
		break;
	    case RETRIGGER:
		title = context.getResources().getString(R.string.retrigger_title);
		break;
	    case THRESHOLD:
		title = context.getResources().getString(R.string.threshold_title);
		break;
	    case TRIGGER_TYPE:
		title = context.getResources().getString(R.string.trigger_type_title);
		break;
	    case VEL_CURVE:
		title = context.getResources().getString(R.string.velocity_curve_title);
		break;
	    case XTALK:
		title = context.getResources().getString(R.string.xtalk_title);
		break;
	    }
	    Log.v(TAG, "return=<"+title+">");
	    return title;
	}
}
