package org.unbiquitous.uImpala.dalvik;

import org.unbiquitous.uImpala.dalvik.impl.core.Game;
import org.unbiquitous.uImpala.engine.core.GameSingletons;
import org.unbiquitous.uImpala.engine.core.GameSettings;
import org.unbiquitous.uos.core.ClassLoaderUtils;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity {

	public void run(GameSettings settings) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // (NEW)
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // (NEW)

		GameSingletons.get(Class.class); // Force everybody to be on the same
											// Thread Tree

		ClassLoaderUtils.builder = new ClassLoaderUtils.DefaultClassLoaderBuilder() {
			public ClassLoader getParentClassLoader() {
				return GameActivity.this.getClassLoader();
			};
		};
		Game.run(settings);
	}

	@Override
	protected void onResume() {
		super.onResume();
		GLSurfaceView glSurface = GameSingletons.get(GLSurfaceView.class);
		if(glSurface != null){
			glSurface.onResume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		GLSurfaceView glSurface = GameSingletons.get(GLSurfaceView.class);
		if(glSurface != null){
			glSurface.onPause();
		}
	}

}