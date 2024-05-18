# EBOOK

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
- [Usage](#usage)
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

## Usage

- Fragment Home
<img width="200" src="https://github.com/Tuh11343/Ebook_Kotlin/assets/86562442/feb3355e-a9d7-4d25-9f54-d9150405382d" />

<img width="200" src="https://github.com/Tuh11343/Ebook_Kotlin/assets/86562442/f1766c8b-9df1-43dc-bfd7-1e3ce806c533" />


- FragmentSearch + Filter
<img width="200" src="https://github.com/Tuh11343/Ebook_Kotlin/assets/86562442/5e5dc54c-58ee-4d4f-8753-784d1da8280d" />

<img width="200" src="https://github.com/Tuh11343/Ebook_Kotlin/assets/86562442/6227283b-0cf1-49b7-b453-39e2248d0a13" />


- FragmentFavorite
<img width="200" src="https://github.com/Tuh11343/Ebook_Kotlin/assets/86562442/ca9e6dfc-463e-4892-9762-30a98e3de1cd" />


- FragmentUser
<img width="200" src="https://github.com/Tuh11343/Ebook_Kotlin/assets/86562442/bb083f96-4ff7-421e-94a3-9f1f627fb5f7"/>

<img width="200" src="https://github.com/Tuh11343/Ebook_Kotlin/assets/86562442/004f093d-cdb5-424d-879d-6f3d308c0c78"/>

<img width="200" src="https://github.com/Tuh11343/Ebook_Kotlin/assets/86562442/c7ce9938-c1fb-46cf-b706-8a7bb1a3dcb8"/>

<img width="200" src="https://github.com/Tuh11343/Ebook_Kotlin/assets/86562442/b675056a-42eb-4e4b-b75c-cf1287497c0d"/>


- Fragment Subscription + Payment
<img width="200" src="https://github.com/Tuh11343/Ebook_Kotlin/assets/86562442/d6df3a54-15da-4f7b-be38-ea22034d4044"/>

<img width="200" src="https://github.com/Tuh11343/Ebook_Kotlin/assets/86562442/d310b964-cbc8-414c-97fb-52736bfd5b04"/>

- FragmentAuthor
<img width="200" src="https://github.com/Tuh11343/Ebook_Kotlin/assets/86562442/b60770d0-fd1f-4847-9fb1-816a3f2ae3a3"/>


- FragmentDetailBook
<img width="200" src="https://github.com/Tuh11343/Ebook_Kotlin/assets/86562442/217035c7-4c86-4d0d-94df-88b7b428fbd1"/>


- FragmentReadBook + Lyric
<img width="200" src="https://github.com/Tuh11343/Ebook_Kotlin/assets/86562442/0cc763a8-ee27-461b-b55a-7795e3dee550"/>

<img width="200" src="https://github.com/Tuh11343/Ebook_Kotlin/assets/86562442/f84c1416-4eee-4f54-b1bb-7161bf4319b9"/>


- MediaPlayer
<img width="200" src="https://github.com/Tuh11343/Ebook_Kotlin/assets/86562442/a45a01ec-ad1b-4cfe-b92b-0a7a5ecfd296"/>


## Acknowledgements

- [Android Developer](https://developer.android.com/)
- [Firebase](https://firebase.google.com/)

## License

[MIT](https://choosealicense.com/licenses/mit/)

## Support

For support, email dotuan2k2@gmail.com for supports.
