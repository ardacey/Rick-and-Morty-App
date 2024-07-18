package com.example.rickandmorty.paging

import retrofit2.Response

class PaginationFactory<Key, Item>(
    private val initialPage: Key,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest:suspend (nextPage: Key) -> Response<Item>,
    private inline val getNextPage:suspend (Item) -> Key,
    private inline val onError: (Throwable?) -> Unit,
    private inline val onSuccess: (items : Item, newPage : Key) -> Unit
):Pagination<Key, Item> {
    private var currentPage = initialPage
    private var isMakingRequest = false

    override suspend fun loadNextItems() {
        if (isMakingRequest) {
            return
        }
        isMakingRequest = true
        onLoadUpdated(true)
        try {
            val response = onRequest(currentPage)
            if (response.isSuccessful) {
                isMakingRequest = false
                val items = response.body()!!
                currentPage = getNextPage(items)!!
                onSuccess(items, currentPage)
                onLoadUpdated(false)
            }
        } catch (e:Exception) {
            onError(e)
            onLoadUpdated(false)
        }
    }

    override fun reset() {
        currentPage = initialPage
    }
}