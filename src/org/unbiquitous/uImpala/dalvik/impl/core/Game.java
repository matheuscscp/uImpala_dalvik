package org.unbiquitous.uImpala.dalvik.impl.core;

import org.unbiquitous.uImpala.dalvik.impl.asset.AssetManager;
import org.unbiquitous.uImpala.dalvik.impl.io.Screen;
import org.unbiquitous.uImpala.dalvik.impl.io.Speaker;
import org.unbiquitous.uImpala.dalvik.impl.time.Time;
import org.unbiquitous.uImpala.engine.core.GameSettings;

import android.app.Activity;
import android.os.AsyncTask;

public class Game extends org.unbiquitous.uImpala.engine.core.Game {
  public static void run(final GameSettings settings) {
	  Activity main = (Activity) settings.get("main_activity");
	  if (main == null){
		  throw new RuntimeException("Please set a 'main_activity' property to start the game.");
	  }
	  new AsyncTask<Void, Void, Void>() {
		protected Void doInBackground(Void ... params) {
			run(Game.class.getName(), settings);
			return null;
		}
	}.execute();
    
  }
  
  protected void initImpl() {
    AssetManager.initImpl();
    Time.initImpl();
    Screen.initImpl();
    Speaker.initImpl();
  }
  
  @Override
	protected void renderScenes() {
		// Nothing to do here, this is done by the Renderer
	}
  
  public void render(){
	  super.renderScenes();
  }
}

