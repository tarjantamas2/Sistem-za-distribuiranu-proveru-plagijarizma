package rs.ac.uns.ftn.frontend.views.tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.ServiceMetadataWithAlgorithms;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.ServiceTaskDto;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.SharedRestClient;

public class ServiceChooserComponent extends HorizontalLayout {

  private final List<ServiceComponent> serviceComponents = new ArrayList<>();

  public ServiceChooserComponent(SharedRestClient sharedRestClient) {
    List<ServiceMetadataWithAlgorithms> services = sharedRestClient.findAllServices();

    services.stream().map(ServiceComponent::new).forEach(this::add);
  }

  public void add(ServiceComponent... components) {
    super.add(components);
    serviceComponents.addAll(Arrays.asList(components));
  }

  public List<ServiceTaskDto> getSelectedServices() {
    return serviceComponents.stream()
      .filter(c -> c.getSelectedAlgorithmId() != null)
      .map(c -> new ServiceTaskDto(c.getService().getId(), c.getSelectedAlgorithmId()))
      .collect(Collectors.toList());
  }
}
