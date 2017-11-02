package agrar.io.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JCheckBox;
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
     * @return The main menu of the game
     */
    public void showMainMenu() {

        menuView.clear();

        //menuView.addImage("/background.png");

        menuView.addLabel("(c) Florian Baader, Matthias Weirich");

        menuView.addButton("Start", 3, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                showNameMenu();
            }
        });

        menuView.addButton("Beenden", 3, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                parent.quit();
            }
        });


        window.showMenu();
    }

    /**
     * @return A menu to display when the game is paused
     */
    public void showPauseMenu() {

        menuView.clear();

        menuView.addTitle("Pausiert");

        menuView.addLabel("Pausiert");

        menuView.addButton("Weiter", 2, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                parent.resumeGame();
            }
        });

        menuView.addButton("Hauptmenu", 2, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                parent.stopGame();
                int sure = JOptionPane.showConfirmDialog(null,
                        "Wenn du die Runde jetzt verl�sst, wird dein Punktestand nicht gespeichert.",
                        "Wirklich beenden?", JOptionPane.OK_CANCEL_OPTION);
                if (sure == 0) {
                    showMainMenu();
                }
            }
        });

        menuView.addButton("Beenden", 2, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int sure = confirmQuit();
                if (sure == 0) {
                    parent.quit();
                }
            }

        });

        window.showMenu();
    }

    /**
     * Shows a menu to the user to confirm quitting the game
     *
     * @return 0 if the user wants to quit, 1 otherwise
     */
    public int confirmQuit() {
        return JOptionPane.showConfirmDialog(null,
                "Wenn du Agrar.io jetzt beendest, wir das Spiel abgebrochen und dein Punktestand nicht gespeicher. Willst du wirklich beenden?",
                "Beenden?", JOptionPane.OK_CANCEL_OPTION);
    }

    public void showNameMenu() {

        menuView.clear();

        menuView.addTitle("Namen ausw�hlen");

        JTextField nameField = menuView.addTextField("Name");


        menuView.addCheckBox("Debug View", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JCheckBox checkbox = (JCheckBox) arg0.getSource();
                parent.setDebugMode(checkbox.isSelected());
            }

        });


        menuView.addButton("OK", 2, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String name = nameField.getText();


                Score s = new Score(0, name);


                parent.StartGame(s);
            }


        });

        window.showMenu();

    }

    /**
     * Shows the "game over" screen to the user
     *
     * @param s Score containing player name and score
     */
    public void showDeathMenu(Score s, boolean isNewHighscore) {

        menuView.clear();

        menuView.addTitle("Game over");
        if (isNewHighscore) {
            menuView.addTitle("Neuer Highscore!!");
        } else {
            menuView.addLabel("Leider kein neuer Highscore :(");
        }

        menuView.addLabel("<html>Punktestand: <b>" + s.getScore() + "</b></hmtl>");

        menuView.addButton("Neue Runde", 3, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                parent.StartGame(s);
            }
        });

        menuView.addButton("Zum Hauptmen�", 2, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                showMainMenu();
            }
        });

        menuView.addButton("Beenden", 2, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                parent.quit();
            }
        });

        window.showMenu();
    }

    /**
     * Shows a Dialog to notify the user that no connection to the DB could be
     * established
     */
    public void showConnectionError(Exception e) {
        JOptionPane.showMessageDialog(null, "Fehler bei der Verbindung mit der Datenbank: " + e.getMessage(),
                "Falsches Passwort", JOptionPane.ERROR_MESSAGE);
    }

    public void showPasswordError(Exception e) {
        JOptionPane.showMessageDialog(null, "Das Passwort stimmt nicht mit dem gespeicherten �berein." + e.getMessage(),
                "Falsches Passwort", JOptionPane.ERROR_MESSAGE);
    }
}
