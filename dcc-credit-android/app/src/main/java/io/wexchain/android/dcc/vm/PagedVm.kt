package io.wexchain.android.dcc.vm

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.wexchain.android.common.SingleLiveEvent
import io.wexchain.android.dcc.App
import io.wexchain.android.dcc.chain.ScfOperations
import io.wexchain.dccchainservice.domain.LoanRecordSummary
import io.wexchain.dccchainservice.domain.PagedList

abstract class PagedVm<T> : ViewModel() {

    val records = ObservableField<List<T>>()

    val loadFailEvent = SingleLiveEvent<String>()

    private var page = 0

    fun load(page: Int, onLoadFinish: ((Int) -> Unit)? = null) {
        if (page == 0 || this.page + 1 == page) {

            loadPage(page)
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    onLoadFinish?.invoke(page)
                }
                .subscribe({ list ->
                    mergeList(list.items, page)
                }, {
                    loadFailEvent.value = "load fail"
                })
        } else {
            onLoadFinish?.invoke(page)
        }
    }

    abstract fun loadPage(page: Int): Single<PagedList<T>>

    fun refresh(onLoadFinish: ((Int) -> Unit)? = null) {
        load(0, onLoadFinish)
    }

    fun loadNext(onLoadFinish: ((Int) -> Unit)? = null) {
        load(page + 1, onLoadFinish)
    }

    private fun mergeList(list: List<T>?, page: Int) {
        list ?: return
        if (page == 0) {
            records.set(list)
        } else if (this.page + 1 == page) {
            records.set((records.get() ?: emptyList()) + list)
        }
        this.page = page
    }
}