package agrar.io.model.menu;

public class PasswordFieldItem extends MenuItem {

	private TextListener textListener;
	private String placeholder;


	public PasswordFieldItem(String string) {
		this.placeholder = string;
	}

	public String getText() {
		if(textListener != null){
			return textListener.getText();
		}
		return "";
	}
	
	public void setTextListener(TextListener l){
		this.textListener = l;
	}

	
	public interface TextListener{
		public String getText();
	}
	
	public String getPlaceholder(){
		return placeholder;
	}
}
