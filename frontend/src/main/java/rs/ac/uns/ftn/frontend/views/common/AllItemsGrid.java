package rs.ac.uns.ftn.frontend.views.common;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.data.provider.Query;

public class AllItemsGrid<T> extends Grid<T> {

  public AllItemsGrid(Class<T> aClass) {
    super(aClass, true);
    addThemeVariants(GridVariant.LUMO_NO_BORDER);
  }

  public void setViewModelItems(List<T> items) {
    setItems(page -> getPage(page, items).stream());
  }

  public <V> void setViewModelItems(List<V> items, Function<V, T> mapper) {
    setViewModelItems(items.stream().map(mapper).collect(Collectors.toList()));
  }

  protected List<T> getPage(Query<T, Void> query, List<T> all) {
    int startIndex = query.getPage() * query.getPageSize();
    if (startIndex >= all.size()) {
      return Collections.emptyList();
    }
    if (startIndex + query.getPageSize() >= all.size()) {
      return all.subList(startIndex, all.size());
    }
    return all.subList(startIndex, query.getPageSize());
  }
}
