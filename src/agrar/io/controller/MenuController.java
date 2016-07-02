package agrar.io.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import agrar.io.model.Score;
import agrar.io.model.menu.ImageItem;
import agrar.io.model.menu.LabelItem;
import agrar.io.model.menu.Menu;
import agrar.io.model.menu.PasswordFieldItem;
import agrar.io.model.menu.TextFieldItem;

public class MenuController {

	private Controller parent;

	public MenuController(Controller parent) {
		this.parent = parent;
	}

	/**
	 * 
	 * @return The main menu of the game
	 */
	public Menu getMainMenu() {
		Menu mainMenu = new Menu(this.parent);

		ImageItem banner = new ImageItem("/banner.png");
		mainMenu.add(banner);

		ButtonItem startButton = new ButtonItem("Start");
		startButton.setAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				parent.StartGame();
			}
		});

		ButtonItem quitButton = new ButtonItem("Beenden");
		quitButton.setAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.quit();
			}
		});

		return mainMenu;
	}

	/**
	 * 
	 * @return A menu to display when the game is paused
	 */
	public Menu getPauseMenu() {
		Menu pauseMenu = new Menu(this.parent);

		LabelItem pausedLabel = new LabelItem("Pausiert");
		pauseMenu.add(pausedLabel);

		ButtonItem continueButton = new ButtonItem("Weiter");
		continueButton.setAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				parent.resumeGame();
			}
		});
		pauseMenu.add(continueButton);

		ButtonItem mainMenuButton = new ButtonItem("Zum Hauptmenu");
		mainMenuButton.setAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.stopGame();
			}
		});

		ButtonItem quitButton = new ButtonItem("Beenden");
		quitButton.setAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int sure = JOptionPane.showConfirmDialog(null,
						"Wenn du Agrar.io beendest, wir das Spiel abgebrochen und dein Punktestand nicht gespeicher. Willst du wirklich beenden?",
						"Beenden?", JOptionPane.OK_CANCEL_OPTION);
				if (sure == 0) {
					parent.quit();
				}
			}
		});

		return pauseMenu;
	}

	public Menu getNameMenu() {
		Menu nameMenu = new Menu(parent);

		LabelItem title = new LabelItem("Namen aussuchen");
		nameMenu.add(title);

		TextFieldItem nameField = new TextFieldItem("Name");
		nameMenu.add(nameField);

		PasswordFieldItem passwordField = new PasswordFieldItem("Passwort");
		nameMenu.add(passwordField);

		ButtonItem confirmButton = new ButtonItem("OK");
		confirmButton.setAction(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = nameField.getText();
				String passwd = passwordField.getText();
				Score s = new Score(0, name, passwd);

				DatabaseAdapter dbAdapter = parent.getDatabaseAdapter();

				try {
					if (dbAdapter.existsInDatabase(s)) {
						if (dbAdapter.checkPassword(s)) {
							startGame(s);
						} else {
							JOptionPane.showMessageDialog(null,
									"Das Passwort stimmt nicht mit dem gespeicherten überein.", "Falsches Passwort",
									JOptionPane.ERROR_MESSAGE);
						}
					} else {
						startGame(s);
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null,
							"Ein Fehler ist aufgetreten bei der Verbindung mit der Datenbank. Versuche es später nochmal.",
							"Verbindungsfehler", JOptionPane.ERROR_MESSAGE);
				}
			}

		});
		nameMenu.add(confirmButton);

		return nameMenu;
	}

	private void startGame(Score s) {
		parent.StartGame(s);
	}
}
