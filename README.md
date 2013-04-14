Universal-init.d
================

## A simple Android application which emulates the behavior of the init.d kernel mechanism
**(automatic execution of the scripts contained within the /system/etc/init.d folder)**

What makes this method really universal is that everything happens on the app side, no system files modifications are required whatsoever. The tool basically emulates the behavior of the init.d kernel mechanism : it will detect whenever the device is rebooted and execute the scripts located in /system/etc/init.d.

With this new init.d support granting mechanism comes a new kernel-sided init.d support detection mechanism, so that your int.d scripts don't get executed twice, which would only make for a longer boot time.
Basically, when you run the test, it will generate a little init.d script which creates a file on your sdcard. Then, after you rebooted, it will check if the file that the generated script creates has indeed been created in order to determine if your kernel already has init.d support built-in. If the file was created, it does. If not, it doesn't, as simple as that.

An additionnal functionnality to this tool is the ability to manage, edit, delete & run your init.d scripts with the touch of a button.

## Dependencies
This project depends on the following open source libraries :

* **[ActionBarSherlock] (http://actionbarsherlock.com)** by Jake Wharton
* **[NineOldAndroids] (http://nineoldandroids.com)** by Jake Wharton
* **[JazzyViewPager] (https://github.com/jfeinstein10/JazzyViewPager)** by Jeremy Feinstein

## Contribute
Clone the repo, make your changes then create a pull request. If your commit is accepted it will probably be merged in a future build of Pimp My Rom.

## Background
This app is a standalone version of the universal init.d support tool included in Pimp My Rom from beta 1.0 onwards.

* Find out more about **Pimp My Rom** at : [http://pimpmyrom.org](http://pimpmyrom.org)
* You can download **Pimp My Rom** for free on [Google Play](https://play.google.com/store/apps/details?id=com.androguide.pimp.my.rom)

## Screenshots

![Wireframe Galay nexus 1](http://168.144.134.166/screenshots/framed-initd1.jpg)![Wireframe Galay nexus 2](http://168.144.134.166/screenshots/framed-initd2.jpg)![Wireframe Galay nexus 3](http://168.144.134.166/screenshots/framed_initd3.jpg)![Wireframe Nexus 7 - 1](http://168.144.134.166/screenshots/framed-initd4.jpg)![Wireframe Nexus 7 - 2](http://168.144.134.166/screenshots/framed-initd5.jpg)![Wireframe Nexus 7 - 3](http://168.144.134.166/screenshots/framed-initd6.jpg)

