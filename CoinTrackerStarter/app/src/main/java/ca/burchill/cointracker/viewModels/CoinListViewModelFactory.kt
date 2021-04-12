package ca.burchill.cointracker.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider



class CoinListViewModelFactory (
    private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoinListViewModel::class.java)) {
            @Suppress("unchecked_cast")
            return CoinListViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}