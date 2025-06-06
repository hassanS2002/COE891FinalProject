package app.tuxguitar.ui.qt.menu;

import app.tuxguitar.ui.menu.UIMenuCheckableItem;

import org.qtjambi.qt.widgets.QActionGroup;

public class QTMenuRadioItem extends QTMenuCheckableItem implements UIMenuCheckableItem {

	public QTMenuRadioItem(QTAbstractMenu<?> parent) {
		super(parent);

		this.getControl().setActionGroup(this.findActionGroup());
	}

	public QActionGroup findActionGroup() {
		if( this.getParent() != null ) {
			QActionGroup toggleGroup = this.getParent().getData(QActionGroup.class.getName());
			if( toggleGroup != null ) {
				return toggleGroup;
			}
			this.getParent().setData(QActionGroup.class.getName(), new QActionGroup(this.getParent().getControl()));

			return this.findActionGroup();
		}
		return null;
	}
}
