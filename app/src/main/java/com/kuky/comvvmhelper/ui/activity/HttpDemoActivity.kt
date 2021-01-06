package com.kuky.comvvmhelper.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import com.kk.android.comvvmhelper.anno.ActivityConfig
import com.kk.android.comvvmhelper.extension.*
import com.kk.android.comvvmhelper.helper.*
import com.kk.android.comvvmhelper.listener.OnErrorReloadListener
import com.kk.android.comvvmhelper.ui.BaseActivity
import com.kk.android.comvvmhelper.widget.RequestStatusCode
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.ActivityHttpDemoBinding
import com.kuky.comvvmhelper.helper.ApiService
import com.kuky.comvvmhelper.viewmodel.HttpViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

@ActivityConfig(statusBarColor = Color.BLACK)
class HttpDemoActivity : BaseActivity<ActivityHttpDemoBinding>() {

    private var mRequestJob: Job? = null

    private val mViewModel by viewModel<HttpViewModel>()

    override fun layoutId() = R.layout.activity_http_demo

    override fun initActivity(savedInstanceState: Bundle?) {
        mBinding.requestCode = RequestStatusCode.Loading
        mBinding.reload = OnErrorReloadListener { requestByRetrofit() }
        requestByRetrofit()

        // ViewModel Manager Pool
        mViewModel.getSingleLiveEvent<String>("modelText").run {
            observe(this@HttpDemoActivity, { ePrint { it } })

            postValue("Model Text ABC")
        }
    }

    private fun download() {
        mRequestJob?.cancel()
        mRequestJob = launch {
            DownloadHelper.instance(this@HttpDemoActivity).simpleDownload {
                downloadUrl = ""

                storedFilePath = File(Environment.DIRECTORY_DCIM, "$packageName/a.jpg").absolutePath

                urlParams = hashMapOf("a" to "1")

                qWrapper = QWrapper("a.jpg", "${Environment.DIRECTORY_DCIM}/$packageName", downloadType = DownloadType.PICTURES)

                onProgressChange = { ePrint { "progress: $it" } }

                onDownloadFinished = { toast("download finished") }

                onDownloadFailed = { toast("download failed") }
            }
        }
    }

    private fun requestByHttp() {
        mRequestJob?.cancel()
        mRequestJob = launch(Dispatchers.IO) {
            http {
                baseUrl = "https://github.com/kukyxs"

                onSuccess = {
                    mBinding.requestResult = it.checkText().renderHtml().toString()
                    mBinding.requestCode = RequestStatusCode.Succeed
                }

                onFail = {
                    mBinding.requestCode = RequestStatusCode.Error
                }
            }
        }
    }

    private fun requestByRetrofit() {
        mRequestJob?.cancel()
        mRequestJob = covLaunch(Dispatchers.IO, onRun = {
            val result = createService<ApiService>().requestRepositoryInfo()
            workOnMain {
                mBinding.requestCode = (result.errorCode == 0).yes {
                    mBinding.requestResult = result.toString()
                    RequestStatusCode.Succeed
                }.otherwise { RequestStatusCode.Error }

                workOnIO {
                    val tops = createService<ApiService>().requestTop("ef69c9ea662b4ca4ac768d4f70b921af")
                    ePrint { tops }
                }
            }
        }, onError = { _, throwable ->
            ePrint { throwable }
            mBinding.requestCode = RequestStatusCode.Error
            longToast(throwable.message ?: "")
        })
    }
}