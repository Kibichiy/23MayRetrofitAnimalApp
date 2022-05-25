package com.example.a23mayretrofitanimalapp

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.a23mayretrofitanimalapp.databinding.ActivityMainBinding
import com.example.a23mayretrofitanimalapp.model.APIClient
import com.example.a23mayretrofitanimalapp.model.APIService
import com.example.a23mayretrofitanimalapp.model.Animals
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var retrofit: Retrofit
    lateinit var apiService: APIService

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAPICall()

        binding.nextButton.setOnClickListener {
            binding.loadingSpinner.visibility = View.VISIBLE
            initAPICall()
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initAPICall() {
        retrofit = APIClient.getRetrofit()
        apiService = retrofit.create(APIService::class.java)

        val animalCall: Call<Animals> = apiService.getRandomAnimal()
        animalCall.enqueue(object : Callback<Animals> {
            override fun onResponse(call: Call<Animals>, response: Response<Animals>) {
                if (response.isSuccessful) {
                    val animal = response.body()!!
                    binding.apply {
                        animal.apply {

                            fun htmlManipulation() {
                                val t1: String = getColoredSpanned(
                                    "Animal Found at: ",
                                    getColor(R.color.text_color_primary)
                                )
                                val r1: String = getColoredSpanned(
                                    geo_range.toString(),
                                    getColor(R.color.teal_200)
                                )
                                val t2: String = getColoredSpanned(
                                    "Habitat: ",
                                    getColor(R.color.text_color_primary)
                                )
                                val r2: String = getColoredSpanned(
                                    habitat.toString(),
                                    getColor(R.color.teal_200)
                                )
                                val t3: String = getColoredSpanned(
                                    "Animal Type: ",
                                    getColor(R.color.text_color_primary)
                                )
                                val r3: String = getColoredSpanned(
                                    animal_type.toString(),
                                    getColor(R.color.teal_200)
                                )
                                val t4: String = getColoredSpanned(
                                    "Lifespan: ",
                                    getColor(R.color.text_color_primary)
                                )
                                val r4: String = getColoredSpanned(
                                    lifespan.toString(),
                                    getColor(R.color.teal_200)
                                )

                                animalAnimalGeoRange.text = Html.fromHtml(t1+r1)
                                animalHabitat.text = Html.fromHtml(t2+r2)
                                animalAnimalType.text = Html.fromHtml(t3+r3)
                                animalLifespan.text = Html.fromHtml(t4+r4)
                            }
                            htmlManipulation()
                        }
                        binding.apply {
                            if (animal.geo_range != null){
                                animalAnimalGeoRange.visibility = View.VISIBLE
                            }
                            if (animal.habitat != null){
                                animalHabitat.visibility = View.VISIBLE
                            }
                            if (animal.animal_type != null){
                                animalAnimalType.visibility = View.VISIBLE
                            }
                            if (animal.lifespan != null){
                                animalLifespan.visibility = View.VISIBLE
                            }
                            Glide.with(this@MainActivity).load(animal.image_link).into(imageOfAnimal)
                            binding.loadingSpinner.visibility = View.GONE
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Animals>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun getColoredSpanned(text: String, color: Int): String {
        return "<font color = $color>$text</font>"
    }
}