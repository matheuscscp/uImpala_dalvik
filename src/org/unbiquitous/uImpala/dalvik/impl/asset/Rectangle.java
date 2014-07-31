package org.unbiquitous.uImpala.dalvik.impl.asset;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import org.unbiquitous.uImpala.engine.core.GameComponents;
import org.unbiquitous.uImpala.engine.io.Screen;
import org.unbiquitous.uImpala.util.Color;
import org.unbiquitous.uImpala.util.math.Point;

public class Rectangle extends org.unbiquitous.uImpala.engine.asset.Rectangle {

	private FloatBuffer vertexBuffer;
	private float[] vertices;
	private float angleInDegrees;

	public Rectangle(Point center, Color paint, float width, float height) {
		super(center, paint, width, height);
		calculateVertex();
	}

	private void calculateVertex() {
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(3 * 4 * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		
		List<Point> points = createPointList();
		
		vertices = toVertexArray(points);
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
	}
	private List<Point> createPointList() {
		int xValue = (int) (width/2), yValue = (int) (height/2);
		List<Point> points = Arrays.asList(	new Point(-xValue,-yValue),new Point(xValue,-yValue),
						new Point(-xValue,yValue),new Point(xValue,yValue));
		
		Collections.sort(points, new Comparator<Point>() {
			public int compare(Point a, Point b) {
				return a.x-b.x + a.y-b.y;
			}
		});
		return points;
	}

	private float[] toVertexArray(List<Point> points) {
		float vertices[] = new float[3 * 4];
		int index = 0;
		for(Point p : points){
			vertices[index++] = p.x ;
			vertices[index++] = p.y ;
			vertices[index++] = 0f;
		}
		return vertices;
	}
	
	@Override
	public void render() {
		GL10 gl = GameComponents.get(GL10.class);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
		gl.glLoadIdentity();
		gl.glPushMatrix();
			// Set the face rotation
	//		gl.glFrontFace(GL10.GL_CW);
			
			Screen screen = GameComponents.get(Screen.class);
			gl.glOrthof(0, screen.getWidth(), screen.getHeight(), 0, -1, 1);
			gl.glColor4f(color.red, color.green, color.blue, color.alpha);
			gl.glTranslatef(center.x, center.y, 0);
			gl.glRotatef(angleInDegrees, 0.0f, 0.0f, 1.0f);
			// Point to our vertex buffer
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
	
			// Enable vertex buffer
//			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	
			// Draw the vertices as triangle strip
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
	
			// Disable the client state before leaving
//			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glPopMatrix();
	}

	@Override
	public void rotate(float angleInDegrees) {
		this.angleInDegrees = angleInDegrees;
	}

}
