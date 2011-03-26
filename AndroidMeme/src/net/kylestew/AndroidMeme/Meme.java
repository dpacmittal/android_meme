package net.kylestew.AndroidMeme;

import java.util.ArrayList;

public class Meme {

	private static final String[] MEMES = { "Y_U_NO", "Why you no", "XZIBIT", "XZIBIT" };

	private String type;
	private String name;

	public Meme(String type, String name) {
		this.type = type;
		this.name = name;
	}

	public static ArrayList<Meme> All() {
		// If you write code like this, your team will hate you
		ArrayList<Meme> memes = new ArrayList<Meme>();
		for (int i = 0; i < MEMES.length; i++) {
			memes.add(new Meme(MEMES[i++], MEMES[i]));
		}
		return memes;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		// The ArrayAdapter calls this to get the spinner item text
		return name;
	}

}
