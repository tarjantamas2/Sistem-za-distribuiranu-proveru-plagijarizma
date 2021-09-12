package rs.ac.uns.ftn.frontend.views.tasks;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class HidableVerticalLayout extends VerticalLayout {

  private final VerticalLayout layout;
  private final Button showHideButton;

  public HidableVerticalLayout(String showCaption, String hideCaption) {
    addClassName("box-shadow-type-1");

    setSpacing(false);
    setMargin(false);

    showHideButton = new Button(showCaption);
    layout = new VerticalLayout();
    layout.setVisible(false);
    layout.setSpacing(false);
    layout.setMargin(false);

    showHideButton.addClickListener(event -> {
      if (layout.isVisible()) {
        showHideButton.setText(showCaption);
        layout.setVisible(false);
      } else {
        showHideButton.setText(hideCaption);
        layout.setVisible(true);
      }
    });

    super.add(showHideButton);
    super.add(layout);
  }

  @Override
  public void add(Component... components) {
    layout.add(components);
  }

  public void setShowHideButtonVisible(boolean v) {
    showHideButton.setVisible(v);
  }
}
