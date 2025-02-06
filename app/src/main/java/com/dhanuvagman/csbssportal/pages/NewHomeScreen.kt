package com.dhanuvagman.csbssportal.pages

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dhanuvagman.csbssportal.AuthViewModel
import com.dhanuvagman.csbssportal.BottomMenuContent
import com.dhanuvagman.csbssportal.NewGrid
import com.dhanuvagman.csbssportal.R
import com.dhanuvagman.csbssportal.model.*
import com.dhanuvagman.csbssportal.ui.theme.*
import kotlinx.coroutines.launch
import java.util.Calendar
import com.dhanuvagman.csbssportal.ui.theme.SimpleDialog as SimpleDialog

@Composable
fun NewHomeScreen(authViewModel: AuthViewModel) {
    var selectedMenuIndex by remember { mutableIntStateOf(0) }
    var context= LocalContext.current
    BackHandler{
        (context as? Activity)?.finish()
    }
    Box(modifier= Modifier
        .background(DeepBlue)
        .fillMaxSize()
    ){
        when (selectedMenuIndex) {
            0 -> Homepage2(context)
            1 -> NoticeScreen()
            2 -> ExploreScreen()
            3 -> ProfileScreen(authViewModel)
        }
        BottomMenu(items = listOf(
            BottomMenuContent("Home", R.drawable.home),
                    BottomMenuContent("Notice", R.drawable.notice),
                BottomMenuContent("Explore", R.drawable.explore),
            BottomMenuContent("Profile", R.drawable.profile)
        ),modifier=Modifier.align(Alignment.BottomCenter),initialSelectionItemIndex = selectedMenuIndex
        ){
                index ->
            selectedMenuIndex = index
        }
    }
}

