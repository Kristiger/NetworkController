package com.main.view;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.basic.elements.Switch;
import com.main.provider.DataProvider;
import com.main.view.util.SWTResourceManager;

public class MainFrame {

	protected Shell shell;

	private CompositeController cp_controller = null;
	private CompositeSwitches cp_switches = null;
	private CompositeDevices cp_devicees = null;
	private CompositeQos cp_qos = null;
	private CompositeFirewall cp_firewall = null;
	private CompositeStaticFlow cp_staticflow = null;

	private TreeItem trtmSwitches;
	private Map<String, Switch> switches = null;

	public MainFrame() {
		open();
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	protected void updateSwitch() {
		// TODO Auto-generated method stub
		switches = DataProvider.getSwitches(true);
	}
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(SWT.CLOSE | SWT.MIN);
		shell.setSize(1260, 850);
		shell.setText("SWT Application");
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FormLayout());

		Composite composite_1 = new Composite(composite, SWT.NONE);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.right = new FormAttachment(0, 230);
		fd_composite_1.bottom = new FormAttachment(100, -10);
		fd_composite_1.top = new FormAttachment(0, 10);
		fd_composite_1.left = new FormAttachment(0, 10);
		composite_1.setLayoutData(fd_composite_1);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));

		final Composite composite_2 = new Composite(composite, SWT.BORDER);
		final StackLayout stackLayout = new StackLayout();
		composite_2.setLayout(stackLayout);
		FormData fd_composite_2 = new FormData();
		fd_composite_2.top = new FormAttachment(0, 10);
		fd_composite_2.bottom = new FormAttachment(100, -10);
		fd_composite_2.left = new FormAttachment(0, 236);
		fd_composite_2.right = new FormAttachment(0, 1244);
		composite_2.setLayoutData(fd_composite_2);

		final Tree tree = new Tree(composite_1, SWT.BORDER);
		tree.setFont(SWTResourceManager.getFont("Microsoft YaHei UI", 10,
				SWT.NORMAL));
		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] items = tree.getSelection();
				if (items.length != 0) {
					if (items[0].getText().equals("Controller")) {
						if (cp_controller == null) {
							cp_controller = new CompositeController(
									composite_2, SWT.None);
						}
						stackLayout.topControl = cp_controller;
						composite_2.layout();
					} else if (items[0].getText().equals("Switches")) {
						updateSwitch();
						trtmSwitches.removeAll();
						// If there are switches and the tree is not disposed,
						// populate it
						if (trtmSwitches != null) {
							for (Switch sw : switches.values()) {
								TreeItem item = new TreeItem(trtmSwitches,
										SWT.NONE);
								item.setText(sw.getDpid());
							}
						}
					} else if (items[0].getText().equals("Device")) {
						if (cp_devicees == null) {
							cp_devicees = new CompositeDevices(composite_2,
									SWT.NONE);
						}
						stackLayout.topControl = cp_devicees;
						composite_2.layout();
					} else if (items[0].getText().equals("Qos")) {
						if (cp_qos == null) {
							cp_qos = new CompositeQos(composite_2, SWT.NONE);
						}
						stackLayout.topControl = cp_switches;
						composite_2.layout();
					} else if (items[0].getText().equals("Firewall")) {
						if (cp_firewall == null) {
							cp_firewall = new CompositeFirewall(composite_2,
									SWT.NONE);
						}
						stackLayout.topControl = cp_firewall;
						composite_2.layout();
					} else if (items[0].getText().equals("StaticFlow")) {
						if (cp_staticflow == null) {
							cp_staticflow = new CompositeStaticFlow(
									composite_2, SWT.NONE);
						}
						stackLayout.topControl = cp_staticflow;
						composite_2.layout();
					} else if (items[0].getText().length() == 23) {
						if (cp_switches == null) {
							cp_switches = new CompositeSwitches(composite_2,
									SWT.NONE);
						}
						cp_switches.setCurrentSwitch(switches.get(items[0]
								.getText()));
						stackLayout.topControl = cp_switches;
						composite_2.layout();
					}

				}
			}
		});

		TreeItem trtmOverview = new TreeItem(tree, SWT.NONE);
		trtmOverview.setText("OverView");

		TreeItem trtmController = new TreeItem(trtmOverview, SWT.NONE);
		trtmController.setText("Controller");
		trtmController.setExpanded(true);

		trtmSwitches = new TreeItem(trtmOverview, SWT.NONE);
		trtmSwitches.setText("Switches");

		TreeItem trtmDevices = new TreeItem(trtmOverview, SWT.NONE);
		trtmDevices.setText("Devices");
		trtmOverview.setExpanded(true);

		TreeItem trtmApplications = new TreeItem(tree, SWT.NONE);
		trtmApplications.setText("Applications");

		TreeItem trtmQos = new TreeItem(trtmApplications, SWT.NONE);
		trtmQos.setText("Qos");

		TreeItem trtmFirewall = new TreeItem(trtmApplications, SWT.NONE);
		trtmFirewall.setText("Firewall");

		TreeItem trtmPushflow = new TreeItem(trtmApplications, SWT.NONE);
		trtmPushflow.setText("StaticFlow");
		trtmApplications.setExpanded(true);
	}
}
