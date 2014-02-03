package de.ocker.olfitracker.gui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * the menu bar is displayed at the very top of the gui and provides interaction
 * possibility for users
 * 
 * @author Florian J. Ocker
 * 
 */
public class MenuBar extends JMenuBar {
  private static final long serialVersionUID = 1L;

  private JMenu _fileMenu;
  private JMenu _helpMenu;

  private JMenuItem _exit;


  public MenuBar() {
    init();
    initListener();
  }

  private void init() {
    // menus
    _fileMenu = new JMenu("Datei");
    _fileMenu.setMnemonic('D');

    _helpMenu = new JMenu("?");

    // menu items
    _exit = new JMenuItem("Beenden");

    _fileMenu.add(_exit);
    
    // add menus
    add(_fileMenu);
    add(_helpMenu);
  }

  private void initListener() {
    // TODO Auto-generated method stub

  }
}