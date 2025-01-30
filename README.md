
# Product Listing

This Android application is built using MVVM architecture and incorporates modern development best practices. It supports offline mode using WorkManager, Dependency Injection (DI) with Koin, and asynchronous operations using Kotlin Coroutines. The UI is efficiently managed using Data Binding.

## Features

* **MVVM Architecture**: Ensures separation of concerns and better code maintainability.

* **WorkManager for Offline Mode**: Handles background tasks and ensures data persistence when the device is offline.

* **Koin for Dependency Injection**: Simplifies dependency management without requiring annotations or reflection.

* **Coroutines for Asynchronous Processing**: Provides an efficient way to handle background tasks.

* **Data Binding**: Allows binding UI components to data sources directly in XML, improving performance and readability.

## Project Setup
### Prerequisites
Ensure you have the following installed:

* Android Studio (latest version recommended)

* Java 8 or higher

* Kotlin (built-in with Android Studio)

* Gradle (latest version recommended)

### Clone the Repository

```sh
git clone <repository-url>
cd <project-directory>
```
### Open in Android Studio

    1. Open Android Studio.

    2. Click on Open an Existing Project.

    3. Navigate to the cloned repository and select the project folder.

    4. Wait for Gradle Sync to complete.

### Configure Dependencies

The project uses Koin for dependency injection, WorkManager for offline handling, and Coroutines for background tasks. Ensure all dependencies are correctly set up in build.gradle.

### Required Dependencies

Make sure the dependencies are added in your build.gradle (Module: app) file
