package com.example.newsappgsu

import android.content.DialogInterface
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.newsapp.model.ArticlesItem
import com.example.newsapp.model.NewsResponse
import com.example.newsapp.model.SourcesItem
import com.example.newsapp.model.SourcesResponse
import com.example.newsappgsu.adapter.NewsAdapter
import com.example.newsappgsu.api.ApiManager
import com.example.newsappgsu.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {
    lateinit var binding:ActivityMainBinding
    lateinit var adapter : NewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupView()
        getSources()
    }

    private fun getSources() {
        var call = ApiManager
            .getApis()
            .getNewsSources(
                Constants.API_KEY,
                Constants.LANGUAGE,
                Constants.COUNTRY
            )
        call.enqueue(object : Callback<SourcesResponse>{
            override fun onResponse(
                call: Call<SourcesResponse>,
                response: Response<SourcesResponse>
            ) {
                if(response.isSuccessful){// 200-299
                    showSourcesInTabLayout(response.body()?.sources)
                }else{// 401 -> response replied with error
                    showDialog(mess = response.body()?.message)
                }
            }

            override fun onFailure(call: Call<SourcesResponse>, t: Throwable) {
                showDialog(t)
            }
        })
    }

    private fun showDialog(t:Throwable ?= null, mess:String?=null) {
        var builder = AlertDialog.Builder(this@MainActivity)

        if (t != null){
            builder.setMessage(t?.message)
        }else{
            builder.setMessage(mess)
        }
        builder.setPositiveButton("retry",object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, p1: Int) {
                dialog?.dismiss()
            }

        }).create().show()
    }


    private fun showSourcesInTabLayout(sources: List<SourcesItem?>?) {
        var tab:TabLayout.Tab ?= null
        sources?.forEach({item->
            tab = binding.tabLayout.newTab()
            tab?.setText(item?.name)
            tab?.setTag(item)
            binding.tabLayout.addTab(tab!!)

            binding.tabLayout.addOnTabSelectedListener(this)
        })
        binding.tabLayout.getTabAt(0)?.select()
        getNews((tab?.tag as SourcesItem).id)
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        var itemTag = tab?.tag as SourcesItem
        getNews(itemTag.id)
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        adapter.changeData(null)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        getNews((tab?.tag as SourcesItem).id)
    }
    private fun getNews(itemId:String?=null) {
        adapter.changeData(null)
        binding.progressBar.visibility = View.VISIBLE
        ApiManager
            .getApis()
            .getNews(
                Constants.API_KEY,
                Constants.LANGUAGE,
                itemId,
                ""
            ).enqueue(object :Callback<NewsResponse>{
                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    binding.progressBar.visibility = View.GONE
                   if (response.isSuccessful){
                       showNewsInRecylerView(response.body()?.articles)
                   }else{
                       showDialog(mess = response.body()?.message)
                   }
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    showDialog(t)
                }

            })
    }

    private fun setupView(){
        adapter = NewsAdapter(listOf())
        binding.recyclerViewHome.adapter = adapter
    }
    private fun showNewsInRecylerView(articles: List<ArticlesItem?>?) {
        adapter.changeData(articles)
    }

}