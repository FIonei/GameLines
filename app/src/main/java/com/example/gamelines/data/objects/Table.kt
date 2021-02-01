package com.example.gamelines.data.objects

class Table(_tableSize: Int) {
    val tableSize = _tableSize
    var cells = Array(tableSize, {Array(tableSize, {0})})

    fun setValue(x: Int, y: Int, value: Int) {cells[x][y] = value}

    fun getValue(x: Int, y: Int): Int {return cells[x][y]}

    fun isEquals(x: Int,y: Int, value: Int): Boolean {return cells[x][y] == value}

    fun getSize():Int{return tableSize}
}