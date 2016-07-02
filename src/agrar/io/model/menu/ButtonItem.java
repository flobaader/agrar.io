package agrar.io.model.menu;

import java.awt.Component;
import java.awt.event.ActionListener;

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

	@Override
	public Component getComponent() {
		JButton button = new JButton(getText());
		return button;
	}
}
