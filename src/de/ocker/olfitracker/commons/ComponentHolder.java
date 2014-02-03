package de.ocker.olfitracker.commons;

import javax.swing.*;
import java.awt.*;

/**
 * fast possibility to add some JPanels to another
 * 
 * @author Florian J. Ocker
 */
public class ComponentHolder extends JPanel {
  private static final long serialVersionUID = 1L;

  /**
   * surrounds all components in a panel to adjust the appearance
   * 
   * @param components
   *          any count of components that should be surrounded by a panel
   */
  public ComponentHolder(Component... components) {
    for (Component component : components) {
      add(component);
    }
  }

  /**
   * surrounds all components in a panel to adjust the appearance
   * 
   * @param layout
   *          any layoutManager that will be assigned to the jPanel
   * @param components
   *          any count of components that should be surrounded by a panel
   */
  public ComponentHolder(LayoutManager layout, Component... components) {
    this(components);
    setLayout(layout);
  }

}
