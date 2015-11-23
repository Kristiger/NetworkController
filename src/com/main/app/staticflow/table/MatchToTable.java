package com.main.app.staticflow.table;

import java.util.List;

import org.eclipse.swt.widgets.TableItem;

import com.basic.elements.Match;
import com.basic.elements.Port;
import com.basic.elements.Switch;
import com.main.view.MainFrame;
import com.main.view.util.DisplayMessage;
import com.tools.util.ErrorCheck;

public class MatchToTable {

	// Gets a match and formats it appropriately for the table
	public static String[][] getMatchTableFormat(Match m){

		String[][] match = {
				{ "Data Layer Destination", m.getDataLayerDestination() },
				{ "Data Layer Source", m.getDataLayerSource() },
				{ "Data Layer Type", m.getDataLayerType() },
				{ "Data Layer VLAN", m.getDataLayerVLAN() },
				{ "Data Layer PCP", m.getDataLayerPCP() },
				{ "Input Port", m.getInputPort() },
				{ "Network Destination", m.getNetworkDestination() },
				{ "Network Protocol", m.getNetworkProtocol() },
				{ "Network Source", m.getNetworkSource() },
				{ "Network Type Of Service", m.getNetworkTypeOfService() },
				{ "Transport Destination", m.getTransportDestination() },
				{ "Transport Source", m.getTransportSource() },
				{ "Wildcards", m.getWildcards() } };
		return match;
	}

	// Returns empty values
	public static String[][] getNewMatchTableFormat() {
		String[][] match = { { "Data Layer Destination", "" },
				{ "Data Layer Source", "" }, { "Data Layer Type", "" },
				{ "Data Layer VLAN", "" }, { "Data Layer PCP", "" },
				{ "Input Port", "" }, { "Network Destination", "" },
				{ "Network Protocol", "" }, { "Network Source", "" },
				{ "Network Type Of Service", "" },
				{ "Transport Destination", "" }, { "Transport Source", "" },
				{ "Wildcards", "" } };
		return match;
	}

	// Checks the entries for valid values, also checks if port entries are
	// valid
	public static boolean errorChecksPassed(Switch sw, TableItem[] items) {
		boolean checkPorts = false;

		if (!items[0].getText(1).isEmpty()
				&& !ErrorCheck.isMac(items[0].getText(1))) {
		    DisplayMessage.displayError(MainFrame.getShell(),"Data Layer Destination must be a valid MAC address.");
			return false;
		}
		if (!items[1].getText(1).isEmpty()
				&& !ErrorCheck.isMac(items[1].getText(1))) {
		    DisplayMessage.displayError(MainFrame.getShell(),"Data Layer Source must be a valid MAC address.");
			return false;
		}
		//how to check 0xabcd 
		if (!items[3].getText(1).isEmpty()
				&& !ErrorCheck.isNumeric(items[3].getText(1))) {
		    DisplayMessage.displayError(MainFrame.getShell(),"Data Layer VLAN must be a valid number.");
			return false;
		}
		if (!items[4].getText(1).isEmpty()
				&& !ErrorCheck.isNumeric(items[4].getText(1))) {
		    DisplayMessage.displayError(MainFrame.getShell(),"Data Layer PCP must be a valid number.");
			return false;
		}
		if (!items[5].getText(1).isEmpty()
				&& !ErrorCheck.isNumeric(items[5].getText(1))) {
		    DisplayMessage.displayError(MainFrame.getShell(),"Input Port must be a valid number.");
			return false;
		} else if (!items[5].getText(1).isEmpty()
				&& ErrorCheck.isNumeric(items[5].getText(1))) {
			checkPorts = true;
		}

		if (!items[6].getText(1).isEmpty()
				&& !ErrorCheck.isIP(items[6].getText(1))) {
		    DisplayMessage.displayError(MainFrame.getShell(),"Network Destination must be a valid IP address.");
			return false;
		}
		/*if (!items[7].getText(1).isEmpty()
				&& !ErrorCheck.isNumeric(items[7].getText(1))) {
		    DisplayMessage.displayError(StaticFlowManager.getShell(),"Network Protocol must be a valid number.");
			return false;
		}*/
		if (!items[8].getText(1).isEmpty()
				&& !ErrorCheck.isIP(items[8].getText(1))) {
		    DisplayMessage.displayError(MainFrame.getShell(),"Network Source must be a valid IP address.");
			return false;
		}
		if (!items[9].getText(1).isEmpty()
				&& !ErrorCheck.isNumeric(items[9].getText(1))) {
		    DisplayMessage.displayError(MainFrame.getShell(),"Network Type of Service must be a valid number.");
			return false;
		}
		if (!items[10].getText(1).isEmpty()
				&& !ErrorCheck.isNumeric(items[10].getText(1))) {
		    DisplayMessage.displayError(MainFrame.getShell(),"Transport Destination must be a valid number.");
			return false;
		}
		if (!items[11].getText(1).isEmpty()
				&& !ErrorCheck.isNumeric(items[11].getText(1))) {
		    DisplayMessage.displayError(MainFrame.getShell(),"Transport Source must be a valid number.");
			return false;
		}
		if (!items[12].getText(1).isEmpty()
				&& !ErrorCheck.isNumeric(items[12].getText(1))) {
		    DisplayMessage.displayError(MainFrame.getShell(),"Wildcards must be a valid number.");
			return false;
		}

		if (checkPorts) {
		    List<Port> ports = sw.getPorts();
		    for(Port p : ports){
                if(p.getPortNumber().equals(items[5].getText(1))){
                    return true;
                }
            }
		    DisplayMessage.displayError(MainFrame.getShell(),"That port does not exist on the switch!");
			return false;
		}
		return true;
	}
}
