# 🏆 HabitMate - Personal Habit Tracker
Track, Improve, and Succeed with Your Daily Habits

**HabitMate** is an Android application built with **Java** and **SQLite** designed to help users develop consistent daily habits.  
It offers a clean user interface, category-based habit organization, progress tracking, motivational summaries, and full CRUD functionality — making habit building simpler and more engaging.

---

## Key Features:

### 🔐 Authentication
- Secure **Register** & **Login** using SQLite  
- Validations for safe input  
- Logout with session clear  

### 📋 Habit Management
- Add habits with name, description, and category  
- Edit existing habits  
- Delete habits with confirmation popup  
- Offline storage using SQLite  

### 🔎 Search & Filter
- **AutoCompleteTextView** for fast habit search  
- Filter habits by category (Health, Study, Fitness, etc.)  
- Display habits dynamically in RecyclerView  

### 📊 Progress Tracking
- View **total habits**, **completed habits**, and **completion percentage**  
- Motivational messages based on progress  
- Daily checkboxes to mark habit completion  

### 🎨 Modern UI
- Material-inspired design  
- CardViews for habit display  
- Spinners, AutoComplete, FABs for smooth UX  
- Consistent light theme for a clean look  

## Technologies Used
  | Component        | Technology |
  |------------------|------------|
  | Frontend         | Java, XML (Android Studio) |
  | Database         | SQLite |
  | Backend Logic    | HabitDatabaseHelper class |
  | UI Components    | RecyclerView, CardView, Spinners, AutoCompleteTextView, FAB |
  | IDE              | Android Studio |
  | Version Control  | Git & GitHub |

  ## 🧠 How It Works 
  1. **Startup**
   - App begins with a splash screen for branding.
   - Navigates to Login/Register.

2. **User Authentication**
   - User credentials stored securely using SQLite.
   - Login validation checks username + password.
   - Successful login opens Dashboard.

3. **Dashboard**
   - Displays all habits using RecyclerView.
   - AutoComplete search suggests habit names instantly.
   - Category filter updates the habit list dynamically.

4. **Habit Addition**
   - User enters habit name, category, and optional notes.
   - Data stored in SQLite through HabitDatabaseHelper.

5. **Habit Details**
   - Clicking a habit opens a detailed view:
     - Name,category and other habit details are displayed
     - Edit and Save Changes option
     - Delete habit button with confirmation dialog

6. **Daily Progress**
   - User marks a habit as completed for the day.
   - Progress screen shows:
     - ✔ Total habits
     - ✔ Completed habits
     - ✔ Completion percentage  
   - Motivational messages based on user performance.

7. **Logout**
   - Session cleared.
   - Redirects user back to the Login screen.
  

## 🚀 Setup & Installation
 1. Clone this Repository:
```bash
git clone https://github.com/VidhiThakkar16/HabitTrackerApp.git
Open the project in Android Studio
```
 2.Sync Gradle

 3.Build and run on:
  * Android Emulator
  * Physical Android Device

## 🔮 Future Enhancements

* Habit reminders & notifications

* Weekly/Monthly analytics charts

* Streak tracking system

* Dark/Light theme toggle

* Firebase cloud backup

* Export habits to PDF/Excel


## 👩‍💻 Author

Vidhi U. Thakkar
* Diploma in Computer Engineering
* Gujarat Technological University

## 🧾 License / Notes

* This project is created as part of the Mobile Application Development (MAD) subject and is open for learning and academic use.
* Feel free to modify or enhance it with proper credit.




  



