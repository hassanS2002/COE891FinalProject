package app.tuxguitar.ui.qt.event;

import app.tuxguitar.ui.event.UIKeyEvent;
import app.tuxguitar.ui.event.UIKeyPressedListenerManager;
import app.tuxguitar.ui.qt.QTComponent;
import app.tuxguitar.ui.qt.resource.QTKey;
import io.qt.core.QEvent;
import io.qt.gui.QKeyEvent;

public class QTKeyPressedListenerManager extends UIKeyPressedListenerManager implements QTEventHandler {

	private QTComponent<?> control;

	public QTKeyPressedListenerManager(QTComponent<?> control) {
		this.control = control;
	}

	public void handle(QKeyEvent event) {
		this.onKeyPressed(new UIKeyEvent(this.control, QTKey.getCombination(event)));
	}

	public boolean handle(QEvent event) {
		this.handle((QKeyEvent) event);

		return true;
	}
}
