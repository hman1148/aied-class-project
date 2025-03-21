package Models.Response;

import java.util.List;

public class ItemsResponse<T> extends ResponseAbstract {

    private List<T> items;

    public ItemsResponse(List<T> items, String message, boolean success) {
        super(message, success);
        this.items = items;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
