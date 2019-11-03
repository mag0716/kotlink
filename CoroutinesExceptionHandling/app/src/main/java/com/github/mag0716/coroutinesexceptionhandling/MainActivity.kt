package com.github.mag0716.coroutinesexceptionhandling

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    companion object {
        private const val TAG = "CoroutinesExceptionHandling"
    }

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // single API
        singleSuccessButton.setOnClickListener { singleApi(true) }
        singleFailedButton.setOnClickListener { singleApi(false) }
        singleWithContextSuccessButton.setOnClickListener { singleApiWithContext(true) }
        singleWithContextFailedButton.setOnClickListener { singleApiWithContext(false) }

        // multiple API(sequential)
        multipleSequentialSuccessButton.setOnClickListener { multipleSequentialApi(true) }
        multipleSequentialFailedButton.setOnClickListener { multipleSequentialApi(false) }
        multipleSequentialWithContextSuccessButton.setOnClickListener {
            multipleSequentialApiWithContext(
                true
            )
        }
        multipleSequentialWithContextFailedButton.setOnClickListener {
            multipleSequentialApiWithContext(
                false
            )
        }

        // multiple API(parallel)
        multipleParallelSuccessButton.setOnClickListener { multipleParallelApi(true) }
        multipleParallelFailedButton.setOnClickListener { multipleParallelApi(false) }
        multipleParallelWithContextSuccessButton.setOnClickListener {
            multipleParallelApiWithContext(
                true
            )
        }
        multipleParallelWithContextFailedButton.setOnClickListener {
            multipleParallelApiWithContext(
                false
            )
        }
    }

    override fun onResume() {
        super.onResume()
        job = Job()
    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }

    private fun singleApi(isSuccess: Boolean) = launch {
        try {
            val data = fetchData(isSuccess)
            Log.d(TAG, "singleApi : $data")
        } catch (exception: Exception) {
            Log.d(TAG, "singleApi: catch $exception")
        }
    }

    private fun singleApiWithContext(isSuccess: Boolean) = launch {
        try {
            val data = fetchDataWithContext(isSuccess)
            Log.d(TAG, "singleApiWithContext : $data")
        } catch (exception: Exception) {
            Log.d(TAG, "singleApiWithContext: catch $exception")
        }
    }

    private fun multipleSequentialApi(isSuccess: Boolean) = launch {
        try {
            val data1 = fetchData(true)
            val data2 = fetchData(isSuccess)
            Log.d(TAG, "multipleSequentialApi :$data1, $data2")
        } catch (exception: Exception) {
            Log.d(TAG, "multipleSequentialApi : catch $exception")
        }
    }

    private fun multipleSequentialApiWithContext(isSuccess: Boolean) = launch {
        try {
            val data1 = fetchDataWithContext(true)
            val data2 = fetchDataWithContext(isSuccess)
            Log.d(TAG, "multipleSequentialApi :$data1, $data2")
        } catch (exception: Exception) {
            Log.d(TAG, "multipleSequentialApi : catch $exception")
        }
    }

    private fun multipleParallelApi(isSuccess: Boolean) = launch {
        try {
            // try - catch だけではクラッシュするので、coroutineScope で囲う必要がある
            coroutineScope {
                val data1 = async { fetchData(true) }
                val data2 = async { fetchData(isSuccess) }
                Log.d(TAG, "multipleSequentialApi :${data1.await()}, ${data2.await()}")
            }
        } catch (exception: Exception) {
            Log.d(TAG, "multipleSequentialApi : catch $exception")
        }
    }

    private fun multipleParallelApiWithContext(isSuccess: Boolean) = launch {
        try {
            // try - catch だけではクラッシュするので、coroutineScope で囲う必要がある
            coroutineScope {
                val data1 = async { fetchDataWithContext(true) }
                val data2 = async { fetchDataWithContext(isSuccess) }
                Log.d(TAG, "multipleSequentialApi :${data1.await()}, ${data2.await()}")
            }
        } catch (exception: Exception) {
            Log.d(TAG, "multipleSequentialApi : catch $exception")
        }
    }

    private suspend fun fetchData(isSuccess: Boolean): String {
        Log.d(TAG, "fetchData : $isSuccess")
        delay(3000)
        if (isSuccess) {
            return "Data"
        } else {
            throw IllegalArgumentException()
        }
    }

    private suspend fun fetchDataWithContext(isSuccess: Boolean): String =
        withContext(Dispatchers.IO) {
            Log.d(TAG, "fetchDataWithContext : $isSuccess")
            delay(3000)
            if (isSuccess) {
                "Data"
            } else {
                throw IllegalArgumentException()
            }
        }
}
