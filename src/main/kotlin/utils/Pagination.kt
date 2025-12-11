package utils

/**
 * Pagination helper class to manage paged data and metadata.
 * 
 * @param T Type of items in the page (e.g., Task)
 * @property items List of items for the current page
 * @property currentPage Current page number (1-based)
 * @property pageSize Number of items per page
 * @property totalItems Total number of items across all pages
 */
data class Page<T>(
    val items: List<T>,
    val currentPage: Int,
    val pageSize: Int,
    val totalItems: Int
) {
    /** Total number of pages (calculated from totalItems and pageSize) */
    val totalPages: Int = (totalItems + pageSize - 1) / pageSize

    /** Whether there is a previous page */
    val hasPrevious: Boolean = currentPage > 1

    /** Whether there is a next page */
    val hasNext: Boolean = currentPage < totalPages

    /** Previous page number (or current page if no previous) */
    val previousPage: Int = if (hasPrevious) currentPage - 1 else currentPage

    /** Next page number (or current page if no next) */
    val nextPage: Int = if (hasNext) currentPage + 1 else currentPage

    /**
     * Converts Page data to a Pebble template-friendly context map.
     * Flattens nested properties for cleaner template syntax.
     * 
     * @param itemsKey Key used to store the list of items in the context (default: "items")
     * @return Map of pagination data for template rendering
     */
    fun toPebbleContext(itemsKey: String = "items"): Map<String, Any> = mapOf(
        itemsKey to items,
        "currentPage" to currentPage,
        "pageSize" to pageSize,
        "totalItems" to totalItems,
        "totalPages" to totalPages,
        "hasPrevious" to hasPrevious,
        "hasNext" to hasNext,
        "previousPage" to previousPage,
        "nextPage" to nextPage
    )

    companion object {
        /**
         * Creates a Page instance with validation for current page bounds.
         * Ensures currentPage is within 1..totalPages.
         * 
         * @param items List of items for the current page
         * @param currentPage Requested page number (1-based)
         * @param pageSize Number of items per page
         * @param totalItems Total number of items across all pages
         * @return Validated Page instance
         */
        fun <T> create(
            items: List<T>,
            currentPage: Int,
            pageSize: Int,
            totalItems: Int
        ): Page<T> {
            val totalPages = (totalItems + pageSize - 1) / pageSize
            val clampedPage = currentPage.coerceIn(1, maxOf(1, totalPages)) // Ensure at least page 1
            return Page(
                items = items,
                currentPage = clampedPage,
                pageSize = pageSize,
                totalItems = totalItems
            )
        }
    }
}

/**
 * Helper function to paginate a list of items.
 * 
 * @param T Type of items in the list
 * @param page Requested page number (1-based)
 * @param pageSize Number of items per page
 * @return Page<T> containing the subset of items and pagination metadata
 */
fun <T> List<T>.paginate(page: Int, pageSize: Int): Page<T> {
    val totalItems = this.size
    val startIndex = (page - 1) * pageSize
        .coerceAtLeast(0) // Prevent negative start index
    val endIndex = (startIndex + pageSize).coerceAtMost(totalItems)
    val pageItems = if (startIndex >= totalItems) emptyList() else this.subList(startIndex, endIndex)
    
    return Page.create(
        items = pageItems,
        currentPage = page,
        pageSize = pageSize,
        totalItems = totalItems
    )
}