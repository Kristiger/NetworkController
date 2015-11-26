package com.main.view;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.basic.elements.Device;
import com.basic.elements.UPDATETYPE;
import com.main.provider.DataProvider;
import com.tools.util.JSONException;
import com.util.xen.XenTools;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;

public class CompositeDevices extends Composite {
	private Table table;

	private Map<String, Device> devices = null;
	private Device currentDevice = null;
	private Label lblIp_1;
	private Label lblMac_1;
	private Label lblVifPort_1;
	private Label lblUuid_1;
	private Label lblAttachedswitch_1;
	private Label lblSwitchport_1;
	private Label lblQosInfo_1;
	private Composite parent = null;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public CompositeDevices(Composite parent, int style) {
		super(parent, style);
		this.parent = parent;
		createContents();
		updateDevices();
	}

	private void updateDevices() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						devices = DataProvider.getDevices(true);
						if (devices != null) {
							for (Device device : devices.values()) {
								if (!device.isActive()) {
									device.setActive(true);
									DataProvider.updateDeviceStore(device,
											UPDATETYPE.INSERT, null);
								}
							}
						}
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		thread.setName("device");
		thread.start();

		Thread thread2 = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					while (true) {
						Display.getDefault().asyncExec(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								populateDeviceTable();
							}
						});
						Thread.sleep(5000);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread2.setName("deviceDisplay");
		thread2.start();
	}

	protected void populateDeviceTable() {
		// TODO Auto-generated method stub
		if (devices != null) {
			table.removeAll();
			String[][] datas = DataProvider.getDeviceTableFormat(devices);
			for (String[] strings : datas) {
				new TableItem(table, SWT.NONE).setText(strings);
			}
		}
	}

	protected void populateDeviceDetail(String mac) {
		// TODO Auto-generated method stub
		currentDevice = devices.get(mac);
		if (currentDevice.getIpAddr() != null)
			lblIp_1.setText(currentDevice.getIpAddr());
		else
			lblIp_1.setText("None");

		if (currentDevice.getMacAddr() != null)
			lblMac_1.setText(currentDevice.getMacAddr());
		else
			lblMac_1.setText("None");

		if (currentDevice.getVmUuid() != null)
			lblUuid_1.setText(currentDevice.getVmUuid());
		else
			lblUuid_1.setText("None");

		if (currentDevice.getVifNumber() != null)
			lblVifPort_1.setText(currentDevice.getVifNumber());
		else
			lblVifPort_1.setText("None");

		if (currentDevice.getSwitchPort() != null)
			lblSwitchport_1.setText(currentDevice.getSwitchPort());
		else
			lblSwitchport_1.setText("None");

		if (currentDevice.getSwitchDpid() != null)
			lblAttachedswitch_1.setText(currentDevice.getSwitchDpid());
		else
			lblAttachedswitch_1.setText("None");

		if (currentDevice.getQosUuid() != null)
			lblQosInfo_1.setText(currentDevice.getQosUuid());
		else
			lblQosInfo_1.setText("None");

	}

	private void createContents() {
		setLayout(new FormLayout());

		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] items = table.getSelection();
				if (items.length > 0) {
					String mac = items[0].getText(2);
					if (!mac.equals("None"))
						populateDeviceDetail(mac);
				}
			}
		});
		FormData fd_table = new FormData();
		fd_table.top = new FormAttachment(0, 10);
		fd_table.left = new FormAttachment(0, 10);
		fd_table.right = new FormAttachment(100, -10);
		table.setLayoutData(fd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(40);
		tblclmnNewColumn.setText("#");

		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(200);
		tblclmnNewColumn_1.setText("IP");

		TableColumn tblclmnNewColumn_2 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_2.setWidth(180);
		tblclmnNewColumn_2.setText("Mac");

		TableColumn tblclmnNewColumn_3 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_3.setWidth(180);
		tblclmnNewColumn_3.setText("Switch");

		Group grpDetail = new Group(this, SWT.NONE);
		fd_table.bottom = new FormAttachment(100, -223);
		grpDetail.setText("Detail");
		grpDetail.setLayout(new FormLayout());
		FormData fd_grpDetail = new FormData();
		fd_grpDetail.top = new FormAttachment(table, 6);

		TableColumn tblclmnNewColumn_4 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_4.setWidth(70);
		tblclmnNewColumn_4.setText("Port");

		TableColumn tblclmnVifport = new TableColumn(table, SWT.NONE);
		tblclmnVifport.setWidth(60);
		tblclmnVifport.setText("VifPort");

		TableColumn tblclmnDeviceuuid = new TableColumn(table, SWT.NONE);
		tblclmnDeviceuuid.setWidth(250);
		tblclmnDeviceuuid.setText("DeviceUuid");
		fd_grpDetail.bottom = new FormAttachment(100, -10);
		fd_grpDetail.left = new FormAttachment(0, 10);
		fd_grpDetail.right = new FormAttachment(100, -10);
		grpDetail.setLayoutData(fd_grpDetail);

		table.setSortColumn(table.getColumn(2));

		Label lblIp = new Label(grpDetail, SWT.NONE);
		FormData fd_lblIp = new FormData();
		fd_lblIp.right = new FormAttachment(0, 68);
		fd_lblIp.top = new FormAttachment(0, 14);
		fd_lblIp.left = new FormAttachment(0, 7);
		lblIp.setLayoutData(fd_lblIp);
		lblIp.setAlignment(SWT.RIGHT);
		lblIp.setText("IP : ");

		Label lblMac = new Label(grpDetail, SWT.NONE);
		FormData fd_lblMac = new FormData();
		fd_lblMac.right = new FormAttachment(50);
		fd_lblMac.top = new FormAttachment(0, 14);
		fd_lblMac.left = new FormAttachment(40);
		lblMac.setLayoutData(fd_lblMac);
		lblMac.setAlignment(SWT.RIGHT);
		lblMac.setText("MAC : ");

		Label lblVifPort = new Label(grpDetail, SWT.NONE);
		FormData fd_lblVifPort = new FormData();
		fd_lblVifPort.right = new FormAttachment(80, 10);
		fd_lblVifPort.top = new FormAttachment(0, 14);
		fd_lblVifPort.left = new FormAttachment(70, 10);
		lblVifPort.setLayoutData(fd_lblVifPort);
		lblVifPort.setAlignment(SWT.RIGHT);
		lblVifPort.setText("VifPort : ");

		Label lblAttachedswitch = new Label(grpDetail, SWT.NONE);
		FormData fd_lblAttachedswitch = new FormData();
		fd_lblAttachedswitch.right = new FormAttachment(50);
		fd_lblAttachedswitch.top = new FormAttachment(0, 37);
		fd_lblAttachedswitch.left = new FormAttachment(0, 299);
		lblAttachedswitch.setLayoutData(fd_lblAttachedswitch);
		lblAttachedswitch.setAlignment(SWT.RIGHT);
		lblAttachedswitch.setText("AttachedSwitch : ");

		Label lblUuid = new Label(grpDetail, SWT.NONE);
		FormData fd_lblUuid = new FormData();
		fd_lblUuid.right = new FormAttachment(0, 68);
		fd_lblUuid.top = new FormAttachment(0, 37);
		fd_lblUuid.left = new FormAttachment(0, 7);
		lblUuid.setLayoutData(fd_lblUuid);
		lblUuid.setAlignment(SWT.RIGHT);
		lblUuid.setText("Uuid : ");

		Label lblSwitchport = new Label(grpDetail, SWT.NONE);
		FormData fd_lblSwitchport = new FormData();
		fd_lblSwitchport.right = new FormAttachment(80, 10);
		fd_lblSwitchport.top = new FormAttachment(0, 37);
		fd_lblSwitchport.left = new FormAttachment(70);
		lblSwitchport.setLayoutData(fd_lblSwitchport);
		lblSwitchport.setAlignment(SWT.RIGHT);
		lblSwitchport.setText("SwitchPort : ");

		Label lblQosInfo = new Label(grpDetail, SWT.WRAP);
		lblQosInfo.setAlignment(SWT.RIGHT);
		FormData fd_lblQosInfo = new FormData();
		fd_lblQosInfo.left = new FormAttachment(0, 17);
		fd_lblQosInfo.top = new FormAttachment(0, 69);
		lblQosInfo.setLayoutData(fd_lblQosInfo);
		lblQosInfo.setText("QosInfo : ");

		lblQosInfo_1 = new Label(grpDetail, SWT.NONE);
		FormData fd_lblQosInfo_1 = new FormData();
		fd_lblQosInfo_1.left = new FormAttachment(lblQosInfo, 6);
		fd_lblQosInfo_1.bottom = new FormAttachment(lblQosInfo, 0, SWT.BOTTOM);
		fd_lblQosInfo_1.right = new FormAttachment(100, -20);
		fd_lblQosInfo_1.top = new FormAttachment(0, 69);
		lblQosInfo_1.setLayoutData(fd_lblQosInfo_1);
		lblQosInfo_1.setText("info");

		lblIp_1 = new Label(grpDetail, SWT.NONE);
		FormData fd_lblIp_1 = new FormData();
		fd_lblIp_1.right = new FormAttachment(40, -10);
		fd_lblIp_1.top = new FormAttachment(0, 14);
		fd_lblIp_1.bottom = new FormAttachment(lblIp, 0, SWT.BOTTOM);
		fd_lblIp_1.left = new FormAttachment(lblIp, 6);
		lblIp_1.setLayoutData(fd_lblIp_1);
		lblIp_1.setText("IP1");

		lblMac_1 = new Label(grpDetail, SWT.NONE);
		FormData fd_lblMac_1 = new FormData();
		fd_lblMac_1.right = new FormAttachment(70);
		fd_lblMac_1.top = new FormAttachment(0, 14);
		fd_lblMac_1.bottom = new FormAttachment(lblIp, 0, SWT.BOTTOM);
		fd_lblMac_1.left = new FormAttachment(lblMac, 6);
		lblMac_1.setLayoutData(fd_lblMac_1);
		lblMac_1.setText("MAC1");

		lblVifPort_1 = new Label(grpDetail, SWT.NONE);
		FormData fd_lblVifport = new FormData();
		fd_lblVifport.left = new FormAttachment(lblVifPort, 6);
		fd_lblVifport.right = new FormAttachment(100, -20);
		fd_lblVifport.top = new FormAttachment(0, 14);
		fd_lblVifport.bottom = new FormAttachment(lblIp, 0, SWT.BOTTOM);
		lblVifPort_1.setLayoutData(fd_lblVifport);
		lblVifPort_1.setText("VifPort1");

		lblUuid_1 = new Label(grpDetail, SWT.NONE);
		FormData fd_lblUuid_1 = new FormData();
		fd_lblUuid_1.right = new FormAttachment(40, -10);
		fd_lblUuid_1.bottom = new FormAttachment(lblUuid, 0, SWT.BOTTOM);
		fd_lblUuid_1.left = new FormAttachment(lblUuid, 6);
		lblUuid_1.setLayoutData(fd_lblUuid_1);
		lblUuid_1.setText("Uuid1");

		lblAttachedswitch_1 = new Label(grpDetail, SWT.NONE);
		FormData fd_lblAttachedswitch_1 = new FormData();
		fd_lblAttachedswitch_1.right = new FormAttachment(70);
		fd_lblAttachedswitch_1.bottom = new FormAttachment(lblAttachedswitch,
				0, SWT.BOTTOM);
		fd_lblAttachedswitch_1.left = new FormAttachment(lblAttachedswitch, 6);
		lblAttachedswitch_1.setLayoutData(fd_lblAttachedswitch_1);
		lblAttachedswitch_1.setText("AttachedSwitch1");

		lblSwitchport_1 = new Label(grpDetail, SWT.NONE);
		FormData fd_lblSwitchport_1 = new FormData();
		fd_lblSwitchport_1.right = new FormAttachment(100, -20);
		fd_lblSwitchport_1.bottom = new FormAttachment(lblAttachedswitch, 0,
				SWT.BOTTOM);
		fd_lblSwitchport_1.left = new FormAttachment(lblSwitchport, 6);
		lblSwitchport_1.setLayoutData(fd_lblSwitchport_1);
		lblSwitchport_1.setText("SwitchPort1");

		Composite composite = new Composite(grpDetail, SWT.BORDER);
		RowLayout rl_composite = new RowLayout(SWT.HORIZONTAL);
		rl_composite.fill = true;
		rl_composite.center = true;
		rl_composite.justify = true;
		rl_composite.marginTop = 10;
		rl_composite.marginLeft = 15;
		composite.setLayout(rl_composite);
		FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(100, -70);
		fd_composite.left = new FormAttachment(0, 10);
		fd_composite.right = new FormAttachment(100, -10);
		fd_composite.bottom = new FormAttachment(100, -10);
		composite.setLayoutData(fd_composite);

		Button btnAddQos = new Button(composite, SWT.NONE);
		btnAddQos.setLayoutData(new RowData(100, 40));
		btnAddQos.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CompositeQos cp_qos = MainFrame.getCp_qos();
				if (currentDevice != null)
					cp_qos.setDevice(currentDevice);
				MainFrame.getMainStackLayout().topControl = cp_qos;
				parent.layout();
			}
		});
		btnAddQos.setText("Add Qos");

		Button btnClearqos = new Button(composite, SWT.NONE);
		btnClearqos.setLayoutData(new RowData(100, 40));
		btnClearqos.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				XenTools.clearPortQos(currentDevice.getVifNumber());
			}
		});
		btnClearqos.setText("ClearQos");

		Button btnAddstaticflow = new Button(composite, SWT.NONE);
		btnAddstaticflow.setLayoutData(new RowData(100, 40));
		btnAddstaticflow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MainFrame.getMainStackLayout().topControl = MainFrame
						.getCp_staticflow();
				parent.layout();
			}
		});
		btnAddstaticflow.setText("AddStaticFlow");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
