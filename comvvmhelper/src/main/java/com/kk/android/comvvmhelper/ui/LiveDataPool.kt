package com.kk.android.comvvmhelper.ui

import androidx.lifecycle.MutableLiveData
import com.kk.android.comvvmhelper.utils.SingleLiveEvent

/**
 * @author kuky.
 * @description LiveDataManager, simply manager LiveData at viewModel
 * example:
 * ```kotlin
 * class YourActivity {
 *    val mViewModel by viewModels<YourViewModel>()
 *
 *    override fun onCreate(savedInstanceState: Bundle?) {
 *        super.onCreate(savedInstanceState)
 *        // .... your logic
 *        mViewModel.getLiveDataEvent<String>("tag").observe(this){ // your logical }
 *    }
 * }
 * ```
 */
class LiveDataPool {
    private val singleEventPool: HashMap<String, SingleLiveEvent<*>> = hashMapOf()

    private val liveEventPool: HashMap<String, MutableLiveData<*>> = hashMapOf()

    @Suppress("UNCHECKED_CAST")
    fun <T> getSingleEvent(tag: String): SingleLiveEvent<T> {
        return (singleEventPool[tag] as? SingleLiveEvent<T>)
            ?: SingleLiveEvent<T>().also { singleEventPool[tag] = it }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getLiveEvent(tag: String): MutableLiveData<T> {
        return (liveEventPool[tag] as? MutableLiveData<T>)
            ?: MutableLiveData<T>().also { liveEventPool[tag] = it }
    }

    fun clear() {
        liveEventPool.clear()
        singleEventPool.clear()
    }
}