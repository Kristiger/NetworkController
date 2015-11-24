package com.main.view;

import java.awt.Toolkit;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
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
import com.tools.util.JSONException;

public class MainFrame {

	protected static Shell shell;
	private static final int WINWIDTH = 1260;
	private static final int WINHEIGHT = 850;

	private CompositeController cp_controller = null;
	private CompositeSwitch cp_switches = null;
	private CompositeDevices cp_devicees = null;
	private static CompositeQos cp_qos = null;
	private CompositeFirewall cp_firewall = null;
	private static CompositeStaticFlow cp_staticflow = null;

	private TreeItem trtmSwitches;
	private Map<String, Switch> switches = null;
	private static Composite composite_2;
	private static StackLayout stackLayout;

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
		try {
			switches = DataProvider.getSwitches(true);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(SWT.CLOSE | SWT.MIN | SWT.MAX);

		shell.setSize(WINWIDTH, WINHEIGHT);
		shell.setLocation(new Point(
				Toolkit.getDefaultToolkit().getScreenSize().width / 2
						- WINWIDTH / 2, Toolkit.getDefaultToolkit()
						.getScreenSize().height / 2 - WINHEIGHT / 2));
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				System.exit(0);
			}
		});
		shell.setText("SWT Application");
		shell.setLayout(new FormLayout());

		Composite composite = new Composite(shell, SWT.NONE);
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100);
		fd_composite.right = new FormAttachment(100);
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		composite.setLayoutData(fd_composite);
		composite.setLayout(new FormLayout());

		Composite composite_1 = new Composite(composite, SWT.NONE);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.right = new FormAttachment(0, 230);
		fd_composite_1.bottom = new FormAttachment(100, -10);
		fd_composite_1.top = new FormAttachment(0, 10);
		fd_composite_1.left = new FormAttachment(0, 10);
		composite_1.setLayoutData(fd_composite_1);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));

		composite_2 = new Composite(composite, SWT.BORDER);
		stackLayout = new StackLayout();
		composite_2.setLayout(stackLayout);
		FormData fd_composite_2 = new FormData();
		fd_composite_2.top = new FormAttachment(0, 10);
		fd_composite_2.bottom = new FormAttachment(100, -10);
		fd_composite_2.left = new FormAttachment(0, 236);
		fd_composite_2.right = new FormAttachment(100, -10);
		composite_2.setLayoutData(fd_composite_2);

		final Tree tree = new Tree(composite_1, SWT.BORDER | SWT.NO_FOCUS);
		tree.setFont(org.eclipse.wb.swt.SWTResourceManager.getFont(
				"Microsoft YaHei UI", 9, SWT.NORMAL));
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
							if (trtmSwitches.getItems().length != 0) {
								trtmSwitches.setExpanded(true);
							}
						}
					} else if (items[0].getText().equals("Devices")) {
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
						stackLayout.topControl = cp_qos;
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
							cp_switches = new CompositeSwitch(composite_2,
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

		cp_controller = new CompositeController(composite_2, SWT.None);
		stackLayout.topControl = cp_controller;
	}

	public static Shell getShell() {
		// TODO Auto-generated method stub
		return shell;
	}

	public static StackLayout getMainStackLayout() {
		return stackLayout;
	}

	public static CompositeQos getCp_qos() {
		if (cp_qos == null) {
			cp_qos = new CompositeQos(composite_2, SWT.NONE);
		}
		return cp_qos;
	}

	public static CompositeStaticFlow getCp_staticflow() {
		if (cp_staticflow == null) {
			cp_staticflow = new CompositeStaticFlow(composite_2, SWT.NONE);
		}
		return cp_staticflow;
	}
}
