package com.kuky.comvvmhelper.ui.activity

import android.os.Bundle
import android.view.ViewGroup
import com.kk.android.comvvmhelper.anko.longToast
import com.kk.android.comvvmhelper.anko.toast
import com.kk.android.comvvmhelper.anno.ActivityConfig
import com.kk.android.comvvmhelper.anno.PublicDirectoryType
import com.kk.android.comvvmhelper.extension.covLaunch
import com.kk.android.comvvmhelper.extension.delayLaunch
import com.kk.android.comvvmhelper.extension.formatDuration
import com.kk.android.comvvmhelper.extension.formatFileSize
import com.kk.android.comvvmhelper.extension.otherwise
import com.kk.android.comvvmhelper.extension.renderHtml
import com.kk.android.comvvmhelper.extension.workOnIO
import com.kk.android.comvvmhelper.extension.workOnMain
import com.kk.android.comvvmhelper.extension.yes
import com.kk.android.comvvmhelper.helper.Http
import com.kk.android.comvvmhelper.helper.createService
import com.kk.android.comvvmhelper.helper.logs
import com.kk.android.comvvmhelper.listener.OnErrorReloadListener
import com.kk.android.comvvmhelper.ui.BaseActivity
import com.kk.android.comvvmhelper.utils.download.Downloader
import com.kk.android.comvvmhelper.utils.dp2px
import com.kk.android.comvvmhelper.widget.RequestStatusCode
import com.kuky.comvvmhelper.R
import com.kuky.comvvmhelper.databinding.ActivityHttpDemoBinding
import com.kuky.comvvmhelper.helper.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

@ActivityConfig(statusBarColorString = "#008577")
class HttpDemoActivity : BaseActivity<ActivityHttpDemoBinding>() {

    private val mEnabledDownload by lazy { intent.getBooleanExtra("switchOn", false) }

    private var mRequestJob: Job? = null

    override fun layoutId() = R.layout.activity_http_demo

    override fun initActivity(savedInstanceState: Bundle?) {
        mBinding.holder = this
        mBinding.showDownload = mEnabledDownload
        mBinding.requestCode = RequestStatusCode.Loading
        mBinding.reload = OnErrorReloadListener { requestByHttp() }
        mBinding.scroller.layoutParams = (mBinding.scroller.layoutParams as ViewGroup.MarginLayoutParams)
            .apply {
                topMargin = mEnabledDownload.yes { 60f.dp2px().toInt() }.otherwise { 0 }
            }

        delayLaunch(1_000) { requestByHttp() }
    }

    fun download() {
        launch {
            Downloader.instance(this@HttpDemoActivity).download {
//                url = "http://imtt.dd.qq.com/sjy.40001/sjy.00001/16891/apk/69E7E4E87B798032925A2CA9D99F4F22.apk?fsname=com.tencent.ggame_1.7.4_174.apk&csr=81e7"
                url = "https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF"
                downloadToPublic = false
                privateStoreFile = File(filesDir, "download/test.apk")
                publicStoreFileName = "test.apk"
                publicPrimaryDir = "$packageName.download"
                targetPublicDir = PublicDirectoryType.DOWNLOADS
                downloadIfFileExists = true
                onDownloadProgressChange = { logs("progress=", it) }
                onDownloadCompleted = { toast("download finished:$it") }
                onDownloadFailed = { logs("download failed", it) }
                onDownloadSpeed = { bytes, elapsedTime, contentLength ->
                    val speed = bytes / elapsedTime
                    val remain = (contentLength - bytes) / speed
                    logs("bytes=", bytes, "spend=", elapsedTime, "speed(/s)=", (speed * 1000).formatFileSize(), "remain=", remain.formatDuration())
                }
            }
        }

//        mRequestJob?.cancel()
//        mRequestJob = launch(Dispatchers.IO) {
//            workOnMain { toast("start download") }
//            DownloadHelper.instance(this@HttpDemoActivity).simpleDownload {
//                downloadUrl = "https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF"
//
//                storedFilePath = File(Environment.DIRECTORY_PICTURES, "$packageName/a.jpg").absolutePath
//
//                qWrapper = QWrapper("a.jpg", "${Environment.DIRECTORY_PICTURES}/$packageName", downloadType = PublicDirectoryType.PICTURES)
//
//                onProgressChange = { ePrint { "progress: $it" } }
//
//                onDownloadFinished = { workOnMain { toast("download finished") } }
//
//                onDownloadFailed = { workOnMain { toast("download failed") } }
//            }
//        }
    }

    private fun requestByHttp() {
        mRequestJob?.cancel()
        mRequestJob = launch(Dispatchers.IO) {
//            // dsl
//            http {
//                baseUrl = "https://github.com/kukyxs"
//
//                onSuccess = {
//                    workOnMain {
//                        mBinding.renderResult.text = it.checkText().renderHtml().toString()
//                        mBinding.requestCode = RequestStatusCode.Succeed
//                    }
//                }
//
//                onFail = {
//                    mBinding.requestCode = RequestStatusCode.Error
//                }
//            }

            // void
            Http().url("https://github.com/kukyxs")
                .catch {
                    mBinding.requestCode = RequestStatusCode.Error
                }
                .onResponse<String> {
                    workOnMain {
                        mBinding.renderResult.text = it?.renderHtml()
                        mBinding.requestCode = RequestStatusCode.Succeed
                    }
                }
                .get()

//            // get result
//            val resp = Http().url("https://github.com/kukyxs")
//                .catch {
//                    mBinding.requestCode = RequestStatusCode.Error
//                }
//                .get<String>()
//
//            workOnMain {
//                mBinding.renderResult.text = resp?.renderHtml()?.toString()
//                mBinding.requestCode = RequestStatusCode.Succeed
//            }
        }
    }

    private fun requestByRetrofit() {
        mRequestJob?.cancel()
        mRequestJob = covLaunch(Dispatchers.IO,
            { _, throwable ->
                workOnMain {
                    mBinding.requestCode = RequestStatusCode.Error
                    longToast(throwable.message ?: "")
                }
            },
            {
                val result = createService<ApiService>().requestRepositoryInfo()

                workOnMain {
                    mBinding.requestCode = (result.errorCode == 0).yes {
                        mBinding.renderResult.text = result.toString()
                        RequestStatusCode.Succeed
                    }.otherwise { RequestStatusCode.Error }

                    workOnIO {
                        val tops = createService<ApiService>().requestTop("ef69c9ea662b4ca4ac768d4f70b921af")
                        logs(tops)
                    }
                }
            })
    }
}