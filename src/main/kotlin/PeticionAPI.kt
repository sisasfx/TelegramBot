import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path
import kotlin.io.path.createFile
import kotlin.io.path.writeText

suspend fun main(){
    val sortedList = sortedListByHour()
    val priceSortedList = sortedListByPrice()

    val bot = bot {
        token = "5610409121:AAE0u6lXHCgkSlAwQnifLs62IYXcm_9sIk8"
        dispatch{
            command("start"){
                bot.sendMessage(ChatId.fromId(message.chat.id), text= "Vols saber quina hora és més cara? " +
                        "Escriu /maxPrice")
                bot.sendMessage(ChatId.fromId(message.chat.id), text = "Vols saber quina hora és mes barata? " +
                        "Escriu /minPrince")
                bot.sendMessage(ChatId.fromId(message.chat.id), text = "Vols saber tots els preus del Mw/h? " +
                        "Escriu /price")
                bot.sendMessage(ChatId.fromId(message.chat.id), text = "Vols guardar la informació en un arxiu txt?" +
                        "Escriu /saveInfo")
                bot.sendMessage(ChatId.fromId(message.chat.id), text = "Vols guardar la informació amb JSON? " +
                        "Escriu /saveJson")

            }
            command("maxPrice"){
                bot.sendMessage(ChatId.fromId(message.chat.id), text= priceSortedList.maxOf { it.precio.toString()})
            }
            command("minPrice"){
                bot.sendMessage(ChatId.fromId(message.chat.id), text = priceSortedList.minOf { it.precio.toString()})
            }
            command("price"){
                bot.sendMessage(ChatId.fromId(message.chat.id), text = sortedList.toString())
            }
            command("saveInfo"){
                saveInfoTxt(priceSortedList)
                bot.sendMessage(ChatId.fromId(message.chat.id), text = "Informació guardada TXT")
            }
            command("saveJson"){
                saveInfoJson(sortedList)
                bot.sendMessage(ChatId.fromId(message.chat.id), text = "Informació guardada en JSON")
            }
        }
    }
    bot.startPolling()
}

suspend fun requestAPI(): List<EnergyFields> {
    val client = HttpClient(CIO){
        install(ContentNegotiation){
            json(Json{
                ignoreUnknownKeys = true
            })
        }
    }
    return client.get("https://api.preciodelaluz.org/v1/prices/cheapests?zone=PCB&n=24").body()

}

suspend fun sortedListByHour(): MutableList<EnergyFields>{
    val energy:List<EnergyFields> = requestAPI()
    val energySorted: MutableList<EnergyFields> = mutableListOf()

    for (e in energy.sortedBy { it.hour }){
        energySorted.add(e)
       // println(Json.encodeToString(e))
    }
    return energySorted
}

suspend fun sortedListByPrice(): MutableList<EnergyFields>{
    val energy:List<EnergyFields> = requestAPI()
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