package com.diamondcrush.util;


import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class TextWrapper {

    private final BitmapFont fnt;
    private final GlyphLayout layout;

    private float width;
    private float height;

    private String text;
    private Vector2 position;

    public TextWrapper(BitmapFont fnt) {
        this.fnt = fnt;
        this.layout = new GlyphLayout();
        fnt.getData().scaleY = -1;
    }

    public void draw(SpriteBatch sp, Align align) {
        switch (align) {
            case CENTER:
                fnt.draw(sp, text, (position.x + width / 3), (position.y));
                break;
            case LEFT:
                fnt.draw(sp, text, (position.x), (position.y + height / 2));
        }


    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public void setText(String text) {
        this.text = text;
        width = layout.width;
        height = layout.height;
        layout.setText(fnt, text);
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public enum Align {
        CENTER, LEFT
    }
}
