package agrar.io.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * JPanel that displays menus in the game
 *
 * @author Matthias
 */
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

    /**
     * Adds a button to the next row of the menu
     *
     * @param text The text on the button
     * @param l    An action listener for the button
     */
    public void addButton(String text, int size, ActionListener l) {
        JButton button = new JButton(text);
        Font f = button.getFont();
        button.setFont(new Font(f.getName(), f.getStyle(), f.getSize() * size));
        button.addActionListener(l);
        c.gridx = 0;
        c.gridwidth = 2;
        c.gridy += 1;

        this.add(button, c);

        invalidate();
        repaint();
    }

    /**
     * Adds a checkbox to the next row of the menu
     *
     * @param text The text on the button
     * @param l    the action listener
     */
    public void addCheckBox(String text, ActionListener l) {
        JCheckBox checkbox = new JCheckBox(text);
        checkbox.addActionListener(l);
        c.gridx = 0;
        c.gridwidth = 2;
        this.add(checkbox, c);
        c.gridy += 1;

        invalidate();
        repaint();
    }

    /**
     * Adds a label to the next row of the menu
     *
     * @param text The text on the label
     */
    public void addLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.white);
        c.gridx = 0;
        c.gridwidth = 2;
        this.add(label, c);
        c.gridy += 1;

        invalidate();
        repaint();
    }

    /**
     * Adds a password field to the next row of the menu
     *
     * @param text The text to place left of the field
     * @return The password field, used to get the content
     */
    public JPasswordField addPasswordField(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.white);
        c.gridwidth = 1;
        c.gridx = 0;
        add(label, c);

        JPasswordField field = new JPasswordField(20);
        c.gridx = 1;
        this.add(field, c);

        c.gridy += 1;

        invalidate();
        repaint();

        return field;
    }

    /**
     * Adds a JTextField to the next row of the menu, including a label left of
     * it to describe the purpose
     *
     * @param text The name of the TextField, used for the label to the left
     * @return The Field, used for getting the content of the field
     */
    public JTextField addTextField(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.white);
        c.gridwidth = 1;
        c.gridx = 0;
        add(label, c);

        JTextField field = new JTextField(20);
        c.gridx = 1;
        this.add(field, c);

        c.gridy += 1;

        invalidate();
        repaint();

        return field;
    }

    /**
     * Removes all contents from the menu and resets it
     */
    public void clear() {

        removeAll();
        invalidate();
        repaint();

        c.gridy = 0;
    }

    /**
     * Adds an image from the resources to the next row of the menu
     *
     * @param string The path of the image, relative to the resource folder
     */
    public void addImage(String string) {

        float scaleFactor = 1F;
        BufferedImage origImage = null;
        try {
            origImage = ImageIO.read(getClass().getResource(string));
        } catch (IOException e) {
            // Should only happen when image is missing, so ignore it
            e.printStackTrace();
            return;
        }

        JLabel imageLabel = new JLabel("", JLabel.CENTER);

        // Max size of the image is ~85% of the frame width and 50% of the
        // height
        // If it is larger, resize it
        if (origImage.getWidth() > 550 || origImage.getHeight() > 230) {

            float aspectRatio = origImage.getWidth() / origImage.getHeight();

            // Aspect of max size (550/230)
            float boxAspect = 2.391304347826087F;

            // scale based on height
            if (aspectRatio < boxAspect) {
                scaleFactor = 230F / (float) origImage.getHeight(); // max.
                // height =
                // 230
            } else {
                // scale based on width
                scaleFactor = 550F / (float) origImage.getWidth(); // max. width
                // = 550
            }
        }

        Dimension size = new Dimension((int) ((float) origImage.getWidth() * scaleFactor),
                (int) ((float) origImage.getHeight() * scaleFactor));

        // scale the image to fit the Label
        ImageIcon image = new ImageIcon(origImage.getScaledInstance((int) size.getWidth(),
                (int) size.getHeight(),
                BufferedImage.SCALE_SMOOTH));
        imageLabel.setIcon(image);

        // resize the label
        imageLabel.setSize(size);
        imageLabel.setPreferredSize(size);

        c.gridwidth = 2;
        c.gridx = 0;
        this.add(imageLabel, c);

        c.gridy += 1;

        invalidate();
        // repaint();
    }


    /**
     * Adds a JLabel with larger text in green color
     *
     * @param text the text of the title
     */
    public void addTitle(String text) {
        JLabel title = new JLabel(text);
        Font f = title.getFont();
        title.setFont(new Font(f.getName(), f.getStyle(), f.getSize() * 4));
        title.setForeground(Color.white);

        c.gridx = 0;
        c.gridwidth = 2;

        this.add(title, c);

        c.gridy++;

        invalidate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        try {
            g.drawImage(ImageIO.read(MenuView.class.getResource("/background.png")), 0, 0, this);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
