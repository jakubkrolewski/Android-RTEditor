# Android-RTEditor

The Android RTEditor is a rich text editor component for Android that can be used as a drop in for EditText.

![Images](http://www.1gravity.com/images/richtexteditor.png)

Features
--------

The editor offers the following <b>character formatting</b> options:

* **Bold**
* *Italic*
* <u>Underline</u>
* <strike>Strike through</strike>
* <sup>Superscript</sup>
* <sub>Subscript</sub>
* Text size
* Text color
* Background color

It also supports the following <b>paragraph formatting</b>:

<ol><li>Numbered</li></ol>
<ul>
<li>Bullet points</li>
<ul style='list-style-type:none;'>Indentation</ul>
<div align="left">Left alignment</div>
<div align="center">Center alignment</div>
<div align="right">Right alignment</div>
</ul>

* [Links](http://www.1gravity.com)
* Images: [![Images](http://www.1gravity.com/smiley.jpg)](https://www.1gravity.com)
* Undo/Redo


The 3 main components
---------------------
####**RTEditText**
is the EditText drop in component. Add it to your layout like you would EditText:
```xml
  <com.onegravity.rteditor.RTEditText
    android:id="@+id/rtEditText"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:imeOptions="actionDone|flagNoEnterAction"
    android:inputType="textMultiLine|textAutoCorrect|textCapSentences" />
```
In code you would typically use methods to set and get the text content:
  * set text: <code>RTEditText.setRichTextEditing(true, "My content");</code>
  * get text: <code>RTEditText.getText(RTFormat.HTML)</code>

####**RTToolbar**
is an interface for the toolbar used to apply text and paragraph formatting and other features listed above. The actual RTToolbar implementation is in a separate module and is a scrollable ribbon but alternative implementations aren't too hard to realize (popup, action buttons, floating buttons...). The toolbar implementation is easy to integrate into your layout:
```xml
  <include android:id="@+id/rte_toolbar_container" layout="@layout/rte_toolbar" />
```

 or if you want to have two ribbons for character and paragraph formatting:
```xml
    <LinearLayout
        android:id="@+id/rte_toolbar_container"
    	android:orientation="vertical"
    	android:layout_width="match_parent"
    	android:layout_height="wrap_content">

    	<include layout="@layout/rte_toolbar_character" />
    	<include layout="@layout/rte_toolbar_paragraph" />

    </LinearLayout>
```

In code you'd typically not interact with the toolbar (see RTManager below for the one exception).

####**RTManager**
is the glue that holds the rich text editors (RTEditText), the toolbar and your app together. Each rich text editor and each toolbar needs to be registered with the RTManager before they are functional. Multiple editors and multiple toolbars can be registered. The RTManager is instantiated by your app in code usually in the onCreate passing in an RTApi object that gives the rich text editor access to its context (your app).
A typical initialization process looks like this:

```
// create RTManager
RTApi rtApi = new RTApi(this, new RTProxyImpl(this), new RTMediaFactoryImpl(this, true));
RTManager rtManager = new RTManager(rtApi, savedInstanceState);

// register toolbar
ViewGroup toolbarContainer = (ViewGroup) findViewById(R.id.rte_toolbar_container);
HorizontalRTToolbar rtToolbar = (HorizontalRTToolbar) findViewById(R.id.rte_toolbar);
if (rtToolbar != null) {
  rtManager.registerToolbar(toolbarContainer, rtToolbar);
}

// register editor & set text
RTEditText rtEditText = (RTEditText) findViewById(R.id.rtEditText);
rtManager.registerEditor(rtEditText, true);
rtEditText.setRichTextEditing(true, message);
```

To retrieve the edited text in html format you'd do:
```
   String text = rtEditText.getText(RTFormat.HTML);
```

Demo project
------------

The project consists of five different modules:
* **ColorPicker**: a color picker based on this: https://github.com/LarsWerkman/HoloColorPicker. The RTEditor uses an enhanced version that allows to enter ARGB values, includes a ColorPickerPreference that can be used in preference screens and shows a white and gray chessboard pattern behind the color visible when the the alpha channel is changed and the color becomes (partly) transparent.
* **MaterialDialog** a library based on this: https://github.com/afollestad/material-dialogs. The RTEditor uses a trimmed-down version because it only uses a subset of its functionality for the link dialog. You can easily remove this module and use the standard Android dialog instead.
* **RTEditor**: the actual rich text editor (excluding the toolbar implementation).
* **RTEditor-Toolbar**: the toolbar implementation.
* **RTEditor-Demo**: this module isn't part of the actual rich text editor component but contains a sample app that shows how to use the component.

Issues
------

If you have an issues with this library, please open a issue here: https://github.com/1gravity/Android-RTEditor/issues and provide enough information to reproduce it reliably. The following information needs to be provided or the issue will be closed without any further notice:

* Which version of the SDK are you using?
* Which Android build are you using? (e.g. MPZ44Q)
* What device are you using?
* What steps will reproduce the problem? (Please provide the minimal reproducible test case.)
* What is the expected output?
* What do you see instead?
* Relevant logcat output.
* Optional: Link to any screenshot(s) that demonstrate the issue (shared privately in Drive.)
* Optional: Link to your APK (either downloadable in Drive or in the Play Store.)

License
-------

Copyright 2015 Emanuel Moecklin

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
