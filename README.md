# SpendSense - Android Expense Management App

An intelligent expense tracking application that automatically parses bank SMS messages to categorize and analyze your spending habits.

## Features

- 🔐 **Google Sign-In Authentication**
- 💬 **Automatic SMS Parsing** - Reads bank transaction messages
- 👥 **Contacts Integration** - Identifies merchants from your contacts
- 📊 **Smart Categorization** - AI-powered expense categorization
- 📈 **Analytics Dashboard** - Visual insights into spending patterns
- 🔒 **Privacy First** - All data processed locally on device

## Prerequisites

To build this APK, you need:

1. **Android Studio** (Arctic Fox or later)
2. **JDK 11 or higher**
3. **Android SDK** (API 24+)
4. **Gradle 8.0+**

## Building the APK

### Method 1: Using Android Studio (Recommended)

1. **Open the Project**
   ```bash
   Open Android Studio → Open → Select SpendSenseApp folder
   ```

2. **Sync Gradle**
   - Wait for Android Studio to sync Gradle files
   - Resolve any dependency issues

3. **Build APK**
   - Go to `Build → Build Bundle(s) / APK(s) → Build APK(s)`
   - Or use shortcut: `Ctrl+Shift+A` (Windows/Linux) or `Cmd+Shift+A` (Mac)
   - Type "Build APK" and select it

4. **Locate APK**
   - APK will be in: `app/build/outputs/apk/debug/app-debug.apk`
   - Click "locate" in the notification to open the folder

### Method 2: Using Command Line

1. **Navigate to Project Directory**
   ```bash
   cd SpendSenseApp
   ```

2. **Make Gradlew Executable** (Linux/Mac)
   ```bash
   chmod +x gradlew
   ```

3. **Build Debug APK**
   ```bash
   ./gradlew assembleDebug
   ```

4. **Build Release APK** (unsigned)
   ```bash
   ./gradlew assembleRelease
   ```

5. **Find APK**
   - Debug: `app/build/outputs/apk/debug/app-debug.apk`
   - Release: `app/build/outputs/apk/release/app-release-unsigned.apk`

### Method 3: Signing Release APK

For production release, you need to sign the APK:

1. **Generate Keystore**
   ```bash
   keytool -genkey -v -keystore spendsense.keystore -alias spendsense -keyalg RSA -keysize 2048 -validity 10000
   ```

2. **Create `keystore.properties` in project root**
   ```properties
   storePassword=YourStorePassword
   keyPassword=YourKeyPassword
   keyAlias=spendsense
   storeFile=../spendsense.keystore
   ```

3. **Update `app/build.gradle`** (add before android block)
   ```gradle
   def keystorePropertiesFile = rootProject.file("keystore.properties")
   def keystoreProperties = new Properties()
   keystoreProperties.load(new FileInputStream(keystorePropertiesFile))
   
   android {
       signingConfigs {
           release {
               keyAlias keystoreProperties['keyAlias']
               keyPassword keystoreProperties['keyPassword']
               storeFile file(keystoreProperties['storeFile'])
               storePassword keystoreProperties['storePassword']
           }
       }
       buildTypes {
           release {
               signingConfig signingConfigs.release
               minifyEnabled false
               proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
           }
       }
   }
   ```

4. **Build Signed APK**
   ```bash
   ./gradlew assembleRelease
   ```

## Google Sign-In Setup

To enable Google Sign-In in production:

1. **Create Firebase Project**
   - Go to https://console.firebase.google.com/
   - Create new project: "SpendSense"

2. **Add Android App**
   - Package name: `com.spendsense.app`
   - Download `google-services.json`
   - Place in `app/` directory

3. **Get SHA-1 Certificate**
   ```bash
   keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
   ```

4. **Add SHA-1 to Firebase**
   - Copy SHA-1 from above command
   - Add to Firebase Console → Project Settings → Your apps → SHA certificate fingerprints

5. **Enable Google Sign-In**
   - Firebase Console → Authentication → Sign-in method
   - Enable "Google" provider

## Permissions

The app requires these permissions (automatically requested):

- **READ_SMS** - To read bank transaction messages
- **READ_CONTACTS** - To identify merchants from contacts
- **INTERNET** - For Google Sign-In

## Installation

### Install on Device

1. **Enable Unknown Sources**
   - Settings → Security → Unknown Sources (Enable)
   - Or Settings → Apps → Special Access → Install Unknown Apps

2. **Transfer APK to Device**
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```
   Or copy APK to device and tap to install

3. **Grant Permissions**
   - App will request SMS and Contacts permissions on first launch

## Project Structure

```
SpendSenseApp/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── assets/
│   │       │   └── index.html          # WebView UI
│   │       ├── java/com/spendsense/app/
│   │       │   └── MainActivity.java   # Main Activity with native bridges
│   │       ├── res/
│   │       │   ├── drawable/
│   │       │   │   └── ic_launcher.xml # App icon
│   │       │   └── values/
│   │       │       ├── strings.xml
│   │       │       └── styles.xml
│   │       └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── gradle.properties
```

## Troubleshooting

### Gradle Sync Failed
- Update Android Studio to latest version
- Update Gradle wrapper: `./gradlew wrapper --gradle-version=8.0`
- Invalidate caches: File → Invalidate Caches / Restart

### Build Errors
- Clean project: `./gradlew clean`
- Rebuild: `./gradlew build --refresh-dependencies`

### Google Sign-In Not Working
- Check `google-services.json` is in `app/` folder
- Verify SHA-1 certificate is added to Firebase
- Check package name matches Firebase configuration

### SMS Reading Not Working
- Ensure READ_SMS permission is granted
- Check Android version (API 24+ required)
- Test on physical device (emulator may not have SMS)

## Development

### Testing Locally
The app includes a web fallback for testing without Android:
- Open `app/src/main/assets/index.html` in a browser
- All features work except SMS reading and Google Sign-In

### Debugging
```bash
# View logs
adb logcat | grep SpendSense

# View detailed logs
adb logcat -s SpendSense:V
```

## Requirements

- **Min SDK**: Android 7.0 (API 24)
- **Target SDK**: Android 13 (API 33)
- **Permissions**: SMS, Contacts, Internet

## License

MIT License - See LICENSE file for details

## Support

For issues or questions:
- Open an issue on GitHub
- Email: support@spendsense.app

## Version History

### v1.0.0 (Current)
- Initial release
- Google Sign-In authentication
- Automatic SMS parsing
- Custom categories
- Analytics dashboard
- CRED-inspired dark UI
