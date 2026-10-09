package hiddenpitch.ui.add

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

data class AddPlaceUiState(val bodyRes: Int = R.string.add_place_placeholder_body)

class AddPlaceViewModel : ViewModel() {
    private val mutableUiState = MutableStateFlow(AddPlaceUiState())
    val uiState: StateFlow<AddPlaceUiState> = mutableUiState.asStateFlow()
}

@Composable
fun AddPlaceScreen(onBack: () -> Unit, factory: ViewModelProvider.Factory) {
    val viewModel: AddPlaceViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PlaceholderScreen(R.string.screen_add_place_title, uiState.bodyRes, onBack)
}
