package com.main.view;

import java.io.IOException;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import swing2swt.layout.FlowLayout;

import com.basic.elements.Flow;
import com.basic.elements.Port;
import com.basic.elements.Switch;
import com.main.provider.DataProvider;
import com.tools.util.JSONException;

public class CompositeSwitches extends Composite {
	private Table tablePort, tableFlow;
	private Label lblDpid, lblManufacturer, lblSoftware, lblHardware;
	private Group grpGroup;
	private Composite composite;
	private Composite composite_1;
	private TableColumn tblclmnFlowcount, tableColumn, tableColumn_1,
			tableColumn_2, tableColumn_3;
	private TableColumn tblclmnPriority, tblclmnTimeout, tblclmnVif;
	private Switch currentSwitch;
	private List<Flow> flows = null;
	private List<Port> ports = null;
	private Label lblSerialnumber;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public CompositeSwitches(Composite parent, int style) {
		super(parent, style);

		// init contents
		createContents();
		// continuing upadte this shell for the latest info
		updateSwitchInfo();
	}

	public void setCurrentSwitch(Switch sw) {
		// TODO Auto-generated method stub
		this.currentSwitch = sw;
		populateSwitchInfo();
	}

	private void populateSwitchInfo() {
		// TODO Auto-generated method stub
		lblDpid.setText("Dpid : " + currentSwitch.getDpid());
		lblManufacturer.setText("Manufacturer : "
				+ currentSwitch.getManufacturerDescription());
		lblSoftware.setText("Software : "
				+ currentSwitch.getSoftwareDescription());
		lblHardware.setText("Haraware : "
				+ currentSwitch.getHardwareDescription());
		lblSerialnumber.setText("SerialNumber : "
				+ currentSwitch.getSerialNumber());
	}

	private void updateSwitchInfo() {

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					if (currentSwitch != null) {
						// update current switch
						DataProvider.getSwitchUpdate(currentSwitch);
						// get current switch latest ports
						ports = currentSwitch.getPorts();
						// fill port into table
						populatePortTable();
					}
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();

		Thread thread2 = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					if (currentSwitch != null) {
						// get latest flow of current switch
						flows = DataProvider.getFlows(currentSwitch.getDpid());
						// fill into flow table
						populateFlowTable();
					}
					Thread.sleep(2000);
				} catch (JSONException e) {
					// TODO: handle exception
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread2.start();
	}

	protected void populatePortTable() {
		// TODO Auto-generated method stub

		// get table format to fill the table item.
		String[][] datas = DataProvider.getPortTableFormat(ports);

		// clear old datas
		tablePort.removeAll();

		// fill into table
		for (String[] data : datas) {
			new TableItem(tablePort, SWT.None).setText(data);
		}
	}

	protected void populateFlowTable() {
		// TODO Auto-generated method stub

		// get table format to fill the table item.
		String[][] datas = DataProvider.getFlowTableFormat(flows);

		// clear old datas
		tableFlow.removeAll();

		// fill into table
		for (String[] data : datas) {
			new TableItem(tableFlow, SWT.NONE).setText(data);
		}
	}

	private void createContents() {
		setLayout(new FormLayout());

		grpGroup = new Group(this, SWT.NONE);
		grpGroup.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
		FormData fd_grpGroup = new FormData();
		fd_grpGroup.bottom = new FormAttachment(0, 60);
		fd_grpGroup.right = new FormAttachment(100, -3);
		fd_grpGroup.top = new FormAttachment(0, 5);
		fd_grpGroup.left = new FormAttachment(0, 3);
		grpGroup.setLayoutData(fd_grpGroup);
		grpGroup.setText("Group");

		lblDpid = new Label(grpGroup, SWT.NONE);
		lblDpid.setText("Dpid : ");

		lblManufacturer = new Label(grpGroup, SWT.NONE);
		lblManufacturer.setText("Manufacturer : ");

		lblSoftware = new Label(grpGroup, SWT.NONE);
		lblSoftware.setText("Software : ");

		lblHardware = new Label(grpGroup, SWT.NONE);
		lblHardware.setText("Hardware : ");

		lblSerialnumber = new Label(grpGroup, SWT.NONE);
		lblSerialnumber.setText("SerialNumber : ");

		composite = new Composite(this, SWT.NONE);
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(40);
		fd_composite.right = new FormAttachment(100, -3);
		fd_composite.left = new FormAttachment(0, 3);
		fd_composite.top = new FormAttachment(0, 70);
		composite.setLayoutData(fd_composite);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));

		tablePort = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		tablePort.setHeaderVisible(true);
		tablePort.setLinesVisible(true);

		TableColumn tblclmnNewColumn = new TableColumn(tablePort, SWT.NONE);
		tblclmnNewColumn.setWidth(58);
		tblclmnNewColumn.setText("#");

		tableColumn = new TableColumn(tablePort, SWT.NONE);
		tableColumn.setWidth(100);
		tableColumn.setText("Port");

		tblclmnVif = new TableColumn(tablePort, SWT.NONE);
		tblclmnVif.setWidth(100);
		tblclmnVif.setText("Vif");

		tableColumn_1 = new TableColumn(tablePort, SWT.NONE);
		tableColumn_1.setWidth(110);
		tableColumn_1.setText("PacketCount");

		tableColumn_2 = new TableColumn(tablePort, SWT.NONE);
		tableColumn_2.setWidth(110);
		tableColumn_2.setText("ByteCount");

		tableColumn_3 = new TableColumn(tablePort, SWT.NONE);
		tableColumn_3.setWidth(110);
		tableColumn_3.setText("FlowCount");

		composite_1 = new Composite(this, SWT.NONE);
		FormData fd_composite_1 = new FormData();
		fd_composite_1.top = new FormAttachment(composite, 10);
		fd_composite_1.right = new FormAttachment(100, -3);
		fd_composite_1.bottom = new FormAttachment(100, -3);
		fd_composite_1.left = new FormAttachment(0, 3);
		composite_1.setLayoutData(fd_composite_1);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));

		tableFlow = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		tableFlow.setHeaderVisible(true);
		tableFlow.setLinesVisible(true);

		TableColumn tblclmnNewColumn_4 = new TableColumn(tableFlow, SWT.NONE);
		tblclmnNewColumn_4.setWidth(40);
		tblclmnNewColumn_4.setText("#");

		tblclmnPriority = new TableColumn(tableFlow, SWT.NONE);
		tblclmnPriority.setWidth(70);
		tblclmnPriority.setText("Priority");

		TableColumn tblclmnNewColumn_5 = new TableColumn(tableFlow, SWT.NONE);
		tblclmnNewColumn_5.setWidth(455);
		tblclmnNewColumn_5.setText("Match");

		TableColumn tblclmnNewColumn_6 = new TableColumn(tableFlow, SWT.NONE);
		tblclmnNewColumn_6.setWidth(125);
		tblclmnNewColumn_6.setText("Action");

		TableColumn tblclmnNewColumn_7 = new TableColumn(tableFlow, SWT.NONE);
		tblclmnNewColumn_7.setText("Packets");
		tblclmnNewColumn_7.setWidth(100);

		tblclmnFlowcount = new TableColumn(tableFlow, SWT.NONE);
		tblclmnFlowcount.setWidth(100);
		tblclmnFlowcount.setText("Bytes");

		tblclmnTimeout = new TableColumn(tableFlow, SWT.NONE);
		tblclmnTimeout.setWidth(100);
		tblclmnTimeout.setText("Timeout");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
