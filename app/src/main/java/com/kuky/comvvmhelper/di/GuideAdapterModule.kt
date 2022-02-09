package com.kuky.comvvmhelper.di

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.kuky.comvvmhelper.entity.GuideDisplay
import com.kuky.comvvmhelper.ui.activity.*
import com.kuky.comvvmhelper.ui.adapter.GuideAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import java.util.*

@Module
@InstallIn(ActivityComponent::class)
class GuideAdapterModule {
    private val mRandom = Random()

    private fun randomDrawable() = ColorDrawable(
        Color.parseColor(
            String.format("#%06x", mRandom.nextInt(256 * 256 * 256))
        )
    )

    @Provides
    fun provideGuideItems() = mutableListOf(
        GuideDisplay("Network", randomDrawable(), HttpDemoActivity::class.java, true, "Show Download"),
        GuideDisplay("ImageDisplay", randomDrawable(), ImageDisplayActivity::class.java),
        GuideDisplay("PermissionRequest", randomDrawable(), PermissionDemoActivity::class.java),
        GuideDisplay("RecyclerViewList", randomDrawable(), RecyclerViewDemoActivity::class.java, true, "Multi Layout"),
        GuideDisplay("MultiManagerDisplay", randomDrawable(), MultiItemDisplayActivity::class.java)
    )

    @Provides
    fun provideGuideAdapter(items: MutableList<GuideDisplay>) = GuideAdapter(items)
}