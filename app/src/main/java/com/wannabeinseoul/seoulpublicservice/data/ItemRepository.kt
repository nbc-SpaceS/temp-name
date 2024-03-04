package com.wannabeinseoul.seoulpublicservice.data

object ItemRepository {
    private val items = mutableMapOf<String, List<Item>>()

    fun setItems(category: String, items: List<Item>) {
        this.items[category] = items
    }

    fun getItems(category: String): List<Item> {
        return items[category] ?: emptyList()
    }
}