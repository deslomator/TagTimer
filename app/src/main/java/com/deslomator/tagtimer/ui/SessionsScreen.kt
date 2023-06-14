package com.deslomator.tagtimer.ui

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.DragScope
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissDirection.EndToStart
import androidx.compose.material3.DismissDirection.StartToEnd
import androidx.compose.material3.DismissValue
import androidx.compose.material3.DismissValue.Default
import androidx.compose.material3.DismissValue.DismissedToEnd
import androidx.compose.material3.DismissValue.DismissedToStart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.OnRemeasuredModifier
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.InspectorValueInfo
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.deslomator.tagtimer.R
import com.deslomator.tagtimer.action.AppAction
import com.deslomator.tagtimer.model.Session
import com.deslomator.tagtimer.state.AppState
import com.deslomator.tagtimer.ui.theme.Pink80
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.math.abs
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionsScreen(
    state: AppState,
    onAction: (AppAction) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onAction(AppAction.AddNewSessionClicked) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.new_session)
                )
            }
        }
    ) { paddingValues ->
        if (state.showSessionDialog) {
            SessionDialog(
                state = state,
                onAction = onAction,
                session = state.currentSession
            )
        }
        if (state.showSessionDeleteDialog) {
            ConfirmationDialog(
                title = stringResource(id = R.string.delete_session),
                onAccept = { onAction(AppAction.AcceptDeleteSessionClicked) },
                onCancel = { onAction(AppAction.DismissDeleteSessionDialog) }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.sessions, { session: Session -> session.id }) { session ->

                    val dismissState = rememberDismissState(initialValue = Default)
                    if (dismissState.isDismissed(EndToStart)) {
                        onAction(AppAction.DeleteSessionClicked(session))
                    }

                    SwipeToDismiss(
                        state = dismissState,
                        modifier = Modifier.padding(vertical = 4.dp),
                        directions = setOf(EndToStart),
                        background = {
                            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
                            val color by animateColorAsState(
                                when (dismissState.targetValue) {
                                    Default -> Color.LightGray
                                    DismissedToEnd -> Color.Green
                                    DismissedToStart -> Pink80
                                }
                            )
                            val alignment = when (direction) {
                                StartToEnd -> Alignment.CenterStart
                                EndToStart -> Alignment.CenterEnd
                            }
                            val icon = when (direction) {
                                StartToEnd -> Icons.Default.Done
                                EndToStart -> Icons.Default.Delete
                            }
                            val scale by animateFloatAsState(
                                if (dismissState.targetValue == Default) 0.75f else 1f
                            )

                            Box(
                                Modifier.fillMaxSize().background(color)
                                    .padding(horizontal = 20.dp),
                                contentAlignment = alignment
                            ) {
                                Icon(
                                    icon,
                                    contentDescription = "Localized description",
                                    modifier = Modifier.scale(scale)
                                )
                            }
                        },
                        dismissContent = {
                            Log.d("Sessions Screen", "Loading item")
                            SessionItem(
                                session = session,
                                onItemClick = { onAction(AppAction.SessionItemClicked(session)) },
                                onEditClick = { onAction(AppAction.EditSessionClicked(session)) },
                                onDeleteClick = { onAction(AppAction.DeleteSessionClicked(session)) },
                                shadowElevation = animateDpAsState(
                                    if (dismissState.dismissDirection != null) 22.dp else 16.dp
                                ).value
                            )
                        }
                    )
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
class DismissState(
    initialValue: DismissValue,
    confirmValueChange: (DismissValue) -> Boolean = { true },
    positionalThreshold: Density.(totalDistance: Float) -> Float =
        SwipeToDismissDefaults.FixedPositionalThreshold,
) {
    internal val swipeableState = SwipeableV2State(
        initialValue = initialValue,
        confirmValueChange = confirmValueChange,
        positionalThreshold = positionalThreshold,
        velocityThreshold = DismissThreshold
    )

    private val offset: Float? get() = swipeableState.offset

    fun requireOffset(): Float = swipeableState.requireOffset()

    val currentValue: DismissValue get() = swipeableState.currentValue

    val targetValue: DismissValue get() = swipeableState.targetValue

    val dismissDirection: DismissDirection? get() =
        if (offset == 0f || offset == null) null else if (offset!! > 0f) StartToEnd else EndToStart

    fun isDismissed(direction: DismissDirection): Boolean {
        return currentValue == if (direction == StartToEnd) DismissedToEnd else DismissedToStart
    }

    companion object {

        fun Saver(
            confirmValueChange: (DismissValue) -> Boolean,
            positionalThreshold: Density.(totalDistance: Float) -> Float,
        ) =
            Saver<DismissState, DismissValue>(
                save = { it.currentValue },
                restore = {
                    DismissState(
                        it, confirmValueChange, positionalThreshold)
                }
            )
    }
}

@Composable
@ExperimentalMaterial3Api
fun rememberDismissState(
    initialValue: DismissValue = Default,
    confirmValueChange: (DismissValue) -> Boolean = { true },
    positionalThreshold: Density.(totalDistance: Float) -> Float =
        SwipeToDismissDefaults.FixedPositionalThreshold,
): DismissState {
    return rememberSaveable(
        saver = DismissState.Saver(confirmValueChange, positionalThreshold)) {
        DismissState(initialValue, confirmValueChange, positionalThreshold)
    }
}

@Composable
@ExperimentalMaterial3Api
fun SwipeToDismiss(
    state: DismissState,
    background: @Composable RowScope.() -> Unit,
    dismissContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    directions: Set<DismissDirection> = setOf(EndToStart, StartToEnd),
) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

    Box(
        modifier
            .swipeableV2(
                state = state.swipeableState,
                orientation = Orientation.Horizontal,
                enabled = state.currentValue == Default,
                reverseDirection = isRtl,
            )
            .swipeAnchors(
                state = state.swipeableState,
                possibleValues = setOf(Default, DismissedToEnd, DismissedToStart)
            ) { value, layoutSize ->
                val width = layoutSize.width.toFloat()
                when (value) {
                    DismissedToEnd -> if (StartToEnd in directions) width else null
                    DismissedToStart -> if (EndToStart in directions) -width else null
                    Default -> 0f
                }
            }
    ) {
        Row(
            content = background,
            modifier = Modifier.matchParentSize()
        )
        Row(
            content = dismissContent,
            modifier = Modifier.offset { IntOffset(state.requireOffset().roundToInt(), 0) }
        )
    }
}

@ExperimentalMaterial3Api
object SwipeToDismissDefaults {
    val FixedPositionalThreshold: Density.(totalDistance: Float) -> Float = { _ -> 56.dp.toPx() }
}

private val DismissThreshold = 125.dp

@ExperimentalMaterial3Api
internal fun <T> Modifier.swipeableV2(
    state: SwipeableV2State<T>,
    orientation: Orientation,
    enabled: Boolean = true,
    reverseDirection: Boolean = false,
    interactionSource: MutableInteractionSource? = null
) = draggable(
    state = state.swipeDraggableState,
    orientation = orientation,
    enabled = enabled,
    interactionSource = interactionSource,
    reverseDirection = reverseDirection,
    startDragImmediately = state.isAnimationRunning,
    onDragStopped = { velocity -> launch { state.settle(velocity) } }
)

@ExperimentalMaterial3Api
internal fun <T> Modifier.swipeAnchors(
    state: SwipeableV2State<T>,
    possibleValues: Set<T>,
    anchorChangeHandler: AnchorChangeHandler<T>? = null,
    calculateAnchor: (value: T, layoutSize: IntSize) -> Float?,
) = this.then(SwipeAnchorsModifier(
    onDensityChanged = { state.density = it },
    onSizeChanged = { layoutSize ->
        val previousAnchors = state.anchors
        val newAnchors = mutableMapOf<T, Float>()
        possibleValues.forEach {
            val anchorValue = calculateAnchor(it, layoutSize)
            if (anchorValue != null) {
                newAnchors[it] = anchorValue
            }
        }
        if (previousAnchors != newAnchors) {
            val previousTarget = state.targetValue
            val stateRequiresCleanup = state.updateAnchors(newAnchors)
            if (stateRequiresCleanup) {
                anchorChangeHandler?.onAnchorsChanged(
                    previousTarget,
                    previousAnchors,
                    newAnchors
                )
            }
        }
    },
    inspectorInfo = debugInspectorInfo {
        name = "swipeAnchors"
        properties["state"] = state
        properties["possibleValues"] = possibleValues
        properties["anchorChangeHandler"] = anchorChangeHandler
        properties["calculateAnchor"] = calculateAnchor
    }
))

@Stable
@ExperimentalMaterial3Api
internal class SwipeableV2State<T>(
    initialValue: T,
    internal val animationSpec: AnimationSpec<Float> = SwipeableV2Defaults.AnimationSpec,
    internal val confirmValueChange: (newValue: T) -> Boolean = { true },
    internal val positionalThreshold: Density.(totalDistance: Float) -> Float =
        SwipeableV2Defaults.PositionalThreshold,
    internal val velocityThreshold: Dp = SwipeableV2Defaults.VelocityThreshold,
) {

    private val swipeMutex = InternalMutatorMutex()

    internal val swipeDraggableState = object : DraggableState {
        private val dragScope = object : DragScope {
            override fun dragBy(pixels: Float) {
                this@SwipeableV2State.dispatchRawDelta(pixels)
            }
        }

        override suspend fun drag(
            dragPriority: MutatePriority,
            block: suspend DragScope.() -> Unit
        ) {
            swipe(dragPriority) { dragScope.block() }
        }

        override fun dispatchRawDelta(delta: Float) {
            this@SwipeableV2State.dispatchRawDelta(delta)
        }
    }

    var currentValue: T by mutableStateOf(initialValue)
        private set

    val targetValue: T by derivedStateOf {
        animationTarget ?: run {
            val currentOffset = offset
            if (currentOffset != null) {
                computeTarget(currentOffset, currentValue, velocity = 0f)
            } else currentValue
        }
    }

    @get:Suppress("AutoBoxing")
    var offset: Float? by mutableStateOf(null)
        private set

    fun requireOffset(): Float = checkNotNull(offset) {
        "The offset was read before being initialized. Did you access the offset in a phase " +
                "before layout, like effects or composition?"
    }

    val isAnimationRunning: Boolean get() = animationTarget != null

    var lastVelocity: Float by mutableStateOf(0f)
        private set

    private val minOffset by derivedStateOf { anchors.minOrNull() ?: Float.NEGATIVE_INFINITY }

    private val maxOffset by derivedStateOf { anchors.maxOrNull() ?: Float.POSITIVE_INFINITY }

    private var animationTarget: T? by mutableStateOf(null)

    internal var anchors by mutableStateOf(emptyMap<T, Float>())

    internal var density: Density? = null

    internal fun updateAnchors(newAnchors: Map<T, Float>): Boolean {
        val previousAnchorsEmpty = anchors.isEmpty()
        anchors = newAnchors
        val initialValueHasAnchor = if (previousAnchorsEmpty) {
            val initialValue = currentValue
            val initialValueAnchor = anchors[initialValue]
            val initialValueHasAnchor = initialValueAnchor != null
            if (initialValueHasAnchor) trySnapTo(initialValue)
            initialValueHasAnchor
        } else true
        return !initialValueHasAnchor || !previousAnchorsEmpty
    }

    private suspend fun animateTo(
        targetValue: T,
        velocity: Float = lastVelocity,
    ) {
        val targetOffset = anchors[targetValue]
        if (targetOffset != null) {
            try {
                swipe {
                    animationTarget = targetValue
                    var prev = offset ?: 0f
                    animate(prev, targetOffset, velocity, animationSpec) { value, velocity ->
                        // Our onDrag coerces the value within the bounds, but an animation may
                        // overshoot, for example a spring animation or an overshooting interpolator
                        // We respect the user's intention and allow the overshoot, but still use
                        // DraggableState's drag for its mutex.
                        offset = value
                        prev = value
                        lastVelocity = velocity
                    }
                    lastVelocity = 0f
                }
            } finally {
                animationTarget = null
                val endOffset = requireOffset()
                val endState = anchors
                    .entries
                    .firstOrNull { (_, anchorOffset) -> abs(anchorOffset - endOffset) < 0.5f }
                    ?.key
                this.currentValue = endState ?: currentValue
            }
        } else {
            currentValue = targetValue
        }
    }

    suspend fun settle(velocity: Float) {
        val previousValue = this.currentValue
        val targetValue = computeTarget(
            offset = requireOffset(),
            currentValue = previousValue,
            velocity = velocity
        )
        if (confirmValueChange(targetValue)) {
            animateTo(targetValue, velocity)
        } else {
            // If the user vetoed the state change, rollback to the previous state.
            animateTo(previousValue, velocity)
        }
    }

    fun dispatchRawDelta(delta: Float): Float {
        val currentDragPosition = offset ?: 0f
        val potentiallyConsumed = currentDragPosition + delta
        val clamped = potentiallyConsumed.coerceIn(minOffset, maxOffset)
        val deltaToConsume = clamped - currentDragPosition
        if (abs(deltaToConsume) >= 0) {
            offset = ((offset ?: 0f) + deltaToConsume).coerceIn(minOffset, maxOffset)
        }
        return deltaToConsume
    }

    private fun computeTarget(
        offset: Float,
        currentValue: T,
        velocity: Float
    ): T {
        val currentAnchors = anchors
        val currentAnchor = currentAnchors[currentValue]
        val currentDensity = requireDensity()
        val velocityThresholdPx = with(currentDensity) { velocityThreshold.toPx() }
        return if (currentAnchor == offset || currentAnchor == null) {
            currentValue
        } else if (currentAnchor < offset) {
            // Swiping from lower to upper (positive).
            if (velocity >= velocityThresholdPx) {
                currentAnchors.closestAnchor(offset, true)
            } else {
                val upper = currentAnchors.closestAnchor(offset, true)
                val distance = abs(currentAnchors.getValue(upper) - currentAnchor)
                val relativeThreshold = abs(positionalThreshold(currentDensity, distance))
                val absoluteThreshold = abs(currentAnchor + relativeThreshold)
                if (offset < absoluteThreshold) currentValue else upper
            }
        } else {
            // Swiping from upper to lower (negative).
            if (velocity <= -velocityThresholdPx) {
                currentAnchors.closestAnchor(offset, false)
            } else {
                val lower = currentAnchors.closestAnchor(offset, false)
                val distance = abs(currentAnchor - currentAnchors.getValue(lower))
                val relativeThreshold = abs(positionalThreshold(currentDensity, distance))
                val absoluteThreshold = abs(currentAnchor - relativeThreshold)
                if (offset < 0) {
                    // For negative offsets, larger absolute thresholds are closer to lower anchors
                    // than smaller ones.
                    if (abs(offset) < absoluteThreshold) currentValue else lower
                } else {
                    if (offset > absoluteThreshold) currentValue else lower
                }
            }
        }
    }

    private fun requireDensity() = requireNotNull(density) {
        "SwipeableState did not have a density attached. Are you using Modifier.swipeable with " +
                "this=$this SwipeableState?"
    }

    private suspend fun swipe(
        swipePriority: MutatePriority = MutatePriority.Default,
        action: suspend () -> Unit
    ): Unit = coroutineScope { swipeMutex.mutate(swipePriority, action) }

    private fun trySnapTo(targetValue: T): Boolean = swipeMutex.tryMutate { snap(targetValue) }

    private fun snap(targetValue: T) {
        val targetOffset = anchors[targetValue]
        if (targetOffset != null) {
            dispatchRawDelta(targetOffset - (offset ?: 0f))
            currentValue = targetValue
            animationTarget = null
        } else {
            currentValue = targetValue
        }
    }

    companion object {

    }
}

@ExperimentalMaterial3Api
internal fun fixedPositionalThreshold(threshold: Dp): Density.(distance: Float) -> Float = {
    threshold.toPx()
}

@Stable
@ExperimentalMaterial3Api
internal object SwipeableV2Defaults {

    @ExperimentalMaterial3Api
    val AnimationSpec = SpringSpec<Float>()


    @ExperimentalMaterial3Api
    val VelocityThreshold: Dp = 125.dp



    @ExperimentalMaterial3Api
    val PositionalThreshold: Density.(totalDistance: Float) -> Float =
        fixedPositionalThreshold(56.dp)
}


@ExperimentalMaterial3Api
internal fun interface AnchorChangeHandler<T> {
    fun onAnchorsChanged(
        previousTargetValue: T,
        previousAnchors: Map<T, Float>,
        newAnchors: Map<T, Float>
    )
}

@Stable
private class SwipeAnchorsModifier(
    private val onDensityChanged: (density: Density) -> Unit,
    private val onSizeChanged: (layoutSize: IntSize) -> Unit,
    inspectorInfo: InspectorInfo.() -> Unit,
) : LayoutModifier, OnRemeasuredModifier, InspectorValueInfo(inspectorInfo) {

    private var lastDensity: Float = -1f
    private var lastFontScale: Float = -1f

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        if (density != lastDensity || fontScale != lastFontScale) {
            onDensityChanged(Density(density, fontScale))
            lastDensity = density
            lastFontScale = fontScale
        }
        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) { placeable.place(0, 0) }
    }

    override fun onRemeasured(size: IntSize) {
        onSizeChanged(size)
    }

    override fun toString() = "SwipeAnchorsModifierImpl(updateDensity=$onDensityChanged, " +
            "onSizeChanged=$onSizeChanged)"
}

