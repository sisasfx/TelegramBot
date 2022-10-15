import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import com.google.gson.Gson
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path
import kotlin.io.path.createFile
import kotlin.io.path.writeText

suspend fun main(){
    val petition = ApiPetition()
    val user = UserOptions()

    val sortedList = user.sortedListByHour(petition)
    val priceSortedList = user.sortedListByPrice(petition)

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
                user.saveInfoTxt(priceSortedList)
                bot.sendMessage(ChatId.fromId(message.chat.id), text = "Informació guardada TXT")
            }
            command("saveJson"){
                user.saveInfoJson(sortedList)
                bot.sendMessage(ChatId.fromId(message.chat.id), text = "Informació guardada en JSON")
            }
        }
    }
    bot.startPolling()
}