@Composable
fun ExploreScreen() {
    Column(modifier= Modifier
        .fillMaxSize()
        .padding(top = 60.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top){

    }
}

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
) {
    val context=LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    val username=sharedPreferences.getString("username","Not Found")
    val usn=sharedPreferences.getString("usn","Not Found")
    val email=sharedPreferences.getString("email","Not Found")
    var imageUri by remember { mutableStateOf<String?>("https://img.freepik.com/free-photo/portrait-smiling-boy-helmet-sunglasses-3d-rendering_1142-41369.jpg") }
    val passoutyear= "20"+((usn?.substring(3,5)?.toInt() ?: 0) +4).toString()

    LaunchedEffect(Unit) {
        val storedImageUri = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            .getString("imgurl", null)
        imageUri = storedImageUri ?: imageUri // Update imageUri if stored value exists
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            imageUri = it.toString()
            sharedPreferences.edit().putString("imgurl",imageUri).apply()
            Toast.makeText(context,"Profile Uploaded SuccesFully",Toast.LENGTH_SHORT).show()
        }
    }
    Column(modifier= Modifier
        .fillMaxSize()
        .padding(top = 60.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top){
        Row(modifier= Modifier
            .fillMaxWidth()
            .padding(5.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
            Image(painter = painterResource(R.drawable.signout), contentDescription ="Sign out" ,modifier = Modifier
                .clickable {
                    authViewModel.signout()
                    Toast
                        .makeText(context, "You Have been Logged out!", Toast.LENGTH_SHORT)
                        .show()
                }
                .size(90.dp)
                .padding(18.dp)
                .clip(
                    RoundedCornerShape(50.dp)
                )
                .background(darkback1)
                .padding(10.dp))
            Spacer(modifier = Modifier.fillMaxWidth(0.1f))
            Image(painter = painterResource(R.drawable.layer_1), contentDescription =null,
                modifier = Modifier
                    .size(90.dp)
                    .padding(18.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(darkback1)
                    .padding(10.dp)
            )
        }

        //image upload section
        AsyncImage(model = sharedPreferences.getString("imgurl",null)
                ?: imageUri, contentDescription = null , modifier = Modifier
            .clickable {
                launcher.launch("image/*")
            }
            .size(185.dp)
            .clip(RoundedCornerShape(80.dp))
            .border(2.dp, darkgrey, shape = RoundedCornerShape(80.dp)))
        Text(
            text = username.toString(),fontFamily = inriaSansRegular,color=Color.White, fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = 30.dp)
                .wrapContentSize(Alignment.Center)
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, darkgrey, shape = RoundedCornerShape(10.dp))
                .background(whitetrans)
                .padding(10.dp),
        )
        Spacer(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(7.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)){
            Box(modifier= Modifier
                .padding(10.dp)
                .height(55.dp)
                .width(75.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, blacktrans, shape = RoundedCornerShape(10.dp))
                .background(darkgrey),Center){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally, // Center texts horizontallyverticalArrangement = Arrangement.spacedBy(4.dp) // Add spacing between texts
                ) {
                    usn?.substring(3,10)
                        ?.let { Text(text = it, fontSize = 14.sp,color = Color.LightGray, fontFamily = inriaSansRegular) }
                    Text(text = "USN", fontSize = 16.sp, fontWeight = FontWeight.Bold,color = Color.LightGray, fontFamily = inriaSansRegular)
                }
            }

            Box(modifier= Modifier
                .padding(10.dp)
                .width(180.dp)
                .height(55.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, blacktrans, shape = RoundedCornerShape(10.dp))
                .background(darkgrey),Center){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally, // Center texts horizontallyverticalArrangement = Arrangement.spacedBy(4.dp) // Add spacing between texts
                ) {
                    Text(text = email.toString(), fontSize = 14.sp, color = Color.LightGray, fontFamily = inriaSansRegular)
                    Text(text = "email", fontSize = 16.sp, fontWeight = FontWeight.Bold,color = Color.LightGray, fontFamily = inriaSansRegular)
                }
            }

            Box(modifier= Modifier
                .padding(10.dp)
                .height(55.dp)
                .width(75.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, blacktrans, shape = RoundedCornerShape(10.dp))
                .background(darkgrey),Center){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally, // Center texts horizontallyverticalArrangement = Arrangement.spacedBy(4.dp) // Add spacing between texts
                ) {
                    Text(text = passoutyear, fontSize = 14.sp,color = Color.LightGray, fontFamily = inriaSansRegular)
                    Text(text = "Passout", fontSize = 16.sp, fontWeight = FontWeight.Bold,color = Color.LightGray, fontFamily = inriaSansRegular)

                }
            }
        }
        Spacer(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(7.dp))
        Row(){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, // Center texts horizontallyverticalArrangement = Arrangement.spacedBy(4.dp) // Add spacing between texts
            ) {
            Text(text = "Achivements", color = Color.LightGray, fontSize = 25.sp)
                Spacer(modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(8.dp))
            Text(text = "No Achivements to Display", color = Color.White, fontSize = 15.sp, modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(darkback1)
                .padding(10.dp))
            }}
        Spacer(modifier = Modifier
            .fillMaxWidth(1f)
            .padding(17.dp))
        Row(){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, // Center texts horizontallyverticalArrangement = Arrangement.spacedBy(4.dp) // Add spacing between texts
            ) {
                Text(text="FeedBack on Application", fontSize = 22.sp, fontFamily = inriaSansRegular, fontWeight = FontWeight.W100, color = Color.Yellow)
                Text(text="We value your feedback to help us enhance this app",color=Color.LightGray, fontSize = 10.sp)
            }}
        var feedbackText by remember { mutableStateOf("") }
        val context1=LocalContext.current
        var errorstate by remember { mutableStateOf(false)}
        var feedtextholder by remember {
            mutableStateOf("Feedback")
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
        ) {
            OutlinedTextField(
                value = feedbackText,
                enabled = true,
                onValueChange = { feedbackText = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Write your feedback here...", color = Color.Gray) },
                singleLine = errorstate,
                label = { Text(text = feedtextholder) },
                isError = errorstate,
                textStyle = androidx.compose.ui.text.TextStyle(color = Color.White,
                    fontSize = 17.sp,
                    fontFamily = inriaSansRegular)
            )
            Box(
                modifier= Modifier
                    .padding(20.dp)
                    .border(1.dp, Color.White, RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        if (feedbackText == "") {
                            errorstate = true
                            feedtextholder = "Fedback Cannot Be Empty!"
                        } else {
                            feedtextholder = "FeedBack"
                            errorstate = false
                            uploadfeedback(feedbackText)
                            feedbackText = ""
                            Toast
                                .makeText(
                                    context1,
                                    "Thankyou For Your Feedback",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    },
            ) {
                Text(text = "Submit Feedback",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontFamily = inriaSansRegular,
                    modifier = Modifier.padding(vertical=7.dp,
                        horizontal = 10.dp))
            }
        }

    Row(){
      Text(text="Developed and Managed By NEXUS", fontFamily = inriaSansRegular, fontSize=10.sp,color=Color.LightGray, modifier = Modifier.padding(top=11.dp))
    }}
    }

@Composable
fun NoticeScreen() {
    val context= LocalContext.current
    Column(modifier= Modifier
        .fillMaxSize()
        .padding(top = 60.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top){
        Row(modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(60.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(BlueViolet2)
            .padding(9.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(id = R.drawable.layer_1),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(34.dp)
            )
            Text(
                text = "Notification",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth(0.7f)
            )
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp))
        Box(
            modifier = Modifier
                .padding(vertical = 15.dp)
                .border(width = 1.dp, Grey40, shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(whitetrans)
                .padding(horizontal = 20.dp, vertical = 10.dp)

        ) {
            Text(
                text = "Events",color = Color.White,
                fontSize = 22.sp
            )
        }

        var events by remember { mutableStateOf<List<Event>>(emptyList()) }

        LaunchedEffect(Unit) {
            events = getAllEvents()
        }
        LazyRow(content = {
            itemsIndexed(events.reversed()) { _, item ->
                NoticeClm(text1 = item.EventName, text2 = item.Edate)
            }
        })
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp))

        var notices by remember { mutableStateOf<List<NoticeS>>(emptyList()) }
        LaunchedEffect(Unit) {
            notices = getAllNotices()
        }
        Box(
            modifier = Modifier
                .padding(vertical = 20.dp)
                .border(width = 1.dp, Grey40, shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(whitetrans)
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Text(
                text = "Notice Section",color = Color.White,
                fontSize = 22.sp
            )
        }
        LazyRow(content = {
            itemsIndexed(notices.reversed()) { _, item ->
                NoticeClm(text1 = item.NoticeName, text2 = item.NoticeBy)
            }
        })
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp))

        var misc by remember { mutableStateOf<List<Misc>>(emptyList()) }
        LaunchedEffect(Unit) {
            misc = getAllmisc()
        }
        Box(
            modifier = Modifier
                .padding(vertical = 20.dp)
                .border(width = 1.dp, Grey40, shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(14.dp))
                .background(whitetrans)
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Text(
                text = "App Updates",color = Color.White,
                fontSize = 22.sp
            )
        }
        val lazyListState: LazyListState = rememberLazyListState()
        LazyRow(
            state = rememberLazyListState(), // Remember the list state
            flingBehavior = rememberSnapFlingBehavior(SnapLayoutInfoProvider(lazyListState)), // Add snapping behavior
            content = {
                itemsIndexed(misc.reversed()) { _, item ->
                    NoticeClm(text1 = item.NoticeName, text2 = "By:"+item.NoticeBy)
                }
            }
        )



}
    Toast.makeText(context,"Fetching from Database",Toast.LENGTH_LONG).show()
}

