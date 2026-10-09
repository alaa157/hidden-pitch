package hiddenpitch.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import hiddenpitch.R
import hiddenpitch.util.AppLanguageChoice
import hiddenpitch.util.LocaleManager

@Immutable
data class SettingsUiState(val selectedLanguage: AppLanguageChoice)

class SettingsViewModel(private val localeManager: LocaleManager) : ViewModel() {
    private val mutableUiState = MutableStateFlow(SettingsUiState(localeManager.currentChoice))
    val uiState: StateFlow<SettingsUiState> = mutableUiState.asStateFlow()

    fun selectLanguage(choice: AppLanguageChoice) {
        mutableUiState.value = SettingsUiState(choice)
        localeManager.setChoice(choice)
    }
}

@Composable
fun SettingsScreen(onBack: () -> Unit, factory: ViewModelProvider.Factory) {
    val viewModel: SettingsViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsContent(uiState.selectedLanguage, viewModel::selectLanguage, onBack)
}

@Composable
internal fun SettingsContent(
    selectedLanguage: AppLanguageChoice,
    onLanguageSelected: (AppLanguageChoice) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.screen_settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.action_back))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(stringResource(R.string.settings_language_title), style = MaterialTheme.typography.titleLarge)
            AppLanguageChoice.entries.forEach { choice ->
                Row(
                    modifier = Modifier.fillMaxWidth().selectable(
                        selected = selectedLanguage == choice,
                        role = Role.RadioButton,
                        onClick = { onLanguageSelected(choice) }
                    ).padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = selectedLanguage == choice, onClick = null)
                    Spacer(Modifier.width(12.dp))
                    Text(stringResource(choice.labelRes), style = MaterialTheme.typography.bodyLarge)
                }
            }
            Spacer(Modifier.width(1.dp))
            OutlinedButton(onClick = {}, enabled = false, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.action_sign_in))
            }
            Text(stringResource(R.string.settings_sign_in_placeholder), style = MaterialTheme.typography.bodyMedium)
        }
    }
}
