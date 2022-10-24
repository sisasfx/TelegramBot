import com.google.gson.Gson
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.writeText

class UserOptions{

    suspend fun sortedListByHour(petition: ApiPetition): MutableList<EnergyFields>{
        val energy:List<EnergyFields> = petition.requestAPI()
        val energySorted: MutableList<EnergyFields> = mutableListOf()

        for (e in energy.sortedBy { it.hour }){
            energySorted.add(e)
            // println(Json.encodeToString(e))
        }
        return energySorted
    }

    suspend fun sortedListByPrice(petition: ApiPetition): MutableList<EnergyFields>{
        val energy:List<EnergyFields> = petition.requestAPI()
        val energySorted: MutableList<EnergyFields> = mutableListOf()

        for (e in energy.sortedBy { it.precio }){
            energySorted.add(e)
        }
        return energySorted
    }

    fun saveInfoTxt(energyList :MutableList<EnergyFields>){
        val path = Path("src/main/resources")
        val filePath = path.resolve("EnergyFile.txt")
        filePath.createFile()

        for (e in energyList){
            filePath.writeText(e.toString()+"\n", options = arrayOf(StandardOpenOption.APPEND))
        }
    }

    fun saveInfoJson(energyList: MutableList<EnergyFields>){
        val path = Path("src/main/resources")
        val filePath = path.resolve("EnergyFile.json")

        val gson = Gson()
        val jsonString = gson.toJson(energyList)

        for (e in energyList){
            filePath.writeText(jsonString)
        }
    }

    fun saveMinPriceJson(energyList: MutableList<EnergyFields>){
        val path = Path("src/main/resources")
        val filePath = path.resolve("minPrice.json")

        val gson = Gson()
        val jsonString = gson.toJson(energyList.minOf { it.precio })
        filePath.writeText(jsonString, options = arrayOf(StandardOpenOption.APPEND))
    }

    fun saveMaxPriceJson(energyList: MutableList<EnergyFields>){
        val path = Path("src/main/resources")
        val filePath = path.resolve("maxPrice.json")

        val gson = Gson()
        val jsonString = gson.toJson(energyList.maxOf { it.precio })
        filePath.writeText(jsonString, options = arrayOf(StandardOpenOption.APPEND))
    }

    fun testToPassBeforeReadDoc(): Boolean {
        val fullPath = Path("src/main/resources/EnergyFile.json")
        val resourcePath = Path("src/main/resources")
        if(fullPath.exists()){
            readDocuments()
        }else if(resourcePath.exists()){
            return false
        }else{
            return false
        }
        return true
    }
    fun readDocuments() = this::class.java.getResource("EnergyFile.json").readText(Charsets.UTF_8)
}