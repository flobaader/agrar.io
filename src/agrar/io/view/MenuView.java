package agrar.io.view;

import javax.swing.JPanel;

import agrar.io.model.menu.*;

public class MenuView extends JPanel{

	private static final long serialVersionUID = 7153771561685855339L;

	public MenuView(){
	}

	public void loadMenu(Menu m){
		removeAll();
		for(MenuItem i : m.getItems()){
			addItem(i);
		}
	}

	private void addItem(MenuItem i) {
		this.add(i.getComponent());
		
	}
}
