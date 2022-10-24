import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
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
                bot.sendMessage(ChatId.fromId(message.chat.id), text = "Vols llegir la informació del documents JSON? "+
                        "Escriu /read")

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
            command("saveMaxPrice"){
                user.saveMaxPriceJson(priceSortedList)
                bot.sendMessage(ChatId.fromId(message.chat.id), text = "Maxim preu del dia guardat")
            }
            command("saveMinPrice"){
                user.saveMinPriceJson(priceSortedList)
                bot.sendMessage(ChatId.fromId(message.chat.id), text = "Minim preu del dia guardat")
            }
            command("read"){
                if(!user.testToPassBeforeReadDoc()){
                    bot.sendMessage(ChatId.fromId(message.chat.id), text = "Ups! crec que no tinc el fitxer creat")
                }
                bot.sendMessage(ChatId.fromId(message.chat.id), text = user.readDocuments())
            }
        }
    }
    bot.startPolling()
}

