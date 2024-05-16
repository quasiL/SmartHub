package cz.cvut.smarthub.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ShowErrorScreen(
    errorMessage: String = "Error",
    onClick : () -> Unit = { },
    innerPaddings: PaddingValues = PaddingValues(0.dp)
){
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddings)
            .clickable { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center
        ){
            Text(text = errorMessage)
        }
    }
}

@Composable
fun ShowLoadingScreen(
    innerPaddings: PaddingValues = PaddingValues(0.dp)
){
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPaddings)
    ) {
        CircularProgressIndicator()
    }
}