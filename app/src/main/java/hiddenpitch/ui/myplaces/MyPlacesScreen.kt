package hiddenpitch.ui.myplaces

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import hiddenpitch.R
import hiddenpitch.ui.common.PlaceholderScreen

data class MyPlacesUiState(val bodyRes: Int = R.string.my_places_placeholder_body)

class MyPlacesViewModel : ViewModel() {
    private val mutableUiState = MutableStateFlow(MyPlacesUiState())
    val uiState: StateFlow<MyPlacesUiState> = mutableUiState.asStateFlow()
}

@Composable
fun MyPlacesScreen(onBack: () -> Unit, factory: ViewModelProvider.Factory) {
    val viewModel: MyPlacesViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PlaceholderScreen(R.string.screen_my_places_title, uiState.bodyRes, onBack)
}
