package com.example.ed.onlinequiz.Model

class Plant(var id: Int, var plant: String, var genus: String, var species: String,
            var cultivar: String, var common: String, var picture_name: String,
            var description: String, var difficulty: Int, var photo_credit: String) {


    constructor(): this(0,"","","","","",
        "", "", 0, "")

    override fun toString(): String {
        return common
    }
}