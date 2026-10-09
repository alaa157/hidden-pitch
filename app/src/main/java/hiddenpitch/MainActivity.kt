package hiddenpitch

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import hiddenpitch.ui.common.HiddenPitchRoot
import hiddenpitch.ui.theme.HiddenPitchTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = (application as HiddenPitchApp).appContainer
        setContent { HiddenPitchTheme { HiddenPitchRoot(container) } }
    }
}
