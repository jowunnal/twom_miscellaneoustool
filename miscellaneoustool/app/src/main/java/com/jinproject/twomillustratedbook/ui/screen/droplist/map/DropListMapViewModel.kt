package com.jinproject.twomillustratedbook.ui.screen.droplist.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.twomillustratedbook.ui.screen.droplist.map.item.MapState
import com.jinproject.twomillustratedbook.ui.screen.droplist.mapper.toMapState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DropListMapViewModel @Inject constructor(
    private val dropListRepository: com.jinproject.domain.repository.DropListRepository
) : ViewModel() {
    private val _mapState: MutableStateFlow<List<MapState>> = MutableStateFlow(emptyList())
    val mapState get() = _mapState.asStateFlow()

    init {
        getDropListMapList()
    }

    private fun getDropListMapList() = dropListRepository.getMaps().onEach { mapModelList ->
        _mapState.update {
            mapModelList.map { mapModel -> mapModel.toMapState() }
        }
    }.catch {

    }.launchIn(viewModelScope)


}