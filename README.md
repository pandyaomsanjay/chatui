# 💬 Chat Clone

A modern, feature‑rich Android messaging application inspired by WhatsApp and built with Firebase. This project delivers a complete real‑time chat experience with phone authentication, one‑to‑one messaging, media sharing, voice notes, status updates, group chats, and a polished Material Design 3 UI.

---

## 📱 Overview

**Chat Clone** is a production‑ready Android application developed with Android Studio and Firebase. It demonstrates how to build a scalable messaging platform with real‑time data sync, user profiles, push notifications, and an intuitive WhatsApp‑style interface.

---

## ✨ Features

### 🔐 Authentication

- **Phone Number Login** – enter your phone number (country picker included)
- **No OTP (Demo Mode)** – for simplified testing, verification is bypassed (you can enable Firebase Phone Auth later)
- **User Profile Setup** – set display name, bio, and profile picture on first login
- User data stored in Firebase Firestore

### 💬 Real‑Time Chat

- **One‑to‑One Messaging** – send and receive text messages instantly
- **Message Status** – delivered and seen receipts (✓✓)
- **Typing Indicator** – see when the other user is typing
- **Online / Last Seen** – real‑time online status with timestamps
- **Message Timestamps** – accurate time display per message

### 📎 Media Sharing

- **Images** – pick from gallery or capture with camera; preview before sending
- **Videos** – share video files (UI ready)
- **Documents / PDFs** – attach files (UI ready)
- **Audio Files** – send audio notes (UI ready)
- **Location** – share location (UI ready)
- All media stored in Firebase Storage

### 🎤 Voice Messages

- Record voice notes with a simple tap
- Playback with play/pause controls
- Duration display and waveform animation (simulated)

### 📌 Chat Management

- **Pin / Archive** – keep important chats at the top (UI ready)
- **Mute Notifications** – per‑chat mute (UI ready)
- **Block / Report** – block or report a user (UI ready)

### 📱 Status (Stories)

- Upload image, video, or text status
- View statuses from your contacts
- Auto‑delete after 24 hours (Firestore expiration logic)

### 👥 Groups & Communities

- Create groups and add members
- Group icon and description
- Admin controls (promote/demote members)
- Community announcements (UI ready)

### 📞 Calls (UI)

- Voice call and video call buttons
- Call history log (missed, outgoing, incoming)
- Call UI screens (ready for WebRTC integration)

### 🔔 Push Notifications

- Firebase Cloud Messaging integration
- Notify for new messages, calls, and status updates
- Device token management

### 🛡️ Privacy & Security

- **Privacy Settings** – control who can see your last seen, profile photo, and status
- **Blocked Users** – manage blocked contacts
- **Two‑Step Verification** – (UI ready)
- Secure Firebase Security Rules

### 🎨 Modern UI/UX

- **Material Design 3** with smooth animations
- **Dark / Light Theme** – automatically adapts to system preference
- **WhatsApp‑style Chat Bubbles** – sent (green) and received (white)
- **Bottom Navigation** – Chats, Status, Communities, Calls, Settings
- **Top App Bar** – app logo, search, camera, and menu icons
- **Professional Typography** and rounded card layouts

---

## 🛠️ Tech Stack

| Technology                     | Usage                                    |
| ------------------------------ | ---------------------------------------- |
| Android Studio                 | Development Environment                  |
| Kotlin                         | Programming Language                     |
| Firebase Authentication        | User Authentication (Phone)              |
| Firebase Firestore             | Real‑time Database & Chat Storage        |
| Firebase Storage               | Media Storage (Images, Voice, etc.)      |
| Firebase Cloud Messaging       | Push Notifications                       |
| Glide                          | Image Loading & Caching                  |
| ExoPlayer                      | Audio / Video Playback                   |
| Material Design 3              | UI Components & Theming                  |
| CountryCodePicker (hbb20)      | Phone Number Input with Country Selector |

---

## 📂 Project Structure

