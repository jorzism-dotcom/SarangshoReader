# Sarangsho Reader — Android OCR helper

এটি একটি সাধারণ Android Studio starter project। আপনার নিজের ফোনে তোলা বা সংরক্ষিত স্ক্রিনশট থেকে বাংলা/ইংরেজি লেখা OCR করে TXT ফাইলে সংরক্ষণ করে।

## কীভাবে ব্যবহার করবেন
1. Android Studio-তে এই ফোল্ডারটি Open করুন।
2. Gradle Sync সম্পন্ন হতে দিন।
3. ফোনে অ্যাপটি Run করুন।
4. Sarangsho অ্যাপে আপনি যে কনটেন্ট দেখতে অনুমোদিত, সেটির স্ক্রিনশট Android-এর স্বাভাবিক screenshot সুবিধা দিয়ে নিন—যদি অ্যাপ/ডিভাইস তা অনুমোদন করে।
5. Sarangsho Reader খুলে স্ক্রিনশট নির্বাচন করুন।
6. একাধিক স্ক্রিনশট একসঙ্গে নির্বাচন করলে OCR ফলাফল ধারাবাহিকভাবে যোগ হবে।
7. TXT ফাইল হিসেবে সংরক্ষণ করুন।

## গুরুত্বপূর্ণ সীমাবদ্ধতা ও নিরাপত্তা
- এই starter app স্বয়ংক্রিয়ভাবে Sarangsho-তে লগইন করে না, অ্যাপের ডেটা/নেটওয়ার্কে প্রবেশ করে না, স্ক্রল বা স্ক্রিন রেকর্ড করে না।
- কোনো অ্যাপ screenshot/capture বন্ধ করে রাখলে সেই সীমাবদ্ধতা পাশ কাটানোর চেষ্টা করবেন না।
- Gmail, পাসওয়ার্ড, OTP বা session cookie কোথাও দেবেন না।
- OCR-এ ভুল হতে পারে; TXT ব্যবহার করার আগে শিরোনাম, নাম, তারিখ ও সংখ্যা যাচাই করুন।
- ML Kit-এর Text Recognition মডেল ডিভাইসে/Google Play services-এর মাধ্যমে কাজ করতে পারে; প্রথমবার মডেল প্রস্তুত হতে ইন্টারনেট লাগতে পারে।
- এটি একটি starter project, প্রস্তুত-স্বাক্ষরিত APK নয়।

## Build APK
Android Studio → Build → Build Bundle(s) / APK(s) → Build APK(s)

সাধারণত APK পাওয়া যায়:
`app/build/outputs/apk/debug/app-debug.apk`


## GitHub থেকে ফোনে APK তৈরি (কম্পিউটার ছাড়াই)
1. GitHub-এ নতুন Public বা Private repository তৈরি করুন।
2. এই ZIP-এর ভেতরের সব ফাইল ও `.github/workflows/android-apk.yml` ফাইলসহ repository-তে upload করুন। `.github` ফোল্ডারটিও অবশ্যই থাকতে হবে।
3. Repository-এর Actions ট্যাবে যান।
4. `Build Android APK` workflow নির্বাচন করে `Run workflow` চাপুন (অথবা `main`-এ ফাইল upload হলেই build শুরু হবে)।
5. সফল হলে workflow run-এর নিচে Artifacts থেকে `SarangshoReader-debug-apk` ডাউনলোড করুন।
6. ZIP artifact extract করে `app-debug.apk` ফোনে ইনস্টল করুন। Android চাইলে শুধু এই ইনস্টলেশনের জন্য browser/files app-কে অনুমতি দিন।

**সতর্কতা:** APK debug build; ব্যক্তিগতভাবে পরীক্ষা করার জন্য। অজানা ব্যক্তির APK বা GitHub token/password কাউকে দেবেন না। GitHub password/OTP এই চ্যাটে দেবেন না.
