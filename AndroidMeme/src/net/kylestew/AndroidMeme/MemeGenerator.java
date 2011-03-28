package net.kylestew.AndroidMeme;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import net.kylestew.AndroidLibs.RESTClient.RESTCall;
import net.kylestew.AndroidLibs.RESTClient.RESTCall.RestVerb;
import net.kylestew.AndroidLibs.RESTClient.RESTConnector;
import net.kylestew.AndroidLibs.RESTClient.RESTResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;


public class MemeGenerator extends Activity {

	public final static String SERVER = "http://memegen.heroku.com"; // remember the 'http'
	
	private Spinner memeType;
	private EditText firstLine;
	private EditText secondLine;
	private ProgressDialog loadingDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meme_generator);

		// Cache UI handles
		memeType = (Spinner) findViewById(R.id.memeType);
		firstLine = (EditText) findViewById(R.id.firstLine);
		secondLine = (EditText) findViewById(R.id.secondLine);

		// Bind meme types to spinner
		bindSpinner();
	}

	private void bindSpinner() {
		// Use an ArrayAdapter to bind an array of Memes to the spinner
		ArrayList<Meme> memes = Meme.All();
		ArrayAdapter<Meme> adapter = new ArrayAdapter<Meme>(this, android.R.layout.simple_spinner_item, memes);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		memeType.setAdapter(adapter);
	}

	private void showMeme(final String memeType, final String imageUrl) {
		// Inflate layout we are going to use for the dialog
		View view = getLayoutInflater().inflate(R.layout.meme_view, null);

		// Cache handles to view elements
		ImageView memeImage = (ImageView) view.findViewById(R.id.image);
		ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

		// Start downloading image in a background thread
		BitmapDownloaderTask task = new BitmapDownloaderTask(memeImage, progressBar);
		task.execute(imageUrl);
		
		// Build the alert dialog with our view
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Your New Meme");
		builder.setView(view);
		builder.setNeutralButton("Awesome!", null);
		builder.setPositiveButton("Share", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_SUBJECT, memeType);
				String url = imageUrl.substring(0, imageUrl.lastIndexOf("?"));
				intent.putExtra(Intent.EXTRA_TEXT, url);
				startActivity(Intent.createChooser(intent, "Share your meme"));
			}
		});
		builder.show();
	}

	public void generate(View view) {
		// Show a loading spinner
		loadingDialog = ProgressDialog.show(this, "", "Generating meme", true);

		// Make a REST call
		Observer observer = new Observer() {

			@Override
			public void update(Observable observable, Object data) {
				RESTResponse response = (RESTResponse) data;
				if (response.hasError()) {
					// Build an error dialog
					AlertDialog.Builder builder = new AlertDialog.Builder(MemeGenerator.this);
					builder.setTitle("Error");
					builder.setMessage("Something wen't wrong");
					builder.setPositiveButton("Ok", null);
					builder.show();
				} else {
					// Pull the image URL from the JSON response
					JSONArray jsonArray = response.getJsonArray();
					if (jsonArray != null) {
						JSONObject jsonObject = jsonArray.optJSONObject(0);
						if (jsonObject != null) {
							jsonObject = jsonObject.optJSONObject("meme");
							if (jsonObject != null) {
								String imageUrl = jsonObject.optString("image_url");
								String memeType = jsonObject.optString("meme_type").replace("_", " ");
								showMeme(memeType, imageUrl);
							}
						}
					}
				}

				// Dismiss loading spinner
				loadingDialog.dismiss();
			}
		};

		RESTCall restCall = new RESTCall();
		restCall.addObserver(observer);
		restCall.setRestVerb(RestVerb.POST);
		restCall.setExpectedResult(RESTCall.CREATED_201);
		restCall.setUrl(SERVER + "/memes");
		Meme selectedMemeType = (Meme) memeType.getSelectedItem();
		restCall.putParam("meme", "meme_type", selectedMemeType.getType());
		restCall.putParam("meme", "first_line", firstLine.getText().toString());
		restCall.putParam("meme", "second_line", secondLine.getText().toString());
		new RESTConnector().execute(restCall);
	}

}