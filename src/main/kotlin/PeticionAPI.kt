import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

suspend fun main(){
    val energy:List<EnergyFields> = requestAPI()
    for (e in energy.sortedBy { it.hour }){
        println(e)
    }

    val bot = bot {
        token = "5610409121:AAE0u6lXHCgkSlAwQnifLs62IYXcm_9sIk8"
        dispatch{
            command("start"){
                bot.sendMessage(ChatId.fromId(message.chat.id), text = "Vols saber el preu del Mw/h?")
            }
        }
    }
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