package id.ac.polbeng.asmira.kabkotaretrofitapp.response

import com.google.gson.annotations.SerializedName
import id.ac.polbeng.asmira.kabkotaretrofitapp.model.KabKotaItem


data class KabKotaResponse(

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("data")
	val data: List<KabKotaItem>
)