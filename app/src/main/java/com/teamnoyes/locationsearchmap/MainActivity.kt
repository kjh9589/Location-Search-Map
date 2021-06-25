package com.teamnoyes.locationsearchmap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.teamnoyes.locationsearchmap.MapActivity.Companion.SEARCH_RESULT_EXTRA_KEY
import com.teamnoyes.locationsearchmap.databinding.ActivityMainBinding
import com.teamnoyes.locationsearchmap.model.LocationLatLngEntity
import com.teamnoyes.locationsearchmap.model.SearchResultEntity
import com.teamnoyes.locationsearchmap.response.search.Poi
import com.teamnoyes.locationsearchmap.response.search.Pois
import com.teamnoyes.locationsearchmap.utility.RetrofitUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SearchRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = Job()

        initViews()
        bindViews()
    }

    private fun initViews() = with(binding) {
        emptyResultTextView.isVisible = false

        adapter = SearchRecyclerAdapter {
            val intent = Intent(this@MainActivity, MapActivity::class.java).apply {
                putExtra(SEARCH_RESULT_EXTRA_KEY, it)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }

    private fun bindViews() = with(binding) {
        searchButton.setOnClickListener {
            searchKeyword(searchBarInputView.text.toString())
        }
    }

    private fun setData(pois: Pois) {
        val dataList = pois.poi.map {
            SearchResultEntity(
                buildingName = it.name ?: "건물명 없음",
                fullAddress = makeMainAddress(it),
                locationLatLng = LocationLatLngEntity(
                    it.noorLat,
                    it.noorLon
                )
            )
        }

        adapter.submitList(dataList)
    }

    private fun searchKeyword(keyword: String) {
        launch(coroutineContext) {
            try {
                withContext(Dispatchers.IO) {
                    val response = RetrofitUtil.apiService.getSearchLocation(
                        keyword = keyword
                    )

                    if (response.isSuccessful) {
                        withContext(Dispatchers.Main) {
                            response.body()?.let { searchResponse ->
                                setData(searchResponse.searchPoiInfo.pois)
                            }
                        }
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun makeMainAddress(poi: Poi): String =
        if (poi.secondNo?.trim().isNullOrEmpty()) {
            (poi.upperAddrName?.trim() ?: "") + " " +
                    (poi.middleAddrName?.trim() ?: "") + " " +
                    (poi.lowerAddrName?.trim() ?: "") + " " +
                    (poi.detailAddrName?.trim() ?: "") + " " +
                    poi.firstNo?.trim()
        } else {
            (poi.upperAddrName?.trim() ?: "") + " " +
                    (poi.middleAddrName?.trim() ?: "") + " " +
                    (poi.lowerAddrName?.trim() ?: "") + " " +
                    (poi.detailAddrName?.trim() ?: "") + " " +
                    (poi.firstNo?.trim() ?: "") + " " +
                    poi.secondNo?.trim()
        }
}