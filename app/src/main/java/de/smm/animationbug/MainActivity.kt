package de.smm.animationbug

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import de.smm.animationbug.ui.theme.AnimationBugTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimationBugTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}
const val animationDuration = 2000

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(
        navController = navController,
        startDestination = "1"
    ) {
        composable("1",
          ) {
            FirstScreen(navController)
        }
        composable("2",
            enterTransition =
            {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Up,
                    animationSpec = tween(animationDuration)
                )
            },
            exitTransition =
            {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Down,
                    animationSpec = tween(animationDuration)
                )
            }) {
            SecondScreen()
        }
    }

}

@Composable
fun FirstScreen(navController: NavController) {
    Surface(color = Color.Green) {
        Button(onClick = { navController.navigate("2") }) {
            Text(text = "GoToScreen")
        }
    }

}

@Composable
fun SecondScreen() {
    Column(modifier = Modifier.fillMaxWidth()) {

        val focusRequester = remember { FocusRequester() }
        val textState = remember { mutableStateOf(TextFieldValue()) }

        TextField( value = textState.value,
            onValueChange = { value : TextFieldValue ->
                textState.value = value
            },
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth()
        )

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        LazyColumn() {
            (0..100).forEach { number ->
                item {
                    Column() {
                        Text(text = "Field: $number", modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp))
                        Spacer(modifier = Modifier
                            .height(2.dp)
                            .background(color = Color.Red))
                    }
                    
                }
            }
        }
        
        
    }
}

