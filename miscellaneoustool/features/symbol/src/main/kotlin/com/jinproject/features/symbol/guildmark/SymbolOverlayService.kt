package com.jinproject.features.symbol.guildmark

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.compositionContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.jinproject.design_compose.component.HorizontalWeightSpacer
import com.jinproject.design_compose.component.VerticalSpacer
import com.jinproject.design_compose.component.button.DefaultIconButton
import com.jinproject.design_compose.theme.MiscellaneousToolTheme
import com.jinproject.design_ui.R
import com.jinproject.features.symbol.createChannel
import com.jinproject.features.symbol.getBitmapFromContentUri
import com.jinproject.features.symbol.guildmark.component.ImagePixels
import com.jinproject.features.symbol.guildmark.component.UsedColorInPixels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SymbolOverlayService : LifecycleService() {
    private var mView: ComposeView? = null
    private val wm by lazy { getSystemService(WINDOW_SERVICE) as WindowManager }
    private var sliderThreshold by mutableFloatStateOf(0f)
    private var imageUri by mutableStateOf("")

    @SuppressLint("WrongConstant")
    override fun onCreate() {
        super.onCreate()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createChannel(
            name = getString(R.string.channel_name), desc = getString(R.string.channel_description)
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_NAME)
            .setPriority(NotificationCompat.PRIORITY_LOW).setSmallIcon(R.mipmap.ic_main)
            .setContentTitle(getString(R.string.symbol_guildMark_overlay_running)).build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            startForeground(CHANNEL_ID, notification, FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        else
            startForeground(CHANNEL_ID, notification)

        inflateOverlayView()
        addViewOnWindowManager()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val imageUri = intent?.getStringExtra(IMAGE_URI)
        val imageThreshold = intent?.getFloatExtra(IMAGE_THRESHOLD, 0f)

        imageUri?.let { uri ->
            this.imageUri = uri
        }
        imageThreshold?.let { thres ->
            this.sliderThreshold = thres
        }

        return START_STICKY
    }

    private fun inflateOverlayView() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mView = inflater.inflate(com.jinproject.features.symbol.R.layout.guild_symbol_overlay, null) as ComposeView

        val lifecycleOwner = object : SavedStateRegistryOwner {
            private var mSavedStateRegistryController: SavedStateRegistryController =
                SavedStateRegistryController.create(this).apply { performRestore(null) }
            override val lifecycle: Lifecycle get() = this@SymbolOverlayService.lifecycle

            override val savedStateRegistry: SavedStateRegistry =
                mSavedStateRegistryController.savedStateRegistry
        }
        val viewModelStore = ViewModelStore()
        val viewModelStoreOwner = object : ViewModelStoreOwner {
            override val viewModelStore: ViewModelStore
                get() = viewModelStore
        }

        val coroutineContext = AndroidUiDispatcher.CurrentThread
        val runRecomposeScope = CoroutineScope(coroutineContext)
        val reComposer = Recomposer(coroutineContext)

        mView?.apply {
            setViewTreeLifecycleOwner(lifecycleOwner)
            setViewTreeSavedStateRegistryOwner(lifecycleOwner)
            setViewTreeViewModelStoreOwner(viewModelStoreOwner)
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
            setContent {
                MiscellaneousToolTheme {
                    val bitMapSample = getBitmapFromContentUri(
                        context = this@SymbolOverlayService,
                        imageUri = imageUri
                    )
                    val guildMarkManager =
                        rememberGuildMarkManager(bitMap = bitMapSample, slider = sliderThreshold)
                    var isTopBarHide by remember {
                        mutableStateOf(false)
                    }
                    val topBarState by animateFloatAsState(targetValue = if(isTopBarHide) 1f else 0f, label = "TopBar State")

                    Box(
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectDragGestures { change, offset ->
                                    require(mView != null) {
                                        "Overlay 되어질 view 가 inflate 되지 않음"
                                    }
                                    change.consume()

                                    val params = mView!!.layoutParams as WindowManager.LayoutParams
                                    params.x = (params.x + offset.x).toInt()
                                    params.y = (params.y + offset.y).toInt()

                                    wm.updateViewLayout(mView, params)
                                }
                            }
                            .pointerInput(Unit) {
                                detectTapGestures {
                                    isTopBarHide = !isTopBarHide
                                }
                            }
                            .padding(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .graphicsLayer {
                                    alpha = if(isTopBarHide) 0.3f else 1f
                                }
                        ) {
                            ImagePixels(
                                guildMarkManager = guildMarkManager,
                                modifier = Modifier
                                    .size(16.dp)
                                    .border(1.dp, MaterialTheme.colorScheme.onBackground)
                            )
                            VerticalSpacer(height = 30.dp)
                            UsedColorInPixels(
                                guildMarkManager = guildMarkManager,
                                itemWidth = 16.dp
                            )
                        }
                        Row(
                            modifier = Modifier
                                .width(204.dp)
                                .align(Alignment.TopCenter)
                                .graphicsLayer {
                                    scaleX = topBarState
                                    scaleY = topBarState
                                    alpha = topBarState
                                }
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            HorizontalWeightSpacer(float = 1f)
                            DefaultIconButton(
                                icon = R.drawable.ic_recycle,
                                onClick = {
                                    guildMarkManager.selectColor(Color.Unspecified)
                                },
                                iconTint = MaterialTheme.colorScheme.surface,
                                backgroundTint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            DefaultIconButton(
                                icon = R.drawable.ic_x,
                                onClick = {
                                    stopSelf()
                                },
                                iconTint = MaterialTheme.colorScheme.surface,
                                backgroundTint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            compositionContext = reComposer
            runRecomposeScope.launch {
                reComposer.runRecomposeAndApplyChanges()
            }
        }
    }

    private fun addViewOnWindowManager() {
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        }
        wm.addView(mView, params)
    }

    companion object {
        const val CHANNEL_NAME = "SymbolOverlay"
        const val CHANNEL_ID = 990
        const val IMAGE_URI = "imageUri"
        const val IMAGE_THRESHOLD = "imageThreshold"
    }
}