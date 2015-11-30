package com.main.view;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.basic.elements.Action;
import com.basic.elements.Flow;
import com.basic.elements.Switch;
import com.main.app.staticflow.pusher.ActionManagerPusher;
import com.main.app.staticflow.pusher.FlowManagerPusher;
import com.main.app.staticflow.pusher.MatchManagerPusher;
import com.main.app.staticflow.table.ActionToTable;
import com.main.app.staticflow.table.MatchToTable;
import com.main.provider.DataProvider;
import com.main.view.util.DisplayMessage;
import com.tools.util.JSONException;

public class CompositeStaticFlow extends Composite {
	private Text textFlowName, textPriority;
	private Table tableAction, tableMatch;
	private Tree treeSwitch, treeFlows, treeAction;
	private TableEditor actionEditor, matchEditor;
	private Flow flow;
	private static Switch currentSwtich;
	private static Action currentAction;
	private String currentSwtichDpid;
	protected String[][] matchTableFormat;
	private boolean unsavedProgress = false;
	private boolean newFlow = false;
	private final int EDITABLECOLUMN = 1;
	private Combo combo;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public CompositeStaticFlow(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());

		createContents();
		populateSwitchTree();
	}

	private void disposeEditors(String editor) {
		// Dispose the editor do it doesn't leave a ghost table item
		if (actionEditor.getEditor() != null && ((editor.equals("action") || editor.equals("all"))))
			actionEditor.getEditor().dispose();
		if (matchEditor.getEditor() != null && ((editor.equals("match") || editor.equals("all"))))
			matchEditor.getEditor().dispose();
	}

	private void populateSwitchTree() {
		disposeEditors("all");
		treeSwitch.removeAll();
		treeFlows.removeAll();
		treeAction.removeAll();

		textFlowName.setText("");
		textPriority.setText("");

		flow = null;
		newFlow = false;
		currentSwtich = null;
		try {
			if (DataProvider.getSwitches(true).size() > 0) {
				for (Switch sw : DataProvider.getSwitches(false).values()) {
					new TreeItem(treeSwitch, SWT.NONE).setText(sw.getDpid());
				}
			} else {
				new TreeItem(treeSwitch, SWT.NONE).setText("None");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void populateFlowTree(TreeItem item) {
		disposeEditors("all");
		treeFlows.removeAll();
		treeAction.removeAll();
		tableMatch.removeAll();
		tableAction.removeAll();

		textFlowName.setText("");
		textPriority.setText("");

		flow = null;
		newFlow = false;

		currentSwtichDpid = item.getText();
		try {
			currentSwtich = DataProvider.getSwitch(currentSwtichDpid);
			treeSwitch.select(item);

			if (DataProvider.getStaticFlows(currentSwtichDpid, true).size() > 0) {
				for (Flow flow : DataProvider.getStaticFlows(currentSwtichDpid, true).values()) {
					if (flow.getName() != null) {
						new TreeItem(treeFlows, SWT.NONE).setText(flow.getName());
					}
				}
			} else {
				new TreeItem(treeFlows, SWT.NONE).setText("None");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void populateFlowView(String flowname) {
		try {
			flow = DataProvider.getStaticFlows(currentSwtichDpid, false).get(flowname);
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (flow != null) {
			textFlowName.setText(flowname);
			newFlow = false;
			if (flow.getPriority() != null)
				textPriority.setText(flow.getPriority());
		}
		populateActionTree();
		populateMatchTable();

	}

	private void populateActionTree() {
		// TODO Auto-generated method stub
		currentAction = null;
		disposeEditors("action");

		tableAction.removeAll();
		treeAction.removeAll();
		if (!flow.getActions().isEmpty()) {
			for (Action action : flow.getActions()) {
				new TreeItem(treeAction, SWT.None).setText(action.getType());
			}
		} else {
			new TreeItem(treeAction, SWT.NONE).setText("None");
		}
	}

	private void populateActionTable(int index) {
		if (flow != null && flow.getActions().size() > 0) {
			currentAction = flow.getActions().get(index);
			tableAction.removeAll();
			for (String[] s : ActionToTable.getActionTableFormat(currentAction)) {
				new TableItem(tableAction, SWT.NO_FOCUS).setText(s);
			}
		}
	}

	private void populateMatchTable() {
		// TODO Auto-generated method stub
		tableMatch.removeAll();
		matchTableFormat = MatchToTable.getMatchTableFormat(flow.getMatch());
		for (String[] s : matchTableFormat) {
			new TableItem(tableMatch, SWT.NO_FOCUS).setText(s);
		}
	}

	private void populateNewMatchTable() {
		tableMatch.removeAll();
		for (String[] s : MatchToTable.getNewMatchTableFormat()) {
			new TableItem(tableMatch, SWT.NO_FOCUS).setText(s);
		}
	}

	private void setupNewAction(String actionType) {
		currentAction = new Action(actionType);
		tableAction.removeAll();

		for (String[] s : ActionToTable.getNewActionTableFormat(currentAction)) {
			new TableItem(tableAction, SWT.NO_FOCUS).setText(s);
		}
	}

	private void setupNewFlow() {
		if (!unsavedProgress) {
			if (currentSwtich != null) {
				flow = new Flow(currentSwtichDpid);
				textFlowName.setText("");
				textPriority.setText("");
				newFlow = true;

				populateActionTree();
				populateMatchTable();
			} else {
				DisplayMessage.displayError(MainFrame.getShell(), "Choose a switch first");
			}
		} else {
			MessageBox msg = new MessageBox(MainFrame.getShell(), SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
			msg.setMessage("Progress Unsaved, ");
			if(msg.open() == SWT.YES){
				unsavedProgress = false;
				setupNewFlow();
			}
		}
	}

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

		treeSwitch = new Tree(composite_1, SWT.BORDER | SWT.None | SWT.NO_FOCUS);
		treeSwitch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] items = treeSwitch.getSelection();
				if (items.length > 0 && !items[0].getText().equals("None")) {
					populateFlowTree(items[0]);
				}
			}
		});
		FormData fd_treeSwitch = new FormData();
		fd_treeSwitch.bottom = new FormAttachment(40);
		fd_treeSwitch.right = new FormAttachment(100, -10);
		fd_treeSwitch.top = new FormAttachment(0, 30);
		fd_treeSwitch.left = new FormAttachment(0, 10);
		treeSwitch.setLayoutData(fd_treeSwitch);

		treeFlows = new Tree(composite_1, SWT.BORDER | SWT.NONE | SWT.NO_FOCUS);
		treeFlows.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] items = treeFlows.getSelection();
				if (items.length > 0 && !items[0].getText().equals("None")) {
					populateFlowView(items[0].getText());
				}
			}
		});
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
		fd_composite_2.left = new FormAttachment(composite_1, 0, SWT.RIGHT);
		composite_2.setLayoutData(fd_composite_2);

		Button btnRefresh = new Button(composite_2, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				populateSwitchTree();
			}
		});
		btnRefresh.setLayoutData(new RowData(100, 40));
		btnRefresh.setText("Refresh");

		Button btnNewflow = new Button(composite_2, SWT.NONE);
		btnNewflow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setupNewFlow();
			}
		});
		btnNewflow.setLayoutData(new RowData(100, 40));
		btnNewflow.setText("NewFlow");

		Button btnPush = new Button(composite_2, SWT.NONE);
		btnPush.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (flow != null && flow.getActions().size() > 0) {
					if (flow != null) {
						if (flow.getName() != null || !textFlowName.getText().equals("")) {

							if (FlowManagerPusher.errorChecksPassed(textPriority.getText())) {
								// Parse the changes made to the flow
								flow.setName(textFlowName.getText());
								flow.setPriority(textPriority.getText());

								// Push the flow and get the response
								String response;
								try {
									response = FlowManagerPusher.push(flow);
									if (response.equals("Flow successfully pushed down to switches")) {
										if (currentSwtich != null) {
											populateFlowTree(treeSwitch.getSelection()[0]);
										} else {
											populateSwitchTree();
										}

										disposeEditors("all");
										unsavedProgress = false;
									}
									DisplayMessage.displayStatus(MainFrame.getShell(), response);
								} catch (IOException | JSONException e1) {
									DisplayMessage.displayError(MainFrame.getShell(),
											"Problem occured while pushing flow, please view the log for details");
									e1.printStackTrace();
								}
							}

						} else {
							DisplayMessage.displayError(MainFrame.getShell(), "Your flow must have a name");
						}
					} else {
						DisplayMessage.displayError(MainFrame.getShell(), "You do not have a flow to push!");
					}
				}
			}
		});
		btnPush.setLayoutData(new RowData(100, 40));
		btnPush.setText("Push");

		Button btnClear = new Button(composite_2, SWT.NONE);
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setupNewFlow();
			}
		});
		btnClear.setLayoutData(new RowData(100, 40));
		btnClear.setText("Clear");

		Button btnDeleteflow = new Button(composite_2, SWT.NONE);
		btnDeleteflow.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (flow != null && newFlow == false) {
					String response;
					try {
						response = FlowManagerPusher.remove(flow);
						if (response.equals("Entry " + flow.getName() + " deleted")) {
							populateFlowTree(treeSwitch.getSelection()[0]);
							disposeEditors("all");
							DisplayMessage.displayStatus(MainFrame.getShell(), response);
						}
					} catch (IOException | JSONException e1) {
						// TODO Auto-generated catch block
						DisplayMessage.displayError(MainFrame.getShell(), "Problem occured while delete flow.");
						e1.printStackTrace();
					}
				} else {
					DisplayMessage.displayError(MainFrame.getShell(), "Must select a flow to delete.");
				}
			}
		});
		btnDeleteflow.setLayoutData(new RowData(100, 40));
		btnDeleteflow.setText("DeleteFlow");

		Button btnDeleteallflows = new Button(composite_2, SWT.NONE);
		btnDeleteallflows.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// are you sure? warning
				int style = SWT.APPLICATION_MODAL | SWT.YES | SWT.NO;
				MessageBox messageBox = new MessageBox(MainFrame.getShell(), style);
				messageBox.setText("Are you sure?!");
				messageBox.setMessage("Are you sure you wish to delete all flows?");
				if (messageBox.open() == SWT.YES) {
					// Delete all the flows for the current switch, populate
					// the table with the new information
					if (currentSwtich != null) {
						try {
							FlowManagerPusher.deleteAll(currentSwtichDpid);
							populateFlowTree(treeSwitch.getSelection()[0]);
						} catch (IOException | JSONException e1) {
							// TODO Auto-generated catch block
							DisplayMessage.displayError(MainFrame.getShell(),
									"Problem occured while delting all flows");
							e1.printStackTrace();
						}
					} else {
						DisplayMessage.displayError(MainFrame.getShell(),
								"Must select a switch before deleting all flows");
					}
				}
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
		composite_3.setLayout(new FormLayout());
		composite_3.setLayoutData(fd_composite_3);

		treeAction = new Tree(composite_3, SWT.BORDER);
		treeAction.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeEditors("action");
				// Populate the action table, if we actually have actions.
				if (!treeAction.getSelection()[0].getText(0).equals("None"))
					populateActionTable(treeAction.indexOf(treeAction.getSelection()[0]));
			}
		});
		FormData fd_treeAction = new FormData();
		fd_treeAction.bottom = new FormAttachment(100, -10);
		fd_treeAction.right = new FormAttachment(0, 200);
		fd_treeAction.top = new FormAttachment(0, 73);
		fd_treeAction.left = new FormAttachment(0, 10);
		treeAction.setLayoutData(fd_treeAction);

		tableAction = new Table(composite_3, SWT.BORDER | SWT.FULL_SELECTION);
		tableAction.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeEditors("action");

				TableItem item = (TableItem) e.item;
				if (item != null) {
					Text newEditor = new Text(tableAction, SWT.NONE);
					newEditor.setText(item.getText(EDITABLECOLUMN));
					newEditor.addModifyListener(new ModifyListener() {

						@Override
						public void modifyText(ModifyEvent arg0) {
							// TODO Auto-generated method stub
							Text text = (Text) actionEditor.getEditor();
							actionEditor.getItem().setText(EDITABLECOLUMN, text.getText());
						}
					});
					newEditor.selectAll();
					newEditor.setFocus();
					actionEditor.setEditor(newEditor, item, EDITABLECOLUMN);

				}
			}
		});
		FormData fd_tableAction = new FormData();
		fd_tableAction.right = new FormAttachment(100, -10);
		fd_tableAction.top = new FormAttachment(0, 103);
		tableAction.setLayoutData(fd_tableAction);
		tableAction.setHeaderVisible(true);
		tableAction.setLinesVisible(true);

		actionEditor = new TableEditor(tableAction);
		actionEditor.horizontalAlignment = SWT.LEFT;
		actionEditor.grabHorizontal = true;
		actionEditor.minimumWidth = 50;

		TableColumn tblclmnType = new TableColumn(tableAction, SWT.NONE);
		tblclmnType.setWidth(146);
		tblclmnType.setText("ActionType");

		TableColumn tblclmnValue = new TableColumn(tableAction, SWT.NONE);
		tblclmnValue.setWidth(124);
		tblclmnValue.setText("Value");

		tableMatch = new Table(composite_3, SWT.BORDER | SWT.FULL_SELECTION);
		tableMatch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeEditors("match");

				TableItem item = (TableItem) e.item;
				if (item != null) {
					Text newEditor = new Text(tableMatch, SWT.NONE);
					newEditor.setText(item.getText(EDITABLECOLUMN));
					newEditor.addModifyListener(new ModifyListener() {

						@Override
						public void modifyText(ModifyEvent arg0) {
							// TODO Auto-generated method stub
							Text text = (Text) matchEditor.getEditor();
							matchEditor.getItem().setText(EDITABLECOLUMN, text.getText());
						}
					});
					newEditor.selectAll();
					newEditor.setFocus();
					matchEditor.setEditor(newEditor, item, EDITABLECOLUMN);

				}
			}
		});
		fd_tableAction.left = new FormAttachment(tableMatch, 0, SWT.LEFT);
		FormData fd_tableMatch = new FormData();
		fd_tableMatch.bottom = new FormAttachment(100, -10);
		fd_tableMatch.right = new FormAttachment(100, -10);
		fd_tableMatch.top = new FormAttachment(50);
		fd_tableMatch.left = new FormAttachment(0, 210);
		tableMatch.setLayoutData(fd_tableMatch);
		tableMatch.setLinesVisible(true);
		tableMatch.setHeaderVisible(true);

		matchEditor = new TableEditor(tableMatch);
		matchEditor.horizontalAlignment = SWT.LEFT;
		matchEditor.grabHorizontal = true;
		matchEditor.minimumWidth = 50;

		TableColumn tblclmnMatch = new TableColumn(tableMatch, SWT.NONE);
		tblclmnMatch.setWidth(217);
		tblclmnMatch.setText("Match");

		TableColumn tblclmnValue_1 = new TableColumn(tableMatch, SWT.NONE);
		tblclmnValue_1.setWidth(149);
		tblclmnValue_1.setText("Value");

		Composite composite_4 = new Composite(composite_3, SWT.NONE);
		FormData fd_composite_4 = new FormData();
		fd_composite_4.bottom = new FormAttachment(0, 67);
		fd_composite_4.right = new FormAttachment(100, -10);
		fd_composite_4.top = new FormAttachment(0, 3);
		fd_composite_4.left = new FormAttachment(0, 10);
		composite_4.setLayoutData(fd_composite_4);

		Label lblNewLabel = new Label(composite_4, SWT.NONE);
		lblNewLabel.setLocation(27, 24);
		lblNewLabel.setSize(78, 15);
		lblNewLabel.setAlignment(SWT.RIGHT);
		lblNewLabel.setText("FlowName");

		textFlowName = new Text(composite_4, SWT.BORDER);
		textFlowName.setLocation(114, 21);
		textFlowName.setSize(111, 21);

		Label lblPriority = new Label(composite_4, SWT.NONE);
		lblPriority.setLocation(342, 24);
		lblPriority.setSize(78, 15);
		lblPriority.setText("Priority");
		lblPriority.setAlignment(SWT.RIGHT);

		textPriority = new Text(composite_4, SWT.BORDER);
		textPriority.setLocation(428, 21);
		textPriority.setSize(111, 21);

		Button btnSaveaction = new Button(composite_3, SWT.NONE);
		fd_tableAction.bottom = new FormAttachment(40);
		btnSaveaction.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (flow != null) {
					if (currentAction != null) {
						if (!tableAction.getItems()[0].getText(1).isEmpty()) {
							if (ActionToTable.errorChecksPassed(currentSwtich, currentAction, tableAction.getItems())) {
								ActionManagerPusher.addAction(tableAction.getItems(), currentAction, flow);

								disposeEditors("action");
								populateActionTree();
							}
						} else {
							DisplayMessage.displayError(MainFrame.getShell(),
									"Must enter a value before you save an action");
						}
					} else {
						DisplayMessage.displayError(MainFrame.getShell(), "Must create an action to save");
					}
				} else {
					DisplayMessage.displayError(MainFrame.getShell(),
							"Must must select or create a flow before modify actions");
				}
			}
		});
		FormData fd_btnSaveaction = new FormData();
		fd_btnSaveaction.bottom = new FormAttachment(tableAction, 45, SWT.BOTTOM);
		fd_btnSaveaction.right = new FormAttachment(treeAction, 120, SWT.RIGHT);
		fd_btnSaveaction.top = new FormAttachment(tableAction, 10, SWT.BOTTOM);
		fd_btnSaveaction.left = new FormAttachment(treeAction, 10);
		btnSaveaction.setLayoutData(fd_btnSaveaction);
		btnSaveaction.setText("SaveAction");

		Button btnSavematch = new Button(composite_3, SWT.NONE);
		btnSavematch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (flow != null) {
					if (MatchToTable.errorChecksPassed(currentSwtich, tableMatch.getItems())) {
						flow.setMatch(MatchManagerPusher.addMatch(tableMatch.getItems()));

						unsavedProgress = true;
						disposeEditors("match");
					}
				} else {
					DisplayMessage.displayError(MainFrame.getShell(), "Must select or create first");
				}

			}
		});
		btnSavematch.setText("SaveMatch");
		FormData fd_btnSavematch = new FormData();
		fd_btnSavematch.bottom = new FormAttachment(tableMatch, -6, SWT.TOP);
		fd_btnSavematch.left = new FormAttachment(100, -100);
		fd_btnSavematch.top = new FormAttachment(tableMatch, -40, SWT.TOP);
		fd_btnSavematch.right = new FormAttachment(100, -10);
		btnSavematch.setLayoutData(fd_btnSavematch);

		combo = new Combo(composite_3, SWT.READ_ONLY);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposeEditors("action");
				if (flow != null) {
					setupNewAction(combo.getItem(combo.getSelectionIndex()));
				} else {
					DisplayMessage.displayError(MainFrame.getShell(), "Must select or create a flow.");
				}
			}
		});
		combo.setItems(new String[] { "output", "enqueue", "strip_vlan", "set_vlan_vid", "set_vlan_pcp", "set_eth_src",
				"set_eth_dst", "set_ip_tos", "set_ipv4_src", "set_ipv4_dst", "set_tp_src", "set_tp_dst" });
		FormData fd_combo = new FormData();
		fd_combo.bottom = new FormAttachment(tableAction, -6);
		fd_combo.right = new FormAttachment(treeAction, 130, SWT.RIGHT);
		fd_combo.left = new FormAttachment(treeAction, 10);
		combo.setLayoutData(fd_combo);

		Button btnNewButton = new Button(composite_3, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (flow != null) {
					populateNewMatchTable();
				} else {
					DisplayMessage.displayError(MainFrame.getShell(), "Must select or create a flow first");
				}
			}
		});
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.bottom = new FormAttachment(tableMatch, -6);
		fd_btnNewButton.top = new FormAttachment(btnSavematch, 0, SWT.TOP);
		fd_btnNewButton.left = new FormAttachment(btnSavematch, -101, SWT.LEFT);
		fd_btnNewButton.right = new FormAttachment(btnSavematch, -10);
		btnNewButton.setLayoutData(fd_btnNewButton);
		btnNewButton.setText("DefaultValues");

		Button btnNewButton_1 = new Button(composite_3, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (flow != null) {
					if (currentAction != null) {
						ActionManagerPusher.removeAction(flow, currentAction);
						populateActionTree();
					}
				} else {
					DisplayMessage.displayError(MainFrame.getShell(), "Must select or create a flow");
				}
			}
		});
		FormData fd_btnNewButton_1 = new FormData();
		fd_btnNewButton_1.top = new FormAttachment(btnSaveaction, 0, SWT.TOP);
		fd_btnNewButton_1.left = new FormAttachment(btnSaveaction, 6);
		fd_btnNewButton_1.bottom = new FormAttachment(btnSaveaction, 0, SWT.BOTTOM);
		fd_btnNewButton_1.right = new FormAttachment(btnSaveaction, 116, SWT.RIGHT);
		btnNewButton_1.setLayoutData(fd_btnNewButton_1);
		btnNewButton_1.setText("Remove");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