@Composable
fun NoticeClm(text1: String, text2: String) {
    Box(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .width(325.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(darkback1)
            .padding(15.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = text1,
                fontSize = 16.sp,
                fontFamily = inriaSansRegular,
                color = Color.White,
                modifier = Modifier.padding(top = 10.dp)
            )
            Text(
                text = text2,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.DarkGray)
                    .padding(10.dp),
                color = Color.White
            )
        }
    }
}



@Composable
fun GreetUser(name:String){
    Row (horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically,modifier= Modifier
        .fillMaxWidth()
        .padding(15.dp)){
        Column(verticalArrangement = Arrangement.Center) {
            Text(text="Hello,$name!",
                color= TextWhite, fontSize = 32.sp, fontFamily = inriaSansRegular, modifier = Modifier.padding(start=10.dp,top=15.dp))
            Text(text="Welcome To CSBS Student portal", color= TextWhite, fontSize = 14.sp, fontFamily = inriaSansRegular, modifier = Modifier.padding(start=14.dp,top=5.dp))

        }
        Icon(painter = painterResource(id = R.drawable.layer_1), contentDescription ="Csbs Login",
            tint= Color.White,modifier=Modifier.size(34.dp))
    }
}

@Composable
fun Timetable(chips:List<String>){
    var selectedChipIndex by remember{
        mutableIntStateOf(0)
    }
    Text(text="Today's Schedule",color= Color.White, modifier = Modifier.padding(start=24.dp,top=25.dp), fontSize = 19.sp, fontFamily = inriaSansRegular)
    LazyRow {
        items(chips.size){

            Box(contentAlignment = Center,modifier = Modifier
                .padding(start = 15.dp, top = 15.dp, bottom = 15.dp)
                .clickable {
                    selectedChipIndex = it
                }
                .clip(RoundedCornerShape(10.dp))
                .background(if (selectedChipIndex == it) ButtonBlue else DarkerButtonBlue)
                .padding(15.dp)
            ){
                Text(text=chips[it],color= TextWhite, fontFamily = inriaSansRegular)
            }
        }
        }

    }

