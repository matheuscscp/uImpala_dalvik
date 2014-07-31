package org.unbiquitous.uImpala.dalvik.impl.core;

import org.unbiquitous.uImpala.dalvik.impl.asset.AssetManager;
import org.unbiquitous.uImpala.dalvik.impl.io.Screen;
import org.unbiquitous.uImpala.dalvik.impl.io.Speaker;
import org.unbiquitous.uImpala.dalvik.impl.time.Time;
import org.unbiquitous.uImpala.engine.core.GameComponents;
import org.unbiquitous.uImpala.engine.core.GameSettings;

import android.app.Activity;
import android.os.AsyncTask;

public class Game extends org.unbiquitous.uImpala.engine.core.Game {
	public static void run(final GameSettings settings) {
		Activity main = (Activity) settings.get("main_activity");
		if (main == null) {
			throw new RuntimeException(
					"Please set a 'main_activity' property to start the game.");
		}
		new AsyncTask<Void, Void, Void>() {
			protected Void doInBackground(Void... params) {
				run(Game.class, settings);
				return null;
			}
		}.execute();

	}

	protected void initImpl() {
		AssetManager.initImpl();
		Time.initImpl();
		Screen.initImpl();
		Speaker.initImpl();
		GameComponents.put(RenderLock.class, new RenderLock());
	}

	@Override
	protected void renderScenes() {
		GameComponents.get(RenderLock.class).allowToRender();
	}

	public static class RenderLock {

		private boolean canRender = false;

		public void waitForRender() {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}

		public void allowToRender() {
			synchronized (this) {
				this.notifyAll();
			}
		}

		public boolean canRender() {
			return canRender;
		}

	}

	public void render() {
		super.renderScenes();
	}
}
