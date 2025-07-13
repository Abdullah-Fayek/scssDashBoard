package com.fola.scss

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fola.data.repositryImp.AuthRepositoryImp
import com.fola.data.session.UserSession
import com.fola.domain.repo.AuthRepository
import com.fola.scss.auth.models.LoginViewModel
import com.fola.scss.auth.ui.LoadingScreen
import com.fola.scss.auth.ui.LoginScreen
import com.fola.scss.basic.BasicScreen
import com.fola.scss.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val tag = "MainActivity"

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        UserSession.init(applicationContext)

        setContent {
            val isLoggedInState = UserSession.isLoggedIn().collectAsState(initial = false)
            val isRefreshing = remember { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                withContext(Dispatchers.IO) {
                    UserSession.refreshKey()
                    isRefreshing.value = false
                }
            }

            AppTheme {
                when {
                    isRefreshing.value -> LoadingScreen()
                    isLoggedInState.value -> BasicScreen()
                    else -> {
                        val repo = AuthRepositoryImp()
                        val factory = LoginViewModelFactory(repo)
                        val loginViewModel: LoginViewModel = viewModel(factory = factory)
                        LoginScreen(viewmodel = loginViewModel)
                    }
                }
            }
        }
    }
}

class LoginViewModelFactory(
    private val userAuthRepository: AuthRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userAuthRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTheme {
        Greeting("Android")
    }
}