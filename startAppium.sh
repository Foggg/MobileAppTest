#!/bin/bash
ios forward 7777 8100&
sleep 3
ios tunnel start --userspace&
sleep 3
ios runwda --bundleid=com.iosfcuk.xctrunner --testrunnerbundleid=ru.smartbuys.couriers --xctestconfig=NONE&
sleep 3
ios uninstall com.iosfcuk.xctrunner
sleep 3
ios install --path=wda.ipa
sleep 5
ios launch com.iosfcuk
sleep 3
appium
