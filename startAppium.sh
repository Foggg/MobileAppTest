#!/bin/bash
appium server -ka 800 --use-plugins=device-farm  -pa /wd/hub --plugin-device-farm-platform=both --plugin-device-farm-ios-device-type=real
