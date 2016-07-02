package agrar.io.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import agrar.io.model.Score;
import agrar.io.view.GameWindow;
import agrar.io.view.MenuView;

public class MenuController {

	private Controller parent;
	private MenuView menuView;
	private GameWindow window;

	public MenuController(Controller parent, MenuView menuView, GameWindow window) {
		this.parent = parent;
		this.menuView = menuView;
		this.window = window;
	}

	/**
	 * 
	 * @return The main menu of the game
	 */
	public void showMainMenu() {
		
		menuView.clear();

		menuView.addImage("resources/banner.png");

		menuView.addButton("Start", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				parent.start();
			}
		});

		menuView.addButton("Beenden", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.quit();
			}
		});

		window.showMenu();
	}

	/**
	 * 
	 * @return A menu to display when the game is paused
	 */
	public void showPauseMenu() {

		menuView.clear();
		
		menuView.addLabel("Pausiert");

		menuView.addButton("Weiter", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				parent.resumeGame();
			}
		});

		menuView.addButton("Hauptmenu", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.stopGame();
			}
		});

		menuView.addButton("Beenden", new ActionListener() {

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

		window.showMenu();
	}

	public void showNameMenu() {

		menuView.clear();
		
		JTextField nameField = menuView.addTextField("Name");

		JPasswordField passwordField = menuView.addPasswordField("Passwort");

		menuView.addButton("OK", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = nameField.getText();
				String passwd = String.valueOf(passwordField.getPassword());
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

		window.showMenu();
	}

	private void startGame(Score s) {
		parent.StartGame(s);
	}

}
