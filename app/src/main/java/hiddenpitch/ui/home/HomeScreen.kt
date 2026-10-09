package hiddenpitch.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import hiddenpitch.R

@Immutable
data class HomeUiState(
    val placeholderRes: Int = R.string.home_placeholder_body,
    val sampleTitleRes: Int = R.string.home_sample_place_title,
    val sampleBodyRes: Int = R.string.home_sample_place_body
)

class HomeViewModel : ViewModel() {
    private val mutableUiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = mutableUiState.asStateFlow()
}

@Composable
fun HomeScreen(onOpenDetail: (String) -> Unit, onOpenAdd: () -> Unit, onOpenMyPlaces: () -> Unit, onOpenSettings: () -> Unit, factory: ViewModelProvider.Factory) {
    val viewModel: HomeViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeContent(uiState, onOpenDetail, onOpenAdd, onOpenMyPlaces, onOpenSettings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeContent(uiState: HomeUiState = HomeUiState(), onOpenDetail: (String) -> Unit, onOpenAdd: () -> Unit, onOpenMyPlaces: () -> Unit, onOpenSettings: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.screen_home_title)) },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Filled.Search, contentDescription = stringResource(R.string.action_search)) }
                    IconButton(onClick = {}) { Icon(painterResource(R.drawable.ic_filter_list), contentDescription = stringResource(R.string.action_filters)) }
                    IconButton(onClick = onOpenMyPlaces) { Icon(Icons.Filled.Person, contentDescription = stringResource(R.string.action_my_places)) }
                    IconButton(onClick = onOpenSettings) { Icon(Icons.Filled.Settings, contentDescription = stringResource(R.string.action_settings)) }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onOpenAdd) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.action_add_place))
                }
            }
        }
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(stringResource(uiState.placeholderRes), style = MaterialTheme.typography.bodyLarge)
            Card(onClick = { onOpenDetail("sample-place") }, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(stringResource(uiState.sampleTitleRes), style = MaterialTheme.typography.titleMedium)
                    Text(stringResource(uiState.sampleBodyRes), style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
