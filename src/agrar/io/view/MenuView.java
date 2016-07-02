package agrar.io.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class MenuView extends JPanel {

	private static final long serialVersionUID = 7153771561685855339L;
	GridBagConstraints c;

	public MenuView() {
		this.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		c.gridy = 0;
	}

	public void addButton(String text, ActionListener l) {
		JButton button = new JButton(text);
		button.addActionListener(l);
		c.gridx = 0;
		c.gridwidth = 2;
		this.add(button, c);
		c.gridy += 1;
	}

	public void addLabel(String text) {
		JLabel label = new JLabel(text);
		c.gridx = 0;
		c.gridwidth = 2;
		this.add(label, c);
		c.gridy += 1;
	}

	public JPasswordField addPasswordField(String text) {
		JLabel label = new JLabel(text);
		c.gridwidth = 1;
		c.gridx = 0;
		add(label, c);
		
		JPasswordField field = new JPasswordField(20);
		c.gridx = 1; 
		this.add(field, c);
		
		c.gridy += 1;
		return field;
	}

	public JTextField addTextField(String text) {
		JLabel label = new JLabel(text);
		c.gridwidth = 1;
		c.gridx = 0;
		add(label, c);
		
		JTextField field = new JTextField(20);
		c.gridx = 1;
		this.add(field, c);
		
		c.gridy += 1;
		return field;
	}

	public void clear() {
		removeAll();
		invalidate();
		c.gridy = 0;
	}

	public void addImage(String string) {
		ImageIcon image = new ImageIcon("resources/banner.png");
		
		JLabel imageLabel = new JLabel("", image, JLabel.CENTER);
		imageLabel.setSize(new Dimension(image.getIconWidth(), image.getIconHeight()));
		c.gridwidth = 2;
		c.gridx = 0;
		this.add(imageLabel, c);
		
		c.gridy += 1;
	}
}
