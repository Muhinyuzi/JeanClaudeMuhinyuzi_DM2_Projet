package com.example.presidentsusa

import android.R.attr.contentDescription
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.presidentsusa.ui.theme.PresidentsUSATheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


class MainActivity : ComponentActivity() {
    private val viewModel: StateViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //viewModel.getPresidents()
        enableEdgeToEdge()
        setContent {
            PresidentsUSATheme {
                //AfficherGrille(viewModel = viewModel)
                //EcranOnePresident(viewModel = viewModel)
                AppNavigation(
                    viewModel = viewModel,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun AppNavigation(viewModel: StateViewModel,
                  modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = "ecran1") {
        composable("ecran1") {
            AfficherGrille(viewModel=viewModel,
                navController=navController,
                modifier=modifier) }

        composable("ecran2/{info}",
            arguments= listOf(navArgument("info"){
                type= NavType.StringType})){ backStackEntry->
            EcranOnePresident(navController, viewModel=viewModel,
                backStackEntry.arguments?.getString("info") ?: "") }
    }
}


@Composable
fun AfficherGrille(viewModel: StateViewModel, navController: NavController, modifier: Modifier = Modifier) {
    val presidents by viewModel.presidents
    Column(
        //.background(Color(0xFFFF1DCE))
        modifier=Modifier
            .padding(24.dp)
            .fillMaxSize()
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.presidents), contentScale = ContentScale.FillBounds
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            modifier = Modifier.height(80.dp),
            painter = painterResource(id = R.drawable.presidentsheader),
            contentDescription = "presidents header"
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            content={
                items(viewModel.presidents.value) { president ->
                    //Text(text = user.name)

                    Column(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
                            .background(Color(0xFFB7C9E2))
                            //.border(1.dp, Color.Black)
                            .clickable{
                                navController.navigate("ecran2/${president.id}")
                            }
                            //.clip(RoundedCornerShape(16.dp))
                            //.border(1.dp, Color.Black)

                    ) {
                        AsyncImage(
                            model = president?.photo?:"",
                            placeholder = painterResource(id =
                            R.drawable.placeholder),
                            error = painterResource(id = R.drawable.error),
                            contentDescription = "Le president",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(top = 8.dp)
                               .height(120.dp)
                                .width(80.dp)
                                .align(Alignment.CenterHorizontally)
                            //.clip(CircleShape)
                        )

                        Text(
                            text = president.name,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(
                               //start = 8.dp,
                                top = 8.dp
                            )
                                .align(Alignment.CenterHorizontally)
                        )
                    }

                }

        })
        DisposableEffect(Unit) {
            viewModel.getPresidents()
            onDispose {}
        }

    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EcranOnePresident(navController: NavController, viewModel: StateViewModel, info:String) {
    val president by viewModel.president

    val postNotificationPermission= rememberPermissionState(permission = android.Manifest.permission.POST_NOTIFICATIONS)
    val AimeNotificationService= AimeNotificationService(LocalContext.current)
    LaunchedEffect(key1 = true ){
        if(!postNotificationPermission.status.isGranted){
            postNotificationPermission.launchPermissionRequest()
        }
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.presidents), contentScale = ContentScale.FillBounds
            )
    ) {

        Column(
            modifier = Modifier
                //.fillMaxWidth(),
                .width(380.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {
            AsyncImage(
                model = president?.photo?:"",
                placeholder = painterResource(id =
                R.drawable.placeholder),
                error = painterResource(id = R.drawable.error),
                contentDescription = "Le president",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(start=8.dp,end=8.dp, top = 16.dp, bottom = 8.dp)
                    .height(500.dp)
                    .fillMaxWidth()
                    .border(1.dp, Color.Black)
                //.clip(CircleShape)
            )
            Column(
                modifier = Modifier
                    .border(2.dp, Color.Black)
                    //.background(Color(0xFFB7C9E2))
                    .background(Color(0xFFC0C0C0))
                    .padding(start=8.dp,end=8.dp, bottom = 8.dp)
                    //.fillMaxWidth()
                    .width(340.dp)

            ) {


                Row {
                    Text(
                        text = "Nom:",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start =
                        8.dp, end = 8.dp, top = 8.dp)
                    )
                    Text(
                        text = "${president?.name}" ,
                        modifier =
                        Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    )
                }

                Row {
                    Text(
                        text = "Années en poste: ",
                        fontWeight = FontWeight.Bold,
                        modifier =
                        Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                    )
                    Text(
                        text = "${president?.yearsInOffice}" ,
                        modifier =
                        Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    )

                }

                Row {
                    Text(
                        text = "Ordre: ",
                        fontWeight = FontWeight.Bold,
                        modifier =
                        Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    )
                    Text(
                        text = "${president?.ordinal}" ,
                        modifier =
                        Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    )


                }



                Icon(
                    imageVector= Icons.Default.ThumbUp,
                    contentDescription = "J'aime",
                    tint = Color.Black,
                    modifier=Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(24.dp)
                        .clickable {
                            president?.name?.let {
                                AimeNotificationService
                                    .showNotification(it)
                            }
                        }
                )
            }


            Button( onClick ={
                    navController.navigate("ecran1"){
                        popUpTo("ecran1") {inclusive=true}
                    }
                    },modifier = Modifier.padding(start=8.dp,end=8.dp, top = 8.dp, bottom = 8.dp)

            )      {
                    Text(
                        text = "Retour"
                    )
                    }


        }
    }
    DisposableEffect(Unit) {
        viewModel.getOnePresident(info)
        onDispose {}
    }
}