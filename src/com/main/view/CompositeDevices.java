package com.main.view;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.basic.elements.Device;

public class CompositeDevices extends Composite {
	private Table table;

	private Map<String, Device> devices = null;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public CompositeDevices(Composite parent, int style) {
		super(parent, style);

		createContents();
		updateDevices();
	}

	private void updateDevices() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	private void createContents() {
		setLayout(new FormLayout());

		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
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
		fd_lblMac.right = new FormAttachment(0, 401);
		fd_lblMac.top = new FormAttachment(0, 14);
		fd_lblMac.left = new FormAttachment(0, 340);
		lblMac.setLayoutData(fd_lblMac);
		lblMac.setAlignment(SWT.RIGHT);
		lblMac.setText("MAC : ");

		Label lblVifPort = new Label(grpDetail, SWT.NONE);
		FormData fd_lblVifPort = new FormData();
		fd_lblVifPort.right = new FormAttachment(0, 663);
		fd_lblVifPort.top = new FormAttachment(0, 14);
		fd_lblVifPort.left = new FormAttachment(0, 602);
		lblVifPort.setLayoutData(fd_lblVifPort);
		lblVifPort.setAlignment(SWT.RIGHT);
		lblVifPort.setText("VifPort : ");

		Label lblAttachedswitch = new Label(grpDetail, SWT.NONE);
		FormData fd_lblAttachedswitch = new FormData();
		fd_lblAttachedswitch.right = new FormAttachment(0, 401);
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
		fd_lblSwitchport.right = new FormAttachment(0, 663);
		fd_lblSwitchport.top = new FormAttachment(0, 37);
		fd_lblSwitchport.left = new FormAttachment(0, 577);
		lblSwitchport.setLayoutData(fd_lblSwitchport);
		lblSwitchport.setAlignment(SWT.RIGHT);
		lblSwitchport.setText("SwitchPort : ");

		Label lblQosInfo = new Label(grpDetail, SWT.NONE);
		FormData fd_lblQosInfo = new FormData();
		fd_lblQosInfo.right = new FormAttachment(0, 68);
		fd_lblQosInfo.top = new FormAttachment(0, 69);
		fd_lblQosInfo.left = new FormAttachment(0, 7);
		lblQosInfo.setLayoutData(fd_lblQosInfo);
		lblQosInfo.setAlignment(SWT.RIGHT);
		lblQosInfo.setText("QosInfo : ");

		Label lblNewLabel = new Label(grpDetail, SWT.NONE);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.right = new FormAttachment(0, 135);
		fd_lblNewLabel.top = new FormAttachment(0, 69);
		fd_lblNewLabel.left = new FormAttachment(0, 74);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("info");

		Button btnAddQos = new Button(grpDetail, SWT.NONE);
		FormData fd_btnAddQos = new FormData();
		fd_btnAddQos.bottom = new FormAttachment(0, 166);
		fd_btnAddQos.right = new FormAttachment(20);
		fd_btnAddQos.top = new FormAttachment(0, 131);
		fd_btnAddQos.left = new FormAttachment(5);
		btnAddQos.setLayoutData(fd_btnAddQos);
		btnAddQos.setText("Add Qos");

		Button btnClearqos = new Button(grpDetail, SWT.NONE);
		FormData fd_btnClearqos = new FormData();
		fd_btnClearqos.bottom = new FormAttachment(0, 166);
		fd_btnClearqos.right = new FormAttachment(40);
		fd_btnClearqos.top = new FormAttachment(0, 131);
		fd_btnClearqos.left = new FormAttachment(25);
		btnClearqos.setLayoutData(fd_btnClearqos);
		btnClearqos.setText("ClearQos");

		Button btnAddqueue = new Button(grpDetail, SWT.NONE);
		FormData fd_btnAddqueue = new FormData();
		fd_btnAddqueue.bottom = new FormAttachment(0, 166);
		fd_btnAddqueue.right = new FormAttachment(60);
		fd_btnAddqueue.top = new FormAttachment(0, 131);
		fd_btnAddqueue.left = new FormAttachment(45);
		btnAddqueue.setLayoutData(fd_btnAddqueue);
		btnAddqueue.setText("AddQueue");

		Button btnAddstaticflow = new Button(grpDetail, SWT.NONE);
		FormData fd_btnAddstaticflow = new FormData();
		fd_btnAddstaticflow.bottom = new FormAttachment(0, 166);
		fd_btnAddstaticflow.right = new FormAttachment(80);
		fd_btnAddstaticflow.top = new FormAttachment(0, 131);
		fd_btnAddstaticflow.left = new FormAttachment(65);
		btnAddstaticflow.setLayoutData(fd_btnAddstaticflow);
		btnAddstaticflow.setText("AddStaticFlow");

		Button btnAddvlan = new Button(grpDetail, SWT.NONE);
		FormData fd_btnAddvlan = new FormData();
		fd_btnAddvlan.bottom = new FormAttachment(0, 166);
		fd_btnAddvlan.right = new FormAttachment(95);
		fd_btnAddvlan.top = new FormAttachment(0, 131);
		fd_btnAddvlan.left = new FormAttachment(85);
		btnAddvlan.setLayoutData(fd_btnAddvlan);
		btnAddvlan.setText("AddVlan");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
