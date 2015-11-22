package com.main.view;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import com.main.provider.DataProvider;
import com.tools.util.JSONException;

public class CompositeController extends Composite {

	private Label lblIp;
	private Label lblPort;
	private Label lblVersion;
	private Label lblHealth;
	private Label lblMemory;
	private Label lblModule;
	private Map<String, String> info;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public CompositeController(Composite parent, int style) {
		super(parent, style);

		createContents();
		populateControllerInfo();
		updateMemory();
	}

	private void populateControllerInfo() {
		// TODO Auto-generated method stub
		try {
			info = DataProvider.getControllerInfo();
			if (info.containsKey("IP"))
				lblIp.setText("IP : " + info.get("IP"));
			if (info.containsKey("PORT"))
				lblPort.setText("PORT : " + info.get("PORT"));
			if (info.containsKey("health"))
				lblHealth.setText("Health : " + info.get("health"));
			if (info.containsKey("memory"))
				lblMemory.setText("Memory : " + info.get("memory"));
			if (info.containsKey("modules"))
				lblModule.setText("Modules : " + info.get("modules"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updateMemory() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					populateControllerInfo();
					Thread.sleep(5000);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		thread.start();
	}

	private void createContents() {
		setLayout(new FormLayout());

		Group grpControllerinfo = new Group(this, SWT.NONE);
		grpControllerinfo.setText("ControllerInfo");
		grpControllerinfo.setLayout(new FormLayout());
		FormData fd_grpControllerinfo = new FormData();
		fd_grpControllerinfo.bottom = new FormAttachment(100, -10);
		fd_grpControllerinfo.right = new FormAttachment(100, -10);
		fd_grpControllerinfo.top = new FormAttachment(0, 10);
		fd_grpControllerinfo.left = new FormAttachment(0, 10);
		grpControllerinfo.setLayoutData(fd_grpControllerinfo);

		Composite composite = new Composite(grpControllerinfo, SWT.BORDER);
		composite.setLayout(null);
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(0, 120);
		fd_composite.top = new FormAttachment(0, 3);
		fd_composite.right = new FormAttachment(100, -3);
		fd_composite.left = new FormAttachment(0, 3);
		composite.setLayoutData(fd_composite);

		Composite composite_1 = new Composite(grpControllerinfo, SWT.BORDER);
		composite_1.setLayout(null);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.top = new FormAttachment(composite, 10);

		lblIp = new Label(composite, SWT.NONE);
		lblIp.setAlignment(SWT.RIGHT);
		lblIp.setBounds(10, 10, 61, 17);
		lblIp.setText("IP : ");

		lblPort = new Label(composite, SWT.NONE);
		lblPort.setAlignment(SWT.RIGHT);
		lblPort.setBounds(325, 10, 61, 17);
		lblPort.setText("Port : ");

		lblVersion = new Label(composite, SWT.NONE);
		lblVersion.setAlignment(SWT.RIGHT);
		lblVersion.setBounds(10, 45, 61, 17);
		lblVersion.setText("Version : ");

		lblHealth = new Label(composite, SWT.NONE);
		lblHealth.setAlignment(SWT.RIGHT);
		lblHealth.setBounds(325, 45, 61, 17);
		lblHealth.setText("Health : ");

		lblMemory = new Label(composite, SWT.NONE);
		lblMemory.setAlignment(SWT.RIGHT);
		lblMemory.setBounds(10, 80, 61, 17);
		lblMemory.setText("Memory : ");
		fd_composite_1.right = new FormAttachment(100, -3);
		fd_composite_1.left = new FormAttachment(0, 3);
		fd_composite_1.bottom = new FormAttachment(100, -10);
		composite_1.setLayoutData(fd_composite_1);

		lblModule = new Label(composite_1, SWT.NONE);
		lblModule.setAlignment(SWT.RIGHT);
		lblModule.setBounds(10, 10, 71, 17);
		lblModule.setText("Modules : ");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
