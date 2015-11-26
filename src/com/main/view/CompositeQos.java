package com.main.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.basic.elements.Device;
import com.basic.elements.UPDATETYPE;
import com.main.app.qos.QosPolicy;
import com.main.app.qos.QosQueue;
import com.main.provider.DataProvider;
import com.main.view.util.DisplayMessage;
import com.tools.util.JSONException;
import com.util.db.DBHelper;
import com.util.xen.XenTools;

public class CompositeQos extends Composite {

	private Text textUpload;
	private Text textDownload;
	private Text textMaxRate;
	private Text textMinRate;
	private Text textDelMax;
	private Text textDelMin;
	private Text textVlan;
	private Combo comboDevice;
	private Label lblIp;
	private Label lblMac;
	private Device device = null;
	private Map<String, Device> devices = null;
	private Combo comboDelete;
	private QosQueue queue;
	private QosPolicy qos;
	private final int MAXQUEUE = 7;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public CompositeQos(Composite parent, int style) {
		super(parent, style);
		createContents();

		try {
			devices = DataProvider.getDevices(false);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		populateDeviceCombo();
	}

	public void setDevice(Device device) {
		this.device = device;
		String[] items = comboDevice.getItems();
		for (int i = 0; i < items.length; i++) {
			if (items[i].equals(device.getMacAddr())) {
				comboDevice.select(i);
				break;
			}
		}
		populateDevice(device.getMacAddr());
	}

	protected void populateDevice(String mac) {
		// TODO Auto-generated method stub
		if (!mac.equals("None")) {
			device = devices.get(mac);
			if (device != null) {
				lblIp.setText(device.getIpAddr());
				lblMac.setText(device.getMacAddr());
			}
		}
	}

	private void populateDeviceCombo() {
		// TODO Auto-generated method stub
		String[] data = devices.keySet().toArray(
				new String[devices.keySet().size()]);
		if (data.length > 0) {
			comboDevice.setItems(data);
			device = devices.get(data[0]);
		} else {
			comboDevice.setItem(0, "None");
		}
		comboDevice.select(0);
		populateRateLimit();
	}

	private void populateRateLimit() {
		// TODO Auto-generated method stub
		if (device != null && device.getUploadRate() != null) {
			textUpload.setText(device.getUploadRate());
		}
		populateQosQueues();
	}

	protected void submitRateLimit() {
		// TODO Auto-generated method stub
		String msg = "";
		if (device != null) {
			if (!textUpload.getText().equals("")
					&& textUpload.getText() != null) {
				try {
					Long upload = Long.parseLong(textUpload.getText());
					if (upload > 0)
						XenTools.setUploadRate(device.getVifNumber(), upload,
								100);
					device.setUploadRate(String.valueOf(upload));
					DataProvider.updateDeviceStore(device, UPDATETYPE.UPDATE,
							"uploadRate");
					msg = "Upload set done.";
				} catch (NumberFormatException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			if (!textDownload.getText().equals("")) {
				try {
					long download = Long.parseLong(textDownload.getText()) * 1000;
					if (download > 0) {
						String qosUuid = XenTools.createRow("qos", download,
								download);
						if (qosUuid != null) {
							XenTools.setPortQos(device.getVifNumber(), qosUuid);
							qos = new QosPolicy(qosUuid, download, download);
							DataProvider.getQoses().put(qosUuid, qos);
							device.setQosUuid(qosUuid);
							DataProvider.updateQosStore(device.getVmUuid(),
									qos, UPDATETYPE.INSERT);
							DataProvider.updateDeviceStore(device,
									UPDATETYPE.BAND, null);
						}
					}
					msg = msg + " Download set done.";
				} catch (NumberFormatException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			DisplayMessage.displayStatus(MainFrame.getShell(), msg);
		}
	}

	protected void addQueueToCurrentDevice() {
		// TODO Auto-generated method stub
		if (device != null && device.getQosUuid() != null) {
			if (!textMaxRate.getText().equals("")
					&& !textMinRate.getText().equals("")) {
				try {
					Long max = Long.parseLong(textMaxRate.getText()) * 1000;
					Long min = Long.parseLong(textMinRate.getText()) * 1000;
					if (max > 0 && min > 0 && min <= max) {
						String queueUuid = XenTools
								.createRow("queue", max, min);
						if (queueUuid != null) {
							QosPolicy qos = DataProvider.getQoses().get(
									device.getQosUuid());
							if (qos != null) {
								Map<Integer, String> queues = qos.getQueues();
								for (int i = 0; i < MAXQUEUE; i++) {
									if (!queues.containsKey(i)) {
										queues.put(i, queueUuid);
										QosQueue queue = new QosQueue(
												queueUuid, max, min);
										DataProvider.getQueues().put(queueUuid,
												queue);
										XenTools.addQosQueue(
												device.getQosUuid(), i,
												queueUuid);
										DataProvider.updateQueueStore(
												qos.getUuid(), queue, i,
												UPDATETYPE.INSERT);
										DisplayMessage.displayStatus(
												MainFrame.getShell(),
												"Add to qos done.Queue ID : "
														+ i);
										return;
									}
								}
								DisplayMessage.displayError(
										MainFrame.getShell(),
										"Queues are full.");
							}
						}
					}
				} catch (NumberFormatException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}

	protected void deleteQueueFromQos() {
		// TODO Auto-generated method stub

		// get select index
		int index = comboDelete.getSelectionIndex();
		String[] items = comboDelete.getItems();

		if (index > -1 && !items[index].equals("None")) {
			// get queue id
			int id = Integer.valueOf(items[index]);
			// get all queues
			Map<String, QosQueue> queues = DataProvider.getQueues();
			// if queue exists
			if (queues.containsKey(queue.getUuid())) {

				// remove from data provider.
				queues.remove(queue.getUuid());

				// remove from qos policy
				Map<Integer, String> qosqueues = DataProvider.getQoses()
						.get(device.getQosUuid()).getQueues();
				if (qosqueues.containsKey(id))
					qosqueues.remove(id);

				// remove from xen sw
				XenTools.removeQosQueue(qos.getUuid(), id);

				// remove from database
				DataProvider.updateQueueStore(device.getQosUuid(), queue, id,
						UPDATETYPE.DELETE);

				DisplayMessage.displayStatus(MainFrame.getShell(), "Queue "
						+ id + " deleted");
				populateQosQueues();
			}
		}
	}

	protected void populateQosQueues() {
		// TODO Auto-generated method stub
		if (device != null && device.getQosUuid() != null) {
			// get all queues from the specific qos
			qos = DataProvider.getQoses().get(device.getQosUuid());
			if (qos != null) {
				Map<Integer, String> queues = qos.getQueues();
				// ids used for store queue id
				List<String> ids = new ArrayList<String>();
				for (Integer i : queues.keySet()) {
					ids.add(i + "");
				}
				// fill into combo box
				if (ids.size() > 0) {
					String[] data = ids.toArray(new String[ids.size()]);
					comboDelete.setItems(data);
					comboDelete.select(0);
					populateQueue(0);
				} else {
					comboDelete.setItems(new String[] { "None" });
				}
			}
		}
	}

	private void populateQueue(int index) {
		// TODO Auto-generated method stub
		if (device != null) {
			qos = DataProvider.getQoses().get(device.getQosUuid());
			// get the queue id
			int id = Integer.valueOf(comboDelete.getItem(index));
			String queueUuid = qos.getQueues().get(id);
			queue = DataProvider.getQueues().get(queueUuid);
			if (queue != null) {
				textDelMax.setText(String.valueOf(queue.getMaxRate() / 1000));
				textDelMin.setText(String.valueOf(queue.getMinRate() / 1000));
			}
		}
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

		comboDevice = new Combo(grpChoosedevice, SWT.READ_ONLY);
		comboDevice.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = comboDevice.getSelectionIndex();
				String item = comboDevice.getItem(index);
				if (index > -1) {
					populateDevice(item);
				}
			}
		});
		comboDevice.setBounds(10, 25, 120, 23);

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

		textUpload = new Text(grpRatelimit, SWT.BORDER);
		textUpload.setBounds(106, 34, 118, 21);

		Label lblKb = new Label(grpRatelimit, SWT.NONE);
		lblKb.setBounds(230, 37, 21, 15);
		lblKb.setText("Kb");

		Label lblDownload = new Label(grpRatelimit, SWT.NONE);
		lblDownload.setText("Download : ");
		lblDownload.setAlignment(SWT.RIGHT);
		lblDownload.setBounds(280, 37, 69, 15);

		textDownload = new Text(grpRatelimit, SWT.BORDER);
		textDownload.setBounds(355, 34, 118, 21);

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

		textMaxRate = new Text(grpAddqueue, SWT.BORDER);
		textMaxRate.setBounds(106, 34, 118, 21);

		Label label_1 = new Label(grpAddqueue, SWT.NONE);
		label_1.setText("Kb");
		label_1.setBounds(230, 37, 21, 15);

		Label lblMinrate = new Label(grpAddqueue, SWT.NONE);
		lblMinrate.setText("MinRate  : ");
		lblMinrate.setBounds(292, 37, 55, 15);

		textMinRate = new Text(grpAddqueue, SWT.BORDER);
		textMinRate.setBounds(355, 34, 118, 21);

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

		comboDelete = new Combo(grpDeletequeue, SWT.READ_ONLY);
		comboDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = comboDelete.getSelectionIndex();
				if (index > -1 && !comboDelete.getItems()[index].equals("None")) {
					populateQueue(index);
				}
			}
		});
		comboDelete.setBounds(38, 27, 103, 23);

		Label label_2 = new Label(grpDeletequeue, SWT.NONE);
		label_2.setText("MaxRate  : ");
		label_2.setBounds(169, 33, 55, 15);

		textDelMax = new Text(grpDeletequeue, SWT.BORDER);
		textDelMax.setEditable(false);
		textDelMax.setBounds(235, 30, 118, 21);

		Label label_4 = new Label(grpDeletequeue, SWT.NONE);
		label_4.setText("Kb");
		label_4.setBounds(359, 33, 21, 15);

		Label label_5 = new Label(grpDeletequeue, SWT.NONE);
		label_5.setText("MinRate  : ");
		label_5.setBounds(421, 33, 55, 15);

		textDelMin = new Text(grpDeletequeue, SWT.BORDER);
		textDelMin.setEditable(false);
		textDelMin.setBounds(484, 30, 118, 21);

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

		Button btnRefresh = new Button(grpDeletequeue, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				populateQosQueues();
			}
		});
		btnRefresh.setBounds(767, 24, 91, 33);
		btnRefresh.setText("Refresh");
		fd_grpSetvlan.top = new FormAttachment(0, 400);
		fd_grpSetvlan.left = new FormAttachment(grpChoosedevice, 0, SWT.LEFT);

		lblIp = new Label(grpChoosedevice, SWT.NONE);
		lblIp.setBounds(178, 31, 208, 15);
		lblIp.setText("IP : ");

		lblMac = new Label(grpChoosedevice, SWT.NONE);
		lblMac.setText("Mac : ");
		lblMac.setBounds(409, 31, 248, 15);
		fd_grpSetvlan.right = new FormAttachment(100);
		grpSetvlan.setLayoutData(fd_grpSetvlan);

		Label lblNewLabel = new Label(grpSetvlan, SWT.NONE);
		lblNewLabel.setAlignment(SWT.RIGHT);
		lblNewLabel.setBounds(38, 39, 55, 15);
		lblNewLabel.setText("VlanId : ");

		textVlan = new Text(grpSetvlan, SWT.BORDER);
		textVlan.setBounds(99, 36, 73, 21);

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
