package com.main.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Group;
import swing2swt.layout.FlowLayout;

public class CompositeQos extends Composite {
	private Text text;
	private Text text_1;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CompositeQos(Composite parent, int style) {
		super(parent, style);

		createContents();
	}
	private void createContents() {
		setLayout(new FormLayout());
		
		text = new Text(this, SWT.BORDER);
		FormData fd_text = new FormData();
		fd_text.top = new FormAttachment(0, 134);
		fd_text.left = new FormAttachment(0, 154);
		text.setLayoutData(fd_text);
		
		Group group = new Group(this, SWT.NONE);
		group.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		FormData fd_group = new FormData();
		fd_group.right = new FormAttachment(0, 424);
		fd_group.top = new FormAttachment(0, 10);
		fd_group.left = new FormAttachment(0, 10);
		group.setLayoutData(fd_group);
		
		text_1 = new Text(group, SWT.BORDER);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
