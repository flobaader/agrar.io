package agrar.io.model.menu;

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
}
