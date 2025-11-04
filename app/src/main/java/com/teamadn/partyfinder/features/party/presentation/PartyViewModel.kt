package com.teamadn.partyfinder.features.party.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamadn.partyfinder.features.party.domain.model.PartyModel
import com.teamadn.partyfinder.features.party.domain.usecase.GetPartiesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class PartyViewModel(private val getPartiesUseCase: GetPartiesUseCase) : ViewModel() {

    sealed class PartyUIState {
        object Loading : PartyUIState()
        class Error(val message: String) : PartyUIState()
        class Success(val data: List<PartyModel>) : PartyUIState()
    }

    private val _uiState = MutableStateFlow<PartyUIState>(PartyUIState.Loading)
    val uiState: StateFlow<PartyUIState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        fetchAndFilterParties()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    private fun fetchAndFilterParties() {
        viewModelScope.launch(Dispatchers.IO) {
            getPartiesUseCase()
                .combine(_searchQuery) { parties, query ->
                    if (query.isBlank()) {
                        parties
                    } else {
                        parties.filter { party ->
                            val queryLower = query.lowercase()
                            (party.name?.lowercase()?.contains(queryLower) == true) ||
                                    (party.description?.lowercase()?.contains(queryLower) == true) ||
                                    (party.style?.lowercase()?.contains(queryLower) == true)
                        }
                    }
                }
                .catch { e ->
                    _uiState.value = PartyUIState.Error(e.message ?: "Unknown Error")
                }
                .collect { filteredParties ->
                    _uiState.value = PartyUIState.Success(filteredParties)
                }
        }
    }
}