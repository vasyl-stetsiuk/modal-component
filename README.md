# Modal Component

A flexible and customizable modal component library for Compose Multiplatform with support for Android, iOS, Desktop, Web (JS), and WebAssembly.

[![Maven Central](https://img.shields.io/maven-central/v/dev.stetsiuk/compose-modal.svg)](https://search.maven.org/artifact/dev.stetsiuk/compose-modal)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Platform Support

| Platform | Status |
|----------|--------|
| Android  | ✅ Supported |
| iOS      | ✅ Supported (arm64, x64, simulatorArm64) |
| Desktop  | ✅ Supported (JVM) |
| Web (JS) | ✅ Supported |
| WebAssembly | ✅ Supported |

## Preview

<p align="center">
  <img src="media/preview.gif" alt="Modal Component Demo" width="300"/>
</p>

## Features

- **Multiplatform Support**: Works seamlessly across Android, iOS, Desktop, Web (JS), and WebAssembly
- **Animated Transitions**: Smooth animations for showing and hiding modals with customizable animation specs
- **Visual Effects**: Background blur, tint, and scale transformations on underlying content
- **Multiple Modals**: Support for stacking multiple modals with accumulated visual effects
- **Gesture Handling**: Built-in support for dismissing on back press and outside click
- **Flexible Positioning**: Configurable content alignment (bottom sheet, center dialog, etc.)
- **State Management**: Powerful state management with visibility ratio tracking
- **Customizable**: Full control over colors, shapes, elevation, and animations

## Installation

Add the dependency to your `build.gradle.kts`:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("dev.stetsiuk:compose-modal:1.0.0")
        }
    }
}
```

## Quick Start

### Basic Usage

```kotlin
@Composable
fun App() {
    ProvideModalComponentHost {
        val modalState = rememberModalComponentState()
        val scope = rememberCoroutineScope()

        // Your app content
        Column {
            Text("Main Content")
            Button(onClick = { scope.launch { modalState.show() } }) {
                Text("Show Modal")
            }
        }

        // Modal component
        ModalComponent(
            state = modalState,
            onDismissRequest = { scope.launch { modalState.hide() } }
        ) {
            Text("Modal Content", modifier = Modifier.padding(24.dp))
        }
    }
}
```

### Bottom Sheet Example

```kotlin
@Composable
fun BottomSheetExample() {
    ProvideModalComponentHost {
        val state = rememberModalComponentState(initialVisibilityRatio = 0f)
        val scope = rememberCoroutineScope()

        // Your content here
        MyAppContent()

        ModalComponent(
            state = state,
            color = Color.White,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            hostConfigs = ModalComponentHostConfig.default(),
            onDismissRequest = { scope.launch { state.hide() } }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text("Bottom Sheet Title", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Bottom sheet content goes here")
            }
        }
    }
}
```

### Custom Animations

```kotlin
// Slide up animation
Image(
    modifier = Modifier
        .fillMaxWidth()
        .offset {
            IntOffset(
                x = 0,
                y = (200.dp - (200.dp * state.visibilityRatio)).roundToPx()
            )
        }
        .alpha(state.visibilityRatio),
    painter = painterResource(Res.drawable.image),
    contentDescription = null
)
```

## API Reference

### ModalComponent

Main composable for creating modals.

```kotlin
@Composable
fun ModalComponent(
    state: ModalComponentState,
    modifier: Modifier = Modifier,
    color: Color = Color.Transparent,
    shape: Shape = RectangleShape,
    contentColor: Color = contentColorFor(color),
    shadowElevation: Dp = 0.dp,
    tonalElevation: Dp = 0.dp,
    border: BorderStroke? = null,
    hostConfigs: ModalComponentHostConfig = ModalComponentHostConfig.default(),
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
)
```

**Parameters:**
- `state`: State object controlling modal visibility and animations
- `modifier`: Modifier for the modal surface
- `color`: Background color of the modal
- `shape`: Shape of the modal (e.g., RoundedCornerShape)
- `contentColor`: Color of content inside the modal
- `shadowElevation`: Elevation for shadow
- `tonalElevation`: Elevation for tonal overlay
- `border`: Optional border for the modal
- `hostConfigs`: Configuration for background effects
- `dismissOnBackPress`: Whether to dismiss on back button press
- `dismissOnClickOutside`: Whether to dismiss when clicking outside
- `onDismissRequest`: Callback invoked when dismiss is requested
- `content`: Content to display inside the modal

### ModalComponentState

State management for modal visibility and animations.

```kotlin
class ModalComponentState(
    initialVisibilityRatio: Float = 0f
) {
    val visibilityRatio: Float // Current visibility ratio (0f to 1f)
    val isVisible: Boolean // Whether modal is visible
    val indexInStack: Int? // Position in modal stack

    suspend fun show(animationSpec: AnimationSpec<Float> = tween(300))
    suspend fun hide(animationSpec: AnimationSpec<Float> = tween(300))
    suspend fun snapTo(targetValue: Float)
}
```

**Factory function:**
```kotlin
@Composable
fun rememberModalComponentState(
    initialVisibilityRatio: Float = 0f
): ModalComponentState
```

### ModalComponentHostConfig

Configuration for background visual effects.

```kotlin
data class ModalComponentHostConfig(
    val contentAlignment: Alignment,      // Alignment of modal content
    val backgroundBlur: Dp,                // Blur amount for background
    val backgroundTint: Color,             // Tint color for background
    val backgroundScaleRatio: Float        // Scale ratio for background (>1f zooms in)
)
```

**Default configuration:**
```kotlin
ModalComponentHostConfig.default()
```

### ProvideModalComponentHost

Root composable that provides modal hosting capabilities.

```kotlin
@Composable
fun ProvideModalComponentHost(
    modifier: Modifier = Modifier,
    state: ModalComponentHostState = rememberModalComponentHostState(),
    content: @Composable () -> Unit
)
```

## Advanced Usage

### Multiple Modals

You can stack multiple modals, and each will apply its visual effects cumulatively:

```kotlin
val states = List(3) { rememberModalComponentState(false) }

