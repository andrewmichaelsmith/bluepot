[![bountysource](https://api.bountysource.com/badge/issue?issue_id=12486899 "bountysource")](https://www.bountysource.com/issues/12486899-bluepot-interface-doesn-t-work-in-debian-jessie)


bluepot
=======

Bluepot is a Bluetooth Honeypot written in Java, it runs on Linux.

Bluepot was a third year university project attempting to implement a fully functional Bluetooth Honeypot. A piece of software designed to accept and store any malware sent to it and interact with common Bluetooth attacks such as “BlueBugging?” and “BlueSnarfing?”. Bluetooth connectivity is provided via hardware Bluetooth dongles.

The system also allows monitoring of attacks via a graphical user interface that provides graphs, lists, a dashboard and further detailed analysis from log files.


dependencies 
============

You need linux, you need root and you need to have at least 1 bluetooh receiver (if you have many it will work wiht those, too).

You must install:

* libbluetooth-dev on Ubuntu
* bluez-libs-devel on Fedora
* bluez-devel on openSUSE

run bluepot
===========

:warning: Running a honetpot as root is a bit risky (but necessary for this software)

    wget https://github.com/andrewmichaelsmith/bluepot/raw/master/bin/bluepot-0.2.tar.gz
    tar xfz bluepot-0.2.tar.gz
    sudo java -jar bluepot/bluepot.jar


![What bluepot looks like](https://raw.github.com/andrewmichaelsmith/bluepot/master/bin/screenshot.png "What bluepot looks like")
