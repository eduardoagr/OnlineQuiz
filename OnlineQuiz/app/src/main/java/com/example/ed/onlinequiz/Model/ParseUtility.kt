package com.example.ed.onlinequiz.Model

import org.json.JSONObject

class ParseUtility {

    fun parseJsonObj(): ArrayList<Plant>? {

        val objList = ArrayList<Plant>()
        val downloadingData = DownloadingData()
        val topLevelObj = downloadingData.downloadJSON("http://www.plantplaces.com/perl/mobile/flashcard.pl")
        val topJsonObj = JSONObject(topLevelObj)
        val JsonValues = topJsonObj.getJSONArray("values")

        var index = 0

        while (index < JsonValues.length()){

            val plant = Plant()
            val obj = JsonValues.getJSONObject(index)

            with(obj) {
                plant.id = getInt("id")
                plant.plant = getString("plant")
                plant.genus = getString("genus")
                plant.species = getString("species")
                plant.cultivar = getString("cultivar")
                plant.common = getString("common")
                plant.picture_name = getString("picture_name")
                plant.description = getString("description")
                plant.difficulty = getInt("difficulty")
                plant.photo_credit = getString("photo_credit")
            }

            objList.add(plant)

            index++
        }

        return objList
    }

}