package com.main.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.basic.elements.Device;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class CompositeQos extends Composite {

	private Device device = null;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;
	private Text text_5;
	private Text text_6;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public CompositeQos(Composite parent, int style) {
		super(parent, style);
		createContents();
	}

	public CompositeQos(Composite parent, int style, Device device) {
		this(parent, style);
		this.device = device;
	}
	
	protected void submitRateLimit() {
		// TODO Auto-generated method stub

	}

	protected void addQueueToCurrentDevice() {
		// TODO Auto-generated method stub

	}

	protected void deleteQueueFromQos() {
		// TODO Auto-generated method stub

	}

	protected void setDeviceVlan() {
		// TODO Auto-generated method stub

	}

	protected void clearDeviceVlan() {
		// TODO Auto-generated method stub

	}

	private void createContents() {
		setLayout(new FormLayout());

		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayout(new FormLayout());
		FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		fd_composite.bottom = new FormAttachment(100);
		fd_composite.right = new FormAttachment(100);
		composite.setLayoutData(fd_composite);

		Group grpChoosedevice = new Group(composite, SWT.NONE);
		grpChoosedevice.setText("ChooseDevice");
		FormData fd_grpChoosedevice = new FormData();
		fd_grpChoosedevice.top = new FormAttachment(0);
		fd_grpChoosedevice.left = new FormAttachment(0);
		fd_grpChoosedevice.bottom = new FormAttachment(0, 73);
		fd_grpChoosedevice.right = new FormAttachment(100);
		grpChoosedevice.setLayoutData(fd_grpChoosedevice);

		Combo combo = new Combo(grpChoosedevice, SWT.READ_ONLY);
		combo.setBounds(10, 25, 120, 23);

		Group grpRatelimit = new Group(composite, SWT.NONE);
		grpRatelimit.setText("RateLimit");
		FormData fd_grpRatelimit = new FormData();
		fd_grpRatelimit.bottom = new FormAttachment(grpChoosedevice, 93,
				SWT.BOTTOM);
		fd_grpRatelimit.top = new FormAttachment(grpChoosedevice, 20);
		fd_grpRatelimit.left = new FormAttachment(0);
		fd_grpRatelimit.right = new FormAttachment(100);
		grpRatelimit.setLayoutData(fd_grpRatelimit);

		Label lblUpload = new Label(grpRatelimit, SWT.NONE);
		lblUpload.setAlignment(SWT.RIGHT);
		lblUpload.setBounds(40, 37, 55, 15);
		lblUpload.setText("Upload : ");

		text = new Text(grpRatelimit, SWT.BORDER);
		text.setBounds(106, 34, 118, 21);

		Label lblKb = new Label(grpRatelimit, SWT.NONE);
		lblKb.setBounds(230, 37, 21, 15);
		lblKb.setText("Kb");

		Label lblDownload = new Label(grpRatelimit, SWT.NONE);
		lblDownload.setText("Download : ");
		lblDownload.setAlignment(SWT.RIGHT);
		lblDownload.setBounds(280, 37, 69, 15);

		text_1 = new Text(grpRatelimit, SWT.BORDER);
		text_1.setBounds(355, 34, 118, 21);

		Label label = new Label(grpRatelimit, SWT.NONE);
		label.setText("Kb");
		label.setBounds(479, 37, 21, 15);

		Group grpAddqueue = new Group(composite, SWT.NONE);
		grpAddqueue.setText("AddQueue");
		FormData fd_grpAddqueue = new FormData();
		fd_grpAddqueue.top = new FormAttachment(grpRatelimit, 15);

		Button btnSubmit = new Button(grpRatelimit, SWT.NONE);
		btnSubmit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				submitRateLimit();
			}
		});
		btnSubmit.setText("Submit");
		btnSubmit.setBounds(524, 27, 91, 33);
		fd_grpAddqueue.right = new FormAttachment(100);
		fd_grpAddqueue.left = new FormAttachment(0);
		grpAddqueue.setLayoutData(fd_grpAddqueue);

		Label lblMaxrate = new Label(grpAddqueue, SWT.NONE);
		lblMaxrate.setBounds(40, 37, 55, 15);
		lblMaxrate.setText("MaxRate  : ");

		text_2 = new Text(grpAddqueue, SWT.BORDER);
		text_2.setBounds(106, 34, 118, 21);

		Label label_1 = new Label(grpAddqueue, SWT.NONE);
		label_1.setText("Kb");
		label_1.setBounds(230, 37, 21, 15);

		Label lblMinrate = new Label(grpAddqueue, SWT.NONE);
		lblMinrate.setText("MinRate  : ");
		lblMinrate.setBounds(292, 37, 55, 15);

		text_3 = new Text(grpAddqueue, SWT.BORDER);
		text_3.setBounds(355, 34, 118, 21);

		Label label_3 = new Label(grpAddqueue, SWT.NONE);
		label_3.setText("Kb");
		label_3.setBounds(479, 37, 21, 15);

		Button btnAdd = new Button(grpAddqueue, SWT.NONE);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addQueueToCurrentDevice();
			}
		});
		btnAdd.setBounds(524, 28, 91, 33);
		btnAdd.setText("ADD");

		Group grpDeletequeue = new Group(composite, SWT.NONE);
		fd_grpAddqueue.bottom = new FormAttachment(grpDeletequeue, -35);
		grpDeletequeue.setText("DeleteQueue");
		FormData fd_grpDeletequeue = new FormData();
		fd_grpDeletequeue.bottom = new FormAttachment(grpAddqueue, 200);
		fd_grpDeletequeue.top = new FormAttachment(0, 296);
		fd_grpDeletequeue.right = new FormAttachment(100);
		fd_grpDeletequeue.left = new FormAttachment(grpChoosedevice, 0,
				SWT.LEFT);
		grpDeletequeue.setLayoutData(fd_grpDeletequeue);

		Combo combo_1 = new Combo(grpDeletequeue, SWT.READ_ONLY);
		combo_1.setBounds(38, 27, 103, 23);

		Label label_2 = new Label(grpDeletequeue, SWT.NONE);
		label_2.setText("MaxRate  : ");
		label_2.setBounds(169, 33, 55, 15);

		text_4 = new Text(grpDeletequeue, SWT.BORDER);
		text_4.setEditable(false);
		text_4.setBounds(235, 30, 118, 21);

		Label label_4 = new Label(grpDeletequeue, SWT.NONE);
		label_4.setText("Kb");
		label_4.setBounds(359, 33, 21, 15);

		Label label_5 = new Label(grpDeletequeue, SWT.NONE);
		label_5.setText("MinRate  : ");
		label_5.setBounds(421, 33, 55, 15);

		text_5 = new Text(grpDeletequeue, SWT.BORDER);
		text_5.setEditable(false);
		text_5.setBounds(484, 30, 118, 21);

		Label label_6 = new Label(grpDeletequeue, SWT.NONE);
		label_6.setText("Kb");
		label_6.setBounds(608, 33, 21, 15);

		Button btnDelete = new Button(grpDeletequeue, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteQueueFromQos();
			}
		});
		btnDelete.setText("Delete");
		btnDelete.setBounds(653, 24, 91, 33);

		Group grpSetvlan = new Group(composite, SWT.NONE);
		grpSetvlan.setText("SetVlan");
		FormData fd_grpSetvlan = new FormData();
		fd_grpSetvlan.bottom = new FormAttachment(grpDeletequeue, 104,
				SWT.BOTTOM);
		fd_grpSetvlan.top = new FormAttachment(0, 400);
		fd_grpSetvlan.left = new FormAttachment(grpChoosedevice, 0, SWT.LEFT);

		Label lblIp = new Label(grpChoosedevice, SWT.NONE);
		lblIp.setBounds(178, 31, 208, 15);
		lblIp.setText("IP : ");

		Label lblMac = new Label(grpChoosedevice, SWT.NONE);
		lblMac.setText("Mac : ");
		lblMac.setBounds(409, 31, 248, 15);
		fd_grpSetvlan.right = new FormAttachment(100);
		grpSetvlan.setLayoutData(fd_grpSetvlan);

		Label lblNewLabel = new Label(grpSetvlan, SWT.NONE);
		lblNewLabel.setAlignment(SWT.RIGHT);
		lblNewLabel.setBounds(38, 39, 55, 15);
		lblNewLabel.setText("VlanId : ");

		text_6 = new Text(grpSetvlan, SWT.BORDER);
		text_6.setBounds(99, 36, 73, 21);

		Button btnSet = new Button(grpSetvlan, SWT.NONE);
		btnSet.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setDeviceVlan();
			}
		});
		btnSet.setText("Set");
		btnSet.setBounds(203, 30, 91, 33);

		Button btnClear = new Button(grpSetvlan, SWT.NONE);
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearDeviceVlan();
			}
		});
		btnClear.setText("Clear");
		btnClear.setBounds(346, 30, 91, 33);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