@Composable
fun TodayQuiz(context: Context) {

    var quote by remember { mutableStateOf<String>("") }

    LaunchedEffect(Unit) {
        quote = getRandomQuote(context)
    }

    val color = LightRed
    Text(
        text = "Today's Insight!",
        fontFamily = inriaSansRegular,
        color = Color.White,
        fontSize = 18.sp,
        modifier = Modifier.padding(start = 18.dp, top = 15.dp)
    )
    Row(verticalAlignment = Alignment.CenterVertically
        , horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(15.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color)
            .padding(horizontal = 15.dp, vertical = 20.dp)
            .fillMaxWidth()
    ){
        Column {
            Text(text=quote,
                 fontFamily = FontFamily.Cursive, fontSize = 22.sp, color = Color.White, fontWeight = FontWeight.W500)
        }

    }
}

@Composable
fun Features(features:List<Feature>){
Column(modifier=Modifier.fillMaxWidth()){
    Text(text="Features", fontSize = 24.sp, fontFamily = inriaSansRegular, color = Color.White,
        modifier=Modifier.padding(vertical = 10.dp, horizontal = 20.dp))
    LazyVerticalGrid(columns= GridCells.Fixed(2),
        contentPadding = PaddingValues(start=7.5.dp,end=7.5.dp,bottom=100.dp), modifier = Modifier.fillMaxHeight()){
        items(features.size){
        FeatureItem(feature = features[it])
        }

   }
}
}

@Composable
fun FeatureItem(feature: Feature){
BoxWithConstraints(modifier= Modifier
    .padding(7.5.dp)
    .aspectRatio(1f)
    .clip(RoundedCornerShape(50.dp))
    .background(feature.darkColor)
    ){
    val width=constraints.maxWidth
    val height=constraints.maxHeight

    val mediumColorPoint1= Offset(0f, height*0.3f)
    val mediumColorPoint2= Offset(width*0.1f, height*0.35f)
    val mediumColorPoint3= Offset(width*0.4f, height*0.05f)
    val mediumColorPoint4= Offset(width*0.75f, height*0.7f)
    val mediumColorPoint5= Offset(width*1.4f, -height.toFloat())

    val mediumColoredPath= Path().apply{
        moveTo(mediumColorPoint1.x,mediumColorPoint1.y)
        NewGrid(mediumColorPoint2,mediumColorPoint5)
        NewGrid(mediumColorPoint3,mediumColorPoint4)
        NewGrid(mediumColorPoint4,mediumColorPoint3)
        NewGrid(mediumColorPoint1,mediumColorPoint2)
        lineTo(width.toFloat()+100f,height.toFloat()+100f)
        lineTo(-100f,height.toFloat()+100f)
        close()
    }
    // Light colored path
    val lightPoint1 = Offset(0f, height * 0.35f)
    val lightPoint2 = Offset(width * 0.1f, height * 0.4f)
    val lightPoint3 = Offset(width * 0.3f, height * 0.35f)
    val lightPoint4 = Offset(width * 0.65f, height.toFloat())
    val lightPoint5 = Offset(width * 1.4f, -height.toFloat() / 3f)

    val lightColoredPath = Path().apply {
        moveTo(lightPoint1.x, lightPoint1.y)
        NewGrid(lightPoint1, lightPoint2)
        NewGrid(lightPoint2, lightPoint3)
        NewGrid(lightPoint3, lightPoint4)
        NewGrid(lightPoint4, lightPoint5)
        lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
        lineTo(-100f, height.toFloat() + 100f)
        close()
    }
    Canvas(
        modifier = Modifier
            .fillMaxSize()
    ) {
        drawPath(
            path = mediumColoredPath,
            color = feature.mediumColor
        )
        drawPath(
            path = lightColoredPath,
            color = feature.lightColor
        )
    }


    var showDialog by remember { mutableStateOf(false)}
        Box(modifier= Modifier
            .fillMaxSize()
            .padding(15.dp)
            .clickable {
                showDialog = true
            }
    ){
        Icon(painter= painterResource(id = feature.iconId),
            contentDescription =feature.title,
            tint=Color.White,
            modifier= Modifier
                .align(Alignment.BottomStart)
                .size(55.dp)
                .padding(bottom = 10.dp, start = 10.dp)
            )
        Text(
            text=feature.title,
            color= Color.White,
            fontSize = 22.sp,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clip(RoundedCornerShape(10.dp))
                .padding(vertical = 10.dp, horizontal = 10.dp)
        )
            if (showDialog) {
                SimpleDialog(onDismissRequest = { showDialog = false },feature.title) // Display dialog
            }

    }
}
}

