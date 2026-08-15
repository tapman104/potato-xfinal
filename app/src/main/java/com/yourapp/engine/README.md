# engine/
MPV bridge layer. All MPVLib calls live here and nowhere else.

## Files
| File | Role |
|------|------|
| PlayerSurface.kt | Extends BaseMPVView. Owns surface lifecycle, initOptions, observeProperties |
| MpvController.kt | Public API for playback commands: loadFile, play, pause, seekTo, seekBy, setTrack |
| MpvEventDispatcher.kt | Implements MPVLib.EventObserver. Emits MpvEvent sealed class via SharedFlow |

## Knows
- is.xyz.mpv.MPVLib
- is.xyz.mpv.BaseMPVView
- Coroutine scopes for event emission
- Android SurfaceView lifecycle

## Should NOT know
- ViewModel or UI state
- Room or repository
- Compose
