package com.example.user.kotlinadapter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.net.Uri
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


class MainActivity : AppCompatActivity() {


    object DemoApi {
        const val URL = "https://en.wikipedia.org/w/"

        object Model {
            data class WikiResponse(val query: Wikiquery)
            data class Wikiquery(val searchinfo: WikiSearchInfo)
            data class WikiSearchInfo(val totalhits: Int)
        }

        interface Service {
            @GET("api.php?")
            fun hits(@Query("action") action : String, @Query("format") format : String, @Query("list") list : String,
                     @Query("srsearch") srsearch : String) : Call<Model.WikiResponse>
        }

        //Retrofit Object
        private val retrofit = Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        //Interface Object
        val service = retrofit.create(Service::class.java)!!
    }


    object GlobalModel {
        val presidents: kotlin.collections.MutableList<President> = java.util.ArrayList()
        init {
            Log.d("USR", "This ( $this ) is a singleton")

            presidents.add(President("Kaarlo Stahlberg", 1919, 1925, "Eka presidentti"))
            presidents.add(President("Lauri Relander", 1925, 1931, "Toka presidentti"))
            presidents.add(President("P. E. Svinhufvud", 1931, 1937, "Kolmas presidentti"))
            presidents.add(President("Kyösti Kallio", 1937, 1940, "Neljas presidentti"))
            presidents.add(President("Risto Ryti", 1940, 1944, "Viides presidentti"))
            presidents.add(President("Carl Gustaf Emil Mannerheim", 1944, 1946, "Kuudes presidentti"))
            presidents.add(President("Juho Kusti Paasikivi", 1946, 1956, "Äkäinen ukko"))
            presidents.add(President("Urho Kekkonen", 1956, 1982, "Pelimies"))
            presidents.add(President("Mauno Koivisto", 1982, 1994, "Manu"))
            presidents.add(President("Martti Ahtisaari", 1994, 2000, "Mahtisaari"))
            presidents.add(President("Tarja Halonen", 2000, 2012, "Eka naispresidentti"))
            presidents.sort()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainlistview.adapter = PresidentListAdapter(this, GlobalModel.presidents)

        mainlistview.setOnItemClickListener { _, _, position: Int, _ ->
            textViewSelect.text = mainlistview.getItemAtPosition(position).toString()
            getHits(GlobalModel.presidents[position])
        }


        mainlistview.setOnItemLongClickListener { _, _, position: Int, _ ->
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://fi.wikipedia.org/wiki/" + GlobalModel.presidents[position].name))
            startActivity(i)
            true
        }
    }

    private fun getHits(president: President) {
        val call = DemoApi.service.hits("query", "json", "search", president.name)

        val value = object : Callback<DemoApi.Model.WikiResponse> {

            override fun onResponse(call: Call<DemoApi.Model.WikiResponse>, response: Response<DemoApi.Model.WikiResponse>?) {
                if (response != null) {
                    var res: DemoApi.Model.WikiResponse = response.body()!!
                    textViewHits.text = getString(R.string.hits, res.query.searchinfo.totalhits.toString())
                }
            }
            override fun onFailure(call: Call<DemoApi.Model.WikiResponse>, t: Throwable) {
                Log.e("DBG", t.toString());
            }
        }
        call.enqueue(value) // asyncronous request
    }
}


