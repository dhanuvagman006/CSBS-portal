package com.dhanuvagman.csbssportal.pages

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhanuvagman.csbssportal.R
import com.dhanuvagman.csbssportal.ui.theme.BlueViolet3
import com.dhanuvagman.csbssportal.ui.theme.LightRed
import com.dhanuvagman.csbssportal.ui.theme.TextWhite
import com.dhanuvagman.csbssportal.ui.theme.inriaSansRegular

@Composable
fun Quizes(qSubect:String){
Column(modifier=Modifier.fillMaxSize().background(BlueViolet3)){
    Row(Modifier.padding(top=40.dp)){
        Text(text="Question: 7/20",modifier=Modifier.fillMaxWidth(1f))
        Icon(painter = painterResource(id = R.drawable.quiz), contentDescription ="Todays Quiz play",tint=Color.White, modifier = Modifier.size(16.dp) )
    }

}
}
