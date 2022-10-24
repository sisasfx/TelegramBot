import data.EnergyPricesApi
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readLines
import kotlin.io.path.readText

class UseCases(private val petition: EnergyPricesApi){

    suspend fun maxPrice(): Double {
        val energy: List<EnergyFields> = petition.listEnergyPrices()
        return energy.maxOf { it.precio }
    }

    suspend fun minPrice():Double{
        val energy: List<EnergyFields> = petition.listEnergyPrices()
        return energy.minOf { it.precio }
    }

    suspend fun sortedListByHour(): MutableList<EnergyFields>{
        val energy:List<EnergyFields> = petition.listEnergyPrices()
        val energySorted: MutableList<EnergyFields> = mutableListOf()

        for (e in energy.sortedBy { it.hour }){
            energySorted.add(e)
            // println(Json.encodeToString(e))
        }
        return energySorted
    }

    suspend fun sortedListByPrice(): MutableList<EnergyFields>{
        val energy:List<EnergyFields> = petition.listEnergyPrices()
        val energySorted: MutableList<EnergyFields> = mutableListOf()

        for (e in energy.sortedBy { it.precio }){
            energySorted.add(e)
        }
        return energySorted
    }


    fun testToPassBeforeReadDoc(): Boolean {
        val fullPath = Path("src/main/files/EnergyFile.json")
        val resourcePath = Path("src/main/files")
        if(fullPath.exists()){
            readDocuments()
        }else if(resourcePath.exists()){
            return false
        }else{
            return false
        }
        return true
    }
    fun readDocuments(): List<String>{
        val path = Path("src/main/files/EnergyFile.json")
        val fileJson = path.readLines()
        return fileJson
    }
}