private fun <T> Map<T, Float>.closestAnchor(
    offset: Float = 0f,
    searchUpwards: Boolean = false
): T {
    require(isNotEmpty()) { "The anchors were empty when trying to find the closest anchor" }
    return minBy { (_, anchor) ->
        val delta = if (searchUpwards) anchor - offset else offset - anchor
        if (delta < 0) Float.POSITIVE_INFINITY else delta
    }.key
}

private fun <T> Map<T, Float>.minOrNull() = minOfOrNull { (_, offset) -> offset }
private fun <T> Map<T, Float>.maxOrNull() = maxOfOrNull { (_, offset) -> offset }


@Stable
class InternalMutatorMutex {
    class Mutator(private val priority: MutatePriority, private val job: Job) {
        fun canInterrupt(other: Mutator) = priority >= other.priority

        fun cancel() = job.cancel()
    }

//    val currentMutator = InternalAtomicReference<Mutator?>(null)
    val currentMutator = java.util.concurrent.atomic.AtomicReference<Mutator?>(null)
    val mutex = Mutex()

    fun tryMutateOrCancel(mutator: Mutator) {
        while (true) {
            val oldMutator = currentMutator.get()
            if (oldMutator == null || mutator.canInterrupt(oldMutator)) {
                if (currentMutator.compareAndSet(oldMutator, mutator)) {
                    oldMutator?.cancel()
                    break
                }
            } else throw CancellationException("Current mutation had a higher priority")
        }
    }
}

internal suspend fun InternalMutatorMutex.mutate(
    priority: MutatePriority = MutatePriority.Default,
    block: suspend () -> Unit
) = coroutineScope {
    val mutator =
        InternalMutatorMutex.Mutator(priority, coroutineContext[Job]!!)

    tryMutateOrCancel(mutator)

    mutex.withLock {
        try {
            block()
        } finally {
            currentMutator.compareAndSet(mutator, null)
        }
    }
}

fun InternalMutatorMutex.tryMutate(block: () -> Unit): Boolean {
    val didLock = mutex.tryLock()
    if (didLock) {
        try {
            block()
        } finally {
            mutex.unlock()
        }
    }
    return didLock
}

