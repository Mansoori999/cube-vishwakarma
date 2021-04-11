package com.vinners.trumanms.feature.wallet.ui.ui

import androidx.lifecycle.*
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.data.models.money.Money
import com.vinners.trumanms.data.models.money.Transaction
import com.vinners.trumanms.data.repository.ProfileRepository
import com.vinners.trumanms.data.repository.WalletRepository
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.launch
import java.time.temporal.TemporalAmount
import javax.inject.Inject

interface WalletEvents {
    val moneyState: LiveData<Lce<Money>>

    val transactionState: LiveData<Lce<List<Transaction>>>

    val redeemMonetState: LiveData<Lse>

    val referralLink: LiveData<Lce<String>>
}

class WalletInfoViewModel @Inject constructor(
    private val walletRepository: WalletRepository,
    private val profileRepository: ProfileRepository
) : ViewModel(), WalletEvents {

    private val _moneyState = MutableLiveData<Lce<Money>>()
    override val moneyState: LiveData<Lce<Money>> = _moneyState

    private val _transactionState = MutableLiveData<Lce<List<Transaction>>>()
    override val transactionState: LiveData<Lce<List<Transaction>>> = _transactionState

    private val _redeemMoneyState = MutableLiveData<Lse>()
    override val redeemMonetState: LiveData<Lse> = _redeemMoneyState


    fun getMoney() {
        viewModelScope.launch {
            _moneyState.value = Lce.Loading
            try {
                val response = walletRepository.getMoney()
                _moneyState.postValue(Lce.Content(response))
            } catch (e: Exception) {
                _moneyState.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }

    fun getTransaction(){
        viewModelScope.launch {
            _transactionState.value = Lce.Loading
            try {
                val response = walletRepository.getTransaction()
                _transactionState.postValue(Lce.Content(response))
            }catch (e: Exception){
                _transactionState.postValue(Lce.Error(e.localizedMessage))
            }
        }
    }

    fun submitRedeem(amount: String){
        viewModelScope.launch {
            _redeemMoneyState.value = Lse.Loading
            try {
                walletRepository.submitRedeem(amount)
                _redeemMoneyState.postValue(Lse.Success)
            }catch (e: Exception){
                _redeemMoneyState.postValue(Lse.Error(e.localizedMessage))
            }
        }
    }


    private val _referralLink = MutableLiveData<Lce<String>>()
    override val referralLink: LiveData<Lce<String>> = _referralLink

    fun generateReferralLink() = viewModelScope.launch{
        _referralLink.value = Lce.loading()

        try {
            val referralLink = profileRepository.generateUserReferralLink()
            _referralLink.value = Lce.content(referralLink)
        } catch (e: Exception) {
            _referralLink.value = Lce.error(e.message ?: "Unable to generate referral link")
        }
    }
}