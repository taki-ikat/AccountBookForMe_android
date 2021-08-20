package com.example.accountbookforme.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accountbookforme.model.Expense
import com.example.accountbookforme.model.TotalEachFilter
import com.example.accountbookforme.repository.ExpenseRepository
import com.example.accountbookforme.util.RestUtil
import kotlinx.coroutines.launch
import java.math.BigDecimal

class ExpensesViewModel : ViewModel() {

    private val expenseRepository: ExpenseRepository =
        RestUtil.retrofit.create(ExpenseRepository::class.java)

    // 支出リスト
    var expenseList: MutableLiveData<List<Expense>> = MutableLiveData()

    // 決済方法ごとの支出額リスト
    var totalPaymentList: MutableLiveData<List<TotalEachFilter>> = MutableLiveData()

    init {
        loadExpenseList()
        getTotalPaymentList()
    }

    /**
     * 支出リスト取得
     */
    private fun loadExpenseList() {

        viewModelScope.launch {
            try {
                val request = expenseRepository.getAll()
                if (request.isSuccessful) {
                    expenseList.value = request.body()
                } else {
                    Log.e("ExpenseViewModel", "Not successful: $request")
                }
            } catch (e: Exception) {
                Log.e("ExpenseViewModel", "Something is wrong: $e")
            }
        }
    }

    /**
     * 決済方法ごとの支出額リスト取得
     */
    private fun getTotalPaymentList() {

        viewModelScope.launch {
            try {
                val request = expenseRepository.getTotalPaymentList()
                if (request.isSuccessful) {
                    totalPaymentList.value = request.body()
                } else {
                    Log.e("ExpenseViewModel", "Not successful: $request")
                }
            } catch (e: Exception) {
                Log.e("ExpenseViewModel", "Something is wrong: $e")
            }
        }
    }


    /**
     * 総額を計算
     */
    fun calcTotal(): BigDecimal? = expenseList.value?.fold(BigDecimal.ZERO) { acc, expense ->
        acc + expense.total
    }
}