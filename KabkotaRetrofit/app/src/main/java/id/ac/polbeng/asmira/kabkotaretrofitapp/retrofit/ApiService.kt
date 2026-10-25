package id.ac.polbeng.asmira.kabkotaretrofitapp.retrofit

import id.ac.polbeng.asmira.kabkotaretrofitapp.response.KabKotaResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("api/read_kabkota.php")
    fun getListUsers(): Call<KabKotaResponse>
}