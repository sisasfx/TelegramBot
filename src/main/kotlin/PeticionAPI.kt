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