# To-Do-Reminder-App
Application which may be usefull during track of your work to do.

## Description
Application is written in java using swing library, hibernate and MySQL database.

## Usage
Main objects in application are tasks.
They can be either done or market as todo.

![There should be screen shoot from the app](/Design/Screenshoots/ToDo1.png)

Each task consists of:
  - name
  - importance
  - reminder date
  - note
  - creation date
  
After creating new task, instance is created in database which defaultly has importance of the task (set to 1), task id and creation date.

![There should be screen shoot from the app](/Design/Screenshoots/ToDo2.png)

Newly created object is being placed in the todo window.

![There should be screen shoot from the app](/Design/Screenshoots/ToDo3.png)

After selecting task, program allows you to edit its name, importance, reminder date and note.
To activate feature you just need to click on field tht is interesting for you.

![There should be screen shoot from the app](/Design/Screenshoots/ToDo4.png)

Each field saves itself automaticly and makes database transaction after changing mouse focus.
Task note area is being saved every 15 seconds while in focuse.

![There should be screen shoot from the app](/Design/Screenshoots/ToDo5.png)

Tasks may be marked as done by clicking on white square next to the name.
After that, object is being moved to the "done tasks" list along with database transaction.

Each task may be removed by right clicking on name.
In addition you can undo already finished task by right clicking and choosing right option.

All tasks may be sorted by id, alphabetically by name, or by importance level.

![There should be screen shoot from the app](/Design/Screenshoots/Sorting1.png)

![There should be screen shoot from the app](/Design/Screenshoots/Sorting2.png)

Additional Libraries:
  - Hibernate

## Installation
Currently requires JDK in version 14.0
