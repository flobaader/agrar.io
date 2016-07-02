package agrar.io.model.menu;

import agrar.io.model.menu.MenuItem;

public class LabelItem extends MenuItem {

	private String text;
	
	public LabelItem(String text){
		this.text = text;
	}

	public String getText() {
		return text;
	}
	
}
