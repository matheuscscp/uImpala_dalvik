package org.unbiquitous.uImpala.dalvik.impl.io;

import java.util.logging.Level;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.unbiquitous.uImpala.dalvik.impl.core.Game.RenderLock;
import org.unbiquitous.uImpala.engine.core.Game;
import org.unbiquitous.uImpala.engine.core.GameComponents;
import org.unbiquitous.uImpala.engine.core.GameSettings;
import org.unbiquitous.uImpala.engine.io.KeyboardSource;
import org.unbiquitous.uImpala.engine.io.MouseEvent;
import org.unbiquitous.uImpala.engine.io.MouseManager;
import org.unbiquitous.uImpala.engine.io.MouseSource;
import org.unbiquitous.uos.core.UOSLogging;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

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
	
	//////////////////////////////////////////////////////////
	
	private GLSurfaceView mGLView;
	private Activity main;
	private TouchSource touch = new TouchSource();
	protected Dimension size = new Dimension();
	private boolean ready = false;

	@Override
	public void open() {
		this.open(null, -1, -1, true, null, true);
	}
	
	public boolean isReady(){
		return ready;
	}
	
	protected void markReady(){
		ready = true;
	}
	
	@Override
	public void open(String t, int w, int h, boolean f, String i, boolean gl) {
		GameSettings settings = GameComponents.get(GameSettings.class);
		main = (Activity) settings.get("main_activity");
		main.runOnUiThread(new Runnable() {
			public void run() {
				mGLView = new TouchSurfaceView(main,touch);
				if (GameComponents.get(MouseManager.class) != null){
					 GameComponents.get(MouseManager.class).add(touch);
				}
				GameComponents.put(GLSurfaceView.class,mGLView);
				
				cofigView(mGLView);
				main.setContentView(mGLView);
			}
			private void cofigView(GLSurfaceView glView) {
				glView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
				glView.setEGLContextClientVersion(1);
				glView.setRenderer(new GL11Renderer(Screen.this));
				GameComponents.put(org.unbiquitous.uImpala.engine.io.Screen.class, Screen.this);
//				glView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
			}
		});
		synchronized (this) {
			if(!isReady()){
				try {
					this.wait();
				} catch (InterruptedException e) {
					UOSLogging.getLogger().log(Level.SEVERE,"Not possible to init screen", e);
				}
			}
		}
	}

	public int getWidth() {
		return (int) size.getWidth();
	}

	public int getHeight() {
		return (int) size.getHeight();
	}
	
	@Override
	public boolean isCloseRequested() {
		// TODO Auto-generated method stub
		return false;
	}

	public MouseSource getMouse() {
		return touch;
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

	public String getTitle() {return null;}
	/**
	 * Takes no effect on Android
	 */
	public void setTitle(String title) {}


	/**
	 * Takes no effect on Android
	 */
	public void setSize(int width, int height) {}

	public boolean isFullscreen() {return true;}

	/**
	 * Takes no effect on Android
	 */
	public void setFullscreen(boolean fullscreen) {}

	public String getIcon() {return null;}

	/**
	 * Takes no effect on Android
	 */
	public void setIcon(String icon) {}

	
}


class TouchSource extends MouseSource{

	Point position = new Point();
	
	public TouchSource() {
		super(1);
	}
	
	public int getX() {
		return position.x;
	}
	
	public int getY() {
		return position.y;
	}
	
}

class TouchSurfaceView extends GLSurfaceView{

	private TouchSource touch;
	private GL11Renderer renderer;

	public TouchSurfaceView(Context context, TouchSource touch) {
		super(context);
		this.touch = touch;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		touch.position.x = (int) event.getX();
		touch.position.y = (int) event.getY();
		touch.add(new MouseEvent(MouseSource.EVENT_BUTTON_DOWN, touch.position.x, touch.position.y, 0));
		return super.onTouchEvent(event);
	}
	
	public void setRenderer(Renderer renderer) {
		this.renderer = (GL11Renderer) renderer;
		super.setRenderer(renderer);
	}
	
	@Override
	public void onPause() {
//		this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		renderer.pause();
		Log.i("debug","onPause");
	}
	
	@Override
	public void onResume() {
//		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		renderer.resume();
		Log.i("debug","onResume");
	}
	
}

class GL11Renderer implements GLSurfaceView.Renderer {
	
	private boolean active = true;
	private Screen screen;

	public GL11Renderer(Screen screen) {
		this.screen = screen;
	}
	
	public void onDrawFrame(GL10 gl) {
		RenderLock renderLock = GameComponents.get(RenderLock.class);
		if (active ){
			renderLock.notifyRendered();
			clear(gl);
			render(gl);
		}
	}

	void pause() {
		active = false;
	}
	
	void resume(){
		active = true;
	}
	
	private void render(GL10 gl) {
		org.unbiquitous.uImpala.dalvik.impl.core.Game game = (org.unbiquitous.uImpala.dalvik.impl.core.Game) GameComponents
				.get(Game.class);
		if(game != null){
			GameComponents.put(GL10.class, gl);
			game.render();
		}
	}

	private void clear(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (height == 0) { // Prevent A Divide By Zero By
			height = 1; // Making Height Equal One
		}

		screen.size = new Dimension(width, height);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
	    
		gl.glShadeModel(GL10.GL_SMOOTH); // Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Black Background
		gl.glClearDepthf(1.0f); // Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); // Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); // The Type Of Depth Testing To Do
		gl.glHint(GL10.GL_POLYGON_SMOOTH_HINT, GL10.GL_NICEST);
		
	    // enable blend
	    gl.glEnable(GL10.GL_BLEND);
	    gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	    
	    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		// Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
	    
	    // backbuffer size
	    gl.glViewport(0, 0, screen.size.width, screen.size.height);
	    
	    // setup projection
	    gl.glMatrixMode(GL10.GL_PROJECTION);
	    gl.glLoadIdentity();
	    gl.glOrthof(0, screen.size.width, screen.size.height, 0, -1, 1);
//	    
	    // clears matrix
	    gl.glMatrixMode(GL10.GL_MODELVIEW);
	    gl.glLoadIdentity();
		
	    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		GameComponents.put(GL10.class, gl);
		synchronized (screen){
			screen.markReady();
			screen.notifyAll();
		}
		Log.i("debug","onSurfaceChanged");
	}

	/**
	 * The Surface is created/init()
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		/*gl.glEnable(GL10.GL_TEXTURE_2D); // enable texture

		gl.glShadeModel(GL10.GL_SMOOTH); // Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Black Background
		gl.glClearDepthf(1.0f); // Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); // Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); // The Type Of Depth Testing To Do
		gl.glHint(GL10.GL_POLYGON_SMOOTH_HINT, GL10.GL_NICEST);
		gl.glEnable(GL10.GL_BLEND);// enable blend
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
//		gl.glBlendFunc(GL10.GL_SRC_ALPHA_SATURATE, GL10.GL_ONE);

//		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		// Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);*/
		
		GameComponents.put(GL10.class, gl);
		Log.i("debug","onSurfaceCreated");
	}

}


class Dimension{
	int width, height;

	public Dimension() {}
	
	public Dimension(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}