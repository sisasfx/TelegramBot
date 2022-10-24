package data

import EnergyFields
import com.google.gson.Gson
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path
import kotlin.io.path.createFile
import kotlin.io.path.writeText

class LocalPricesStorage(private val petition: EnergyPricesApi) {

    private suspend fun energyPriceList(): List<EnergyFields> {
        return petition.listEnergyPrices()
    }

    suspend fun saveInfoTxt(){
        val energyList = energyPriceList()
        val path = Path("src/main/files")
        val filePath = path.resolve("EnergyFile.txt")
        filePath.createFile()

        for (e in energyList){
            filePath.writeText(e.toString()+"\n", options = arrayOf(StandardOpenOption.APPEND))
        }
    }

    suspend fun saveInfoJson(){
        val energyList = energyPriceList()
        val path = Path("src/main/files")
        val filePath = path.resolve("EnergyFile.json")

        val gson = Gson()
        val jsonString = gson.toJson(energyList)

        for (e in energyList){
            filePath.writeText(jsonString)
        }
    }

    suspend fun saveMinPriceJson(){
        val energyList = energyPriceList()
        val path = Path("src/main/files")
        val filePath = path.resolve("minPrice.json")

        val gson = Gson()
        val jsonString = gson.toJson(energyList.minOf { it.precio })
        filePath.writeText(jsonString, options = arrayOf(StandardOpenOption.APPEND))
    }

    suspend fun saveMaxPriceJson(){
        val energyList = energyPriceList()
        val path = Path("src/main/files")
        val filePath = path.resolve("maxPrice.json")

        val gson = Gson()
        val jsonString = gson.toJson(energyList.maxOf { it.precio })
        filePath.writeText(jsonString, options = arrayOf(StandardOpenOption.APPEND))
    }
}