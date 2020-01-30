

[![Build Status](https://travis-ci.com/SuperCaliMan/CustomCalendar.svg?token=JDhHLN4a6eXQ6S7wR9SA&branch=master)](https://travis-ci.com/SuperCaliMan/CustomCalendar)
[![Version status](https://jitpack.io/v/SuperCaliMan/CustomCalendar.svg)](https://jitpack.io/v/SuperCaliMan/CustomCalendar)
# CustomCalendar 
A calendar custom view for Android

![Screencap](img/screen.gif)

## Download

```
dependencies {
    implementation 'com.github.SuperCaliMan:CustomCalendar:1.0'
}
```

## Example usage

- Add the view to your layout:

```xml
<com.github.customcalendar.CustomCalendarView
        android:id="@+id/custom_calendar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:weeks="6"
        app:firstBarColor ="#0000FF"
        app:secondBarColor = "#F000F0">
</com.github.customcalendar.CustomCalendarView>
```




- Set calendar:
```kotlin
val customCalendar = findViewById<CustomCalendarView>(R.id.custom_calendar)
val listDay = CustomCalendarView.getDayList(1,2020)
customCalendar.setCalendar(listDay)
```
- Then set the listener:

```kotlin
customCalendar.setOnClickListener(object :OnClickListenerCalendar{
            override fun onClickListenerCalendar(v: View, d: DateCustom) {
                 Toast.makeText(applicationContext,d.toString,Toast.LENGTH_SHORT).show()
            }
        })
```

## Attributes
you can set this attributes by XML or Kotlin
- **Weeks**: set number of week in custom calendar view
- **BarColor**: set color of first/second bar


License
-------

    Copyright 2020 Alberto Caliman

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
