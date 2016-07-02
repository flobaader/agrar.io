package agrar.io.model.menu;

import java.awt.Component;
import java.awt.event.TextEvent;

import javax.swing.JTextField;

import agrar.io.model.menu.PasswordFieldItem.TextListener;

public class TextFieldItem extends MenuItem {

	public TextFieldItem(String string) {

		this.placeholder = string;
	}

	private TextListener textListener;
	private String placeholder;

	public String getText() {
		if (textListener != null) {
			return textListener.getText();
		}
		return "";
	}

	public void setTextListener(TextListener l) {
		this.textListener = l;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	@Override
	public Component getComponent() {

		JTextField field = new JTextField();
		textListener = new TextListener() {

			@Override
			public String getText() {
				return field.getText();
			}

		};

		return field;
	}
}
