import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EnergyFields(
    @SerialName("hour") val hour: String,
    @SerialName("price") val precio :Double,
    @SerialName("units") val units: String

/*
Peticion para obtener datos en tiempo real del precio de la energia.

https://apidatos.ree.es/es/datos/mercados/precios-mercados-tiempo-real?start_date=2022-10-10T00:01&end_date=2022-10-10T22:00&time_trunc=hour

ESTA API ES MAS SENCILLA
https://api.preciodelaluz.org/v1/prices/all?zone=PCB
*/

)
