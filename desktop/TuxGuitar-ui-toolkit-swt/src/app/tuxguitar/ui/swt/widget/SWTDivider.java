package app.tuxguitar.ui.swt.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import app.tuxguitar.ui.resource.UISize;
import app.tuxguitar.ui.widget.UIDivider;

public class SWTDivider extends SWTControl<Composite> implements UIDivider {

	private static final float DEFAULT_PACKED_SIZE = 2f;

	public SWTDivider(SWTContainer<? extends Composite> parent) {
		super(new Composite(parent.getControl(), SWT.NONE), parent);
	}

	@Override
	public void computePackedSize(Float fixedWidth, Float fixedHeight) {
		this.setPackedSize(new UISize(fixedWidth != null ? fixedWidth : DEFAULT_PACKED_SIZE, fixedHeight != null ? fixedHeight : DEFAULT_PACKED_SIZE));
	}
}
