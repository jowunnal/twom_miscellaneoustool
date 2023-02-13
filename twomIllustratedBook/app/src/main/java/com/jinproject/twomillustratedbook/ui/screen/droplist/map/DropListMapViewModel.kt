package com.jinproject.twomillustratedbook.ui.screen.droplist.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.twomillustratedbook.data.repository.DropListRepository
import com.jinproject.twomillustratedbook.ui.screen.droplist.map.item.MapState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DropListMapViewModel @Inject constructor(
    private val dropListRepository: DropListRepository
) : ViewModel() {
    private val _mapState: MutableStateFlow<List<MapState>> = MutableStateFlow(emptyList())
    val mapState get() = _mapState.asStateFlow()

    init {
        getDropListMapList()
    }

    private fun getDropListMapList() = dropListRepository.getMaps().onEach { mapModelList ->
        _mapState.emit(
            mapModelList.map { mapModel -> mapModel.toMapState() }.sortedWith(compareBy{mapState -> mapState.name})
        )
    }.catch {

    }.launchIn(viewModelScope)


}