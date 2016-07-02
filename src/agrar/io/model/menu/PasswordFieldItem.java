package agrar.io.model.menu;

import java.awt.Component;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

import javax.swing.JPasswordField;

public class PasswordFieldItem extends MenuItem {

	private TextListener textListener;
	private String placeholder;

	public PasswordFieldItem(String string) {
		this.placeholder = string;
	}

	public String getText() {
		if (textListener != null) {
			return textListener.getText();
		}
		return "";
	}

	public interface TextListener {
		public String getText();
	}

	public String getPlaceholder() {
		return placeholder;
	}

	@Override
	public Component getComponent() {
		JPasswordField field = new JPasswordField(20);
		textListener = new TextListener() {

			@Override
			public String getText() {
				return String.valueOf(field.getPassword());
			}
		};
		return field;
	}
}
