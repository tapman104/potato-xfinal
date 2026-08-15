# repository/
Bridges data sources to the rest of the app.

## Files
| File | Role |
|------|------|
| MediaRepository.kt | Scans file system for video files. Returns List<MediaFile> |

## Knows
- File system API (java.io.File, DocumentFile)
- Video file extensions: mp4, mkv, avi, mov, webm, m4v, ts, flv
- Room DAOs (once data/ layer is wired)

## Should NOT know
- MPVLib or engine/
- ViewModel or UI state
- Compose
