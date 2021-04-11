package com.vinners.trumanms.feature.auth.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vinners.trumanms.feature.auth.ui.CoroutinesTestRule
import com.vinners.trumanms.feature.auth.ui.TestLogger
import com.nhaarman.mockitokotlin2.*
import com.vinners.trumanms.core.TaskState
import com.vinners.trumanms.core.analytics.AnalyticsHelper
import com.vinners.trumanms.data.models.LoggedInUser
import com.vinners.trumanms.data.sessionManagement.UserSessionManager
import com.vinners.trumanms.feature.auth.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(JUnit4::class)
class LoginViewModelTest {

    companion object {
        private const val INVALID_EMAIL = "invalidemail.com"
        private const val INVALID_PASSWORD = "" //Empty String

        private const val VALID_EMAIL = "valid@email.com"
        private const val VALID_NON_EXISTENT_EMAIL = "validNonExistent@email.com"
        private const val VALID_PASSWORD = "ValidPassword"

        private val LOGGED_IN_USER = LoggedInUser(
            sessionToken = "cjnsuec",
            displayName = "Harami",
            email = "Ddd"
        )
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule =
        CoroutinesTestRule()


    //Mocks
    @Mock
    lateinit var userSessionManager: UserSessionManager

    @Mock
    lateinit var analyticsHelper: AnalyticsHelper

    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        this.viewModel = LoginViewModel(
            this.userSessionManager,
            TestLogger(),
            analyticsHelper
        )
    }

    @Test
    fun test_invalid_email() {
        viewModel.login(
            email = INVALID_EMAIL,
            password = VALID_PASSWORD
        )

        assert(viewModel.loginFormState.value!!.usernameError == R.string.invalid_username)
    }

    @Test
    fun test_invalid_password() {
        viewModel.login(
            email = VALID_EMAIL,
            password = INVALID_PASSWORD
        )

        assert(viewModel.loginFormState.value!!.passwordError == R.string.invalid_password)
    }

//    @Test
//    fun test_loginWithEmailAndPasswordSuccess() = runBlocking {
//
//        doAnswer {
//            AnswersWithDelay(
//                1000,
//                Returns(
//                    LOGGED_IN_USER
//                )
//            )
//        }.whenever(userSessionManager)
//            .loginWithEmailAndPassword(
//                email = VALID_EMAIL,
//                password = VALID_PASSWORD
//            )
//
//        println(viewModel.loginResult.value)
//
//        viewModel.login(
//            email = VALID_EMAIL,
//            password = VALID_PASSWORD
//        )
//
//        println(viewModel.loginResult.value)
//
//        verify(userSessionManager, times(1))
//            .loginWithEmailAndPassword(
//                email = VALID_EMAIL,
//                password = VALID_PASSWORD
//            )
//
//        // No validation Error
//        assert(viewModel.loginFormState.value == null)
//        println(viewModel.loginResult.value)
//        assert(viewModel.loginResult.value == TaskState.Loading)
//
//
//
//        delay(150)
//
//        assert(viewModel.loginResult.value is TaskState<String>)
//        assert((viewModel.loginResult.value as TaskState.Success<String>).item.equals("Login Successful"))
//    }

    @Test
    fun test_loginWithEmailAndPasswordFailure() = runBlocking {

        whenever(
            userSessionManager.loginWithEmailAndPassword(
                email = VALID_NON_EXISTENT_EMAIL,
                password = VALID_PASSWORD
            )
        ).doThrow(IllegalArgumentException("Invalid Email"))

        viewModel.login(
            email = VALID_NON_EXISTENT_EMAIL,
            password = VALID_PASSWORD
        )

        assert(viewModel.loginFormState.value == null)
//        assert(viewModel.loginResult.value == TaskState.Loading)

        verify(userSessionManager, times(1))
            .loginWithEmailAndPassword(
                email = VALID_NON_EXISTENT_EMAIL,
                password = VALID_PASSWORD
            )

        assert(viewModel.loginResult.value is TaskState.Error)
        assert(
            (viewModel.loginResult.value as TaskState.Error).error.equals(
                "Invalid Email"
            )
        )
    }


}
