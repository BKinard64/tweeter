package edu.byu.cs.tweeter.model.net.response;

import java.util.List;
import java.util.Objects;

public class PagedResponse<T> extends Response {
    private List<T> items;
    private final boolean hasMorePages;

    public PagedResponse(List<T> items, boolean hasMorePages) {
        super(true);
        this.items = items;
        this.hasMorePages = hasMorePages;
    }

    public PagedResponse(boolean success, String message, List<T> items, boolean hasMorePages) {
        super(success, message);
        this.items = items;
        this.hasMorePages = hasMorePages;
    }

    public List<T> getItems() {
        return items;
    }

    public boolean getHasMorePages() {
        return hasMorePages;
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        PagedResponse<T> that = (PagedResponse<T>) obj;

        return (Objects.equals(items, that.items) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }
}
