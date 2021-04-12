package ca.burchill.cointracker.viewModels

import android.app.Application
import androidx.lifecycle.*
import ca.burchill.cointracker.database.getDatabase
import ca.burchill.cointracker.network.CoinApi
import ca.burchill.cointracker.network.CoinApiResponse
import ca.burchill.cointracker.network.*
import ca.burchill.cointracker.repository.CoinsRepository
import ca.burchill.cointracker.work.Refresh
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import timber.log.Timber
import java.io.IOException


enum class CoinApiStatus { LOADING, ERROR, DONE }



    class CoinListViewModel(application: Application) : AndroidViewModel(application) {

        private val coinsRepository = CoinsRepository(getDatabase(application))
        val coins = coinsRepository.coins

        // The internal MutableLiveData that stores the status of the most recent request
        private val _status = MutableLiveData<CoinApiStatus>()
        val status: LiveData<CoinApiStatus>
            get() = _status





        // or use viewModelScope
        private var viewModelJob = Job()
        private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


        init {
            RefreshRepo()
        }


                    private fun RefreshRepo(){
                        viewModelScope.launch{
                            try{
                                _status.value = CoinApiStatus.LOADING
                                coinsRepository.refreshCoins()
                                _status.value = CoinApiStatus.DONE
                            } catch (networtError: IOException){
                                if(coins.value.isNullOrEmpty())
                                    _status.value = CoinApiStatus.ERROR
                            }
                        }
                    }



                 override fun onCleared(){
                        super.onCleared()
                        viewModelJob.cancel()
                    }



                }