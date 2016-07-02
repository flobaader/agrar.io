package agrar.io.model.menu;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ImageItem extends MenuItem {

	private String path;

	public ImageItem(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	@Override
	public Component getComponent() {
		
		ImageIcon image = new ImageIcon(getPath());
		JLabel label = new JLabel("", image, JLabel.CENTER);

		return label;
	}

}
