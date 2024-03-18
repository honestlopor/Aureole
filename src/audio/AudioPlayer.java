package audio;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import tiles.Floor;
import tiles.Tile;

public class AudioPlayer {
	
	public static int MENU_1 = 0;
	public static int WORLD = 1;
	public static int HOUSE = 2;
	public static int CAVE = 3;
	public static int BOSSROOM = 4;
	
	public static int DIE = 0;
	public static int GAMEOVER = 1;
	public static int TILE_COMPLETED = 2;
	public static int ATTACK = 3;
	
	private Clip[] songs, effects;
	private int currentSongId;
	private float volume = 0.75f;
	private boolean songMute, effectMute;
	
	public AudioPlayer() {
		loadSongs();
		loadEffects();
		playSong(MENU_1);
	}
	
	private void loadSongs() {
		String[] names = {"menu", "world", "house", "cave", "bossroom"};
		songs = new Clip[names.length];
		for (int i = 0; i < songs.length; i++)
			songs[i] = getClip(names[i]);
	}
	
	private void loadEffects() {
		String[] effectNames = {"die", "gameover", "gamecompleted", "attack"};
		effects = new Clip[effectNames.length];
		for (int i = 0; i < effects.length; i++)
			effects[i] = getClip(effectNames[i]);
		
		updateEffectsVolume();
		
	}
	
	private Clip getClip(String name) {
		URL url = getClass().getResource("/audio/" + name + ".wav");
		AudioInputStream audio;
		
		try {
			audio = AudioSystem.getAudioInputStream(url);
			Clip c = AudioSystem.getClip();
			c.open(audio);
			return c;
			
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public void setVolume(float volume) {
		this.volume = volume;
		updateSongVolume();
		updateEffectsVolume();
	}
	
	public void stopSong() {
		if (songs[currentSongId].isActive())
			songs[currentSongId].stop();
	}
	
	public void setTileSong(Tile currentTile) {
//		System.out.println(currentTile.getFloorType());

		if (currentTile.getFloorType() == Floor.WORLD) 
			playSong(WORLD);
		if (currentTile.getFloorType() == Floor.HOUSE) 
			playSong(HOUSE);
		if (currentTile.getFloorType() == Floor.CAVE) 
			playSong(CAVE);
		if (currentTile.getFloorType() == Floor.BOSSROOM) 
			playSong(BOSSROOM);

	}
	
	public void tileCompleted() {
		stopSong();
//		playEffect(TILE_COMPLETED);
	}
	
	public void playAttackSound() {
		playEffect(3);
	}
	
	public void playEffect(int effect) {
		effects[effect].setMicrosecondPosition(0);
		effects[effect].start();
		
	}
	
	public void playSong(int song) {
		stopSong();
		
		currentSongId = song;
		updateSongVolume();
		songs[currentSongId].setMicrosecondPosition(0);
		songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void toggleSongMute() {
		this.songMute = !songMute;
		for (Clip c : songs) {
			BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
			booleanControl.setValue(songMute);
		}
	}
	
	public void toggleEffectMute() {
		this.effectMute = !effectMute;
		for (Clip c : effects) {
			BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
			booleanControl.setValue(effectMute);
		}
	}
	
	private void updateSongVolume() {
		
		FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
		float range = gainControl.getMaximum() - gainControl.getMinimum();
		float gain = (range * volume) + gainControl.getMinimum();
		gainControl.setValue(gain);
		
	}
	
	private void updateEffectsVolume() {
		for (Clip c : effects) {
			FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
			float range = gainControl.getMaximum() - gainControl.getMinimum();
			float gain = (range * volume) + gainControl.getMinimum();
			gainControl.setValue(gain);
			
		}
	}
	
	
}
