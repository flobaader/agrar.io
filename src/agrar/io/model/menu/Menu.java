package agrar.io.model.menu;

import java.util.ArrayList;

import agrar.io.controller.Controller;

public class Menu {

	protected Controller controller;
	protected ArrayList<MenuItem> menuItems;

	public Menu(Controller c) {
		this.controller = c;
	}

	public void add(MenuItem i) {
		menuItems.add(i);
	}
	
	public ArrayList<MenuItem> getItems(){
		return menuItems;
	}

}
