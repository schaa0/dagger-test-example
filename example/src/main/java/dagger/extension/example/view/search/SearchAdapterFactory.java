package dagger.extension.example.view.search;

import java.util.List;

public interface SearchAdapterFactory {
    SearchAdapter create(List<SearchItemViewModel> viewModels);
}
