package agrar.io.model.menu;

import java.awt.Component;

import javax.swing.JLabel;

import agrar.io.model.menu.MenuItem;

public class LabelItem extends MenuItem {

	private String text;
	
	public LabelItem(String text){
		this.text = text;
	}

	public String getText() {
		return text;
	}

	@Override
	public Component getComponent() {
		return new JLabel(getText());
	}
	
}
