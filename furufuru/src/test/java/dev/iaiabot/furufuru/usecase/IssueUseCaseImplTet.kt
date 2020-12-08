package dev.iaiabot.furufuru.usecase

import com.google.common.truth.Truth.assertThat
import dev.iaiabot.furufuru.data.repository.ContentRepository
import dev.iaiabot.furufuru.data.repository.IssueRepository
import dev.iaiabot.furufuru.data.repository.ScreenshotRepository
import dev.iaiabot.furufuru.util.FurufuruSettings
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal object IssueUseCaseImplTet : Spek({
    lateinit var usecase: IssueUseCase
    lateinit var issueRepository: IssueRepository
    lateinit var screenshotRepository: ScreenshotRepository
    lateinit var contentRepository: ContentRepository
    lateinit var furufuruSettings: FurufuruSettings

    describe("#getScreenShot()") {
        context("1発で取得出来る場合") {
            beforeGroup {
                screenshotRepository = mockk(relaxed = true) {
                    every { get() } returns "SCREEN_SHOT"
                }
                usecase = IssueUseCaseImpl(
                    issueRepository,
                    screenshotRepository,
                    contentRepository,
                    furufuruSettings
                )
            }

            it("null以外が返る") {
                val result = runBlocking {
                    usecase.getScreenShot()
                }
                assertThat(result).isNotEmpty()
            }
        }

        context("3回とも取り出せない場合") {
            beforeGroup {
                screenshotRepository = mockk(relaxed = true) {
                    every { get() } returns null
                }
                usecase = IssueUseCaseImpl(
                    issueRepository,
                    screenshotRepository,
                    contentRepository,
                    furufuruSettings
                )
            }

            it("nullが返る") {
                val result = runBlocking {
                    usecase.getScreenShot()
                }
                assertThat(result).isNotEmpty()
            }
        }
    }
})
