package com.example.feature

import android.app.Application
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.example.data.FURUFURU_BRANCH
import com.example.data.GITHUB_REPOSITORY
import com.example.data.GITHUB_REPOSITORY_OWNER
import com.example.feature.service.SensorService
import com.example.feature.ui.prepare.PrepareActivity


class Furufuru(private val application: Application) {
    companion object {
        const val DEFAULT_FURUFURU_BRANCH = "furufuru-image-branch"
        fun builder(
            application: Application,
            githubReposOwner: String,
            githubRepository: String,
            furufuruBranch: String? = null
        ): Furufuru {
            GITHUB_REPOSITORY_OWNER = githubReposOwner
            GITHUB_REPOSITORY = githubRepository
            FURUFURU_BRANCH = furufuruBranch ?: DEFAULT_FURUFURU_BRANCH
            return Furufuru(application)
        }

        fun openIssue(context: Context) {
            PrepareActivity.createIntent(context).run {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(this)
            }
        }
    }

    fun build() {
        Intent(application, SensorService::class.java).also { intent ->
            application.bindService(intent, serviceConnection, Service.BIND_AUTO_CREATE)
        }
    }

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) { }
        override fun onServiceDisconnected(name: ComponentName?) { }
    }
}
