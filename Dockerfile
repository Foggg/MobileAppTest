FROM dockerfog/android-base-image:latest

ENV NODE_VERSION=20
ENV APPIUM_VERSION=2.17.1
ENV APPIUM_DRIVER_UIAUTOMATOR2_VERSION=4.2.0
ENV APPIUM_DRIVER_XCUITEST_VERSION=9.2.0
ENV DEVICE_FARM=9.8.5

RUN apt-get -qqy update
RUN apt-get -qqy --no-install-recommends install curl git libimobiledevice-utils libimobiledevice6 usbmuxd \
&& rm -rf /var/lib/apt/lists/*

RUN useradd fermausr \
         --create-home \
         --shell /bin/bash
WORKDIR /home/fermausr
RUN touch ferma.log

RUN curl -sL https://deb.nodesource.com/setup_${NODE_VERSION}.x | bash && \
    apt-get -qqy install nodejs && \
    npm init && \
    npm install -g appium@${APPIUM_VERSION} && \
    npm install -g prisma && \
    npx prisma init && \
    prisma generate --allow-no-models && \
    exit 0 && \
    npm cache clean && \
    apt-get remove --purge -y npm && \
    apt-get autoremove --purge -y && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/* && \
    apt-get clean

RUN npm i -g go-ios

USER fermausr

RUN appium driver install uiautomator2@${APPIUM_DRIVER_UIAUTOMATOR2_VERSION}
RUN appium driver install xcuitest@${APPIUM_DRIVER_XCUITEST_VERSION}

COPY WDA21.ipa /home/fermausr/wda.ipa

EXPOSE 4723

COPY startAppium.sh /
ENTRYPOINT ["/bin/bash","-c","/startAppium.sh"]

#ENTRYPOINT ["appium"]
#ADD https://nexus.sovcombank.group/repository/appium-device-farm/device-farm.apk /home/fermausr/.appium/node_modules/appium-device-farm/lib/stream.apk
#ENTRYPOINT ["appium", "server", "-ka", "800", "--use-plugins=device-farm", "-pa", "/wd/hub", "--plugin-device-farm-platform=android", "--plugin-device-farm-max-sessions=30", "--plugin-device-farm-remote-connection-timeout=120000", "--allow-cors", "--relaxed-security", "--log", "ferma.log", "--log-level", "error:warn"]