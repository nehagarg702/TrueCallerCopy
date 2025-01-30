# TruecallerCopy

## Overview

TruecallerCopy is an Android application developed using **Kotlin** and **MVVM architecture**. The app provides a **Caller Identification Popup** for incoming calls, similar to the Truecaller app, displaying the caller's name, phone number, location, and spam status based on dummy data or a mock API.

## Features

- **Caller Identification Popup**:
  - Displays a popup during incoming calls with:
    - **Caller Name** (fetched from a mock API).
    - **Phone Number**.
    - **Location** (dummy data/mock API).
    - **Spam Identification** (flagged as spam or not).
  - The popup runs seamlessly over the default incoming call screen and is dismissible.
  - Popup is interactable, allowing the user to view or dismiss the information.
  - **Permissions**: Requires `READ_CALL_LOG`, `READ_CONTACTS`, `SYSTEM_ALERT_WINDOW`, and **auto-restart permission**.
  
- **Mock Data**: Currently, the app uses hardcoded values for caller details such as name, location, and spam status. This can be replaced with a real API in production.
  
- **Overlay Popup**: The popup requires the `SYSTEM_ALERT_WINDOW` permission to display over other apps, including the default call screen.

- **Incoming Calls Only**: The app only supports incoming calls for now and does not handle outgoing calls.

- **Auto-Restart**: After granting necessary permissions, the app automatically restarts to ensure that everything functions correctly. The app also requires **auto-restart permission** to ensure the overlay will be shown even if the app is not running in the background.

## Tech Stack

- **Language**: Kotlin
- **Architecture**: MVVM
- **Dependency Injection**: Koin
- **Networking**: Retrofit (for future API integration)
- **Image Loading**: Coil
- **UI**: Jetpack Compose
- **Data Handling**: Mock API (for caller details)
  
## Prerequisites

- **Android Studio** (latest version recommended)
- **Gradle**
- Android device or emulator running API level 21 or higher

## Getting Started

1. **Clone the Repository**:
   ```sh
   git clone https://github.com/nehagarg702/TrueCallerCopy

   ### Open the Project in Android Studio:

2. Open **Android Studio**.
3. Click on **File > Open** and select the project folder.

### Build the Project:

1. Sync Gradle dependencies and wait for the project to build.

### Run the App:

1. Connect a device or start an emulator.
2. Click the **Run** button in Android Studio to install and run the app.

## Known Issues & Future Improvements

- **Popup Design**: The popup design can be enhanced for better user experience.
- **Real-Time Caller Identification**: The app currently uses mock data, but this can be replaced with a live API to fetch actual caller details.
- **Customizable Popup Behavior**: Future updates may include settings for users to customize the popup behavior (e.g., themes, visibility).

## License

This project is open-source and available under the **MIT License**.

---

For any questions or contributions, feel free to reach out!
