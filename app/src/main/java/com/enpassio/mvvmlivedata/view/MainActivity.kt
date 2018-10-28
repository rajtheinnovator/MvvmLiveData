package com.enpassio.mvvmlivedata.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.enpassio.mvvmlivedata.R
import com.enpassio.mvvmlivedata.model.MovieModel
import com.enpassio.mvvmlivedata.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mMainViewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        mMainViewModel.movieModel = MovieModel()
        loadView()
        respondToClicks()
        listenToObservables()
    }

    private fun listenToObservables() {
        mMainViewModel.getItemObservable().observe(this, Observer { }) //start detail activity
        mMainViewModel.getResultListObservable().observe(this, Observer {
            hideProgressBar()
            updateMovieList(it!!)
        })
        mMainViewModel.getResultListErrorObservable().observe(this, Observer {
            hideProgressBar()
            showErrorMessage(it!!.message())
        })
    }

    private fun loadView() {
        setContentView(R.layout.activity_main)
        movieAdapter = MovieAdapter()
        main_activity_recyclerView.adapter = movieAdapter
    }

    private fun respondToClicks() {
        main_activity_button.setOnClickListener {
            showProgressBar()
            mMainViewModel.findMovie(main_activity_editText.text.toString())
        }
        movieAdapter setItemClickMethod {
            mMainViewModel.doOnItemClick(it)
        }
    }

    fun showProgressBar() {
        main_activity_progress_bar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        main_activity_progress_bar.visibility = View.GONE
    }

    fun showErrorMessage(errorMsg: String) {
        Toast.makeText(this, "Error retrieving data: $errorMsg", Toast.LENGTH_SHORT).show()
    }

    fun updateMovieList(t: List<String>) {
        movieAdapter.updateList(t)
        movieAdapter.notifyDataSetChanged()
    }
}
