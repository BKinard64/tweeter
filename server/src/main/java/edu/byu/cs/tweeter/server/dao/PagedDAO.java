package edu.byu.cs.tweeter.server.dao;

import java.util.List;

public abstract class PagedDAO<T> {

    /**
     * Determines the index for the first item in the specified 'allItems' list that should
     * be returned in the current request. This will be the index of the next item after the
     * specified 'lastItem'.
     *
     * @param lastItem the last item that was returned in the previous
     *                          request or null if there was no previous request.
     * @param allItems the generated list of items from which we are returning paged results.
     * @return the index of the first item to be returned.
     */
    protected int getItemsStartingIndex(T lastItem, List<T> allItems) {

        int itemsIndex = 0;

        if(lastItem != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allItems.size(); i++) {
                if(lastItem.equals(allItems.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    itemsIndex = i + 1;
                    break;
                }
            }
        }

        return itemsIndex;
    }
}
