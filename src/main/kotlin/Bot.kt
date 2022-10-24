import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import data.EnergyPricesApi
import data.LocalPricesStorage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
suspend fun main(){
    val petition = EnergyPricesApi()
    val user = UseCases(petition)
    val localPriceFile = LocalPricesStorage(petition)

    val bot = bot {
        token = "5610409121:AAE0u6lXHCgkSlAwQnifLs62IYXcm_9sIk8"
        dispatch{
            command("start"){
                bot.sendMessage(ChatId.fromId(message.chat.id), text= "Vols saber quina hora és més cara? " +
                        "Escriu /maxPrice")
                bot.sendMessage(ChatId.fromId(message.chat.id), text = "Vols saber quina hora és mes barata? " +
                        "Escriu /minPrince")
                bot.sendMessage(ChatId.fromId(message.chat.id), text = "Vols saber tots els preus ordenats del Mw/h? " +
                        "Escriu /price")
                bot.sendMessage(ChatId.fromId(message.chat.id), text = "Vols saber tots els preus ordenats per hores? "+
                        "Escriu /energyList")
                bot.sendMessage(ChatId.fromId(message.chat.id), text = "Vols guardar la informació en un arxiu txt?" +
                        "Escriu /saveInfo")
                bot.sendMessage(ChatId.fromId(message.chat.id), text = "Vols guardar la informació amb JSON? " +
                        "Escriu /saveJson")
                bot.sendMessage(ChatId.fromId(message.chat.id), text = "Vols llegir la informació del documents JSON? "+
                        "Escriu /read")

            }
            command("maxPrice"){
                GlobalScope.launch {
                    bot.sendMessage(ChatId.fromId(message.chat.id), text= user.maxPrice().toString())
                }
            }
            command("minPrice"){
                GlobalScope.launch {
                    bot.sendMessage(ChatId.fromId(message.chat.id), text = user.minPrice().toString())
                }
            }
            command("price"){
                GlobalScope.launch {
                    bot.sendMessage(ChatId.fromId(message.chat.id), text = user.sortedListByPrice().toString())
                }
            }
            command("energyList"){
                GlobalScope.launch {
                    bot.sendMessage(ChatId.fromId(message.chat.id), text = user.sortedListByHour().toString())
                }
            }
            command("saveInfo"){
                GlobalScope.launch {
                    localPriceFile.saveInfoTxt()
                }
                bot.sendMessage(ChatId.fromId(message.chat.id), text = "Informació guardada TXT")
            }
            command("saveJson"){
                GlobalScope.launch {
                    localPriceFile.saveInfoJson()
                }
                bot.sendMessage(ChatId.fromId(message.chat.id), text = "Informació guardada en JSON")
            }
            command("saveMaxPrice"){
                GlobalScope.launch {
                    localPriceFile.saveMaxPriceJson()
                }
                bot.sendMessage(ChatId.fromId(message.chat.id), text = "Maxim preu del dia guardat")
            }
            command("saveMinPrice"){
                GlobalScope.launch {
                    localPriceFile.saveMinPriceJson()
                }
                bot.sendMessage(ChatId.fromId(message.chat.id), text = "Minim preu del dia guardat")
            }
            command("read"){
                if(!user.testToPassBeforeReadDoc()){
                    bot.sendMessage(ChatId.fromId(message.chat.id), text = "Ups! crec que no tinc el fitxer creat")
                }
                bot.sendMessage(ChatId.fromId(message.chat.id), text = user.readDocuments().toString())
            }
        }
    }
    bot.startPolling()
}

