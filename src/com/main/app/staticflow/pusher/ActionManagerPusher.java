package com.main.app.staticflow.pusher;

import java.util.List;

import org.eclipse.swt.widgets.TableItem;

import com.basic.elements.Action;
import com.basic.elements.Flow;

public class ActionManagerPusher {

	public static void addAction(TableItem[] items, Action action, Flow flow) {

		List<Action> actions = flow.getActions();

		if (actions.contains(action)) {
			modifyAction(items, action);
		} else {
			// Construct the action
			if (action.getType().equals("enqueue")) {
				action = new Action(items[2].getText(1), items[0].getText(1)
						+ ":" + items[1].getText(1));
				actions.add(action);
			} else if (action.getType().equals("strip-vlan")) {
				action = new Action(items[0].getText(1));
				actions.add(action);
			} else {
				action = new Action(items[1].getText(1), items[0].getText(1));
				actions.add(action);
			}
		}
	}

	public static void modifyAction(TableItem[] items, Action action) {
		String actionType = action.getType();

		// Construct the action
		if (actionType.equals("enqueue")) {
			action.setValue(items[0].getText(1) + ":" + items[1].getText(1));
		} else {
			action.setValue(items[0].getText(1));
		}
	}

	public static void removeAction(Flow flow, Action action) {
		List<Action> actions = flow.getActions();
		if (actions.contains(action)) {
			actions.remove(action);
		}
	}

	// Removes all the actions
	public static void removeAllActions(Flow flow) {
		List<Action> actions = flow.getActions();
		actions.clear();
	}
}