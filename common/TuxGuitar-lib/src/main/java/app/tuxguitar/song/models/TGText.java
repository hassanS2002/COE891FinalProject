package app.tuxguitar.song.models;

import app.tuxguitar.song.factory.TGFactory;

public class TGText{

	private String value;
	private TGBeat beat;

	public TGText(){
		super();
	}

	public TGBeat getBeat() {
		return this.beat;
	}

	public void setBeat(TGBeat beat) {
		this.beat = beat;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isEmpty(){
		return (this.value == null || this.value.length() == 0);
	}

	public void copyFrom(TGText text) {
		this.setValue(text.getValue());
	}

	public TGText clone(TGFactory factory) {
		TGText tgText = factory.newText();
		tgText.copyFrom(this);
		return tgText;
	}
}
