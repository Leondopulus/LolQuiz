package ru.hamlet.lolquiz

data class LolItem(
    val id: Int,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val lvl1Components: List<Int>,
) {
}