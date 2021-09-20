package dev.iaiabot.furufuru.feature.ui.issue

import android.app.Application
import androidx.lifecycle.*
import dev.iaiabot.furufuru.usecase.GetScreenShotUseCase
import dev.iaiabot.furufuru.usecase.PostIssueUseCase
import dev.iaiabot.furufuru.usecase.user.LoadUserNameUseCase
import dev.iaiabot.furufuru.usecase.user.SaveUsernameUseCase
import dev.iaiabot.furufuru.util.GithubSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class IssueViewModel(
    application: Application,
    private val saveUsernameUseCase: SaveUsernameUseCase,
    private val loadUserNameUseCase: LoadUserNameUseCase,
    private val githubSettings: GithubSettings,
    private val postIssueUseCase: PostIssueUseCase,
    getScreenShotUseCase: GetScreenShotUseCase,
) : AndroidViewModel(
    application
), LifecycleObserver {
    val title = MutableLiveData("")
    val body = MutableLiveData("")
    val userName = liveData {
        emit(loadUserNameUseCase())
    }
    val nowSending = MutableLiveData(false)
    val fileStr = getScreenShotUseCase().asLiveData()
    val labels = liveData {
        emit(githubSettings.labels)
    }
    private val command = MutableLiveData<Command>()
    private val selectedLabels = mutableListOf<String>()

    fun onCheckedChangeLabel(isChecked: Boolean, label: String) {
        if (isChecked) {
            selectedLabels.add(label)
        } else {
            selectedLabels.remove(label)
        }
    }

    fun post() {
        val title = title.value ?: return
        if (title.isEmpty()) return
        val body = body.value ?: return
        val userName = userName.value ?: ""

        viewModelScope.launch(Dispatchers.IO) {
            saveUsernameUseCase(userName)
        }

        if (nowSending.value == true) {
            return
        }
        nowSending.postValue(true)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                postIssueUseCase(title, userName, body, selectedLabels)
                command.postValue(Command.Finish)
            } catch (e: Exception) {
                command.postValue(Command.Error(e.message ?: "error"))
            } finally {
                nowSending.postValue(false)
            }
        }
    }
}

// FIXME: Contract作る
internal sealed class Command {
    object Finish : Command()
    object ShowFilePath : Command()
    class Error(val errorMessage: String) : Command()
}
