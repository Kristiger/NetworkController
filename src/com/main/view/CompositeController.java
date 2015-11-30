package com.main.view;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
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
	private Label lblIp_1;
	private Label lblPort_1;
	private Label lblVersion_1;
	private Label lblHealth_1;
	private Label lblMemory_1;
	private Label lblModules;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public CompositeController(Composite parent, int style) {
		super(parent, style);
		createContents();
		updateMemory();
	}

	private void populateControllerInfo() {
		// TODO Auto-generated method stub
		try {
			info = DataProvider.getControllerInfo();
			if (info.containsKey("IP"))
				lblIp_1.setText(info.get("IP"));
			if (info.containsKey("PORT"))
				lblPort_1.setText(info.get("PORT"));
			if (info.containsKey("health"))
				lblHealth_1.setText(info.get("health"));
			if (info.containsKey("memory"))
				lblMemory_1.setText(info.get("memory"));
			if (info.containsKey("modules"))
				lblModules.setText(info.get("modules"));
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
					while (true) {
						Display.getDefault().asyncExec(new Runnable() {

							@Override
							public void run() {
								populateControllerInfo();
							}
						});
						Thread.sleep(5000);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		thread.setName("controller");
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
		lblPort.setBounds(287, 10, 61, 17);
		lblPort.setText("Port : ");

		lblVersion = new Label(composite, SWT.NONE);
		lblVersion.setAlignment(SWT.RIGHT);
		lblVersion.setBounds(10, 45, 61, 17);
		lblVersion.setText("Version : ");

		lblHealth = new Label(composite, SWT.NONE);
		lblHealth.setAlignment(SWT.RIGHT);
		lblHealth.setBounds(287, 45, 61, 17);
		lblHealth.setText("Health : ");

		lblMemory = new Label(composite, SWT.NONE);
		lblMemory.setAlignment(SWT.RIGHT);
		lblMemory.setBounds(10, 80, 61, 17);
		lblMemory.setText("Memory : ");

		lblIp_1 = new Label(composite, SWT.NONE);
		lblIp_1.setBounds(77, 10, 130, 15);
		lblIp_1.setText("IP1");

		lblPort_1 = new Label(composite, SWT.NONE);
		lblPort_1.setBounds(349, 10, 55, 15);
		lblPort_1.setText("PORT1");

		lblVersion_1 = new Label(composite, SWT.NONE);
		lblVersion_1.setBounds(77, 45, 130, 15);
		lblVersion_1.setText("Version1");

		lblHealth_1 = new Label(composite, SWT.NONE);
		lblHealth_1.setBounds(349, 45, 90, 15);
		lblHealth_1.setText("Health1");

		lblMemory_1 = new Label(composite, SWT.NONE);
		lblMemory_1.setBounds(77, 80, 130, 15);
		lblMemory_1.setText("Memory1");
		fd_composite_1.right = new FormAttachment(100, -3);
		fd_composite_1.left = new FormAttachment(0, 3);
		fd_composite_1.bottom = new FormAttachment(100, -10);
		composite_1.setLayoutData(fd_composite_1);

		lblModule = new Label(composite_1, SWT.NONE);
		lblModule.setAlignment(SWT.RIGHT);
		lblModule.setBounds(10, 10, 71, 17);
		lblModule.setText("Modules : ");

		lblModules = new Label(composite_1, SWT.WRAP);
		lblModules.setLocation(87, 10);
		lblModules.setSize(700, 350);
		lblModules.setLayoutData("width 500:pref:pref");
		lblModules.setText("Modules1");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
