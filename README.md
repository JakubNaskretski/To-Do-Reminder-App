# To-Do-Reminder-App
Application which may be usefull during track of your work to do.

## Description
Application is written in java using swing library, hibernate and MySQL database.

## Usage
Main objects in application are tasks.
They can be either done or market as todo.

![There shuld be screen shoot from the app](/screenshoots/ToDo1.png)

Each task consists of:
  - name
  - importance
  - reminder date
  - note
  - creation date
  
After creating new task, instance is created in database which defaultly has importance of the task (set to 1), task id and creation date.

![There shuld be screen shoot from the app](/screenshoots/ToDo2.png)

Newly created object is being placed in to do window.

![There shuld be screen shoot from the app](/screenshoots/ToDo3.png)

After selecting task, program allows you to edit its name, importance, reminder date and note.
To activate feature you just need to click on field tht is interesting for you.

![There shuld be screen shoot from the app](/screenshoots/ToDo4.png)

Each field saves itself automaticly and makes database transaction after changing mouse focus.
Task note area is being saved every 15 seconds while in focuse.

![There shuld be screen shoot from the app](/screenshoots/ToDo5.png)

Tasks may be marked as done by clicking on white square next to the name.
After that, object is being moved to the "done tasks" list along with database transaction.

Each task may be removed by right clicking on name.
In addition you can undo already finished task by right clicking and choosing right option.

All tasks may be sorted by id, alphabetically by name, or by importance level.

![There shuld be screen shoot from the app](/screenshoots/Sorting1.png)

![There shuld be screen shoot from the app](/screenshoots/Sorting2.png)

Additional Libraries:
  - Hibernate

## Installation
Currently requires JDK in version 14.0
