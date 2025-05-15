FROM dockerfog/android-base-image:latest

ENV NODE_VERSION=22
ENV APPIUM_VERSION=2.18.0
ENV APPIUM_DRIVER_UIAUTOMATOR2_VERSION=4.2.3
ENV APPIUM_DRIVER_XCUITEST_VERSION=9.2.4
ENV DEVICE_FARM=10.0.3
ENV ANDROID_ADB_SERVER_ADDRESS="localhost"

RUN apt-get -qqy update && \
    apt-get -qqy --no-install-recommends install \
    curl \
    git \
    make \
    g++ \
    python3 \
    nodejs \
    npm \
    libimobiledevice-utils \
    libimobiledevice6 usbmuxd \
    && rm -rf /var/lib/apt/lists/*

RUN useradd fermausr \
         --create-home \
         --shell /bin/bash
WORKDIR /home/fermausr
RUN touch ferma.log

RUN npm install -g appium@${APPIUM_VERSION} && \
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

ENV GO_IOS=/usr/local/bin/ios

USER fermausr

RUN appium driver install uiautomator2@${APPIUM_DRIVER_UIAUTOMATOR2_VERSION} && \
    appium driver install xcuitest@${APPIUM_DRIVER_XCUITEST_VERSION}

RUN appium plugin install --source=npm appium-device-farm@${DEVICE_FARM} && \
    appium plugin run device-farm setup

ADD device-farm.apk /home/fermausr/.appium/node_modules/appium-device-farm/lib/stream.apk

EXPOSE 4723

COPY startAppium.sh /

ENTRYPOINT ["/bin/bash","-c","/startAppium.sh"]
