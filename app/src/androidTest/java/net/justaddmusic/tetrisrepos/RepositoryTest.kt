package net.justaddmusic.tetrisrepos

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import net.justaddmusic.tetrisrepos.di.DaggerAppComponent
import net.justaddmusic.tetrisrepos.model.SearchResult
import net.justaddmusic.tetrisrepos.repository.GitHubRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RepositoryTest {

    @Test
    fun useSearchPagination() {
        val appContext = InstrumentationRegistry.getTargetContext()

        val repo = DaggerAppComponent
                .builder()
                .context(appContext)
                .build()
                .gitHubRepository()

        val results: MutableList<SearchResult> = mutableListOf()

        for (index in 1..10)
            paginationStep(repo, index, results)

        assertEquals(100, results.size)
    }

    private fun paginationStep(repo: GitHubRepository, stepIndex: Int, accumulateResults: MutableList<SearchResult>) {
        val response = repo.search("tetris", 10, stepIndex).execute().body()
        assertNotNull(response)
        assertEquals(10, response?.items?.size)
        accumulateResults.addAll(response?.items ?: listOf())
    }
}
