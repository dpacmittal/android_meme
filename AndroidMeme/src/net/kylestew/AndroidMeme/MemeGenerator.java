package net.kylestew.AndroidMeme;

import android.app.Activity;
import android.os.Bundle;

public class MemeGenerator extends Activity {
	
	public final static String SERVER = "http://10.0.2.2:3000"; // emulator localhost
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meme_generator);
    }
    
}