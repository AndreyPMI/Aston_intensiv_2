package com.spinthedrum.presentation.viewModel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.spinthedrum.presentation.ui.WheelViewInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class WheelUiState(
    val selectedColor: Int? = null,
    val wheelSize: Int = 150,
    val bitmap: Bitmap? = null,
    val text: String? = null
)

class WheelViewModel(private val context: Context) : ViewModel() {
    private val _uiState = MutableStateFlow(WheelUiState())
    val uiState: StateFlow<WheelUiState> = _uiState
    private var currentImage: Bitmap? = null
    fun spinWheel(wheelView: WheelViewInterface) {
        viewModelScope.launch {
            wheelView.rotate(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    _uiState.value = _uiState.value.copy(bitmap = null, text = null)
                    val selectedColor = wheelView.getSelectedColor()
                    when (selectedColor) {
                        Color.RED, Color.YELLOW, Color.CYAN, Color.rgb(128, 0, 128) -> {
                            val text = when (selectedColor) {
                                Color.RED -> "Красный"
                                Color.YELLOW -> "Жёлтый"
                                Color.CYAN -> "Голубой"
                                else -> "Фиолетовый"
                            }
                            _uiState.value = _uiState.value.copy(
                                selectedColor = selectedColor,
                                bitmap = null,
                                text = text
                            )
                        }

                        else -> {
                            viewModelScope.launch {
                                val url = "https://avatar.iran.liara.run/public"
                                loadImageFromURL(url)
                            }
                        }
                    }
                }
            })
        }
    }

    fun clearContent() {
        _uiState.value = _uiState.value.copy(selectedColor = null, bitmap = null, text = null)
        currentImage = null
    }

    fun setWheelSizeFromView(size: Int) {
        _uiState.value = _uiState.value.copy(wheelSize = size)
    }

    private suspend fun loadImageFromURL(url: String) {
        withContext(Dispatchers.IO) {
            val request = ImageRequest.Builder(context)
                .data(url)
                .build()
            val result =   context.imageLoader.execute(request)
            if (result is SuccessResult){
                currentImage = result.drawable.toBitmap()
                _uiState.value = _uiState.value.copy( bitmap = currentImage)
            } else {
                currentImage = null
                _uiState.value = _uiState.value.copy( bitmap = currentImage)
            }
        }
    }
    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                WheelViewModel(context)
            }
        }
    }
}