@Composable
fun BottomMenu(
    items:List<BottomMenuContent>,
    modifier: Modifier=Modifier,
    activeHighlights:Color= ButtonBlue,
    activeTextColor: Color=Color.White,
    inactiveTextColor: Color= AquaBlue,
    initialSelectionItemIndex:Int =0,
    onItemSelected: (Int) -> Unit

){
var selectedItemIndex by remember{
    mutableStateOf(initialSelectionItemIndex)
}
    Row(horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier= modifier
            .fillMaxWidth()
            .background(DeepBlue)
            .padding(15.dp)
        ){
    items.forEachIndexed { index, item->
        BottomMenuItem(items = item,
            isSelected = (index==selectedItemIndex),
            activeHighlights=activeHighlights,
            activeTextColor=activeTextColor,
            inactiveTextColor=inactiveTextColor
            ) {
        selectedItemIndex=index
            onItemSelected(index)
        }
    }
    }
}

@Composable
fun BottomMenuItem(items: BottomMenuContent,
                   isSelected:Boolean=false,
                   activeHighlights:Color= ButtonBlue,
                   activeTextColor: Color=Color.White,
                   inactiveTextColor: Color= AquaBlue,
                   onItemClick:()->Unit
    ) {

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable { onItemClick() }
        ){

        Box(contentAlignment = Center,modifier= Modifier
            .clip(RoundedCornerShape(15.dp))
            .background(if (isSelected) activeHighlights else Color.Transparent)
            .padding(10.dp)){
            Icon(painter = painterResource(id = items.iconId), contentDescription = items.title,
                tint=if(isSelected) activeTextColor else inactiveTextColor,
                modifier=Modifier.size(20.dp))


        }
        Text(
            text=items.title,
            color=if(isSelected) activeTextColor else inactiveTextColor
        )

    }

}


@Composable
fun Homepage2(context: Context) {
    Column(modifier= Modifier.padding(top=40.dp)) {
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val username=sharedPreferences.getString("username","Not Found")

        var quote="Success is not final, failure is not fatal: It is the courage to continue that counts."
        GreetUser(username.toString())
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        var todayname = "Sunday"  // Use var to allow modification

        LaunchedEffect(dayOfWeek) {
            todayname = when (dayOfWeek) {
                Calendar.SUNDAY -> "Sunday"
                Calendar.MONDAY -> "Monday"
                Calendar.TUESDAY -> "Tuesday"
                Calendar.WEDNESDAY -> "Wednesday"
                Calendar.THURSDAY -> "Thursday"
                Calendar.FRIDAY -> "Friday"
                Calendar.SATURDAY -> "Saturday"
                else -> "Unknown"
            }
        }


        val subjectList: SnapshotStateList<String> = remember { mutableStateListOf() }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            coroutineScope.launch {
                val list=gettimetable(context,"23CB",todayname)
                subjectList.addAll(list.toMutableStateList())

        }}

        Timetable(chips =subjectList)
        TodayQuiz(context)
        Features(features = listOf(
            Feature(
                title="ScoreBoard",
                R.drawable.scoreboard,
                OrangeYellow1,
                OrangeYellow2,
                OrangeYellow3
            ),
            Feature(
                title="GPA Calculator",
                R.drawable.resources,//change Required
                BlueViolet1,
                BlueViolet2,
                BlueViolet3
            ),
            Feature(
                title="Assignments",
                R.drawable.assignment,
                LightBlue1,
                LightBlue2,
                LightBlue3
            ),
            Feature(
                title="Attendence",
                R.drawable.attendencesvg,//change Required
                Beige1,
                Beige2,
                Beige3
            )

        ))
    }
}
