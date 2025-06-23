package com.javakaian.game.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * @author javakaian
 */
public abstract class GameObject {

    public Vector2 position;

    public Vector2 size;

    public Vector2 center;

    protected Sprite sprite;

    protected Sprite spriteSelected;

    protected boolean visible;

    protected Rectangle boundRect;

    protected boolean isSelected = false;

    public GameObject(float x, float y, float width, float height) {
        this.position = new Vector2(x, y);
        this.size = new Vector2(width, height);
        this.center = new Vector2(position.x + size.x / 2, position.y + size.y / 2);
        this.boundRect = new Rectangle(x, y, this.size.x, this.size.y);
        this.visible = true;
    }

    public void render(ShapeRenderer sr) {
        if (visible) {
            sr.rect(this.position.x, this.position.y, this.size.x, this.size.y);
        }
    }

    public void render(SpriteBatch sb) {
        if (!visible)
            return;
        if (isSelected) {
            sb.draw(spriteSelected, this.position.x, this.position.y, this.size.x, this.size.y);
        } else {
            sb.draw(sprite, this.position.x, this.position.y, this.size.x, this.size.y);
        }
    }

    public void update(float deltaTime) {

        // update position
        boundRect.x = position.x;
        boundRect.y = position.y;
        boundRect.width = size.x;
        boundRect.height = size.y;

        // update center
        center.x = position.x + size.x / 2;
        center.y = position.y + size.y / 2;
    }
    public Vector2 getPosition() {
        return position;
    }
}
