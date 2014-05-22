package org.unbiquitous.uImpala.dalvik.impl.io;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.unbiquitous.uImpala.engine.core.Game;
import org.unbiquitous.uImpala.engine.core.GameComponents;
import org.unbiquitous.uImpala.engine.core.GameSettings;
import org.unbiquitous.uImpala.engine.io.KeyboardSource;
import org.unbiquitous.uImpala.engine.io.MouseSource;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.util.Log;

public class Screen extends org.unbiquitous.uImpala.engine.io.Screen {
	private static class Factory implements
			org.unbiquitous.uImpala.engine.io.Screen.Factory {
		public Screen create() {
			if (screen != null)
				return null;
			screen = new Screen();
			return screen;
		}
	}

	public static synchronized void initImpl() {
		if (factory == null)
			factory = new Factory();
	}

	private static Screen screen = null;
	GLSurfaceView mGLView;
	Activity main;

	@Override
	public void open(String t, int w, int h, boolean f, String i, boolean gl) {
		Log.i("debug","start screen");
		// TODO Auto-generated method stub
		GameSettings settings = GameComponents.get(GameSettings.class);
		main = (Activity) settings.get("main_activity");
		main.runOnUiThread(new Runnable() {
			public void run() {
				mGLView = new GLSurfaceView(main);
				mGLView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
				mGLView.setEGLContextClientVersion(1);
				mGLView.setRenderer(new GL11Renderer());
//				mGLView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
				main.setContentView(mGLView);
			}
		});

		
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTitle(String title) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isFullscreen() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFullscreen(boolean fullscreen) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIcon(String icon) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isCloseRequested() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MouseSource getMouse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public KeyboardSource getKeyboard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void update() {
		// TODO Auto-generated method stub
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isUpdating() {
		// TODO Auto-generated method stub
		return false;
	}

}

class GL11Renderer implements GLSurfaceView.Renderer {
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();

		org.unbiquitous.uImpala.dalvik.impl.core.Game game = (org.unbiquitous.uImpala.dalvik.impl.core.Game) GameComponents
				.get(Game.class);
		if(game != null){
			GameComponents.put(GL10.class, gl);
			//TODO: How to handle the FrameRate ?
			game.render();
		}
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0) { // Prevent A Divide By Zero By
			height = 1; // Making Height Equal One
		}

		gl.glViewport(0, 0, width, height); // Reset The Current Viewport
		gl.glOrthof(0, width, height, 0, 0, 1); // Set to use 2D Graphs with
												// Cartesian coordinates
		GameComponents.put(GL10.class, gl);
		Log.i("debug","onSurfaceChanged");
	}

	/**
	 * The Surface is created/init()
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glEnable(GL10.GL_TEXTURE_2D); // enable texture

		gl.glShadeModel(GL10.GL_SMOOTH); // Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Black Background
		gl.glClearDepthf(1.0f); // Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); // Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); // The Type Of Depth Testing To Do

		// enable blend
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		// Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		GameComponents.put(GL10.class, gl);
		Log.i("debug","onSurfaceCreated");
	}

}
