#  ToDoApp - Java Swing Desktop To-Do List

**Overview**  

A clean, modern, and fully functional desktop To-Do application built with **Java Swing**.  
This project allows users to **add, delete, mark complete/incomplete, search/filter**, and **persist tasks** between sessions - making it a great demonstration of Java desktop development skills.

##  Features
-  **Add Tasks** — via Enter key or "Add Task" button.
-  **Delete Tasks** — via Delete key or "Delete" button (with confirmation).
-  **Toggle Completion** — double-click or use the "Toggle Done" button.
-  **Clear Completed** — instantly remove all finished tasks.
-  **Search / Filter** — real-time task filtering by keywords.
-  **Persistent Storage** — saves tasks to tasks.dat and loads them automatically.
-  **Keyboard Shortcuts**:  
-  **Enter** = Add task  
-  **Delete** = Delete selected task  
-  **Ctrl+S** = Save tasks  
-  **Ctrl+L** = Load tasks

##  Tech Stack
- **Java 11+** (compatible with Java 8+)
- **Swing** for GUI (JFrame, JList, JButton, JTextField, etc.)
- **Java Serialization** for simple file-based persistence

## How to Run
### 1) ** Compile the application **
```bash
cd C:\Users\saiva\OneDrive\Desktop\ToDoApp
javac *.java
```
### 2) ** Run the application **
```bash
java ToDoApp
```

## Screenshots ##

1) Overall UI : ![Overall Output](screenshots/Overalloutput.png)
2) ToggleDone : ![Toggle Done](screenshots/ToggleDone.png)
3) Delete Function : 1) ![Delete Step 1](screenshots/delete1.png) 
                     2) ![Delete Step 2](screenshots/delete2.png)
4) Load Function : ![Load Feature](screenshots/Load.png)
5) Save Function : ![Save Feature](screenshots/Save.png)
