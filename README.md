# RealEstate (Android)

A professional native Android real estate app scaffold built with Kotlin, Jetpack Compose, Material 3, Hilt (DI), and Navigation. Includes a fake in-memory repository, Home and Details screens, and sample images via Coil.

## Tech Stack
- Kotlin + Android Gradle Plugin 8.4
- Jetpack Compose (Material 3, Navigation)
- Hilt for dependency injection
- Coil for image loading

## Requirements
- JDK 17+
- Android Studio (Hedgehog/Koala or newer)
- Android SDK (API 34 recommended)

## Setup
1. Open the project in Android Studio.
2. Ensure the Android SDK is installed and configured:
   - File > Settings > Appearance & Behavior > System Settings > Android SDK
   - Install Android 34 (and a compatible Build Tools)
3. If building via CLI, set SDK path in `local.properties`:
   ```
   sdk.dir=/absolute/path/to/Android/Sdk
   ```
   Or export ANDROID_HOME/ANDROID_SDK_ROOT.

## Build & Run
- CLI: `./gradlew :app:assembleDebug`
- Android Studio: Run the `app` configuration on an emulator or device.

## Project Structure
- `app/src/main/java/com/example/realestate`
  - `App.kt`: Hilt-enabled Application
  - `MainActivity.kt`: Compose host
  - `navigation/AppNavHost.kt`: Routes and NavHost
  - `data/`: `model/Property`, `repository/` with `PropertyRepository`, `FakePropertyRepository`
  - `ui/home/`: `HomeScreen`, `HomeViewModel`
  - `ui/details/`: `DetailsScreen`, `DetailsViewModel`
  - `ui/theme/`: Material 3 theme and typography
- `app/src/main/res/values/strings.xml`: App name

## Features
- Property list with search (title, address, city, state)
- Favorite toggle
- Details screen with image pager and actions placeholders

## Next Steps
- Search filters (price range, beds/baths, property type)
- Map view (Google Maps) with markers and clustering
- Authentication (Firebase Auth or custom)
- Backend API integration (Retrofit/OkHttp)
- Saved searches and notifications
- Analytics and crash reporting