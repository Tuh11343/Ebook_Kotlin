# EBOOk

Mobile app for reading book online

# ATTENTION

MAKE SURE TO FOLLOW THE README RUN LOCALLY GUIDELINE TO AVOID CONFLICT

## Introduction

Mobile app for reading book online

## File structure

```
.
|── java
    ├── adapter
    |   ├── BookViewNormalAdapter.kt
    ├── fragment
    |   ├── FragmentHome.kt
    ├── listener
    |   ├── IBookListener.kt
    │   └── FilterListener.kt
    ├── model
    |   ├── Account.kt
    │   └── Book.kt
    ├── network
    │   ├── api
    |       ├── BookAPIService.kt
    |   └── RetrofitClient.kt
    ├── repository
    |   └── BookRepository.kt
    ├── service
    │   └── MusicService.kt
    ├── view
    |   ├── SplashScreen.kt
    │   └── MainActivity.kt
    |── viewmodels
    |    ├── MainViewModel.kt
    |    └── HomeViewModel.kt
└── res
    ├── anim
    |   ├── rotate.xml
    ├── drawable
    ├── layout
    ├── menu
    ├── values
    ├── ...
    └── ...
```

## Table of Contents

- [Tech Stack](#techstack)
- [Features](#features)
- [Environment Variables](#environment-variables)
- [Run Locally](#run-)
- [Acknowledgements](#acknowledgements)
- [License](#license)

## Tech Stack

- Android Studio
- Firebase (Authentication)

## Features

- Will update later

## Environment Variables

To run this project you will need to run backend api at https://github.com/Tuh11343/EBook_Backend

## Run Locally
Clone the project

```bash
  git clone https://github.com/Tuh11343/Ebook_Kotlin.git
```
- Open project on android studio
- Connect to the same wifi and change your URL network/RetrofitClient.kt
  Example
  - Turn on cmd and run ipconfig to get the IPV4
    
## Acknowledgements

- [Android Developer](https://developer.android.com/)
- [Firebase](https://firebase.google.com/)

## License

[MIT](https://choosealicense.com/licenses/mit/)

## Support

For support, email dotuan2k2@gmail.com for supports.
