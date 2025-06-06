package app.tuxguitar.ui.qt.widget;

import java.util.ArrayList;
import java.util.List;

import app.tuxguitar.ui.widget.UIControl;
import io.qt.widgets.QWidget;

public abstract class QTAbstractContainer<T extends QWidget> extends QTWidget<T> implements QTContainer {

	private List<QTWidget<? extends QWidget>> children;

	public QTAbstractContainer(T control, QTContainer parent, boolean immediatelyShow) {
		super(control, parent, immediatelyShow);

		this.children = new ArrayList<QTWidget<? extends QWidget>>();
	}

	public QTAbstractContainer(T control, QTContainer parent) {
		this(control, parent, true);
	}

	public List<UIControl> getChildren() {
		List<QTWidget<? extends QWidget>> children = new ArrayList<QTWidget<? extends QWidget>>(this.children);
		for(QTWidget<? extends QWidget> child : children) {
			if( child.isDisposed()) {
				this.removeChild(child);
			}
		}

		return new ArrayList<UIControl>(this.children);
	}

	public void addChild(QTWidget<? extends QWidget> uiControl) {
		if(!this.children.contains(uiControl)) {
			this.children.add(uiControl);
		}
	}

	public void removeChild(QTWidget<? extends QWidget> uiControl) {
		if( this.children.contains(uiControl)) {
			this.children.remove(uiControl);
		}
	}

	public void dispose() {
		List<QTWidget<? extends QWidget>> children = new ArrayList<QTWidget<? extends QWidget>>(this.children);
		for(QTWidget<? extends QWidget> child : children) {
			if(!child.isDisposed()) {
				child.dispose();
			}
		}
		super.dispose();
	}
}
