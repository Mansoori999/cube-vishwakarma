package com.vinners.trumanms.feature.auth.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import com.vinners.trumanms.data.models.LoggedInUser
import com.vinners.trumanms.data.sessionManagement.UserSessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class AuthViewModelTest {

    companion object {
        private val LOGGED_IN_USER = LoggedInUser(
            sessionToken = "token",
            displayName = "Name",
            email = "test@gmail.com",
            mobileNumber = "98823928333"
        )
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule =
        CoroutinesTestRule()

    @Mock
    lateinit var userSessionManager: UserSessionManager

    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        this.viewModel = AuthViewModel(
            this.userSessionManager
        )
    }

    @Test
    fun isUserLoggedIn() = runBlocking {

        //Testing Get User Logged In
        whenever(userSessionManager.getLoggedInUser())
            .thenReturn(LOGGED_IN_USER)
        assert(viewModel.isUserLoggedIn())

        //Testing Get User Not Logged In
        whenever(userSessionManager.getLoggedInUser())
            .thenReturn(null)
        assert(viewModel.isUserLoggedIn().not())
    }

    @Test()
    fun getLoggedInUser_user_logged_in() = runBlocking {
        whenever(userSessionManager.getLoggedInUser())
            .thenReturn(LOGGED_IN_USER)
        assert(viewModel.getLoggedInUser() == LOGGED_IN_USER)
    }

    @Test(expected = IllegalStateException::class)
    fun getLoggedInUser_user_not_logged_in() = runBlocking {
        whenever(userSessionManager.getLoggedInUser())
            .thenReturn(null)

        viewModel.getLoggedInUser()
        assert(true)
    }

}