package agrar.io.controller;

import java.awt.event.ActionListener;

import agrar.io.model.menu.MenuItem;

public class ButtonItem extends MenuItem {

	private String text;
	private ActionListener action;

	public ButtonItem(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setAction(ActionListener action){
		this.action = action;
	}
	
	public ActionListener getAction(){
		return action;
	}
}
