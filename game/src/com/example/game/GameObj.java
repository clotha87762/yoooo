package com.example.game;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;


public class GameObj {

	/**
	 * ������ܨ���
	 */
	public float angle = 0;
	
	/**
	 * ����v���귽
	 */
	public Drawable drawable;
	
	/**
	 * �O�_���
	 */
	public boolean Visible = true;
	
	/**
	 * ����P��
	 */
	public boolean Enable = true;
	
	/**
	 * �Ȧs�������m
	 */
	private Rect saveRect;
	
	/**
	 * �Ȧs�����󨤫�
	 */
	public float saveAngle;

	public GameObj(Drawable drawable) {
		this.drawable = drawable;
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
				.getIntrinsicHeight());
		this.save();
	}
	public GameObj(GameObj gameObj,Drawable drawable) {
		this.drawable = drawable;
		this.drawable.setBounds(gameObj.drawable.copyBounds());
		this.angle=gameObj.angle;
		this.save();
	}
	
	/**
	 * �x�s�ثe���󪬺A
	 */
	public void save() {
		if (Enable) {
			saveRect = drawable.copyBounds();
			saveAngle = angle;
		}
	}

	/**
	 * ��_���󪬺A
	 */
	public void restore() {
		if (Enable) {
			drawable.setBounds(saveRect);
			angle = saveAngle;
		}
	}

	
	
	/**
	 * ���ફ��
	 */
	public void rotate(float angle) {
		if (Enable) {
			this.angle += angle;
			this.angle %= 360;
		}
	}

	
	/**
	 * �]�w���󨤫�
	 */
	public void setAngle(float angle) {
		if (Enable) {
			this.angle = angle;
			this.angle %= 360;
		}
	}

	/**
	 * �o�쪫�󨤫� 
	 */
	public float getAngle(float angle) {
		return angle;
	}

	
	/**
	 * ���ʪ����s���y���I�W
	 */
	public void moveTo(int newLeft, int newTop) {
		if (Enable) {
			Rect rect = drawable.getBounds();
			drawable.setBounds(newLeft, newTop, newLeft + rect.width(), newTop
					+ rect.height());
		}
	}
	
	/**
	 * ���ʪ����s���y���I�W
	 */
	public void moveTo(float newLeft, float newTop) {
		moveTo((int)newLeft,(int)newTop);
	}

	/**
	 * ���󲾰ʤ@�ӦV�q�Z��
	 */
	public void move(int dx, int dy) {
		if (Enable) {
			Rect rect = drawable.getBounds();
			drawable.setBounds(rect.left + dx, rect.top + dy, rect.right + dx,
					rect.bottom + dy);
		}
	}
	
	/**
	 * ���󲾰ʤ@�ӦV�q�Z��
	 */
	public void move(float dx, float dy) {
		move((int)dx,(int)dy);
	}
	
	/**
	 * ����d���Y��
	 */
	public void scale(int addScaleX, int addScaleY) {
		if (Enable) {
			Rect rect = drawable.getBounds();
			drawable.setBounds(rect.left - addScaleX, rect.top - addScaleY,
					rect.right + addScaleX, rect.bottom + addScaleY);
		}
	}

	public void draw(Canvas canvas) {
		if (Visible) {
			canvas.save();
			canvas.rotate(angle, drawable.getBounds().centerX(), drawable
					.getBounds().centerY());
			drawable.draw(canvas);
			canvas.restore();
		}
	}

	/**
	 * �o�쪫�󤤤�X�y��
	 */
	public int centerX() {
		return drawable.getBounds().centerX();
	}

	/**
	 *�o�쪫�󤤤�Y�y��
	 */
	public int centerY() {
		return drawable.getBounds().centerY();
	}

	/**
	 * �o�쪫��d��
	 */
	public Rect getRect() {
		return drawable.getBounds();
	}

	/**
	 * �o�쪫�󰪫�
	 */
	public int getHeight() {
		return drawable.getBounds().height();
	}

	/**
	 * �o�쪫��e��
	 */
	public int getWidth() {
		return drawable.getBounds().width();
	}

	/**
	 * �o���l�v������
	 */
	public int getSrcHeight() {
		return drawable.getIntrinsicHeight();
	}

	/**
	 * �o���l�v���e��
	 */
	public int getSrcWidth() {
		return drawable.getIntrinsicWidth();
	}

	/**
	 * �]�w����d��
	 */
	public void setRect(Rect rect) {
		drawable.setBounds(rect);
	}

	/**
	 * �]�w����d��
	 */
	public void setRect(int left, int top, int right, int bottom) {
		drawable.setBounds(left, top, right, bottom);

	}

	/**
	 * �P�_����ϧ_�P�Ѽƽd��ۥ�
	 * ��ۥ�ɦ۰ʽվ㪫��d��
	 */
	public boolean intersect(Rect r) {
		return drawable.getBounds().intersect(r);
	}
	
	/**
	 * �P�_����ϧ_�P�Ѽƽd��ۥ�
	 * ��ۥ�ɦ۰ʽվ㪫��d��
	 */
	public boolean intersect(GameObj obj) {
		return this.intersect(obj.getRect());
	}
	
	/**
	 * �P�_����d��O�_�]��P�Ѽƽd��
	 */
	public boolean contains(Rect r) {
		return drawable.getBounds().contains(r);
	}
	
	/**
	 * �P�_����d��O�_�]��P�Ѽƽd��
	 */
	public boolean contains(GameObj obj) {
		return this.contains(obj.getRect());
	}
	
	/**
	 * �P�_����d��O�_�]��P�Ѽ��I
	 */
	public boolean contains(int x,int y) {
		return drawable.getBounds().contains(x, y);
	}
	
}