ProvideModalComponentHost {
    MyContent()

    states.forEachIndexed { index, state ->
        ModalComponent(
            state = state,
            onDismissRequest = { scope.launch { state.hide() } }
        ) {
            Text("Modal $index")
        }
    }
}
```

### Custom Background Effects

Create a custom configuration for different modal styles:

```kotlin
// Center dialog with strong blur
val centerDialogConfig = ModalComponentHostConfig(
    contentAlignment = Alignment.Center,
    backgroundBlur = 20.dp,
    backgroundTint = Color.Black.copy(0.4f),
    backgroundScaleRatio = 1.02f
)

// Side sheet with minimal effects
val sideSheetConfig = ModalComponentHostConfig(
    contentAlignment = Alignment.CenterEnd,
    backgroundBlur = 8.dp,
    backgroundTint = Color.Black.copy(0.05f),
    backgroundScaleRatio = 1.0f
)
```

### Custom Animation Specs

Control animation timing and curves:

```kotlin
// Fast animation
scope.launch {
    state.show(animationSpec = tween(150, easing = FastOutSlowInEasing))
}

// Spring animation
scope.launch {
    state.show(animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    ))
}
```

### Manual Control

Use `visibilityRatio` for gesture-driven interactions:

```kotlin
Slider(
    value = state.visibilityRatio,
    onValueChange = { scope.launch { state.snapTo(it) } }
)
```

## Requirements

- **Kotlin**: 1.9.0 or higher
- **Compose Multiplatform**: Latest stable version
- **Minimum SDK (Android)**: API 24
- **iOS Deployment Target**: iOS 14.0+

## License

```
Copyright 2024 Vasyl Stetsiuk

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Author

**Vasyl Stetsiuk**
- Email: stecyuk.vasil@gmail.com
- GitHub: [@vasyl-stetsiuk](https://github.com/vasyl-stetsiuk)

## Links

- [GitHub Repository](https://github.com/vasyl-stetsiuk/modal-component)
- [Issue Tracker](https://github.com/vasyl-stetsiuk/modal-component/issues)
- [Maven Central](https://search.maven.org/artifact/dev.stetsiuk/compose-modal)