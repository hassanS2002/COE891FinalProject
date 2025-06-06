package app.tuxguitar.awt.graphics;

import java.awt.Font;

import app.tuxguitar.ui.resource.UIFont;

public class AWTFont implements UIFont {

	private Font handle;
	private AWTFontMetrics fontMetrics;

	public AWTFont( Font handle ){
		this.handle = handle;
	}

	public AWTFont(String name, float height, boolean bold, boolean italic){
		this( new Font(name, ( (bold ? Font.BOLD : 0) | (italic ? Font.ITALIC : 0)) , Math.round(height)) );
	}

	public Font getHandle(){
		return this.handle;
	}

	public String getName() {
		return this.handle.getName();
	}

	public float getHeight() {
		return this.handle.getSize();
	}

	public boolean isBold() {
		return this.handle.isBold();
	}

	public boolean isItalic() {
		return this.handle.isItalic();
	}

	public boolean isDisposed(){
		return (this.handle == null);
	}

	public void dispose(){
		this.handle = null;
	}

	public AWTFontMetrics getFontMetrics() {
		if( this.fontMetrics == null ) {
			this.fontMetrics = new AWTFontMetrics(this.getHandle());
		}
		return this.fontMetrics;
	}
}
