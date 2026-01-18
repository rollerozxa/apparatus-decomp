<h1 align="center"><img src="android/res/drawable-mdpi-v4/icon.png" align="absmiddle"> Apparatus</h1>
<p align="center"><img src="https://github.com/user-attachments/assets/998cdbe2-398d-48ce-8166-f2365a495814" height=480></p>

Apparatus is a physics-based puzzle and sandbox game by Bithack originally released for Android in 2011. This project aims to be a decompilation of the game, reproducing the game's source code as well as offering improvements over the last version of the game, such as running the game on desktop platforms.

For more information about the original game, see [apparatus.voxelmanip.se](http://apparatus.voxelmanip.se/).

## Building
These instructions give a very brief overview of how to get the game built and running.

### Android
After you have some version of OpenJDK installed and the Android SDK command-line tools downloaded, you can build a debug version of the game like such

```bash
./gradlew :android:assembleDebug
```

Then install it on a connected device or emulator with:

```bash
./gradlew :android:installDebug
```

To make a release build, you can use `./gradlew :android:assembleRelease`. You will then have to sign it yourself.

### Desktop
The desktop version is rather experimental and some things that work in the Android version do not work here due to the lack of dialogs.

To build the desktop version, run:

```bash
./gradlew :desktop:build
```

Then to run it use:

```bash
./gradlew :desktop:run
```
