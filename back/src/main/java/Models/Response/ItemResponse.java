package Models.Response;

public class ItemResponse<T> extends ResponseAbstract {

    private T item;

    public ItemResponse(T item, String message, boolean success) {
        super(message, success);
        this.item = item;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

}
