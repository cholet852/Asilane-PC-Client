/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.asilane.core;

import java.util.Locale;

import javazoom.jl.player.Player;

import com.darkprograms.speech.synthesiser.PlayerThread;
import com.darkprograms.speech.synthesiser.Synthesiser;

/**
 * This is for running the IA response speeched in another thread
 * 
 * @author walane
 */
public class TextToSpeechThread implements Runnable {
	private static TextToSpeechThread INSTANCE;
	private String textToSpeech;
	private Locale lang;
	private Player player;
	private PlayerThread playerThread;

	public static TextToSpeechThread getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TextToSpeechThread();
		}
		return INSTANCE;
	}

	/**
	 * play the IA response speeched in another thread
	 * 
	 * @param textToSpeech
	 */
	public void textToSpeech(final String textToSpeech, final Locale lang) {
		// One play at once
		stopSpeech();
		this.textToSpeech = textToSpeech;
		this.lang = lang;
	}

	@SuppressWarnings("deprecation")
	public void stopSpeech() {
		if (playerThread != null && playerThread.isAlive()) {
			playerThread.stop();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			final Synthesiser synthesiser = new Synthesiser(lang.toString());
			player = new Player(synthesiser.getMP3Data(textToSpeech));
			playerThread = new PlayerThread(player);
			playerThread.start();
		} catch (final Exception e) {
			System.out.println("Google text to speech not avaible.");
		}
	}
}