import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.navigation.NavController
import com.romkapo.todoapp.R
import com.romkapo.todoapp.di.components.common.daggerViewModel
import com.romkapo.todoapp.presentation.screen.auth.AuthViewModel
import com.romkapo.todoapp.presentation.screen.auth.YandexSignInActivityResultContract
import com.romkapo.todoapp.ui.theme.Black
import com.romkapo.todoapp.ui.theme.Typography
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = daggerViewModel()
) {
    val sdk = YandexAuthSdk(LocalContext.current, YandexAuthOptions(LocalContext.current))
    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(YandexSignInActivityResultContract()) { pair ->
            val token = sdk.extractToken(pair.first, pair.second)
            try {
                if (token != null) {
                    viewModel.putToken(token.value)
                    navController.navigate("todo_list")
                } else {
                    cantAuthToast(context)
                }
            } catch (e: YandexAuthException) {
                cantAuthToast(context)
            }
        }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = stringResource(id = R.string.auth), style = Typography.titleLarge)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedButton(
                modifier = Modifier.size(298.dp, 44.dp),
                onClick = { navController.navigate("todo_list") }) {
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = stringResource(id = R.string.auth_guest)
                )
            }
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .width(270.dp)
                    .padding(vertical = 16.dp)
            ) {
                Divider()
                val color = MaterialTheme.colorScheme.background
                Text(
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(0.2f)
                        .drawBehind { drawRect(color = color) },
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.or)
                )
            }
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Black),
                modifier = Modifier.size(298.dp, 44.dp),
                onClick = { launcher.launch(sdk) }) {

                Image(
                    painter = painterResource(id = R.drawable.yandex_logo),
                    contentDescription = "icon",
                    modifier = Modifier
                        .size(40.dp)
                )
                Text(
                    text = "Войти с Яндекс ID",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 1.43.em,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )

            }
        }
    }
}


private fun cantAuthToast(context: Context) {
    Toast.makeText(
        context,
        context.getText(R.string.failed_to_login),
        Toast.LENGTH_LONG,
    ).show()
}