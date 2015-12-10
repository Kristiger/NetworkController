package com.main.app.staticflow.table;

import java.util.Map;

import org.eclipse.swt.widgets.TableItem;

import com.basic.elements.Action;
import com.basic.elements.Port;
import com.basic.elements.Switch;
import com.main.view.MainFrame;
import com.main.view.util.DisplayMessage;
import com.tools.util.ErrorCheck;

public class ActionToTable {

	// Gets an action by it's index and formats it so that it can be displayed
	// in a table
	public static String[][] getActionTableFormat(Action action) {

		if (action.getType().equals("strip-vlan")) {
			String[][] act = { { "Type", action.getType() } };
			return act;
		} else if (action.getType().equals("enqueue")) {
			StringBuilder param = new StringBuilder(action.getParam());
			StringBuilder value = new StringBuilder(action.getValue());
			String[][] act = { { param.substring(0, param.indexOf(":")), value.substring(0, value.indexOf(":")) },
					{ param.substring(param.indexOf(":") + 1, param.length()),
							value.substring(value.indexOf(":") + 1, value.length()) },
					{ "Type", action.getType(), } };
			return act;
		} else {
			String[][] act = { { action.getParam(), action.getValue() }, { "Type", action.getType() } };
			return act;
		}
	}

	// Creates a blank new actions and formats it so it can be displayed in a
	// table
	public static String[][] getNewActionTableFormat(Action action) {

		if (action.getType().equals("strip-vlan")) {
			String[][] act = { { "Type", action.getType() } };
			return act;
		} else if (action.getType().equals("enqueue")) {
			StringBuilder param = new StringBuilder(action.getParam());
			String[][] act = { { param.substring(0, param.indexOf(":")), "" },
					{ param.substring(param.indexOf(":") + 1, param.length()), "" }, { "Type", action.getType() } };
			return act;
		} else {
			String[][] act = { { action.getParam(), action.getValue() }, { "Type", action.getType() } };
			return act;
		}
	}

	// Checks the values for valid entries, also checks if port entries are
	// valid
	public static boolean errorChecksPassed(Switch sw, Action action, TableItem[] items) {

		boolean checkPorts = false;

		if (action.getType().equals("output") || action.getType().equals("set-vlan-id")
				|| action.getType().equals("set-vlan-priority") || action.getType().equals("set-tos-bits")
				|| action.getType().equals("set-src-port") || action.getType().equals("set-dst-port")) {
			checkPorts = true;
			if (!ErrorCheck.isNumeric(items[0].getText(1))) {
				DisplayMessage.displayError(MainFrame.getShell(),
						"The value number must be an integer. Please check your entry.");
				return false;
			}
		}

		if (action.getType().equals("enqueue")) {
			if (!ErrorCheck.isNumeric(items[0].getText(1)) || !ErrorCheck.isNumeric(items[1].getText(1))) {
				DisplayMessage.displayError(MainFrame.getShell(),
						"The value number must be an integer. Please check your entry.");
				return false;
			}
		}

		if (action.getType().equals("set-src-mac") || action.getType().equals("set-dst-mac")) {
			if (!ErrorCheck.isMac(items[0].getText(1))) {
				DisplayMessage.displayError(MainFrame.getShell(),
						"The value number must be a proper MAC address. Please check your entry.");
				return false;
			}
		}

		if (action.getType().equals("set-src-ip") || action.getType().equals("set-dst-ip")) {
			if (!ErrorCheck.isIP(items[0].getText(1))) {
				DisplayMessage.displayError(MainFrame.getShell(),
						"The value number must be a proper IP address. Please check your entry.");
				return false;
			}
		}

		if (checkPorts) {
			Map<String, Port> ports = sw.getPorts();
			if (ports.containsKey(items[0].getText(1))) {
				return true;
			}
			DisplayMessage.displayError(MainFrame.getShell(), "That port does not exist on the switch!");
			return false;
		}

		return true;
	}
}