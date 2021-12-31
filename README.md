## Asteroid Radar

Asteroid Radar is an application built to demonstrate core Kotlin and Android Development skills.

This app utilizes the following libraries:

* [Retrofit](https://square.github.io/retrofit/) to make api calls to an HTTP web service.
* [Moshi](https://github.com/square/moshi) which handles the deserialization of the returned JSON to Kotlin data objects. 
* [Picasso](https://github.com/square/picasso) to load and cache images by URL.
* [Room](https://developer.android.com/training/data-storage/room) for local database storage.
* [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) for background-threading.
  
It leverages the following components from the Jetpack library:

* [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel).
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata).
* [Data Binding](https://developer.android.com/topic/libraries/data-binding/) with binding adapters.
* [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) with the SafeArgs plugin for parameter passing between fragments.