```text
app/src/main/java/com/example/chatclone/
├── activities/
│   ├── LoginActivity.kt
│   ├── OtpActivity.kt          (removed – OTP disabled)
│   ├── SetupProfileActivity.kt
│   ├── HomeActivity.kt
│   ├── ChatActivity.kt
│   ├── UserProfileActivity.kt
│   ├── SettingsActivity.kt
│   ├── NotificationSettingsActivity.kt
│   └── PrivacySettingsActivity.kt
├── fragments/
│   ├── ChatsFragment.kt
│   ├── StatusFragment.kt
│   ├── CommunitiesFragment.kt
│   ├── CallsFragment.kt
│   └── SettingsFragment.kt
├── adapters/
│   ├── ChatAdapter.kt
│   ├── MessageAdapter.kt
│   ├── StatusAdapter.kt
│   └── SettingsAdapter.kt
├── models/
│   ├── Chat.kt
│   ├── Message.kt
│   ├── User.kt
│   ├── Status.kt
│   └── CallLog.kt
├── services/
│   ├── MyFirebaseMessagingService.kt
│   └── ContactSyncService.kt
└── utils/
    ├── Constants.kt
    └── PermissionHelper.kt

res/
├── layout/
│   ├── activity_login.xml
│   ├── activity_otp.xml          (removed)
│   ├── activity_setup_profile.xml
│   ├── activity_home.xml
│   ├── activity_chat.xml
│   ├── activity_user_profile.xml
│   ├── activity_settings.xml
│   ├── activity_notification_settings.xml
│   ├── activity_privacy_settings.xml
│   ├── fragment_chats.xml
│   ├── fragment_status.xml
│   ├── fragment_communities.xml
│   ├── fragment_calls.xml
│   ├── item_chat.xml
│   ├── item_message_sent.xml
│   ├── item_message_received.xml
│   ├── item_message_image_*.xml
│   ├── item_message_voice_*.xml
│   ├── item_settings.xml
│   └── dialog_media_preview.xml
├── drawable/
│   ├── otp_box_background.xml   (optional, kept for reference)
│   ├── message_bubble_sent.xml
│   ├── message_bubble_received.xml
│   └── ... other icons and shapes
└── values/
    ├── colors.xml
    ├── strings.xml
    ├── styles.xml
    └── themes.xml (DayNight support)
```

---

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/pandyaomsanjay/chatui.git
cd chatui   
```

### 2. Open in Android Studio

- Open Android Studio → **Open Project** → select the project folder.
- Wait for Gradle sync to finish.

### 3. Firebase Setup (Mandatory)

1. Go to the [Firebase Console](https://console.firebase.google.com/) and create a new project.
2. **Add your Android app** – use package name `com.example.chatclone`.
3. Download the `google-services.json` file and place it in the **`app/`** folder.
4. Enable **Authentication** → **Phone** (if you decide to enable OTP later) or keep it disabled for the simplified demo.
5. Enable **Cloud Firestore** and **Storage** – set up security rules (you can start in test mode).
6. Enable **Cloud Messaging** for push notifications.
7. Add your debug **SHA‑1** and **SHA‑256** fingerprints in the project settings (if using Phone Auth).

### 4. Build and Run

- Connect a physical Android device (or emulator).
- Click the **Run** button (green triangle).
- The app will launch with the Login screen.

---

## 📱 Screens (Key UI)

| Screen | Description |
|--------|-------------|
| **Splash** | App logo with gradient background |
| **Login** | Phone number input with country picker |
| **Profile Setup** | Enter name, bio, and optional profile picture |
| **Home** | Bottom navigation (Chats, Status, Communities, Calls, Settings) + chat list |
| **Chat** | Message bubbles, typing indicator, attachment options |
| **Profile** | View and edit user details |
| **Settings** | Notifications, Privacy, Logout |

---

## 🧪 Testing Notes

- **No OTP**: The login flow skips verification for quick testing. To enable OTP, uncomment the Firebase Phone Auth logic in `LoginActivity.kt` and re‑add `OtpActivity`.
- **Media Sharing**: Camera and gallery attachments are fully functional; uploads are stored in Firebase Storage.
- **Voice Messages**: Recording is simulated; actual recording can be enabled with `MediaRecorder`.
- **Groups / Communities**: UI and Firestore data structure are ready; group creation logic can be added.

---

## 🔮 Future Enhancements

- **End‑to‑End Encryption** – encrypt messages before storing in Firestore.
- **Video / Voice Calls** – integrate WebRTC for real‑time communication.
- **Message Reactions** – add emoji reactions to messages.
- **Stories (Status) with Expiry** – fully implement auto‑delete (already structured).
- **Contact Sync** – read device contacts and match with registered users.
- **Push Notifications** – complete FCM integration (service is ready).
- **Group Admin Controls** – add/remove members, change group icon.
- **AI Chat Assistant** – integrate a chatbot for automated replies.

---

## 🤝 Contributing

Contributions are welcome! Fork the repository, make your changes, and submit a pull request.

1. Fork the project.
2. Create your feature branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

---

## ⭐ Support

If you find this project useful, please give it a star ⭐ on GitHub to help others discover it.

---

**Chat Clone** – Build modern messaging apps with ease. 🚀
