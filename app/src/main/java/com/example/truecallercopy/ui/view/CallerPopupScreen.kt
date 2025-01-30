package com.example.truecallercopy.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.truecallercopy.data.model.CallerInfo
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag

@Composable
fun CallerPopupScreen(callerInfo: CallerInfo, onClose: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    var offsetX by remember { mutableStateOf(0f) }
    var popupWidth by remember { mutableStateOf(0f) }
    var popupHeight by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf((screenHeight.value - popupHeight) / 2 - 100f) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val density = LocalDensity.current.density
        Card(
            modifier = Modifier
                .offset(x = offsetX.dp, y = offsetY.dp)
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .onGloballyPositioned { coordinates ->
                    popupWidth = (coordinates.size.width / density)
                    popupHeight = (coordinates.size.height / density)
                }
                .pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                        // Calculate new offsets based on the drag
                        val verticalSensitivity = 0.4f // Adjust this value to control the speed
                        val newOffsetX = offsetX + dragAmount.x
                        val newOffsetY = offsetY + (dragAmount.y * verticalSensitivity)

                        // Constrain the movement within the screen bounds
                        offsetX = newOffsetX.coerceIn(0f, screenWidth.value - popupWidth)
                        offsetY = newOffsetY.coerceIn(50f, screenHeight.value - popupHeight - 50f)
                    }
                }
                .composed {
                    this.then(Modifier.semantics {
                        testTag = "caller_card"
                    })
                },
            shape = RoundedCornerShape(8.dp),
            elevation = 8.dp
        ) {
            Column(
                Modifier.background(
                    brush = Brush.verticalGradient(
                        listOf(Color(0xFF4C9EBD), Color(0xFF1F2C54))
                    )
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val painter = rememberImagePainter(data = null) // Replace with actual image URL

                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .background(Color.Gray, shape = CircleShape)
                        ) {
                            Image(
                                painter = painter,
                                contentDescription = "Caller Image",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Gray, shape = CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = callerInfo.name,
                                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold, color = Color.White),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Text(
                                text = callerInfo.location,
                                style = MaterialTheme.typography.body2.copy(color = Color.White),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.padding()) {
                        Text(
                            text = callerInfo.phoneNumber,
                            style = MaterialTheme.typography.body2.copy(color = Color.White),
                        )

                        if (callerInfo.isSpam) {
                            Text(
                                text = "Spam Caller",
                                color = Color.Red,
                                style = MaterialTheme.typography.body2,
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                    ) {
                        Text(
                            text = "TrueCallerCopy",
                            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold, color = Color.White),
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}