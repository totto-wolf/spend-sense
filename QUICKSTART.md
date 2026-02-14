# SpendSense - Quick Start Guide

## 📦 What's Included

You have received the complete Android project source code for SpendSense. This includes:

- ✅ Full Android Studio project structure
- ✅ Java source code with native SMS/Contacts integration
- ✅ WebView-based UI with CRED-inspired design
- ✅ Google Sign-In integration
- ✅ Build configuration files
- ✅ App icon and resources

## 🚀 Quickest Way to Build APK

### Option 1: Using Android Studio (EASIEST - 5 minutes)

1. **Download & Install Android Studio**
   - Get it from: https://developer.android.com/studio
   - Install with default settings

2. **Extract the ZIP**
   - Unzip `SpendSenseApp.zip` to any location
   - Example: `C:\Projects\SpendSenseApp` or `~/Projects/SpendSenseApp`

3. **Open in Android Studio**
   - Launch Android Studio
   - Click "Open" → Navigate to extracted `SpendSenseApp` folder → Click OK
   - Wait for Gradle sync (2-3 minutes first time)

4. **Build the APK**
   - Click `Build` menu → `Build Bundle(s) / APK(s)` → `Build APK(s)`
   - Wait 30-60 seconds
   - Click "locate" in the notification that appears

5. **Get Your APK**
   - APK location: `app/build/outputs/apk/debug/app-debug.apk`
   - Transfer this file to your Android phone
   - Install and enjoy!

### Option 2: Command Line (For Developers)

```bash
# Extract ZIP
unzip SpendSenseApp.zip
cd SpendSenseApp

# Build APK (Linux/Mac)
chmod +x gradlew
./gradlew assembleDebug

# Build APK (Windows)
gradlew.bat assembleDebug

# APK will be in: app/build/outputs/apk/debug/app-debug.apk
```

## 📱 Installing on Your Phone

### Method 1: USB Cable
```bash
# Connect phone with USB debugging enabled
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Method 2: Direct Install
1. Copy `app-debug.apk` to your phone
2. Enable "Install from Unknown Sources" in Settings
3. Tap the APK file to install
4. Grant SMS and Contacts permissions when prompted

## ⚠️ Important Notes

### For Testing (Debug Build)
The debug APK works immediately with:
- ✅ SMS reading (requires permission)
- ✅ Contacts reading (requires permission)
- ✅ Full UI and analytics
- ⚠️ Google Sign-In requires Firebase setup (see below)

### Google Sign-In Setup (Optional but Recommended)

If you want real Google Sign-In (not the demo mode):

1. **Go to Firebase Console**
   - https://console.firebase.google.com/
   - Create new project: "SpendSense"

2. **Add Android App**
   - Click "Add app" → Android
   - Package name: `com.spendsense.app`
   - Download `google-services.json`

3. **Add google-services.json**
   - Place file in: `SpendSenseApp/app/google-services.json`

4. **Get Debug SHA-1**
   ```bash
   keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
   ```

5. **Add SHA-1 to Firebase**
   - Copy SHA-1 from above
   - Firebase Console → Project Settings → Your App → Add Fingerprint

6. **Enable Google Sign-In**
   - Firebase → Authentication → Get Started
   - Enable "Google" provider
   - Save

7. **Rebuild APK**
   - Follow "Quick Build" steps above again

## 🐛 Troubleshooting

### "Gradle sync failed"
- **Solution**: Update Android Studio to latest version
- Or: `./gradlew clean` then rebuild

### "SDK not found"
- **Solution**: Install Android SDK via Android Studio
- Tools → SDK Manager → Install API 33

### "Build failed"
- **Solution**: Clean and rebuild
  ```bash
  ./gradlew clean assembleDebug
  ```

### Google Sign-In shows "Demo User"
- This is normal without Firebase setup
- Follow "Google Sign-In Setup" above for real authentication

### SMS not reading
- Ensure READ_SMS permission is granted in app
- Test on real device (emulator may not have SMS)
- Check Android version is 7.0+ (API 24+)

## 📋 System Requirements

### For Building:
- **OS**: Windows 10+, macOS 10.14+, or Ubuntu 18.04+
- **RAM**: 8GB minimum (16GB recommended)
- **Disk**: 10GB free space
- **Internet**: Required for downloading dependencies

### For Running:
- **Android Version**: 7.0 (Nougat) or higher
- **Permissions**: SMS, Contacts

## 📞 Need Help?

If you encounter issues:

1. **Check README.md** - Detailed troubleshooting guide
2. **Check build logs** - Look for specific error messages
3. **Clean and rebuild** - Often fixes mysterious issues
4. **Update Android Studio** - Many issues are version-related

## 🎯 What Works Out of the Box

✅ Complete UI with dark theme
✅ SMS parsing (with permission)
✅ Transaction categorization
✅ Analytics dashboard
✅ Custom categories
✅ Local data storage
✅ Progress tracking
✅ Demo sign-in mode

## 🔧 What Needs Setup (Optional)

🔸 Real Google Sign-In (needs Firebase)
🔸 Release signing (for Play Store)
🔸 Custom branding
🔸 API integrations

## 📄 File Locations

```
SpendSenseApp/
├── app/
│   ├── src/main/
│   │   ├── assets/index.html          ← Your web app
│   │   ├── java/.../MainActivity.java ← Native code
│   │   └── AndroidManifest.xml        ← Permissions
│   └── build.gradle                   ← App config
└── README.md                          ← Full documentation
```

## ⏱️ Build Time Estimates

- **First time**: 5-10 minutes (downloading dependencies)
- **Subsequent builds**: 30-60 seconds
- **Clean builds**: 2-3 minutes

## 🎉 Success Checklist

After building, you should have:
- [x] `app-debug.apk` file created
- [x] APK size around 5-8 MB
- [x] Installable on Android 7.0+
- [x] SMS parsing works with permission
- [x] UI loads and looks great
- [x] Analytics show after parsing

---

**You're all set! Build your APK and start tracking expenses!** 🚀

For detailed documentation, see README.md
For troubleshooting, check the "Troubleshooting" section in README.md
