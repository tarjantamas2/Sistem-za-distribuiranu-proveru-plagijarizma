package rs.ac.uns.ftn.frontend.views.tasks;

import java.util.List;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import rs.ac.uns.ftn.distributed.plagiarismchecker.common.TaskResultPart;

public class TaskResultPartsDetailView extends VerticalLayout {

  public TaskResultPartsDetailView(List<TaskResultPart> parts) {
    setSpacing(false);
    setMargin(false);
    if (parts == null) {
      return;
    }
    HidableVerticalLayout layout = new HidableVerticalLayout("Show match details", "Hide match details");
    parts.stream().map(TaskResultPartDetailView::new).forEach(layout::add);
    layout.add(layout);
  }
}
