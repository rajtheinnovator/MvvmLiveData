package com.enpassio.mvvmlivedata.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.enpassio.mvvmlivedata.model.MovieModel
import com.enpassio.mvvmlivedata.model.ResultEntity
import io.reactivex.observers.DisposableSingleObserver
import retrofit2.HttpException

class MovieViewModel() : ViewModel() {
    private val resultListObservable = MutableLiveData<List<String>>()
    private val resultListErrorObservable = MutableLiveData<HttpException>()
    private val itemObservable = MutableLiveData<ResultEntity>()
    fun getResultListObservable(): LiveData<List<String>> = resultListObservable
    fun getResultListErrorObservable(): LiveData<HttpException> = resultListErrorObservable
    fun getItemObservable(): LiveData<ResultEntity> = itemObservable
    private lateinit var entityList: List<ResultEntity>
    lateinit var movieModel: MovieModel
    private val schedulersWrapper = SchedulersWrapper()

    fun findMovie(movieQuery: String) {
        movieModel.fetchAgainstMovieQuery(movieQuery)!!.subscribeOn(schedulersWrapper.io())
            .observeOn(schedulersWrapper.main())
            .subscribeWith(object : DisposableSingleObserver<List<ResultEntity>?>() {
                override fun onSuccess(t: List<ResultEntity>) {
                    entityList = t
                    resultListObservable.postValue(fetchItemTextFrom(t))
                }

                override fun onError(e: Throwable) {
                    resultListErrorObservable.postValue(e as HttpException)
                }
            })
    }

    private fun fetchItemTextFrom(it: List<ResultEntity>): ArrayList<String> {
        val li = arrayListOf<String>()
        for (resultEntity in it) {
            li.add("${resultEntity.year}: ${resultEntity.title}")
        }
        return li
    }

    fun doOnItemClick(position: Int) {
        itemObservable.value = entityList[position]
    }
}