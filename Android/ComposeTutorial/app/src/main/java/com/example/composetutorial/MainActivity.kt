package com.example.composetutorial

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composetutorial.ui.theme.ComposeTutorialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTutorialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MessageCard(msg = Message("wjc","hello"))
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!", maxLines = 1, overflow = TextOverflow.Ellipsis)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeTutorialTheme {
        Greeting("Android")
    }
}
data class Message(val author:String,val body:String)
@Composable
fun MessageCard(msg:Message){
    Row(modifier = Modifier.padding(all=8.dp)) {
        Image(painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = "Contact pic", modifier = Modifier
            .size(40.dp)
            .clip(
                CircleShape
            )
            .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape))
        Spacer(modifier = Modifier.width(8.dp))
        var isExpanded by remember{ mutableStateOf(false) }
        val surfaceColor by animateColorAsState(targetValue = if(isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface)
        Column (modifier = Modifier.clickable { isExpanded=!isExpanded }){
            Text(text = msg.author, color = MaterialTheme.colors.secondaryVariant, style = MaterialTheme.typography.subtitle2)
            Spacer(modifier = Modifier.height(4.dp))
            Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp, color = surfaceColor, modifier = Modifier.animateContentSize().padding(1.dp)) {
                Text(text = msg.body, style = MaterialTheme.typography.body2, modifier = Modifier.padding(all = 4.dp), maxLines = if(isExpanded) Int.MAX_VALUE else 1)
            }
        }
    }
}

@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun showMessageCard(){
    ComposeTutorialTheme {
        Surface {
            MessageCard(msg = Message("wjc","hello"))
        }
    }
}

@Composable
fun Conversation(messageList:List<Message>){
    LazyColumn{
        items(messageList){message->
            MessageCard(msg = message)
        }
    }
}
@Preview
@Composable
fun PreviewConversation(){
    ComposeTutorialTheme {
        Conversation(messageList = SampleData.conversationSample)
    }
}

object SampleData{
    val conversationSample= mutableListOf<Message>()
    init {
        repeat(15){
            val str=StringBuilder("多拉风加大了")
            repeat(it){
                str.append("的法拉盛就法拉伐")
            }
            val message=Message("wjc$it","$str")
            conversationSample.add(message)
        }

    }
}