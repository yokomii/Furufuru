package dev.iaiabot.furufuru.feature

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Bundle
import android.view.View
import dev.iaiabot.furufuru.data.FURUFURU_BRANCH
import dev.iaiabot.furufuru.data.GITHUB_API_TOKEN
import dev.iaiabot.furufuru.data.GITHUB_REPOSITORY
import dev.iaiabot.furufuru.data.GITHUB_REPOSITORY_OWNER
import dev.iaiabot.furufuru.di.apiModule
import dev.iaiabot.furufuru.di.repositoryModule
import dev.iaiabot.furufuru.di.useCaseModule
import dev.iaiabot.furufuru.di.viewModelModule
import dev.iaiabot.furufuru.feature.service.SensorService
import dev.iaiabot.furufuru.feature.ui.issue.IssueActivity
import dev.iaiabot.furufuru.feature.utils.screenshot.ScreenShotter
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.inject

class Furufuru(private val application: Application) {

    companion object {
        private const val DEFAULT_FURUFURU_BRANCH = "furufuru-image-branch"
        private var instance: Furufuru? = null

        fun builder(
            application: Application,
            githubApiToken: String,
            githubReposOwner: String,
            githubRepository: String,
            furufuruBranch: String? = null
        ): Furufuru {
            GITHUB_API_TOKEN = githubApiToken
            GITHUB_REPOSITORY_OWNER = githubReposOwner
            GITHUB_REPOSITORY = githubRepository
            FURUFURU_BRANCH = furufuruBranch ?: DEFAULT_FURUFURU_BRANCH
            return Furufuru(application).also {
                instance = it
            }
        }

        internal fun takeScreenshot() {
            getInstance()?.takeScreenshot()
        }

        internal fun getApplicationName() = getInstance()?.getApplicationName()
        internal fun getAppVersionName() = getInstance()?.getApplicationVersion()

        private fun getInstance(): Furufuru? {
            return instance
        }
    }

    private var sensorServiceConnection = SensorService.Connection()

    init {
        startKoin {
            androidLogger()
            androidContext(application)
            modules(
                listOf(
                    viewModelModule,
                    apiModule,
                    repositoryModule,
                    useCaseModule
                )
            )
        }
    }

    fun build() {
        application.registerActivityLifecycleCallbacks(applicationLifecycleCallbacks)
    }

    fun getApplicationName(): String? {
        val applicationInfo = application.applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else application.getString(
            stringId
        )
    }

    fun getApplicationVersion(): String {
        val pInfo: PackageInfo =
            application.packageManager.getPackageInfo(
                application.packageName, 0
            )
        return pInfo.versionName
    }

    fun takeScreenshot() {
        applicationLifecycleCallbacks.takeScreenshot()
    }

    private fun bindSensorService(activity: Activity) {
        Intent(activity, SensorService::class.java).also { intent ->
            activity.bindService(intent, sensorServiceConnection, Service.BIND_AUTO_CREATE)
        }
    }

    private fun unbindSensorService(activity: Activity) {
        activity.unbindService(sensorServiceConnection)
    }

    private val applicationLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        private var currentActivity: Activity? = null
        private val screenShotter by inject(ScreenShotter::class.java)

        fun takeScreenshot() {
            val activity = currentActivity ?: return
            screenShotter.takeScreenshot(
                activity,
                activity.window,
                activity.window.decorView.findViewById<View>(android.R.id.content).rootView
            )
        }

        override fun onActivityResumed(activity: Activity) {
            if (activity is IssueActivity) {
                return
            }
            currentActivity = activity
            bindSensorService(activity)
        }

        override fun onActivityPaused(activity: Activity) {
            if (activity is IssueActivity) {
                return
            }
            currentActivity = null
            unbindSensorService(activity)
        }

        override fun onActivityDestroyed(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityStarted(activity: Activity) {
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        }
    }
}
