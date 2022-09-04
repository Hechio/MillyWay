# Milky Way


## Table of Contents

- [Prerequisite](#prerequisite)
- [The App](#theapp)
- [Architecture](#architecture)
- [Testing & Automation](#testing)
- [ScreenShots](#screenshots)
- [Sample App and Source Code](#sampleappandsourcecode)

## Prerequisite

This project uses the Gradle build system. To build this project, use the `fastlane debug_build`
`gradlew build` command after cloning or use "Import Project" in Android Studio.

## The App

A small app allows users to scroll through the list of Milky Way images taken in 2017 from  [NASA API](https://images.nasa.gov/docs/images.nasa.gov_api_docs.pdf).
The app has one Activity namely MainActivity and two fragments which are connected using navigation graph. MainActivity acts as entry point with a NavHostFragment which hold the two fragmnents.

The project has been written in Kotlin language. For network requests, it uses Retrofit with RxJava.

Dagger Hilt has been used for Dependency injection.

## Architecture
The project is built using the MVVM architectural pattern and make heavy use of a couple of Android Jetpack components. Mvvm allows for the separation of concern which also makes testing easier.


## MVVM implementation
The first time the app is opened, the data will be fetched from the backend api service and stored locally with 
the help of Room database.
But if there is no internet or the api service is down, the data will be fetched from the local cache.
This is handled in the repository class.
ViewModel is basically responsible for updating the UI (Activity/Fragment) with the data changes.
The ViewModel will initialise an instance of the Repository class and update the UI based with this data.



## Testing
All tests are under the Android Test package. All the tests are run using JUnit.
To run tests using fastlane run `fastlane test` command in your CL.
Test automation have also been achieved using CircleCi.

## ScreenShots


The app is available in both day and night theme.


<img src="https://user-images.githubusercontent.com/47601553/188320273-43024d8d-87f0-4e07-88e6-30df51525b51.jpg" width="200" style="max-width:100%;"> <img src="https://user-images.githubusercontent.com/47601553/188320309-78dad7d5-408b-46c7-86c9-36b08b9c2e31.jpg" width="200" style="max-width:100%;">   <img src="https://user-images.githubusercontent.com/47601553/188320401-eaf3fc67-290b-4ca7-a05f-abd1d8561d0c.jpg" width="200" style="max-width:100%;">  <img src="https://user-images.githubusercontent.com/47601553/188320392-60cc96e4-0762-4cd4-ac4a-1051c96a3ec3.jpg" width="200" style="max-width:100%;"> 

Libraries used in the whole application are:

- [Viewmodel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Manage UI related data in a lifecycle conscious way 
- [RxJava](https://github.com/ReactiveX/RxJava) - RxJava is a Java VM implementation of Reactive Extensions: a library for composing asynchronous and event-based programs by using observable sequences.
- [Lottie](http://airbnb.io/lottie/#/) -Lottie is a library for Android, iOS, Web, and Windows that parses Adobe After Effects animations exported as json with Bodymovin and renders them natively on mobile and on the web!.
- [Dagger Hilt](https://dagger.dev/hilt/) - Used for Dependency injection
    - To simplify Dagger-related infrastructure for Android apps.
    - To create a standard set of components and scopes to ease setup, readability/understanding, and code sharing between apps.
    - To provide an easy way to provision different bindings to various build types (e.g. testing, debug, or release).

- [Retrofit](https://square.github.io/retrofit/) - Turns your HTTP API into a Java interface.
- [Fastlane](https://docs.fastlane.tools/getting-started/android/setup/) -  Automate beta deployments and releases for Android apps. 🚀
- [Mockito](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html) - Enables mock creation, verification and stubbing for testing
- [CircleCi](https://circleci.com/continuous-integration/) - Achieving continuous integration
- [MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver) - A scriptable web server for testing HTTP clients

## Sample App

Clone the project and run `fastlane debug` in terminal under the project directory to generate app or run in from the android studio.

[Debug.APK](https://drive.google.com/file/d/106ReAHeyjL4rIzjtcQdxODMs8fhRXPa2/view?usp=sharing) - Access to the project's github reporitory
