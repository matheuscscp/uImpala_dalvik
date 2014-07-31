package org.unbiquitous.uImpala.dalvik.impl.asset;

import java.awt.Font;

import org.unbiquitous.uImpala.engine.asset.Rectangle;
import org.unbiquitous.uImpala.engine.asset.SimetricShape;
import org.unbiquitous.uImpala.util.Color;
import org.unbiquitous.uImpala.util.math.Point;

public class AssetManager extends
		org.unbiquitous.uImpala.engine.asset.AssetManager {
	private static class Factory implements
			org.unbiquitous.uImpala.engine.asset.AssetManager.Factory {
		public AssetManager create() {
			return new AssetManager();
		}
	}

	public static synchronized void initImpl() {
		if (factory == null)
			factory = new Factory();
	}

	@Override
	public Sprite newSprite(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Text newText(String fontPath, String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Text newText(Font font, String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Audio newAudio(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public SimetricShape newSimetricShape(Point center, Color paint,
			float radius, int numberOfSides) {
		return new org.unbiquitous.uImpala.dalvik.impl.asset.SimetricShape(center, paint, radius, numberOfSides);
	}

	@Override
	public SimetricShape newCircle(Point center, Color paint, float radius) {
		return new org.unbiquitous.uImpala.dalvik.impl.asset.SimetricShape(center, paint, radius, 72);
	}

	@Override
	public Rectangle newRectangle(Point center, Color paint, float width,
			float height) {
		return new org.unbiquitous.uImpala.dalvik.impl.asset.Rectangle(center, paint, width, height);
	}

}
