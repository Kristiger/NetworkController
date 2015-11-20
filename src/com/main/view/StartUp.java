package com.main.view;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.main.provider.DataProvider;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;

public class StartUp {

	protected Shell shell;
	private String IP;
	private String PORT;
	private Text textIP;
	private Text textPort;
	private Label lblInputIpAnd;

	public StartUp() {
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

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 166);
		shell.setText("SWT Application");

		Group grpConnect = new Group(shell, SWT.NONE);
		grpConnect.setText("Connect");
		grpConnect.setBounds(10, 10, 414, 108);

		textIP = new Text(grpConnect, SWT.BORDER);
		textIP.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		textIP.setBounds(26, 47, 130, 29);

		textPort = new Text(grpConnect, SWT.BORDER);
		textPort.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		textPort.setBounds(168, 47, 85, 29);

		Button btnNewButton = new Button(grpConnect, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IP = textIP.getText();
				PORT = textPort.getText();
				if(!IP.equals("") && !PORT.equals("")){
					connectToRemote(IP, PORT);
				}else {
					lblInputIpAnd.setVisible(true);
				}
			}
		});
		btnNewButton.setBounds(292, 37, 93, 41);
		btnNewButton.setText("New Button");
		
		lblInputIpAnd = new Label(grpConnect, SWT.NONE);
		lblInputIpAnd.setTouchEnabled(true);
		lblInputIpAnd.setBounds(36, 83, 146, 15);
		lblInputIpAnd.setText("Input IP and PORT");
		lblInputIpAnd.setVisible(false);
	}

	protected void connectToRemote(String iP2, String pORT2) {
		// TODO Auto-generated method stub
		try {
			if (InetAddress.getByName(IP).isReachable(5000)) {
				DataProvider.setIP(IP);
				DataProvider.setPORT(PORT);
				new MainFrame();
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
