package com.main;

import com.main.provider.DataProvider;
import com.main.view.MainFrame;
import com.main.view.StartUp;

public class NetworkController {

	public static void main(String[] args) {
		// new StartUp();
		DataProvider.setIP("localhost");
		DataProvider.setPORT("8080");
		new MainFrame();
	}
}