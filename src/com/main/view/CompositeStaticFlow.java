package com.main.view;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import swing2swt.layout.FlowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class CompositeStaticFlow extends Composite {
	private Text text;
	private Text text_1;
	private Table tableAction;
	private Table tableMatch;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CompositeStaticFlow(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		
		createContents();
	}

	/**
	 * 
	 */
	private void createContents() {
		Composite composite = new Composite(this, SWT.NONE);
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100);
		fd_composite.right = new FormAttachment(100);
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		composite.setLayoutData(fd_composite);
		FormLayout fl_composite = new FormLayout();
		composite.setLayout(fl_composite);
		
		Composite composite_1 = new Composite(composite, SWT.BORDER);
		composite_1.setLayout(new FormLayout());
		FormData fd_composite_1 = new FormData();
		fd_composite_1.bottom = new FormAttachment(100, -10);
		fd_composite_1.top = new FormAttachment(0, 10);
		fd_composite_1.right = new FormAttachment(0, 250);
		fd_composite_1.left = new FormAttachment(0, 10);
		composite_1.setLayoutData(fd_composite_1);
		
		Tree treeSwitch = new Tree(composite_1, SWT.BORDER);
		FormData fd_treeSwitch = new FormData();
		fd_treeSwitch.bottom = new FormAttachment(40);
		fd_treeSwitch.right = new FormAttachment(100, -10);
		fd_treeSwitch.top = new FormAttachment(0, 30);
		fd_treeSwitch.left = new FormAttachment(0, 10);
		treeSwitch.setLayoutData(fd_treeSwitch);
		
		Tree treeFlows = new Tree(composite_1, SWT.BORDER);
		FormData fd_treeFlows = new FormData();
		fd_treeFlows.bottom = new FormAttachment(100, -10);
		fd_treeFlows.right = new FormAttachment(100, -10);
		fd_treeFlows.top = new FormAttachment(50);
		fd_treeFlows.left = new FormAttachment(0, 10);
		treeFlows.setLayoutData(fd_treeFlows);
		
		Composite composite_2 = new Composite(composite, SWT.BORDER);
		RowLayout rl_composite_2 = new RowLayout(SWT.HORIZONTAL);
		rl_composite_2.justify = true;
		rl_composite_2.center = true;
		rl_composite_2.fill = true;
		rl_composite_2.marginBottom = 10;
		rl_composite_2.spacing = 20;
		rl_composite_2.marginWidth = 15;
		rl_composite_2.marginRight = 15;
		rl_composite_2.marginTop = 15;
		composite_2.setLayout(rl_composite_2);
		FormData fd_composite_2 = new FormData();
		fd_composite_2.bottom = new FormAttachment(0, 82);
		fd_composite_2.right = new FormAttachment(100, -10);
		fd_composite_2.top = new FormAttachment(0, 10);
		fd_composite_2.left = new FormAttachment(composite_1,0,SWT.RIGHT);
		composite_2.setLayoutData(fd_composite_2);
		
		Button btnNewButton = new Button(composite_2, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnNewButton.setLayoutData(new RowData(100, 40));
		btnNewButton.setText("Refresh");
		
		Button btnNewflow = new Button(composite_2, SWT.NONE);
		btnNewflow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnNewflow.setLayoutData(new RowData(100, 40));
		btnNewflow.setText("NewFlow");
		
		Button btnPush = new Button(composite_2, SWT.NONE);
		btnPush.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnPush.setLayoutData(new RowData(100, 40));
		btnPush.setText("Push");
		
		Button btnClear = new Button(composite_2, SWT.NONE);
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnClear.setLayoutData(new RowData(100, 40));
		btnClear.setText("Clear");
		
		Button btnDeleteflow = new Button(composite_2, SWT.NONE);
		btnDeleteflow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnDeleteflow.setLayoutData(new RowData(100, 40));
		btnDeleteflow.setText("DeleteFlow");
		
		Button btnDeleteallflows = new Button(composite_2, SWT.NONE);
		btnDeleteallflows.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnDeleteallflows.setLayoutData(new RowData(100, 40));
		btnDeleteallflows.setText("DeleteAllFlows");
		
		Composite composite_3 = new Composite(composite, SWT.NONE);
		FormData fd_composite_3 = new FormData();
		fd_composite_3.bottom = new FormAttachment(100, 100, -10);
		fd_composite_3.right = new FormAttachment(100, 100, -10);
		fd_composite_3.top = new FormAttachment(composite_2, 6);
		fd_composite_3.left = new FormAttachment(composite_1, 6);
		
		Label lblSwitches = new Label(composite_1, SWT.NONE);
		FormData fd_lblSwitches = new FormData();
		fd_lblSwitches.bottom = new FormAttachment(treeSwitch, -6);
		fd_lblSwitches.left = new FormAttachment(0, 26);
		lblSwitches.setLayoutData(fd_lblSwitches);
		lblSwitches.setText("Switches");
		
		Label lblStaticflows = new Label(composite_1, SWT.NONE);
		FormData fd_lblStaticflows = new FormData();
		fd_lblStaticflows.bottom = new FormAttachment(treeFlows, -5);
		fd_lblStaticflows.left = new FormAttachment(lblSwitches, 0, SWT.LEFT);
		lblStaticflows.setLayoutData(fd_lblStaticflows);
		lblStaticflows.setText("StaticFlows");
		composite_3.setLayoutData(fd_composite_3);
		
		Tree treeAction = new Tree(composite_3, SWT.BORDER);
		treeAction.setBounds(10, 73, 184, 435);
		
		tableAction = new Table(composite_3, SWT.BORDER | SWT.FULL_SELECTION);
		tableAction.setBounds(200, 73, 370, 156);
		tableAction.setHeaderVisible(true);
		tableAction.setLinesVisible(true);
		
		TableColumn tblclmnType = new TableColumn(tableAction, SWT.NONE);
		tblclmnType.setWidth(146);
		tblclmnType.setText("ActionType");
		
		TableColumn tblclmnValue = new TableColumn(tableAction, SWT.NONE);
		tblclmnValue.setWidth(124);
		tblclmnValue.setText("Value");
		
		tableMatch = new Table(composite_3, SWT.BORDER | SWT.FULL_SELECTION);
		tableMatch.setLinesVisible(true);
		tableMatch.setHeaderVisible(true);
		tableMatch.setBounds(200, 237, 370, 271);
		
		TableColumn tblclmnMatch = new TableColumn(tableMatch, SWT.NONE);
		tblclmnMatch.setWidth(217);
		tblclmnMatch.setText("Match");
		
		TableColumn tblclmnValue_1 = new TableColumn(tableMatch, SWT.NONE);
		tblclmnValue_1.setWidth(149);
		tblclmnValue_1.setText("Value");
		
		Composite composite_4 = new Composite(composite_3, SWT.NONE);
		composite_4.setBounds(10, 3, 629, 64);
		
		Label lblNewLabel = new Label(composite_4, SWT.NONE);
		lblNewLabel.setLocation(27, 24);
		lblNewLabel.setSize(78, 15);
		lblNewLabel.setAlignment(SWT.RIGHT);
		lblNewLabel.setText("FlowName");
		
		text = new Text(composite_4, SWT.BORDER);
		text.setLocation(114, 21);
		text.setSize(111, 21);
		
		Label label = new Label(composite_4, SWT.NONE);
		label.setLocation(342, 24);
		label.setSize(78, 15);
		label.setText("FlowName");
		label.setAlignment(SWT.RIGHT);
		
		text_1 = new Text(composite_4, SWT.BORDER);
		text_1.setLocation(428, 21);
		text_1.setSize(111, 21);
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
