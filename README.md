# 💬 Chat Clone

A modern Android Chat UI Clone inspired by WhatsApp and Facebook Messenger. This project demonstrates a polished messaging interface with phone login (no OTP for demo), chat screens, media attachment UI, voice recording simulation, and settings – all built with Material Design 3.

---

## 📱 Overview

**Chat Clone** is a fully functional UI prototype developed with Android Studio. It showcases a realistic messaging experience with chat bubbles, typing indicators, read receipts, image sharing, voice recording, and a bottom‑navigation home screen – all without needing a backend. It’s perfect for learning Android UI patterns or as a starting point for a real‑time messaging app.

---

## ✨ Features (Implemented)

### 🔐 Login
- **Phone Number Input** with country code picker (uses `CountryCodePicker` library).
- **No OTP** – for quick demo, login is instant; you can later add Firebase Phone Auth.

### 💬 Chat Screen
- **Send / receive text messages** – simulated with random replies.
- **Typing indicator** – shows when the other user is typing (simulated).
- **Message status** – delivered (✓✓) and seen (blue ✓✓) ticks.
- **Timestamp** on every message.
- **Sent / received bubbles** – green and white with rounded corners.

### 📎 Media Sharing
- **Gallery picker** – select an image and preview before sending.
- **Camera capture** – take a photo and preview.
- **Voice recording** – simulated recording with timer and send as voice note.
- **Preview dialog** for images before sending.

### 📌 Home Screen
- **Chat list** – sample contacts with online status and unread badges.
- **Search** – filter chats by name or last message.
- **Bottom navigation** – Chats, Status, Calls, Settings (status & calls are placeholders).
- **Floating Action Button** – placeholder for new chat.

### ⚙️ Settings & Profile
- **Profile screen** – view and edit name, phone, bio, and profile picture (local storage).
- **Notification settings** – toggles for message/group notifications, sound, vibrate.
- **Privacy settings** – drop‑downs for last seen, profile photo, status visibility.
- **Logout** – clears local user data and returns to login.

### 🎨 UI/UX
- **Material Design 3** – modern components, smooth transitions.
- **Dark / Light theme** – follows system preference.
- **WhatsApp‑style chat bubbles** – sent (green) and received (white).
- **Professional typography** and responsive layouts.

---

## 🛠️ Tech Stack

| Technology                 | Usage                              |
| -------------------------- | ---------------------------------- |
| Android Studio (Giraffe+)  | Development Environment            |
| Kotlin                     | Programming Language               |
| RecyclerView               | Chat lists & message adapter       |
| Glide                      | Image loading (avatars, previews)  |
| Material Design 3          | UI Components & Theming            |
| CountryCodePicker (hbb20)  | Phone input with country selection |
| SharedPreferences          | Local user data storage            |

---

## 📂 Project Structure

```text
app/src/main/java/com/example/chatclone/
├── activities/
│   ├── LoginActivity.kt         – Phone login (no OTP)
│   ├── HomeActivity.kt          – Chat list + bottom nav
│   ├── ChatActivity.kt          – Message screen with sending/receiving
│   ├── UserProfileActivity.kt   – Edit profile (local storage)
│   ├── SettingsActivity.kt      – Settings menu
│   ├── NotificationSettingsActivity.kt
│   ├── PrivacySettingsActivity.kt
│   ├── SetupProfileActivity.kt  – (not used in demo)
│   └── WelcomeActivity.kt       – Splash screen
├── adapters/
│   ├── ChatAdapter.kt           – Chat list adapter
│   ├── MessageAdapter.kt        – Message bubbles with status
│   └── SettingsAdapter.kt       – Settings list adapter
├── models/
│   ├── Chat.kt                  – Chat item data
│   └── Message.kt               – Message data with media type
└── ... (no fragments, services, or Firebase yet)
res/
├── layout/
│   ├── activity_login.xml
│   ├── activity_home.xml
│   ├── activity_chat.xml
│   ├── activity_user_profile.xml
│   ├── activity_settings.xml
│   ├── activity_notification_settings.xml
│   ├── activity_privacy_settings.xml
│   ├── activity_welcome.xml
│   ├── activity_setup_profile.xml
│   ├── dialog_media_preview.xml
│   ├── item_chat.xml
│   ├── item_message_sent.xml
│   ├── item_message_received.xml
│   ├── item_message_image_sent.xml
│   ├── item_message_image_received.xml
│   ├── item_message_voice_sent.xml
│   ├── item_message_voice_received.xml
│   └── item_settings.xml
├── drawable/
│   ├── otp_box_background.xml   (kept for reference)
│   ├── message_bubble_sent.xml
│   ├── message_bubble_received.xml
│   └── ... (icons, shapes)
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

- Open Android Studio → **Open Project** → select the cloned folder.
- Wait for Gradle sync to finish.

### 3. Run the App

- Connect an Android device or start an emulator.
- Click the **Run** button (green triangle).
- The app will launch with the Welcome screen, then Login.

**No Firebase setup required** – all data is local (sample chats, profile stored in `SharedPreferences`). If you later want to add Firebase, follow the optional steps below.

---

## 📱 Screens (Overview)

| Screen | Description |
|--------|-------------|
| **Welcome** | Splash with app name and "Get Started" button. |
| **Login** | Phone number with country picker; tap Continue to go to Home. |
| **Home** | Chat list with sample contacts, search, bottom navigation (Chats, Status, Calls, Settings). |
| **Chat** | Message bubbles, typing indicator, attachment (gallery/camera), voice recording. |
| **Profile** | View/edit name, phone, bio, and profile picture (saved locally). |
| **Settings** | Menu with Profile, Notifications, Privacy, Chats (placeholder), Logout. |
| **Notifications** | Toggle switches for message/group notifications, sound, vibrate. |
| **Privacy** | Drop‑downs for last seen, profile photo, and status visibility. |

---

## 🧪 Testing Notes

- **No OTP** – the login is instant; any valid phone number works.
- **Messages** – sending a message triggers a simulated reply after 2 seconds (random reply).
- **Media** – gallery and camera work (requires permissions); images are previewed and sent as messages with a placeholder text.
- **Voice** – recording is simulated (timer only); the recorded audio is not saved.
- **Profile** – all profile data is stored in `SharedPreferences`; changes persist until logout.
- **Status & Calls** – bottom navigation tabs show "Coming soon" toasts.

---

## 🔮 Future Enhancements

- **Firebase Integration** – real‑time Firestore, phone auth, and Storage.
- **End‑to‑End Encryption** – secure messaging.
- **Group Chats** – create and manage groups.
- **Voice / Video Calls** – WebRTC integration.
- **Status (Stories)** – upload and view media statuses.
- **Push Notifications** – FCM for incoming messages.
- **Contact Sync** – match device contacts with app users.

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

If you found this project helpful, please give it a star ⭐ on GitHub.

---

**Chat Clone** – A clean, modern Android chat UI to kickstart your messaging app. 🚀
