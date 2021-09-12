package rs.ac.uns.ftn.frontend.views.tasks;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.ServiceMetadataWithAlgorithms;
import rs.ac.uns.ftn.frontend.views.common.AllItemsGrid;

public class ServiceComponent extends VerticalLayout {

  private final AllItemsGrid<AlgorithmViewModel> algorithms;
  private final ServiceMetadataWithAlgorithms service;

  public ServiceComponent(ServiceMetadataWithAlgorithms service) {
    this.service = service;

    addClassName("box-shadow-type-1");
    setWidth(450f, Unit.PIXELS);

    algorithms = new AllItemsGrid<>(AlgorithmViewModel.class);
    algorithms.setColumns("name", "description", "supportedMimeTypes");
    algorithms.setViewModelItems(service.getAlgorithms(), AlgorithmViewModel::fromDto);

    add(new Label(service.getDescription()));
    add(algorithms);
  }

  public String getSelectedAlgorithmId() {
    AlgorithmViewModel algorithmViewModel = algorithms.asSingleSelect().getValue();
    if (algorithmViewModel == null) {
      return null;
    }
    return algorithmViewModel.getId();
  }

  public ServiceMetadataWithAlgorithms getService() {
    return service;
  }
}
