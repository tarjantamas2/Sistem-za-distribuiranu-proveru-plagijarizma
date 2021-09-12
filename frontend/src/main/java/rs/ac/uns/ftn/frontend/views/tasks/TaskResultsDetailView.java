package rs.ac.uns.ftn.frontend.views.tasks;

import java.util.List;

import rs.ac.uns.ftn.distributed.plagiarismchecker.common.TaskResult;

public class TaskResultsDetailView extends HidableVerticalLayout {

  public TaskResultsDetailView(List<TaskResult> results) {
    super("Show result details", "Hide result details");
    if (results == null || results.isEmpty()) {
      setShowHideButtonVisible(false);
    } else {
      setShowHideButtonVisible(true);
      results.stream().map(TaskResultDetailView::new).forEach(this::add);
    }
  }
}
