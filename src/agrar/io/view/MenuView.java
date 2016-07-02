package agrar.io.view;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class MenuView extends JPanel {

	private static final long serialVersionUID = 7153771561685855339L;

	public MenuView() {
	}

	public void addButton(String text, ActionListener l) {
		JButton button = new JButton(text);
		button.addActionListener(l);
		this.add(button);
	}

	public void addLabel(String text) {
		JLabel label = new JLabel(text);
		this.add(label);
	}

	public JPasswordField addPasswordField(String text) {
		JPasswordField field = new JPasswordField(20);
		this.add(field);
		return field;
	}

	public JTextField addTextField(String text) {
		JTextField field = new JTextField(20);
		this.add(field);
		return field;
	}

	public void clear() {
		removeAll();
		invalidate();
	}

	public void addImage(String string) {
		ImageIcon image = new ImageIcon("resources/banner.png");
		JLabel imageLabel = new JLabel("", image, JLabel.CENTER);

		this.add(imageLabel);
	}
}
