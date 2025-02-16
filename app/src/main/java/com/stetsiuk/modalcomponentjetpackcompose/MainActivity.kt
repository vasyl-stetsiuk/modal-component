package com.stetsiuk.modalcomponentjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.stetsiuk.modalcomponentjetpackcompose.ui.theme.ModalComponentJetpackComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ModalComponentJetpackComposeTheme {
                // ModalContentPreview1()
                ModalContentPreview2()
            }
        }
    }
}