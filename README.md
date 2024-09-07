# Wi-Fi Scanner Android Application

## **Overview**

This Android application scans for all available Wi-Fi signals. If no signals are found, it loads Wi-Fi data from a Room database. If the Room database is also empty, it generates and displays fake Wi-Fi signals. The app features a house layout with rooms, where Wi-Fi signals are scattered across the layout. Users can interact with the app to select points of interest by clicking on the area.

## **Features**

- **Wi-Fi Scanning**: Scans for all available Wi-Fi signals.
- **Room Data Integration**: Loads Wi-Fi data from a Room database if available.
- **Fake Data**: Generates and displays fake Wi-Fi signals if no real data is found.
- **House Layout**: Visual representation of a house layout with rooms.
- **Custom Views**: Includes custom views for map overlays and scattered Wi-Fi markers.
- **Point of Interest Selection**: Users can click on the layout to select a point of interest.

## **Tech Stack**

- **Language**: Kotlin
- **Database**: Room Database
- **Dependency Injection**: Hilt
- **Build Configuration**: Gradle with `libs.versions.toml`
- **Custom Views**: Custom view for scattered Wi-Fi markers and map overlay
- **Layout**: DSL for Gradle configuration

## **Setup**

1. **Clone the Repository**

   ```bash
   git clone https://github.com/Dkathayat/Wi-Fi-Scanner-Android-Application
