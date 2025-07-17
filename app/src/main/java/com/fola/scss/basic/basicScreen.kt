package com.fola.scss.basic

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Dehaze
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.fola.data.remote.dto.ProfileDTO
import com.fola.data.session.UserSession
import com.fola.domain.model.User
import com.fola.scss.assests.icons.PersonCircle
import com.fola.scss.main.MainApp
import com.fola.scss.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    SideDrawerColumn(
                        modifier = modifier,
                        onMenuClick = {
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("test") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        }) {
                            Icon(
                                Icons.Default.Dehaze,
                                contentDescription = ""
                            )
                        }
                    }
                )

            }
        ) { innerPadding ->
            MainApp(modifier = Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun SideDrawerColumn(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .width(280.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background),
    ) {
        UserSection(onClick = onMenuClick)

        Spacer(modifier = Modifier.weight(1f))

        LogOutSection(modifier = modifier)
    }
}

@Composable
fun LogOutSection(modifier: Modifier) {

    Row(
        modifier.fillMaxWidth()
    ) {
        FilledTonalButton(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    UserSession.clearSession()
                }
            },
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(.2f),
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ),
            shape = RectangleShape,
            modifier = modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.Logout,
                contentDescription = "Logout",
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Text(
                text = "Logout",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }

}
@Preview(showBackground = true)
@Composable
private fun LogoutButtonPrev() {
    AppTheme(darkTheme = false) {
        SideDrawerColumn(modifier = Modifier)
    }
}

@Composable
fun UserSection(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val user = UserSession.getUser()
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.tertiaryContainer.copy(.1f))
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = PersonCircle,
                contentDescription = "User Icon",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .size(52.dp)
                    .fillMaxWidth()
            )
            IconButton(onClick = onClick)
            {
                Icon(
                    imageVector = Icons.Default.Dehaze,
                    contentDescription = "Menu Icon",

                    )
            }
        }

        Text(
            text = "${user.firstName} ${user.lastName}",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = user.userName,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }

}


@Preview
@Composable
private fun BasicPrev() {
    AppTheme(darkTheme = true) {
        BasicScreen()
    }
}