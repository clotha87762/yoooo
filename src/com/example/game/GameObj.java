package com.example.game;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;


public class GameObj {

	/**
	 * 物件顯示角度
	 */
	public float angle = 0;
	
	/**
	 * 物件影像資源
	 */
	public Drawable drawable;
	
	/**
	 * 是否顯示
	 */
	public boolean Visible = true;
	
	/**
	 * 控制致能
	 */
	public boolean Enable = true;
	
	/**
	 * 暫存的物件位置
	 */
	private Rect saveRect;
	
	/**
	 * 暫存的物件角度
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
	 * 儲存目前物件狀態
	 */
	public void save() {
		if (Enable) {
			saveRect = drawable.copyBounds();
			saveAngle = angle;
		}
	}

	/**
	 * 恢復物件狀態
	 */
	public void restore() {
		if (Enable) {
			drawable.setBounds(saveRect);
			angle = saveAngle;
		}
	}

	
	
	/**
	 * 旋轉物件
	 */
	public void rotate(float angle) {
		if (Enable) {
			this.angle += angle;
			this.angle %= 360;
		}
	}

	
	/**
	 * 設定物件角度
	 */
	public void setAngle(float angle) {
		if (Enable) {
			this.angle = angle;
			this.angle %= 360;
		}
	}

	/**
	 * 得到物件角度 
	 */
	public float getAngle(float angle) {
		return angle;
	}

	
	/**
	 * 移動物件到新的座標點上
	 */
	public void moveTo(int newLeft, int newTop) {
		if (Enable) {
			Rect rect = drawable.getBounds();
			drawable.setBounds(newLeft, newTop, newLeft + rect.width(), newTop
					+ rect.height());
		}
	}
	
	/**
	 * 移動物件到新的座標點上
	 */
	public void moveTo(float newLeft, float newTop) {
		moveTo((int)newLeft,(int)newTop);
	}

	/**
	 * 物件移動一個向量距離
	 */
	public void move(int dx, int dy) {
		if (Enable) {
			Rect rect = drawable.getBounds();
			drawable.setBounds(rect.left + dx, rect.top + dy, rect.right + dx,
					rect.bottom + dy);
		}
	}
	
	/**
	 * 物件移動一個向量距離
	 */
	public void move(float dx, float dy) {
		move((int)dx,(int)dy);
	}
	
	/**
	 * 物件範圍縮放
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
	 * 得到物件中心X座標
	 */
	public int centerX() {
		return drawable.getBounds().centerX();
	}

	/**
	 *得到物件中心Y座標
	 */
	public int centerY() {
		return drawable.getBounds().centerY();
	}

	/**
	 * 得到物件範圍
	 */
	public Rect getRect() {
		return drawable.getBounds();
	}

	/**
	 * 得到物件高度
	 */
	public int getHeight() {
		return drawable.getBounds().height();
	}

	/**
	 * 得到物件寬度
	 */
	public int getWidth() {
		return drawable.getBounds().width();
	}

	/**
	 * 得到原始影像高度
	 */
	public int getSrcHeight() {
		return drawable.getIntrinsicHeight();
	}

	/**
	 * 得到原始影像寬度
	 */
	public int getSrcWidth() {
		return drawable.getIntrinsicWidth();
	}

	/**
	 * 設定物件範圍
	 */
	public void setRect(Rect rect) {
		drawable.setBounds(rect);
	}

	/**
	 * 設定物件範圍
	 */
	public void setRect(int left, int top, int right, int bottom) {
		drawable.setBounds(left, top, right, bottom);

	}

	/**
	 * 判斷物件使否與參數範圍相交
	 * 當相交時自動調整物件範圍
	 */
	public boolean intersect(Rect r) {
		return drawable.getBounds().intersect(r);
	}
	
	/**
	 * 判斷物件使否與參數範圍相交
	 * 當相交時自動調整物件範圍
	 */
	public boolean intersect(GameObj obj) {
		return this.intersect(obj.getRect());
	}
	
	/**
	 * 判斷物件範圍是否包函與參數範圍
	 */
	public boolean contains(Rect r) {
		return drawable.getBounds().contains(r);
	}
	
	/**
	 * 判斷物件範圍是否包函與參數範圍
	 */
	public boolean contains(GameObj obj) {
		return this.contains(obj.getRect());
	}
	
	/**
	 * 判斷物件範圍是否包函與參數點
	 */
	public boolean contains(int x,int y) {
		return drawable.getBounds().contains(x, y);
	}
	
}
