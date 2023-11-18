package com.example.cocktailbank.util

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.cocktailbank.data.cache.RecipeDetailCache

class RecipeDetailWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    override fun doWork(): Result {
        Log.d("clearDetailCache","Clear!")
        RecipeDetailCache.recipeDetailMap.clear()
        return Result.success()
    }
}